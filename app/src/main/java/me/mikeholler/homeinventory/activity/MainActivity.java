package me.mikeholler.homeinventory.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import io.realm.Realm;
import io.realm.RealmResults;
import me.mikeholler.homeinventory.R;
import me.mikeholler.homeinventory.adapter.ItemRecyclerAdapter;
import me.mikeholler.homeinventory.db.Item;
import me.mikeholler.homeinventory.util.RealmRecyclerAdapter;


/**
 * Activity that allows the user to select an inventory item for further information.
 */
public final class MainActivity extends Activity implements ItemRecyclerAdapter.OnItemClickListener {

    /**
     * The list holding and displaying each inventory item in the database.
     */
    private AbsListView mItemListView;

    private RealmRecyclerAdapter<Item, ?> mItemAdapter;

    @Override
    protected void onCreate(final Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
        setContentView(R.layout.activity_main);

        final Realm realm = Realm.getInstance(this);
        realm.beginTransaction();
        final Item item = realm.createObject(Item.class);
        item.setName("itemname");
        realm.commitTransaction();

        final RealmResults<Item> itemResults = realm.allObjects(Item.class);

        mItemListView = (AbsListView) findViewById(R.id.itemList);
        mItemAdapter = new ItemRecyclerAdapter(this);
        mItemListView.setAdapter(mItemAdapter);
        mItemAdapter.setResults(itemResults);
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

        return super.onOptionsItemSelected(aItem);
    }

    @Override
    public void onItemClick(final Item aItem) {

    }
}
