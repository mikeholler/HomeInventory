package me.mikeholler.homeinventory.adapter;

import android.view.View;
import android.view.ViewGroup;

import me.mikeholler.homeinventory.db.model.Item;
import me.mikeholler.homeinventory.util.RealmRecyclerAdapter;
import me.mikeholler.homeinventory.view.IInventoryListItemView;
import me.mikeholler.homeinventory.view.InventoryListItemView;

/**
 * Adapter for inventory items.
 */
public final class ItemRecyclerAdapter extends RealmRecyclerAdapter<Item, ItemRecyclerAdapter.ViewHolder> {

    /**
     * View holder for item views.
     */
    protected static class ViewHolder extends RealmRecyclerAdapter.ViewHolder {

        /**
         * The held view.
         */
        private IInventoryListItemView view;

        /**
         * Constructor.
         *
         * @param aView the view being held
         */
        public ViewHolder(final View aView) {
            super(aView);
            view = (IInventoryListItemView) aView;
        }
    }

    /**
     * Click event listener.
     */
    private final OnItemClickListener listener;

    /**
     * Constructor.
     *
     * @param listener the click listener
     */
    public ItemRecyclerAdapter(final OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        // TODO: make it possible use IInventoryListItemView instead to support different screens
        return new ViewHolder(new InventoryListItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Item item = getItem(position);
        holder.view.setItemName(item.getName());
        holder.view.setItemQuantity(item.getQuantity());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }

    /**
     * Called when item is clicked.
     */
    public interface OnItemClickListener {
        /**
         * Row click event.
         *
         * @param aItem the item backed
         */
        void onItemClick(final Item aItem);
    }
}
