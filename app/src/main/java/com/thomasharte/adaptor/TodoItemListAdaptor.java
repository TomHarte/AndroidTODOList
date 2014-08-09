package com.thomasharte.adaptor;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.thomasharte.model.TodoItem;
import com.thomasharte.model.TodoList;
import com.thomasharte.todo.R;

import java.text.DateFormat;
import java.util.Date;

public class TodoItemListAdaptor extends BaseAdapter {

	private final TodoList todoList;
	private final Context context;
	private final DisplayMetrics displayMetrics;

	public TodoItemListAdaptor (Context context, TodoList todoList, DisplayMetrics displayMetrics) {
		super();
		this.todoList = todoList;
		this.context = context;
		this.displayMetrics = displayMetrics;
	}

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

		// add an animation if we've not seen this row before
		long timeDifference = (new Date()).getTime() - item.getInsertionDate().getTime();
		if(timeDifference < 250) {
			Animation animation = new TranslateAnimation(displayMetrics.widthPixels, 0, 0, 0);
			animation.setDuration(250);
			convertView.setAnimation(animation);
		}

		// Return the completed view to render on screen
		return convertView;
	}

	public int getCount() {
		return todoList.getCount();
	}
	public Object getItem(int position) {
		return position;
	}
	public long getItemId(int position) {
		return position;
	}
}
