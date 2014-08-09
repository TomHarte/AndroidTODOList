package com.thomasharte.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by thomasharte on 13/07/2014.
 */
public class TodoItem implements Serializable {
    private static final long serialVersionUID = -927491755829361937L;

    private final Date date;
    private final String description;
    private final long rowID;

	private final Boolean isDone;

    // the full constructor stores the row ID; so this creates
    // the sort of items we expect to hang around
    public TodoItem(long rowID, String description, Date date, Boolean isDone) {
        super();

        this.description = description;
        this.date = date;
        this.rowID = rowID;
		this.isDone = isDone;
    }

    // a short-form constructor sets a default row ID of -1;
    // so it is for the creation of transient items
    public TodoItem(String description, Date date, Boolean isDone) {
        this(-1, description, date, isDone);
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
	public Boolean getIsDone() {
		return isDone;
	}
}
