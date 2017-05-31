package brunodles.animewatcher

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*

class GenericAdapter<ITEM, BINDER : ViewDataBinding>(
        @LayoutRes private val layoutId: Int,
        private val onBind: (ViewHolder<BINDER>, ITEM, Int) -> Unit) :
        RecyclerView.Adapter<GenericAdapter.ViewHolder<BINDER>>() {
    var layoutInflater: LayoutInflater? = null

    var list: List<ITEM> = Collections.emptyList()
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, index: Int): ViewHolder<BINDER> {
        val context = viewGroup?.context
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<BINDER>(layoutInflater, layoutId, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<BINDER>?, index: Int) {
        if (holder != null) onBind(holder, list[index], index)
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder<BINDER : ViewDataBinding>(val binder: BINDER) : RecyclerView.ViewHolder(binder.root)
}