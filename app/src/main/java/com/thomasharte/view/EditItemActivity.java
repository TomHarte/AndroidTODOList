package com.thomasharte.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.thomasharte.model.TodoItem;
import com.thomasharte.todo.R;

import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends ActionBarActivity {

    // there'll be only one thing to communicate upward - the final content
    // of an item
    public static final String ITEM_CONTENT_KEY = "itemContent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // grab the todo item we're editing from the intent
        TodoItem item = (TodoItem)
                getIntent().getSerializableExtra(MainActivity.ITEM_CONTENT_KEY);

        // stuff the description into the edit text (which I'm just going to find afresh
        // every time I want it rather than storing to an instance variable,
        // maybe that's considered ugly?)
        EditText etItem = (EditText)findViewById(R.id.etItem);
        etItem.setText(item.getDescription());

        // use the user's selected calendar to break the item's date
        // into year, month, day, hour and minute
        Date date = item.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        // push the year, month, day to the date picker
        DatePicker dpPicker = (DatePicker)findViewById(R.id.dpDate);
        dpPicker.updateDate(year, month, day);

        // push the minute, hour to the time picker
        TimePicker tpPicker = (TimePicker)findViewById(R.id.tpTime);
        tpPicker.setCurrentHour(hour);
        tpPicker.setCurrentMinute(minute);

		// push the done tick mark
		CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
		checkBox.setChecked(item.getIsDone());
    }

    // we'll treat the back button as another opportunity to save
    // the item (in effect there's no 'cancel' option in this app)
    @Override
    public void onBackPressed() {
        saveTodoItem(null);
    }

    public void saveTodoItem(View v) {
        // grab the description
        EditText etItem = (EditText)findViewById(R.id.etItem);
        String description = etItem.getText().toString();

        // get year, month, day from the date picker
        DatePicker dpPicker = (DatePicker)findViewById(R.id.dpDate);
        int day = dpPicker.getDayOfMonth();
        int year = dpPicker.getYear();
        int month = Calendar.JANUARY;
        // there's the whiff of extreme desperation here for a reason:
        // DatePicker returns an int, Calendar wants an int but I can't
        // seem to figure out how to guarantee that the int is equal to
        // one of the individually defined month values other than this
        // tedious and error-prone switch. This will need to be revisited
        switch (dpPicker.getMonth()) {
            default: break;
            case 0: month = Calendar.JANUARY; break;
            case 1: month = Calendar.FEBRUARY; break;
            case 2: month = Calendar.MARCH; break;
            case 3: month = Calendar.APRIL; break;
            case 4: month = Calendar.MAY; break;
            case 5: month = Calendar.JUNE; break;
            case 6: month = Calendar.JULY; break;
            case 7: month = Calendar.AUGUST; break;
            case 8: month = Calendar.SEPTEMBER; break;
            case 9: month = Calendar.OCTOBER; break;
            case 10: month = Calendar.NOVEMBER; break;
            case 11: month = Calendar.DECEMBER; break;
        }

        // get hour and minute from the time picker
        TimePicker tpPicker = (TimePicker)findViewById(R.id.tpTime);
        int hour = tpPicker.getCurrentHour();
        int minute = tpPicker.getCurrentMinute();

        // use the user's calendar to map to a date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        Date date = calendar.getTime();

		// determine whether this item is now 'done'
		CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
		Boolean isDone = checkBox.isChecked();

		// we can report that this activity ended ok, with the generated
        // content being the latest string. Idiomatically I decided it was
        // probably unclean to reuse MainActivity's ITEM_CONTENT_KEY as
        // that assumes I know who triggered me, or else requires anybody
        // that isn't MainActivity to know not just me but also MainActivity
        Intent data = new Intent();
        data.putExtra(EditItemActivity.ITEM_CONTENT_KEY, new TodoItem(description, date, isDone));
        setResult(RESULT_OK, data);
        finish();
    }
}
