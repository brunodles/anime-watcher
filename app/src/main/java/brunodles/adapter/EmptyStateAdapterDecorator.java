package brunodles.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

@SuppressWarnings("unchecked")
public class EmptyStateAdapterDecorator extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_EMPTY_STATE = -1;
    private final RecyclerView.Adapter inner;
    private final int typeEmptyState;
    private final BindingProvider emptyStateBindingProvider;
    private int lastItemCount;

    public EmptyStateAdapterDecorator(final RecyclerView.Adapter inner, int typeEmptyState,
            BindingProvider emptyStateBindingProvider) {
        super();
        this.inner = inner;
        this.typeEmptyState = typeEmptyState;
        this.emptyStateBindingProvider = emptyStateBindingProvider;
        inner.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                notifyItemRangeChanged(positionStart, itemCount, payload);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (lastItemCount == 0) {
                    notifyItemChanged(0);
                    notifyItemRangeInserted(1, itemCount - 1);
                } else {
                    notifyItemRangeInserted(positionStart, itemCount);
                }
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                if (lastItemCount <= itemCount) {
                    notifyItemChanged(0);
                    notifyItemRangeRemoved(positionStart, itemCount - 1);
                } else {
                    notifyItemRangeRemoved(positionStart, itemCount);
                }
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    public EmptyStateAdapterDecorator(RecyclerView.Adapter inner,
            BindingProvider emptyStateBindingProvider) {
        this(inner, TYPE_EMPTY_STATE, emptyStateBindingProvider);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == typeEmptyState)
            return emptyStateBindingProvider.onCreateViewHolder(parent, viewType);
        else
            return inner.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == typeEmptyState)
            emptyStateBindingProvider.onBindViewHolder(holder, position);
        else
            inner.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        lastItemCount = inner.getItemCount();
        if (lastItemCount == 0)
            return 1;
        return lastItemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (lastItemCount == 0)
            return typeEmptyState;
        return inner.getItemViewType(position);
    }

    //    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
//        inner.onBindViewHolder(holder, position, payloads);
//    }
    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        inner.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return inner.getItemId(position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        inner.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return inner.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        inner.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        inner.onViewDetachedFromWindow(holder);
    }

//    @Override
//    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
//        inner.registerAdapterDataObserver(observer);
//    }

//    @Override
//    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
//        inner.unregisterAdapterDataObserver(observer);
//    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        inner.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        inner.onDetachedFromRecyclerView(recyclerView);
    }

    public interface BindingProvider<VH extends RecyclerView.ViewHolder> {

        VH onCreateViewHolder(ViewGroup parent, int viewType);

        void onBindViewHolder(VH holder, int position);
    }
}
