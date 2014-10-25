package me.mikeholler.homeinventory.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import me.mikeholler.homeinventory.R;

/**
 * Implementation of a view used to create an item.
 */
public final class CreateItemView extends RelativeLayout
        implements ICreateItemView, View.OnClickListener {

    /**
     * View holding the item name.
     */
    private TextView itemNameView;

    /**
     * View holding the item upc.
     */
    private TextView itemUpcView;

    /**
     * View holding the item quantity.
     */
    private NumberPicker itemQuantityView;

    /**
     * Listener for press on scan button.
     */
    private OnScanItemUpcListener scanItemListener;

    /**
     * Listener for press on create button.
     */
    private OnCreateItemListener createItemListener;

    /**
     * Constructor.
     *
     * @param context the activity context
     */
    public CreateItemView(final Context context) {
        super(context);
        init();
    }

    /**
     * Constructor.
     *
     * @param context the activity context
     * @param attrs the layout attributes
     */
    public CreateItemView(final Context context, final AttributeSet attrs) {
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
    public CreateItemView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Initialize the view.
     */
    public void init() {
        inflate(getContext(), R.layout.view_create_item, this);
        itemNameView = (TextView) findViewById(R.id.itemName);
        itemQuantityView = (NumberPicker) findViewById(R.id.itemQuantity);
        itemUpcView = (TextView) findViewById(R.id.itemUpc);
        findViewById(R.id.scanUpc).setOnClickListener(this);
        findViewById(R.id.itemCreate).setOnClickListener(this);

        itemQuantityView.setMinValue(0);
        itemQuantityView.setMaxValue(100);
        itemQuantityView.setValue(1);
    }

    @Override
    public String getName() {
        return itemNameView.getText().toString().trim();
    }

    @Override
    public String getUpc() {
        return itemUpcView.getText().toString().trim();
    }

    @Override
    public int getInitialQuantity() {
        return itemQuantityView.getValue();
    }

    @Override
    public void setUpc(final String upc) {
        itemUpcView.setText(upc);
    }

    @Override
    public void setOnScanUpcListener(final OnScanItemUpcListener scanItemListener) {
        this.scanItemListener = scanItemListener;
    }

    @Override
    public void setOnCreateListener(final OnCreateItemListener createItemListener) {
        this.createItemListener = createItemListener;
    }

    @Override
    public void onClick(final View aView) {
        final int id = aView.getId();
        if (id == R.id.scanUpc && scanItemListener != null) {
            scanItemListener.onScanItemUpc();
        } else if (id == R.id.itemCreate && createItemListener != null) {
            createItemListener.onCreateItem();
        }
    }
}
