package com.thomasharte.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.thomasharte.adaptor.TodoItemListAdaptor;
import com.thomasharte.model.TodoItem;
import com.thomasharte.model.TodoList;
import com.thomasharte.todo.R;

import java.util.Date;


public class MainActivity extends ActionBarActivity {

    TodoItemListAdaptor itemsAdaptor;
    ListView lvItems;
    TodoList todoList;  // see notes below about factored out model; probably it wasn't
                        // the ideal thing to do

    // there'll be only one thing to communicate onward - the content
    // of an item
    public static final String ITEM_CONTENT_KEY = "itemContent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // my iOS prejudice took over; I factored out the stuff to do with maintaining the TODO
        // list (albeit not to achieve particularly excellent encapsulation) on the assumption
        // that both this activity and the edit activity would reference the single canonical
        // place — only later did I realise that a child activity can return a result. So
        // the list stuff is all off in its own model class rather than in here
        todoList = TodoList.getInstance(this.getApplicationContext());

        // get the list view, create a suitable adaptor and gift it
        lvItems = (ListView)findViewById(R.id.lvItems);
        itemsAdaptor = new TodoItemListAdaptor(this, todoList);
        lvItems.setAdapter(itemsAdaptor);

        // set up our two listening actions
        setupListViewListeners();
    }

    private void setupListViewListeners() {

        // the long click listener, directly from the tutorial
        ListView.OnItemLongClickListener longClickListener =
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        todoList.removeItem(position);
                        itemsAdaptor.notifyDataSetChanged();
                        return true;
                    }
                };
        lvItems.setOnItemLongClickListener(longClickListener);

        // a [regular] click listener, that will create an intent to edit and then start
        // the appropriate activity to obtain a result
        ListView.OnItemClickListener clickListener =
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra(ITEM_CONTENT_KEY, todoList.getItem(position));
                        startActivityForResult(i, position);
                            // I've used the position as the request code; all the documentation
                            // says is "If >= 0, this code will be returned in onActivityResult()
                            // when the activity exits." so I'm not sure this is abuse but it's
                            // probably debatable
                    }
                };
        lvItems.setOnItemClickListener(clickListener);
    }

    public void AddTotoItem(View v) {
        // pretty much directly from the tutorial, but for my factoring out
        // of the model
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        todoList.addItem(new TodoItem(etNewItem.getText().toString(), new Date()));
        itemsAdaptor.notifyDataSetChanged();
        etNewItem.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // remember I used request code as a direct array index? So
        // this is as easy as pulling the new string out and sending it
        // off to the model
        if (resultCode == RESULT_OK) {
            TodoItem item = (TodoItem)data.getExtras().getSerializable(EditItemActivity.ITEM_CONTENT_KEY);
            todoList.setItem(requestCode, item);
            itemsAdaptor.notifyDataSetChanged();
        }
    }

    // template stuff follows...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
