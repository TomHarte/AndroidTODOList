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

    public TodoItem(String description, Date date) {
        super();
        this.description = description;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
