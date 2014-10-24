package me.mikeholler.homeinventory.util;

import android.widget.BaseAdapter;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * A CursorAdapter-like Adapter for RealmResults.
 *
 * @param <E> the realm object type
 */
public abstract class RealmAdapter<E extends RealmObject> extends BaseAdapter {

    /**
     * The data backing this adapter.
     */
    private RealmResults<E> results;

    /**
     * Set the data backing the list view.
     *
     * @param aResults the results of a realm query
     */
    public final void setResults(final RealmResults<E> aResults) {
        this.results = aResults;
        notifyDataSetChanged();
    }

    @Override
    public final int getCount() {
        return results == null ? 0 : results.size();
    }

    @Override
    public final E getItem(final int aPosition) {
        return results == null ? null : results.get(aPosition);
    }

    @Override
    public long getItemId(final int position) {
        return -1;
    }

}
