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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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
                .addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                        //                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                        //                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildChanged(linkData: DataSnapshot?, p1: String?) {
                        linkData?.getValue(String::class.java)?.let { link ->
                            Firebase.videoRef(link).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                override fun onCancelled(p0: DatabaseError?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(episodeData: DataSnapshot?) {
                                    episodeData?.getValue(Episode::class.java)?.let {
                                        val index = list.replace(it, linkData.key)
                                        if (index >= 0)
                                            notifyItemChanged(index)
                                    }
                                }

                            })
                        }
                    }

                    override fun onChildAdded(linkData: DataSnapshot?, p1: String?) {
                        Log.d(TAG, "onChildAdded: key = ${linkData?.key}")
                        linkData?.getValue(String::class.java)?.let { link ->
                            Firebase.videoRef(link).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                override fun onCancelled(p0: DatabaseError?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(episodeData: DataSnapshot?) {
                                    episodeData?.getValue(Episode::class.java)?.let {
                                        val previousSize = list.size
                                        val index = list.add(it, linkData.key)
                                        if (index >= 0 && previousSize == 0)
                                            notifyItemChanged(index)
                                        else
                                            notifyItemInserted(index)
                                    }
                                }

                            })
                        }
                    }

                    override fun onChildRemoved(p0: DataSnapshot?) {
                        //                        p0?.getValue(String::class.java)?.let { link ->
                        p0?.key?.let {
                            val index = list.removeByKey(it)
                            Log.d(TAG, "onChildRemoved: key = $it at $index")
                            notifyItemRemoved(index)
                        }
                    }

                })
    }
}
