package me.mikeholler.homeinventory.db;

import java.util.Date;

import io.realm.RealmObject;

/**
 * A transaction is an update of the inventory of an individual item.
 */
@SuppressWarnings("checkstyle:designforextension")
public class Transaction extends RealmObject {

//    /** An in transaction, or an increase in the current inventory. */
//    public static final int TYPE_IN = 0;
//    /** An out transaction, or decrease in the current inventory. */
//    public static final int TYPE_OUT = 1;

    /**
     * The type of the transaction.
     * <p>
     * One of {@link #TYPE_IN} or {@link #TYPE_OUT}.
     */
    private int type;
    /** The unique id of the item involved in this transaction. */
    private String itemId;
    /** The quantity of items being transacted in the transaction. */
    private int quantity;
    /** The time the transaction occurred. */
    private Date dateTime;

    /**
     * Get the type of transaction.
     *
     * @return one of {@link #TYPE_IN} or {@link #TYPE_OUT}
     */
    public int getType() {
        return type;
    }

    /**
     * Set the type of transaction.
     *
     * @param type one of {@link #TYPE_IN} or {@link #TYPE_OUT}
     */
    public void setType(final int type) {
        this.type = type;
    }

    /**
     * Get the id of the item involved in the transaction.
     *
     * @return the item id
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Set the id of the item involved in the transaction.
     *
     * @param itemId the item id
     */
    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    /**
     * Get the quantity of items involved in this transaction.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set the quantity of items involved in this transaction.
     *
     * @param quantity the quantity
     */
    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    /**
     * Get the time this transaction occurred.
     *
     * @return the transaction time
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Set the time this transaction occurred.
     *
     * @param dateTime the transaction time
     */
    public void setDateTime(final Date dateTime) {
        this.dateTime = dateTime;
    }
}
