package brunodles.animewatcher.home

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import brunodles.animewatcher.databinding.ItemEpisodeBinding
import brunodles.animewatcher.databinding.ItemLoginBinding
import brunodles.animewatcher.databinding.ItemUnknownBinding
import brunodles.animewatcher.explorer.PageExplorer

typealias OnItemClick<ITEM_TYPE> = (ITEM_TYPE) -> Unit


class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val TYPE_UNKNOWN = 0
        val TYPE_LOGIN = 1
        val TYPE_EPISODE = 2
        val TYPE_LINK = 3
    }

    private val list: ArrayList<Any> = ArrayList()
    private var layoutInflater: LayoutInflater? = null
    var onPageExplorerClickListener: OnItemClick<PageExplorer>? = null
    var onLinkClickListener: OnItemClick<String>? = null
    var onLoginClickListener: OnItemClick<LoginRequest>? = null

    override fun getItemCount(): Int = list.size

    fun setItems(items: List<Any>) {
        list.clear()
        list.addAll(items)
    }

    fun add(item: Any) = list.add(item)

    fun addAll(items: List<Any>) = list.addAll(items)

//    fun setOnClickListener()

    override fun getItemViewType(position: Int): Int = when (list[position]) {
        is LoginRequest -> TYPE_LOGIN
        is PageExplorer -> TYPE_EPISODE
        is String -> TYPE_LINK
        else -> TYPE_UNKNOWN
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
//        if (holder is ViewHolder<*, *>) {
////            holder.clickListener = clickListener
////            holder.onBind(list[position])
//        }
        when (getItemViewType(position)) {
            TYPE_LOGIN -> (holder as LoginHolder).let {
                it.onBind(list[position] as LoginRequest)
                it.clickListener = onLoginClickListener
            }
            TYPE_EPISODE -> (holder as EpisodeHolder).onBind(list[position] as PageExplorer)
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
            TYPE_LOGIN -> LoginHolder(ItemLoginBinding.inflate(layoutInflater!!, parent, false))
            TYPE_EPISODE -> EpisodeHolder(ItemEpisodeBinding.inflate(layoutInflater!!, parent, false))
            TYPE_LINK -> LinkHolder(ItemUnknownBinding.inflate(layoutInflater!!, parent, false))
            else -> UnknownHolder(ItemUnknownBinding.inflate(layoutInflater!!, parent, false))
        }
    }

    open class ViewHolder<out BINDER : ViewDataBinding, ITEM : Any>(protected val binder: BINDER)
        : RecyclerView.ViewHolder(binder.root) {

        var clickListener: OnItemClick<ITEM>? = null

        open fun onBind(item: ITEM) {
            binder.root.setOnClickListener { clickListener?.invoke(item) }
        }
    }

    class UnknownHolder(binder: ItemUnknownBinding) : ViewHolder<ItemUnknownBinding, Any>(binder)
    class LoginHolder(binder: ItemLoginBinding) : ViewHolder<ItemLoginBinding, LoginRequest>(binder)
    class EpisodeHolder(binder: ItemEpisodeBinding) : ViewHolder<ItemEpisodeBinding, PageExplorer>(binder)

    class LinkHolder(binder: ItemUnknownBinding) : ViewHolder<ItemUnknownBinding, String>(binder) {
        override fun onBind(item: String) {
            super.onBind(item)
            binder.text.text = item
            binder.text.setOnClickListener { clickListener?.invoke(item) }
        }
    }

    fun removeLogin() {
        list.filter { it is LoginRequest }.forEach {
            val index = list.indexOf(it)
            if (list.remove(it))
                notifyItemRemoved(index)
        }
    }

//    @FunctionalInterface interface OnItemClick<ITEM_TYPE : Any> {
//        fun onItemClick(item: ITEM_TYPE)
//    }
}

