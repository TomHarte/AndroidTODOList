package com.thomasharte.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.thomasharte.todo.MainActivity;
import com.thomasharte.todo.R;

public class EditItemActivity extends ActionBarActivity {

    // there'll be only one thing to communicate upward - the final content
    // of an item
    public static final String ITEM_CONTENT_KEY = "itemContent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // grab the text of the item we're editing from the intent
        String itemContent = (String)
                getIntent().getSerializableExtra(MainActivity.ITEM_CONTENT_KEY);

        // stuff it into the edit text (which I'm just going to find afresh
        // every time I want it rather than storing to an instance variable,
        // maybe that's considered ugly?)
        EditText etItem = (EditText)findViewById(R.id.etItem);
        etItem.setText(itemContent);
    }


    public void saveTodoItem(View v) {
        // grab the view again, so that...
        EditText etItem = (EditText)findViewById(R.id.etItem);

        // we can report that this activity ended ok, with the generated
        // content being the latest string. Idiomatically I decided it was
        // probably unclean to reuse MainActivity's ITEM_CONTENT_KEY as
        // that assumes I know who triggered me, or else requires anybody
        // that isn't MainActivity to know not just me but also MainActivity
        Intent data = new Intent();
        data.putExtra(EditItemActivity.ITEM_CONTENT_KEY, etItem.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_item, menu);
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
