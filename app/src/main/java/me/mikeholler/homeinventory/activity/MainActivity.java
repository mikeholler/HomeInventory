package me.mikeholler.homeinventory.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.mikeholler.homeinventory.R;
import me.mikeholler.homeinventory.adapter.ItemRecyclerAdapter;
import me.mikeholler.homeinventory.db.model.Item;
import me.mikeholler.homeinventory.db.model.Transaction;
import me.mikeholler.homeinventory.db.model.Upc;
import me.mikeholler.homeinventory.util.RealmRecyclerAdapter;
import me.mikeholler.homeinventory.zxing.IntentIntegrator;
import me.mikeholler.homeinventory.zxing.IntentResult;


/**
 * Activity that allows the user to select an inventory item for further information.
 */
public final class MainActivity extends Activity
        implements ItemRecyclerAdapter.OnItemClickListener,
        View.OnClickListener {

    /**
     * Tag for use with {@code android.util.Log}.
     */
    private static final String LOG_TAG = MainActivity.class.getCanonicalName();

    /**
     * Scan action for zxing barcode scanning.
     */
    private static final String EXTRA_SCAN_ACTION = "EXTRA_SCAN_ACTION";

    /**
     * Scanning an item into the system.
     * <p>
     * Possible value for {@link #EXTRA_SCAN_ACTION}.
     */
    private static final int SCAN_ACTION_IN = 0;

    /**
     * Scanning an item out of the system.
     * <p>
     * Possible value for {@link #EXTRA_SCAN_ACTION}.
     */
    private static final int SCAN_ACTION_OUT = 1;

    /**
     * Request code for the create item activity.
     */
    private static final int REQUEST_NEW_ITEM = 0;

    /**
     * The last scan action type.
     * <p>
     * One of {@link #SCAN_ACTION_IN} or {@link #SCAN_ACTION_OUT}.
     */
    private int scanAction;

    /**
     * The list holding and displaying each inventory item in the database.
     */
    private AbsListView mItemListView;

    private RealmRecyclerAdapter<Item, ?> mItemAdapter;

    @Override
    protected void onCreate(final Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
        setContentView(R.layout.activity_main);

        mItemListView = (AbsListView) findViewById(R.id.itemList);
        mItemAdapter = new ItemRecyclerAdapter(this);
        mItemListView.setAdapter(mItemAdapter);
        refreshItemList();
        findViewById(R.id.scanIn).setOnClickListener(this);
        findViewById(R.id.scanOut).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu aMenu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, aMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem aItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = aItem.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_new_item) {
            trackNewItem();
            return true;
        }

        return super.onOptionsItemSelected(aItem);
    }

    @Override
    public void onItemClick(final Item aItem) {

    }

    /**
     * Create a new item to be tracked by the inventory system.
     */
    public void trackNewItem() {
        final Intent intent = new Intent(this, CreateItemActivity.class);
        startActivityForResult(intent, REQUEST_NEW_ITEM);
    }

    /**
     * Create a new item to be tracked by the inventory system.
     *
     * @param upc the upc to create an item for
     */
    public void trackNewItem(final String upc) {
        final Intent intent = new Intent(this, CreateItemActivity.class);
        intent.putExtra(CreateItemActivity.EXTRA_UPC, upc);
        startActivityForResult(intent, REQUEST_NEW_ITEM);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_ITEM && resultCode == RESULT_OK) {
            refreshItemList();
        } else if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
            final IntentResult zxingResult =
                    IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            final int quantity = scanAction == SCAN_ACTION_OUT ? -1 : 1;
            incrementItem(zxingResult.getContents(), quantity);
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.scanIn: scanInItem(); break;
            case R.id.scanOut: scanOutItem(); break;
            default: Log.d(LOG_TAG, "Unknown id in onClick: " + view.getId());
        }
    }

    /**
     * Increment an item.
     *
     * @param upc the upc to increment
     * @param quantity the number to increment by (can be negative)
     */
    private void incrementItem(final String upc, final int quantity) {
        if (quantity == 0) {
            return;
        }

        final Realm realm = Realm.getInstance(this);
        realm.beginTransaction();

        // TODO: fix this once realm supports queries on links
        final RealmResults<Item> items = realm.where(Item.class).findAll();
        Item existingItem = null;
        for (final Item item : items) {
            for (final Upc itemUpc : item.getUpcs()) {
                if (itemUpc.getUpc().equals(upc)) {
                    existingItem = item;
                }
            }
        }

        if (existingItem == null) {
            showUpcNotFoundOptions(upc, quantity);
        } else {
            incrementItem(existingItem, quantity);
        }

        realm.commitTransaction();
    }

    /**
     * Increment an item.
     * <p>
     * Assumes a realm transaction has been started already.
     *
     * @param existingItem the item to increment
     * @param quantity the number to increment by (can be negative)
     */
    private void incrementItem(final Item existingItem, final int quantity) {
        final int totalQuantity = existingItem.getQuantity() + quantity;

        if (totalQuantity >= 0) {
            final Realm realm = Realm.getInstance(this);
                            final Transaction transaction = realm.createObject(Transaction.class);
                            transaction.setQuantity(quantity);
                            transaction.setDateTime(DateTime.now().toDate());

                            existingItem.getTransactions().add(transaction);
                            existingItem.setQuantity(totalQuantity);
                        }

                        refreshItemList();
    }

    /**
     * Show options for handling a UPC not found problem.
     *
     * @param upc the upc
     * @param quantity the number to increment by (can be negative)
     */
    private void showUpcNotFoundOptions(final String upc, final int quantity) {
        final String[] choices = new String[]{"Link with Existing Item", "Track a New Item"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.setTitle("UPC Not Found")
                .setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        if (i == 0) {
                            showLinkUpcToExistingItemAndIncDialog(upc, quantity);
                        } else {
                            trackNewItem(upc);
                        }
                    }
                }).create();
        dialog.show();
    }

    /**
     * Link upc to another item.
     *
     * @param upc the upc to increment
     * @param quantity the number to increment by (can be negative)
     */
    private void showLinkUpcToExistingItemAndIncDialog(final String upc, final int quantity) {
        final Realm realm = Realm.getInstance(this);
        final RealmResults<Item> items = realm.where(Item.class).findAll();
        final List<String> namesList = new ArrayList<String>();

        for (final Item item : items) {
            namesList.add(item.getName());
        }

        final String[] names = namesList.toArray(new String[namesList.size()]);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Link UPC to Existing Item")
                .setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface aDialogInterface, final int i) {
                        // Add upc to an item.
                        realm.beginTransaction();
                        final Upc realmUpc = realm.createObject(Upc.class);
                        realmUpc.setUpc(upc);
                        items.get(i).getUpcs().add(realmUpc);

                        incrementItem(items.get(i), quantity);
                        realm.commitTransaction();
                    }
                }).create();
        dialog.show();
    }

    /**
     * Scan an item into the inventory system.
     */
    private void scanInItem() {
        scanAction = SCAN_ACTION_IN;
        final IntentIntegrator zxingIntegrator = new IntentIntegrator(this);
        zxingIntegrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES);
    }

    /**
     * Scan an item out of the inventory system.
     */
    private void scanOutItem() {
        scanAction = SCAN_ACTION_OUT;
        final IntentIntegrator zxingIntegrator = new IntentIntegrator(this);
        zxingIntegrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES);
    }

    /**
     * Refresh the item list using the contents of the database.
     */
    private void refreshItemList() {
        Log.d(LOG_TAG, "Refreshing item list.");
        mItemAdapter.setResults(Realm.getInstance(MainActivity.this).where(Item.class).findAll());
    }

}
