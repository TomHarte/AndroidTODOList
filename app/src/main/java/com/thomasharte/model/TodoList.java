package com.thomasharte.model;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
/*
    As per the comments in MainActivity; I factored out the model assuming it
    would have an independent relationship with my two activities. Then I found
    out that activities can have return results and therefore that this was
    completely unnecessary. But it was done by then. This is why some more formal
    lessons would be to my benefit...
 */
public class TodoList {

    private ArrayList<String> items;
    private Context applicationContext;
    private static TodoList instance = null;

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
        super();
        this.applicationContext = applicationContext;
        readItems();
    }

    // these are directly form the tutorial but for the use of the
    // instance variable applicationContext; getFilesDir isn't directly
    // available because this class does not inherit from Context
    private void readItems() {
        File filesDir = applicationContext.getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
            e.printStackTrace();
        }
    }

    private void saveItems() {
        File filesDir = applicationContext.getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // this is where the data encapsulation falls off a cliff; despite
    // asking that external actors use our add, remove and set, the
    // direct array is exposed. That's because I'm inexperienced at
    // dealing with adaptors and was unsure what else to do
    public ArrayList<String> getItems() {
        return items;
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
}
