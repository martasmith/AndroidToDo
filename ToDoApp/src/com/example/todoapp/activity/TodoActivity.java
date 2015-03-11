
package com.example.todoapp.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.todoapp.R;
import com.example.todoapp.activity.db.ItemDataSource;
import com.example.todoapp.activity.model.Item;
import com.example.todoapp.adapter.ItemListAdapter;

public class TodoActivity extends Activity {

    private final int EDIT_REQUEST_CODE = 10;
    private final int ADD_REQUEST_CODE = 20;
    private Item newItem;
    private Item currentItem;
    private List<Item> itemList;
    private ItemListAdapter itemListAdapter;
    private ListView lvItems;
    private Intent i;
    private ItemDataSource itemDataSource;
    private CheckBox taskDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        taskDone = (CheckBox) findViewById(R.id.taskDone);
        itemDataSource = new ItemDataSource(this);
        itemList = new ArrayList<Item>();
        itemDataSource.openDBHandle();
        itemList = itemDataSource.fetchAllItems(0, 0);

        getActionBar().setTitle(Html.fromHtml("&nbsp;<b><font color=\"#FAE2C0\">My To-Do List</font></b>"));

        polulateCustomListView();
        setupListViewListener();
    }

    private void polulateCustomListView() {
        itemListAdapter = new ItemListAdapter(this, itemList);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemListAdapter);
    }

    private void setupListViewListener() {

        lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                currentItem = itemListAdapter.getItem(pos);
                // remove it from the db
                itemDataSource.openDBHandle();
                itemDataSource.deleteItem(currentItem);
                // remove it from the list via the adapter
                itemListAdapter.remove(currentItem);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                Log.d("martas", "OnItemClickListener is being called");
                Intent i = new Intent(TodoActivity.this, AddEditItemActivity.class);
                currentItem = itemListAdapter.getItem(pos);
                i.putExtra("descStr", currentItem.getDescription());
                i.putExtra("priority", currentItem.getPriority());
                i.putExtra("dueMonth", currentItem.getDueMonth());
                i.putExtra("dueDay", currentItem.getDueDay());
                i.putExtra("dueYear", currentItem.getDueYear());
                i.putExtra("actionType", "edit");
                i.putExtra("activityTitle", "Edit Current Item");
                i.putExtra("itemPos", pos);
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // Retrieve data Strings from Intent
            String descStr = data.getExtras().getString("descStr");
            int priority = data.getExtras().getInt("priority", 0);
            int iconColor = data.getExtras().getInt("iconColor");
            String iconLetter = data.getExtras().getString("iconLetter");
            int dueDay = data.getExtras().getInt("dueDay");
            int dueMonth = data.getExtras().getInt("dueMonth");
            int dueYear = data.getExtras().getInt("dueYear");
            int taskDone = 0;

            // Determine whether we need to add a new item or edit an existing one
            if (requestCode == EDIT_REQUEST_CODE) {
                int itemPos = data.getExtras().getInt("itemPos");
                // we want to update the current item here
                currentItem = itemListAdapter.getItem(itemPos);
                currentItem.setPriority(priority);
                currentItem.setIconColor(iconColor);
                currentItem.setIconLetter(iconLetter);
                currentItem.setDescription(descStr);
                currentItem.setDueDay(dueDay);
                currentItem.setDueMonth(dueMonth);
                currentItem.setDueYear(dueYear);
                // open db handle, and update item in database
                itemDataSource.openDBHandle();
                itemDataSource.updateItem(currentItem);
                // modify item in view and list
                itemListAdapter.notifyDataSetChanged();
            } else if (requestCode == ADD_REQUEST_CODE) {
                Log.d("martas", " in add request code, item data: priority= " + priority
                        + " iconColor= "
                        + iconColor + " iconLetter= " + iconLetter + " descStr= " + descStr
                        + " dueDay= " + dueDay + " dueMonth= " + dueMonth + " dueDay= " + dueDay);
                // populate new item with data
                newItem = new Item(priority, iconColor, iconLetter, descStr, dueYear, dueMonth,
                        dueDay, taskDone);
                // open db handle, and insert data into database
                itemDataSource.openDBHandle();
                newItem = itemDataSource.insertItem(newItem);
                // add item to view and list
                itemListAdapter.add(newItem);
            }
        }
    }

    public void onAddItem(MenuItem mi) {
        i = new Intent(TodoActivity.this, AddEditItemActivity.class);
        i.putExtra("itemPos", -1);
        i.putExtra("actionType", "add");
        i.putExtra("activityTitle", "Add New Item");
        startActivityForResult(i, ADD_REQUEST_CODE);
    }

    public void markItemCompleted(View v) {
        taskDone = (CheckBox) v;
        int rowId = (Integer) v.getTag();
        // checkedItems.add(rowId);
        currentItem = itemListAdapter.getItem(rowId);
        if (taskDone.isChecked()) {
            currentItem.setTaskDone(1);
        } else {
            currentItem.setTaskDone(0);
        }
        // open db handle, and update item in database
        itemDataSource.openDBHandle();
        itemDataSource.updateItem(currentItem);

    }

    public void deleteCheckedItems() {

        // TODO: try only removing it from the adapter, and not from the list
        Iterator<Item> iterator = itemList.iterator();
        while (iterator.hasNext()) {
            Item thisItem = iterator.next();
            if (thisItem.getTaskDone() == 1) {
                // remove the current element from the itemList
                iterator.remove();
                itemListAdapter.remove(thisItem);
            }
        }

        // remove items from the DB
        itemDataSource.openDBHandle();
        itemDataSource.deleteAllChecked();

        // refresh adapter
        itemListAdapter.notifyDataSetChanged();
    }

    public void sortByPriority() {
        // clear array, re-populate it, refresh view
        itemListAdapter.clear();
        itemDataSource.openDBHandle();
        itemList = itemDataSource.fetchAllItems(1, 0);
        itemListAdapter.addAll(itemList);
    }

    public void sortByDate() {
        // clear array, re-populate it, refresh view
        itemListAdapter.clear();
        itemDataSource.openDBHandle();
        itemList = itemDataSource.fetchAllItems(0, 1);
        itemListAdapter.addAll(itemList);
    }

    public void sortByOriginal() {
        // clear array, re-populate it, refresh view
        itemListAdapter.clear();
        itemDataSource.openDBHandle();
        itemList = itemDataSource.fetchAllItems(0,0);
        itemListAdapter.addAll(itemList);
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
        if (id == R.id.action_delete_checked) {
            deleteCheckedItems();
            return true;
        } else if (id == R.id.action_sort_by_priority) {
            sortByPriority();
            return true;
        } else if (id == R.id.action_sort_by_duedate) {
            sortByDate();
            return true;
        } else if (id == R.id.action_sort_by_original) {
            sortByOriginal();
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
