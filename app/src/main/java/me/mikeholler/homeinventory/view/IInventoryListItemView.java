package me.mikeholler.homeinventory.view;

import android.view.View;

/**
 * Inventory list item interface.
 */
public interface IInventoryListItemView {

    /**
     * Set the view to show the inventory item's name.
     *
     * @param itemName the item's name
     */
    void setItemName(final String itemName);

    void setItemQuantity(final int quantity);

    /**
     * Set the click event listener.
     *
     * @param listener the listener
     */
    void setOnClickListener(final View.OnClickListener listener);

}
