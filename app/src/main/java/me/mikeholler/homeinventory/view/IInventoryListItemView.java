package me.mikeholler.homeinventory.view;

import android.view.View;

/**
 * Inventory list item interface.
 */
public interface IInventoryListItemView {

    /**
     * Set the view to show the inventory item's name.
     *
     * @param aItemName the item's name
     */
    void setItemName(final String aItemName);

    /**
     * Set the click event listener.
     *
     * @param aListener the listener
     */
    void setOnClickListener(final View.OnClickListener aListener);

}
