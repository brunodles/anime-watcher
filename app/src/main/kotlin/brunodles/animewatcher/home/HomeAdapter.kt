package brunodles.animewatcher.home

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import brunodles.animewatcher.ArrayWithKeys
import brunodles.animewatcher.databinding.ItemEmptyBinding
import brunodles.animewatcher.databinding.ItemEpisodeBinding
import brunodles.animewatcher.databinding.ItemUnknownBinding
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.loadImageInto
import brunodles.animewatcher.persistence.Firebase
import brunodles.rxfirebase.EventType
import brunodles.rxfirebase.TypedEvent
import brunodles.rxfirebase.singleObservable
import brunodles.rxfirebase.typedChildObserver
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

typealias OnItemClick<ITEM_TYPE> = (ITEM_TYPE) -> Unit

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_UNKNOWN = 0
        const val TYPE_EMPTY = 1
        const val TYPE_EPISODE = 2
        const val TYPE_LINK = 3

        const val TAG = "HomeAdapter"
    }

    private val list = ArrayWithKeys<String, Any>()
    private var layoutInflater: LayoutInflater? = null
    private var onEpisodeClickListener: WeakReference<OnItemClick<Episode>>? = null
    private var onLinkClickListener: WeakReference<OnItemClick<String>>? = null

    fun setEpisodeClickListener(listener: OnItemClick<Episode>) {
        onEpisodeClickListener = WeakReference(listener)
    }

    fun setLinkClickListener(listener: OnItemClick<String>) {
        onLinkClickListener = WeakReference(listener)
    }

    override fun getItemCount(): Int = if (list.isEmpty()) 1 else list.size

    //    fun clear() {
    //        list.clear()
    //        notifyDataSetChanged()
    //    }
    //
    //    fun setItems(items: List<Any>) {
    //        list.clear()
    //        list.addAll(items)
    //        notifyDataSetChanged()
    //    }
    //
    //    fun add(item: Any) {
    //        list.add(item)
    //        notifyItemInserted(list.size)
    //    }
    //
    //    fun remove(item: Any) {
    //        val index = list.indexOf(item)
    //        list.remove(index)
    //        notifyItemRemoved(index)
    //    }
    //
    //    fun addAll(items: List<Any>) {
    //        val startIndex = list.size + 1
    //        list.addAll(items)
    //        notifyItemRangeInserted(startIndex, items.size)
    //    }

    override fun getItemViewType(position: Int): Int {
        if (list.isEmpty()) return TYPE_EMPTY
        return when (list[position]) {
            is Episode -> TYPE_EPISODE
            is String -> TYPE_LINK
            else -> TYPE_UNKNOWN
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            TYPE_EPISODE -> (holder as EpisodeHolder).let {
                it.onBind(list[position] as Episode)
                it.clickListener = onEpisodeClickListener
            }
            TYPE_LINK -> (holder as LinkHolder).let {
                it.onBind(list[position] as String)
                it.clickListener = onLinkClickListener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val context = parent?.context
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(context)

        return when (viewType) {
            TYPE_EMPTY -> EmptyHolder(ItemEmptyBinding.inflate(layoutInflater!!, parent, false))
            TYPE_EPISODE -> EpisodeHolder(ItemEpisodeBinding.inflate(layoutInflater!!, parent, false))
            TYPE_LINK -> LinkHolder(ItemUnknownBinding.inflate(layoutInflater!!, parent, false))
            else -> UnknownHolder(ItemUnknownBinding.inflate(layoutInflater!!, parent, false))
        }
    }

    open class ViewHolder<out BINDER : ViewDataBinding, ITEM : Any>(protected val binder: BINDER)
        : RecyclerView.ViewHolder(binder.root) {

        var clickListener: WeakReference<OnItemClick<ITEM>>? = null

        open fun onBind(item: ITEM) {
            binder.root.setOnClickListener {
                Log.d(TAG, "onBind: ${clickListener?.get()}")
                clickListener?.get()?.invoke(item)
            }
        }
    }

    class EmptyHolder(binder: ItemEmptyBinding) : ViewHolder<ItemEmptyBinding, Any>(binder)
    class UnknownHolder(binder: ItemUnknownBinding) : ViewHolder<ItemUnknownBinding, Any>(binder)
    class EpisodeHolder(binder: ItemEpisodeBinding) :
            ViewHolder<ItemEpisodeBinding, Episode>(binder) {
        override fun onBind(item: Episode) {
            super.onBind(item)
            binder.description.text = "${item.number} - ${item.description}"
            loadImageInto(item.image, binder.image)
        }
    }

    class LinkHolder(binder: ItemUnknownBinding) : ViewHolder<ItemUnknownBinding, String>(binder) {
        override fun onBind(item: String) {
            super.onBind(item)
            binder.text.text = item
            binder.text.setOnClickListener { clickListener?.get()?.invoke(item) }
        }
    }

    fun setUser(user: FirebaseUser) {
        Firebase.history(user)
                .limitToLast(100)
                .typedChildObserver(String::class.java)
                .subscribeOn(Schedulers.io())
                .flatMap { link ->
                    Firebase.videoRef(link.element)
                            .singleObservable(Episode::class.java)
                            .map { TypedEvent(link.event, it, link.key) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            when (it.event) {
                                EventType.CHANGED -> {
                                    val index = list.replace(it.element, it.key)
                                    if (index >= 0) notifyItemChanged(index)
                                }
                                EventType.MOVED -> TODO()
                                EventType.ADDED -> {
                                    val previousSize = list.size
                                    val index = list.add(it.element, it.key)
                                    if (index >= 0 && previousSize == 0)
                                        notifyItemChanged(index)
                                    else
                                        notifyItemInserted(index)
                                }
                                EventType.REMOVED -> {
                                    val index = list.removeByKey(it.key)
                                    if (list.isEmpty())
                                        notifyItemChanged(0)
                                    else
                                        notifyItemRemoved(index)
                                }
                            }
                        },
                        onError = {
                            Log.e(TAG, "setUser: ", it)
                        }
                )
    }
}
