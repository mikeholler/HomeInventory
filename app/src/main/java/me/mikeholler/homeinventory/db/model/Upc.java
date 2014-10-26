package me.mikeholler.homeinventory.db.model;

import io.realm.RealmObject;

/**
 * Realm model describing a upc.
 */
@SuppressWarnings("checkstyle:designforextension")
public class Upc extends RealmObject {

    /**
     * A upc.
     */
    private String upc;

    /**
     * Get the upc.
     *
     * @return the upc
     */
    public String getUpc() {
        return upc;
    }

    /**
     * Set the upc.
     *
     * @param aUpc the upc
     */
    public void setUpc(final String aUpc) {
        upc = aUpc;
    }
}
