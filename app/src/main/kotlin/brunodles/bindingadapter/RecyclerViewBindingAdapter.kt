package brunodles.bindingadapter

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView

object RecyclerViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("app:nestedScrollingEnabled", requireAll = false)
    fun nestedScrollingEnabled(recyclerView: RecyclerView, nestedScrollingEnabled: Boolean) {
        recyclerView.isNestedScrollingEnabled = nestedScrollingEnabled
    }
}