package me.mikeholler.homeinventory.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import io.realm.Realm;
import io.realm.RealmResults;
import me.mikeholler.homeinventory.R;
import me.mikeholler.homeinventory.adapter.ItemRecyclerAdapter;
import me.mikeholler.homeinventory.db.model.Item;
import me.mikeholler.homeinventory.util.RealmRecyclerAdapter;


/**
 * Activity that allows the user to select an inventory item for further information.
 */
public final class MainActivity extends Activity implements ItemRecyclerAdapter.OnItemClickListener {

    /**
     * Request code for the create item activity.
     */
    private static final int REQUEST_NEW_ITEM = 0;

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

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_ITEM && resultCode == RESULT_OK) {
            refreshItemList();
        }
    }

    /**
     * Refresh the item list using the contents of the database.
     */
    private void refreshItemList() {
        mItemAdapter.setResults(Realm.getInstance(MainActivity.this).where(Item.class).findAll());
    }

}
