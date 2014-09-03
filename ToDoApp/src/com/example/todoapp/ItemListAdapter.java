package com.example.todoapp;

import com.example.todoapp.model.Item;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends ArrayAdapter<Item> {
	
	private Context context;
	private List<Item> items;

	public ItemListAdapter(Context context, int resource, List<Item> items) {
		super(context, resource, items);
		this.context = context;
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item currentItem = items.get(position);
		
		//First, we need an inflater object for our view
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		//Then we create our view object
		View view = inflater.inflate(R.layout.item_todo_layout, null);
		
		//image
		ImageView image = (ImageView) view.findViewById(R.id.item_icon);
		image.setImageResource(currentItem.getIconId());
		
		//description
		TextView itemDesc = (TextView) view.findViewById(R.id.item_desc);
		itemDesc.setText(currentItem.getDescription());
		
		//due date
		TextView itemDate = (TextView) view.findViewById(R.id.item_date);
		itemDate.setText(currentItem.getDueDate());
		
		return view;
		
	}

}
