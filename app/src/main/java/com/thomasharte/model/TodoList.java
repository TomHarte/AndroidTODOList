package com.thomasharte.model;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
    As per the comments in MainActivity; I factored out the model assuming it
    would have an independent relationship with my two activities. Then I found
    out that activities can have return results and therefore that this was
    completely unnecessary. But it was done by then. This is why some more formal
    lessons would be to my benefit...
 */
public class TodoList extends SQLiteOpenHelper {

    private ArrayList<String> items;
    private static TodoList instance = null;

    private static final String TABLE_NAME = "todoItems";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String DATE_COLUMN = "date";

    // I assumed I'd be creating a singleton to provide a central
    // store for my two activities, hence my stab at getInstance...
    public static TodoList getInstance(Context context) {
        if(instance != null) return instance;
        return instance = new TodoList(context);
    }

    // a hopefully vanilla constructor; it stores the context given
    // (as we'll need it to perform getFilesDir) and does the initial
    // restore of items from disk
    public TodoList(Context applicationContext) {
        super(applicationContext, "todoList", null, 1);
        readItems();
    }

    // one table, that's all we've got, with columns for description and date
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + TABLE_NAME  + " (" + DESCRIPTION_COLUMN  + " text, " + DATE_COLUMN + " integer);");
    }

    // we'll just drop the table and then create it afresh — no attempt is
    // made at migration
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    // these are directly form the tutorial but for the use of the
    // instance variable applicationContext; getFilesDir isn't directly
    // available because this class does not inherit from Context
    private void readItems() {
        items = new ArrayList<String>();

        String[] columns = {DESCRIPTION_COLUMN, DATE_COLUMN};
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, columns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            items.add(cursor.getString(0));
            cursor.moveToNext();
        }
    }

    private void saveItems() {
        // yuck: delete everything and start again
        getWritableDatabase().delete(TABLE_NAME, null, null);

        ContentValues values = new ContentValues();
        for(String string : items)
        {
            values.put(DESCRIPTION_COLUMN, string);
            getWritableDatabase().insert(TABLE_NAME, null, values);
        }
    }

    // the following are what would be used in totality were proper
    // data encapsulation being applied; they vend, add and update
    // items, ensuring a write to disk when necessary
    public String getItem(int index) {
        return items.get(index);
    }

    public void addItem(String item) {
        items.add(item);
        saveItems();
    }

    public void removeItem(int index) {
        items.remove(index);
        saveItems();
    }

    public void setItem(int index, String item){
        items.set(index, item);
        saveItems();
    }

    public int getCount() {
        return items.size();
    }
}
