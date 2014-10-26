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
            new ValidateUpcTask().execute(zxingResult.getContents());
        }
    }

    @Override
    public void onCreateItem() {
        new CreateItemAsyncTask().execute();
    }

    /**
     * Validation for the new upc.
     */
    private class ValidateUpcTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(final String... upcs) {
            final String upc = upcs[0];
            final Realm realm = Realm.getInstance(CreateItemActivity.this);
            final long count = realm.where(Upc.class).equalTo("upc", upc).count();
            return count == 0 ? upc : null;
        }

        @Override
        protected void onPostExecute(final String validUpc) {
            if (validUpc == null) {
                Toast.makeText(CreateItemActivity.this, "Barcode already in use by another item.",
                        Toast.LENGTH_SHORT).show();
            } else {
                createView.setUpc(validUpc);
            }
        }
    }

    /**
     * Create an item in the background and exit.
     */
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

            final Upc upc = realm.createObject(Upc.class);
            upc.setUpc(createView.getUpc());

            final Item item = realm.createObject(Item.class);
            item.setId(UUID.randomUUID().toString());
            item.setName(createView.getName());
            item.getUpcs().add(upc);
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
