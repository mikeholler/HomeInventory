package me.mikeholler.homeinventory.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import me.mikeholler.homeinventory.R;
import me.mikeholler.homeinventory.db.model.Item;
import me.mikeholler.homeinventory.db.model.Transaction;
import me.mikeholler.homeinventory.db.model.Upc;
import me.mikeholler.homeinventory.view.ICreateItemView;
import me.mikeholler.homeinventory.zxing.IntentIntegrator;
import me.mikeholler.homeinventory.zxing.IntentResult;

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
        final IntentIntegrator zxingIntegrator = new IntentIntegrator(this);
        zxingIntegrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
            final IntentResult zxingResult =
                    IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            final String upc = zxingResult.getContents();

            if (isUpcValid(upc)) {
                Toast.makeText(CreateItemActivity.this, "UPC already in use by another item.",
                        Toast.LENGTH_SHORT).show();
                createView.setUpc("");
            } else {
                createView.setUpc(upc);
            }

        }
    }

    @Override
    public void onCreateItem() {
        new CreateItemAsyncTask().execute();
    }

    /**
     * Check if the UPC in the UI is valid.
     *
     * @param upc the upc
     *
     * @return true if valid
     */
    public boolean isUpcValid(final String upc) {
        final Realm realm = Realm.getInstance(CreateItemActivity.this);
        final long count = realm.where(Upc.class).equalTo("upc", upc).count();
        return count == 0;
    }

    /**
     * Create an item in the background and exit.
     */
    private class CreateItemAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            if (createView.getName().isEmpty()) {
                Toast.makeText(CreateItemActivity.this, "A name is required.",
                        Toast.LENGTH_SHORT).show();
                cancel(true);
            }
        }

        @Override
        protected Void doInBackground(final Void... aVoids) {
            final DateTime now = DateTime.now();
            final int quantity = createView.getInitialQuantity();
            final Realm realm = Realm.getInstance(CreateItemActivity.this);
            realm.beginTransaction();

            // Initial quantity is optional, so if it is zero there is no need to make a transaction
            final Transaction transaction;
            if (quantity != 0) {
                transaction = realm.createObject(Transaction.class);
                transaction.setDateTime(now.toDate());
                transaction.setQuantity(quantity);
            } else {
                transaction = null;
            }

            // UPCs are optional, so if one was not specified then we ignore it
            final Upc upc;
            if (!createView.getUpc().isEmpty()) {
                upc = realm.createObject(Upc.class);
                upc.setUpc(createView.getUpc());
            } else {
                upc = null;
            }

            final Item item = realm.createObject(Item.class);
            item.setId(UUID.randomUUID().toString());
            item.setName(createView.getName());
            item.setQuantity(quantity);
            item.setCreated(now.toDate());
            if (upc != null) {
                item.getUpcs().add(upc);
            }
            if (transaction != null) {
                item.getTransactions().add(transaction);
            }

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
