package me.mikeholler.homeinventory.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.mikeholler.homeinventory.R;

/**
 * List view containing items being tracked by the inventory system.
 */
public final class InventoryListItemView extends RelativeLayout
        implements IInventoryListItemView {

    /**
     * View holding the inventory item's name.
     */
    private TextView mItemNameView;

    /**
     * Constructor.
     *
     * @param context the activity context
     */
    public InventoryListItemView(final Context context) {
        super(context);
        init();
    }

    /**
     * Constructor.
     *
     * @param context the activity context
     * @param attrs the layout attributes
     */
    public InventoryListItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructor.
     *
     * @param context the activity context
     * @param attrs the layout attributes
     * @param defStyle the default style
     */
    public InventoryListItemView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Initialize the view.
     */
    public void init() {
        inflate(getContext(), R.layout.view_inventory_list_item, this);
        mItemNameView = (TextView) findViewById(R.id.itemName);
    }

    @Override
    public void setItemName(final String itemName) {
        mItemNameView.setText(itemName);
    }

}
