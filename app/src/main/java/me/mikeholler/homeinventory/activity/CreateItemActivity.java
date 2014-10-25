package me.mikeholler.homeinventory.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import me.mikeholler.homeinventory.R;
import me.mikeholler.homeinventory.db.model.Item;
import me.mikeholler.homeinventory.db.model.Transaction;
import me.mikeholler.homeinventory.view.ICreateItemView;

/**
 * Activity for creating items (intended for smaller devices).
 */
public final class CreateItemActivity extends Activity
        implements ICreateItemView.OnScanItemUpcListener,
        ICreateItemView.OnCreateItemListener {

    /**
     * Create item view.
     */
    private ICreateItemView createView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        createView = (ICreateItemView) findViewById(R.id.itemCreateView);
        createView.setOnScanUpcListener(this);
        createView.setOnCreateListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScanItemUpc() {

    }

    @Override
    public void onCreateItem() {
        new CreateItemAsyncTask().execute();
    }

    private class CreateItemAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(final Void... aVoids) {
            final DateTime now = DateTime.now();
            final int quantity = createView.getInitialQuantity();
            final Realm realm = Realm.getInstance(CreateItemActivity.this);
            realm.beginTransaction();

            final Transaction transaction = realm.createObject(Transaction.class);
            transaction.setDateTime(now.toDate());
            transaction.setQuantity(quantity);

            final Item item = realm.createObject(Item.class);
            item.setId(UUID.randomUUID().toString());
            item.setName(createView.getName());
            item.setUpc(createView.getUpc());
            item.setQuantity(quantity);
            item.setCreated(now.toDate());
            item.getTransactions().add(transaction);

            realm.commitTransaction();
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            setResult(RESULT_OK);
            finish();
        }
    }

}
