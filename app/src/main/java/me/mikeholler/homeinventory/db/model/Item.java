package me.mikeholler.homeinventory.db.model;

import java.util.Date;

import io.realm.RealmList;
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
    private RealmList<Upc> upcs;
    /** The number of items in stock. */
    private int quantity;
    /** The date the item was added to the database. */
    private Date created;
    /** List of transactions. */
    private RealmList<Transaction> transactions;

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
     * Get the UPC codes for the item.
     *
     * @return the UPC codes
     */
    public RealmList<Upc> getUpcs() {
        return upcs;
    }

    /**
     * Set the UPC codes for the item.
     *
     * @param upcs the UPC codes
     */
    public void setUpcs(final RealmList<Upc> upcs) {
        this.upcs = upcs;
    }

    /**
     * Get the quantity of items in stock.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set the quantity of items in stock.
     *
     * @param aQuantity the quantity
     */
    public void setQuantity(final int aQuantity) {
        quantity = aQuantity;
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

    /**
     * Get all transactions this item was involved in.
     *
     * @return the transactions
     */
    public RealmList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Set the transactions the item was involved in.
     *
     * @param aTransactions the transactions
     */
    public void setTransactions(final RealmList<Transaction> aTransactions) {
        transactions = aTransactions;
    }

}
