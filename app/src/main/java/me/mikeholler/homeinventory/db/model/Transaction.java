package me.mikeholler.homeinventory.db.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * A transaction is an update of the inventory of an individual item.
 */
@SuppressWarnings("checkstyle:designforextension")
public class Transaction extends RealmObject {

    /** The quantity of items being transacted. */
    private int quantity;
    /** The time the transaction occurred. */
    private Date dateTime;

    /**
     * Get the quantity of items involved in this transaction.
     * <p>
     * A quantity is positive if items owned increased, negative if decreased.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set the quantity of items involved in this transaction.
     * <p>
     * A quantity is positive if items owned increased, negative if decreased.
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
