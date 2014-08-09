package com.thomasharte.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.thomasharte.model.TodoItem;
import com.thomasharte.model.TodoList;
import com.thomasharte.todo.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by thomasharte on 16/07/2014.
 */
public class TodoItemListAdaptor extends BaseAdapter {

    private final TodoList todoList;
    private final Context context;

    public TodoItemListAdaptor (Context context, TodoList todoList) {
        super();
        this.todoList = todoList;
        this.context = context;
    }

    // Translates a particular `BoxOfficeMovie` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		final TodoItem item = todoList.getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_todo_item, parent, false);
		}

		// Lookup views within item layout
        TextView descriptionView = (TextView) convertView.findViewById(R.id.description);
        TextView dateView = (TextView) convertView.findViewById(R.id.date);
		CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);

		// add a listener to the checkbox, binding in the latest TodoItem
		CompoundButton.OnCheckedChangeListener checkChangedListener =
				new CompoundButton.OnCheckedChangeListener () {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
						// check that this is an actual change and not a spurious one resulting from
						// us not having setChecked yet...
						if(isChecked != item.getIsDone()) {
							TodoItem modifiedItem = new TodoItem(item.getDescription(), item.getDate(), isChecked);
							todoList.setItem(position, modifiedItem);
						}
					}
				};
		checkBox.setOnCheckedChangeListener(checkChangedListener);

		// Populate the data into the template view using the data object
		descriptionView.setText(item.getDescription());
		Date date = item.getDate();
		dateView.setText(DateFormat.getDateInstance().format(date) + ", " + DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
		checkBox.setChecked(item.getIsDone());

		// Return the completed view to render on screen
		return convertView;
	}

    public int getCount()
    {
        return todoList.getCount();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }
}
