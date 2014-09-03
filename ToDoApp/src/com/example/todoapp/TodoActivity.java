package com.example.todoapp;

import java.util.ArrayList;
import java.util.List;

import com.example.todoapp.db.ItemDataSource;
import com.example.todoapp.model.Item;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;


public class TodoActivity extends Activity {
	
	private final int REQUEST_CODE = 10;
	private Item newItem;
	private Item currentItem;
	private List<Item> itemList;
	private ItemListAdapter itemListAdapter;
	private ListView lvItems;
	private Button addBtn;
	private Intent i;
	private ItemDataSource itemDataSource;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemDataSource = new ItemDataSource(this);
        itemList = new ArrayList<Item>();
        itemDataSource.openDBHandle();
        itemList = itemDataSource.fetchAllItems();
       
        addBtn = (Button) findViewById(R.id.btnAdd);
        
        polulateCustomListView();
        setupAddBtnListener();
        setupListViewListener();
    }
    
     private void polulateCustomListView() {
    	 itemListAdapter = new ItemListAdapter(this,R.layout.item_todo_layout, itemList);
         lvItems = (ListView) findViewById(R.id.lvItems);
         lvItems.setAdapter(itemListAdapter);
    }
     
    private void setupAddBtnListener() {
 		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				i = new Intent(TodoActivity.this,AddEditItemActivity.class);
				i.putExtra("intention", "add");
				i.putExtra("itemPos", -1);
				startActivityForResult(i,REQUEST_CODE);
			}	
 		});
 	}
    
    private void setupListViewListener() {
   
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
				currentItem = itemListAdapter.getItem(pos);
				itemList.remove(currentItem);
				// also remove it from the db
				itemDataSource.openDBHandle();
				itemDataSource.deleteItem(currentItem);
				// notify adapter to refresh data
				itemListAdapter.notifyDataSetChanged();
				return true;
			}
		});	
		
		lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
				Intent i = new Intent(TodoActivity.this, AddEditItemActivity.class);
				currentItem = itemListAdapter.getItem(pos);
				i.putExtra("descStr", currentItem.getDescription());
				i.putExtra("iconId", currentItem.getIconId());
				i.putExtra("dueDateStr", currentItem.getDueDate());
				i.putExtra("intention", "edit");
				i.putExtra("itemPos", pos);
				startActivityForResult(i,REQUEST_CODE);
			}
		});
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
    		
    		// Retrieve data Strings from Intent
    		String descStr = data.getExtras().getString("descStr");
    		int iconId = data.getExtras().getInt("iconId", 0);
    		String dueDateStr = data.getExtras().getString("dueDateStr");
    		String intention = data.getExtras().getString("intention");
    		
    		
    		//Determine whether we need to add a new item or edit an existing one
    		if (intention.equals("edit")) {
    			int itemPos =  data.getExtras().getInt("itemPos");
    			// we want to update the current item here
    			currentItem = itemListAdapter.getItem(itemPos);
    			currentItem.setIconId(iconId);
    			currentItem.setDescription(descStr);
    			currentItem.setDueDate(dueDateStr);
    			// open db handle, and update item in database
    			itemDataSource.openDBHandle();
    			itemDataSource.updateItem(currentItem);
    			//modify item in view and list
    			itemListAdapter.notifyDataSetChanged();
    		} else {
    			//populate new item with data
    			newItem = new Item(iconId, descStr, dueDateStr);
    			// open db handle, and insert data into database
    			itemDataSource.openDBHandle();
    			newItem = itemDataSource.insertItem(newItem);
    			// add item to view and list
    			itemListAdapter.add(newItem);
    		}
    	}
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
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
    
    @Override
    protected void onResume() {
    	super.onResume();
    	itemDataSource.openDBHandle();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	itemDataSource.closeDBHandle();
    }
}
