package com.thomasharte.com.thomasharte.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thomasharte.model.TodoItem;
import com.thomasharte.model.TodoList;
import com.thomasharte.todo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thomasharte on 16/07/2014.
 */
public class TodoItemListAdaptor extends BaseAdapter {

    private TodoList todoList;
    private Context context;

    public TodoItemListAdaptor (Context context, TodoList todoList) {
        super();
        this.todoList = todoList;
        this.context = context;
    }

    // Translates a particular `BoxOfficeMovie` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TodoItem item = todoList.getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_todo_item, parent, false);
        }
        // Lookup views within item layout
        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        // Populate the data into the template view using the data object
        description.setText(item.getDescription());

        try{
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            date.setText(sdf.format(item.getDate()));
        }
        catch(Exception ex){
            date.setText("");
        }

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
