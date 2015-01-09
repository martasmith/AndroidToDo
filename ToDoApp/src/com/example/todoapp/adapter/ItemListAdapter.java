
package com.example.todoapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todoapp.R;
import com.example.todoapp.activity.model.Item;
import com.example.todoapp.util.LetterAvatar;

public class ItemListAdapter extends ArrayAdapter<Item> {

    private Context context;
    private List<Item> items;
    private LetterAvatar la;

    public ItemListAdapter(Context context, List<Item> items) {
        super(context, R.layout.item_todo_layout, items);
        this.context = context;
        this.items = items;
    }

    // ViewHolder class
    public static class ViewHolder {
        ImageView ivPlaceHolder;
        TextView itemDesc, itemDate;
        CheckBox taskDone;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item currentItem = items.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo_layout,
                    parent, false);
            viewHolder.ivPlaceHolder = (ImageView) convertView.findViewById(R.id.item_icon);
            viewHolder.itemDesc = (TextView) convertView.findViewById(R.id.item_desc);
            viewHolder.itemDate = (TextView) convertView.findViewById(R.id.item_date);
            viewHolder.taskDone = (CheckBox) convertView.findViewById(R.id.taskDone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        la = new LetterAvatar(context, currentItem.getIconColor(), currentItem.getIconLetter(), 20);
        viewHolder.ivPlaceHolder.setImageDrawable(la);
        viewHolder.itemDesc.setText(currentItem.getDescription());
        String dueDate = currentItem.getDueMonth() + "-" + currentItem.getDueDay() + "-" + currentItem.getDueYear();
        viewHolder.itemDate.setText("Due: " + dueDate);
        viewHolder.taskDone.setTag(position);

        if (currentItem.getTaskDone() == 1) {
            viewHolder.taskDone.setChecked(true);
        } else {
            viewHolder.taskDone.setChecked(false);
        }

        return convertView;

    }

}
