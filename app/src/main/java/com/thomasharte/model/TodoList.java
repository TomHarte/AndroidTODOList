package com.thomasharte.model;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
	This has been factored out as it's a model thing; it also
	proved to be the easiest way to combine array-style usage
	with an updating SQL store. At this point I'm unclear
	on the normal Android idioms for how a SQLiteOpenHelper ties
	into things so possibly I've created a design
 */
public class TodoList extends SQLiteOpenHelper {

	// the item list is kept in memory as an array rather than
	// being live-fetched from SQL
	private ArrayList<TodoItem> items;

	// this is for the singleton instance
	private static TodoList instance = null;

	// table and column names; fairly uncontroversial
	private static final String TABLE_NAME = "todoItems";
	private static final String DESCRIPTION_COLUMN = "description";
	private static final String DATE_COLUMN = "date";
	private static final String DONE_COLUMN = "done";
	private static final String ID_COLUMN = "ROWID";	// this is the one SQLite provides for us

	private static final int databaseVersion = 5;

	// I've designed this as a singleton, should the store
	// be needed by several activities, hence this stab at getInstance...
	public static TodoList getInstance(Context context) {
		if(instance != null) return instance;
		return instance = new TodoList(context);
	}

	// a fairly vanilla constructor; it calls the appropriate super
	// constructor and does the initial restore of items
	private TodoList(Context applicationContext) {
		super(applicationContext, "todoList", null, databaseVersion);
		readItems();
	}

	// one table, that's all we've got, with columns for description and date
	// (plus the implicit ROWID column, that SQLite gives us for free)
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(
					"create table " + TABLE_NAME  + " (" +
					ID_COLUMN + " integer primary key autoincrement, " + 	// we'll assume elsewhere that ROWID is autoincrement, e.g.
																			// by assuming that sorting by ROWID will return insert order,
																			// so let's stipulate that it is
					DESCRIPTION_COLUMN  + " text, " +
					DATE_COLUMN + " integer, " +
					DONE_COLUMN + " integer " +
					");");
	}

	// we'll just drop the table and then create it afresh — no attempt is
	// made at migration
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_NAME);
		onCreate(db);
	}

	// readItems restores every item in the SQL store into memory. This is because
	// I'm unclear on the cost/performance effects and am gambling on a small
	// list of TODO items
	private void readItems() {
		items = new ArrayList<TodoItem>();

		// simple enough: order by the [autoincrementing] ID column,
		// populate our TODO items with the ID, description and date
		String[] columns = {ID_COLUMN, DESCRIPTION_COLUMN, DATE_COLUMN, DONE_COLUMN};
		Cursor cursor = getReadableDatabase().query(TABLE_NAME, columns, null, null, null, null, ID_COLUMN, null);

		cursor.moveToFirst();
		Date insertionDate = new Date(0);
		while(!cursor.isAfterLast()) {
			items.add(new TodoItem(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)), cursor.getInt(3) > 0, insertionDate));
			cursor.moveToNext();
		}
	}

	// the next two are a couple of helpers for mapping TodoItems to
	// SQL values
	private ContentValues contentValuesForItem(TodoItem item) {
		ContentValues values = new ContentValues();
		values.put(DESCRIPTION_COLUMN, item.getDescription());
		values.put(DATE_COLUMN, item.getDate().getTime());
		values.put(DONE_COLUMN, item.getIsDone() ? 1 : 0);
		return values;
	}

	private String whereClauseForItem(TodoItem item) {
		return ID_COLUMN + " = " + item.getRowID();
	}

	// the following act a lot like a standard Array, but
	// parrot material to the SQL store upon any update
	// of the array
	public TodoItem getItem(int index) {
		return items.get(index);
	}

	public int getCount() {
		return items.size();
	}

	public void addItem(TodoItem item) {
		// add the thing, then create the recorded TodoItem according to
		// the row ID we obtain with the insert
		long rowID = getWritableDatabase().insert(TABLE_NAME, null, contentValuesForItem(item));
		items.add(new TodoItem(rowID, item.getDescription(), item.getDate(), item.getIsDone(), item.getInsertionDate()));
	}

	public void removeItem(int index) {
		// query: should the interface here be changed to expect a TodoItem
		// rather than the index of one?
		TodoItem item = items.get(index);
		getWritableDatabase().delete(TABLE_NAME, whereClauseForItem(item), null);
		items.remove(index);
	}

	public void setItem(int index, TodoItem item) {
		// create a patched item consisting of the correct row ID, which
		// we'll crib from the existing item, and the (possibly) new
		// description and date. Then update the row and put the patched
		// item into the array (as the row ID will be assumed to be correct
		// in the future)
		TodoItem existingItem = items.get(index);
		TodoItem modifiedItem = new TodoItem(existingItem.getRowID(), item.getDescription(), item.getDate(), item.getIsDone(), existingItem.getInsertionDate());

		getWritableDatabase().update(TABLE_NAME, contentValuesForItem(modifiedItem), whereClauseForItem(modifiedItem), null);
		items.set(index, modifiedItem);
	}
}
