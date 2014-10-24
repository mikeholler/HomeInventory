package me.mikeholler.homeinventory.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * RecyclerView.Adapter-like Adapter that binds RealmResults to ListView.
 *
 * This source was found on
 * <a href="https://gist.github.com/xingrz/95e4aa31e386b3629bc5">Github.</a>
 *
 * @param <E> the element
 * @param <VH> the view holder
 *
 * @author xingrz
 */
public abstract class RealmRecyclerAdapter<E extends RealmObject, VH extends RealmRecyclerAdapter.ViewHolder>
        extends RealmAdapter<E> {

    /**
     * Simple view holder pattern class.
     */
    public abstract static class ViewHolder {

        /**
         * The item view being held.
         */
        private final View mView;

        /**
         * Basic mView holder constructor.
         *
         * @param aView the list mView item
         */
        public ViewHolder(final View aView) {
            this.mView = aView;
            this.mView.setTag(this);
        }

        /**
         * Get the view being held.
         *
         * @return the view
         */
        public final View getView() {
            return mView;
        }
    }

    @Override
    public final View getView(final int aPosition, View aConvertView, final ViewGroup aParent) {
        if (aConvertView == null) {
            aConvertView = onCreateViewHolder(aParent, getItemViewType(aPosition)).getView();
        } else {
            onViewRecycled(getViewHolder(aConvertView));
        }

        onBindViewHolder(getViewHolder(aConvertView), aPosition);

        return aConvertView;
    }

    /**
     * Create a view holder.
     *
     * @param aParent the parent view
     * @param aViewType the view type
     *
     * @return the view holder
     */
    public abstract VH onCreateViewHolder(final ViewGroup aParent, final int aViewType);

    /**
     * Bind the view holder to a view.
     *
     * @param aHolder the view holder
     * @param aPosition the view's position in the layout
     */
    public abstract void onBindViewHolder(final VH aHolder, final int aPosition);

    /**
     * Recycle an old view.
     *
     * @param aHolder the old view
     */
    public void onViewRecycled(final VH aHolder) { }


    /**
     * Get the current view holder from a view.
     *
     * @param aView the view with a view holder tag
     *
     * @return the view's view holder
     */
    @SuppressWarnings("unchecked")
    private VH getViewHolder(final View aView) {
        return (VH) aView.getTag();
    }

}
