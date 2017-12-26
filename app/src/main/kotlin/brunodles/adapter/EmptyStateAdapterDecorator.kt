//package brunodles.adapter
//
//import android.support.v7.widget.RecyclerView
//import android.view.ViewGroup
//
//class EmptyStateAdapterDecorator(val inner: RecyclerView.Adapter<RecyclerView.ViewHolder>)
//    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver?) {
//        inner.registerAdapterDataObserver(observer)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any>?) {
//        inner.onBindViewHolder(holder, position, payloads)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
//        inner.onBindViewHolder(holder, position)
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return inner.getItemViewType(position)
//    }
//
//    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
//        inner.onViewAttachedToWindow(holder)
//    }
//
//    override fun getItemId(position: Int): Long {
//        return inner.getItemId(position)
//    }
//
//    override fun getItemCount(): Int {
//        return inner.itemCount
//    }
//
//    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
//        inner.onAttachedToRecyclerView(recyclerView)
//    }
//
//    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
//        inner.onViewRecycled(holder)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
//        return inner.onCreateViewHolder(parent, viewType)
//    }
//
//    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
//        inner.onDetachedFromRecyclerView(recyclerView)
//    }
//
//    override fun setHasStableIds(hasStableIds: Boolean) {
//        inner.setHasStableIds(hasStableIds)
//    }
//
//    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder?) {
//        inner.onViewDetachedFromWindow(holder)
//    }
//
//    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver?) {
//        inner.unregisterAdapterDataObserver(observer)
//    }
//
//    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder?): Boolean {
//        return inner.onFailedToRecycleView(holder)
//    }
//}