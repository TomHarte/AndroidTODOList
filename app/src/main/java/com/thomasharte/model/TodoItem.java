package com.thomasharte.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by thomasharte on 13/07/2014.
 */
public class TodoItem implements Serializable {
    private static final long serialVersionUID = -927491755829361937L;

    private Date date;
    private String description;
    private long rowID;

    // the full constructor stores the row ID; so this creates
    // the sort of items we expect to hang around
    public TodoItem(long rowID, String description, Date date) {
        super();

        this.description = description;
        this.date = date;
        this.rowID = rowID;
    }

    // a short-form constructor sets a default row ID of -1;
    // so it is for the creation of transient items
    public TodoItem(String description, Date date) {
        super();

        this.description = description;
        this.date = date;
        this.rowID = -1;
    }

    // rote getters; no setters are provided: this class is immutable
    public Date getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    public long getRowID()  {
        return rowID;
    }
}
