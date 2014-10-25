package me.mikeholler.homeinventory.view;

/**
 * Interface for creating an item.
 */
public interface ICreateItemView {

    /**
     * Get the name of the item being tracked.
     *
     * @return the name
     */
    String getName();

    /**
     * Get the UPC of the item being tracked.
     *
     * @return the upc
     */
    String getUpc();

    /**
     * Set the upc in the view.
     *
     * @param upc the item upc
     */
    void setUpc(String upc);

    /**
     * Get the initial quantity of the item being tracked.
     *
     * @return the initial quantity
     */
    int getInitialQuantity();

    /**
     * Listen for a scan upc action.
     *
     * @param scanItemListener the listener
     */
    void setOnScanUpcListener(OnScanItemUpcListener scanItemListener);

    /**
     * Listen for a save item action.
     *
     * @param createItemListener the listener
     */
    void setOnCreateListener(OnCreateItemListener createItemListener);

    /**
     * Scan item action listener.
     */
    interface OnScanItemUpcListener {

        /**
         * Called when a product upc should be scanned.
         */
        void onScanItemUpc();
    }

    /**
     * Create item action listener.
     */
    interface OnCreateItemListener {

        /**
         * Called when the view contents should be persisted as a new item.
         */
        void onCreateItem();
    }

}
