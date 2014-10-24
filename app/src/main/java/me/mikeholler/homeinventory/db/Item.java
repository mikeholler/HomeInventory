package me.mikeholler.homeinventory.db;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Realm model for an item that is registered in the inventory system.
 */
@SuppressWarnings("checkstyle:designforextension")
public class Item extends RealmObject {

    /** The unique identifier for the item. */
    private String id;
    /** The name of the item. */
    private String name;
    /** The item's UPC. */
    private String upc;
    /** The date the item was added to the database. */
    private Date created;

    /**
     * Get the unique item identifier.
     *
     * @return the item identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Set the unique item identifier.
     *
     * @param id the item identifier
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get the item's name.
     *
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the item's name.
     *
     * @param name the item's name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get the UPC code for the item.
     *
     * @return the UPC code
     */
    public String getUpc() {
        return upc;
    }

    /**
     * Set the UPC code for the item.
     *
     * @param upc the UPC code
     */
    public void setUpc(final String upc) {
        this.upc = upc;
    }

    /**
     * Get the date this item was created.
     *
     * @return the date of creation
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Set the date the item was created.
     *
     * @param created the date of creation
     */
    public void setCreated(final Date created) {
        this.created = created;
    }
}
