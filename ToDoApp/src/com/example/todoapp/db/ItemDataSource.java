package com.example.todoapp.db;

import java.util.ArrayList;
import java.util.List;

import com.example.todoapp.model.Item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemDataSource {
	
	SQLiteOpenHelper dbHelper;
	SQLiteDatabase db;
	
	private static final String[] dbColumns = {
		ItemDBOpenHelper.COLUMN_ITEMID,
		ItemDBOpenHelper.COLUMN_ICONID,
		ItemDBOpenHelper.COLUMN_DESCRIPTION,
		ItemDBOpenHelper.COLUMN_DUEDATE};
	
	public ItemDataSource(Context context) {
        dbHelper = new ItemDBOpenHelper(context);
	}
	
	public Item insertItem(Item item) {
		ContentValues cv = new ContentValues();
		cv.put(ItemDBOpenHelper.COLUMN_ICONID, item.getIconId());
		cv.put(ItemDBOpenHelper.COLUMN_DESCRIPTION, item.getDescription());
		cv.put(ItemDBOpenHelper.COLUMN_DUEDATE, item.getDueDate());
		long insertId = db.insert(ItemDBOpenHelper.TABLE_ITEMS, null, cv);
		item.setItemId(insertId);
		return item;
	}
	
	public List<Item> fetchAllItems() {
		Log.d("martas", "Entering getAllItems method...");
		List<Item> items = new ArrayList<Item>();
		Cursor cursor = db.query(ItemDBOpenHelper.TABLE_ITEMS, dbColumns, null, null, null, null, null);
		//Cursor cursor = db.rawQuery("SELECT * FROM items", null);
		Log.d("martas", "got some sort of results, I suppose...");
		int resCount = cursor.getCount();
		Log.i("martas", "results count is: " + resCount);
		if (resCount > 0) {
			while(cursor.moveToNext()) {
				Item item = new Item();
				item.setItemId(cursor.getLong(cursor.getColumnIndex(ItemDBOpenHelper.COLUMN_ITEMID)));
				item.setIconId(cursor.getInt(cursor.getColumnIndex(ItemDBOpenHelper.COLUMN_ICONID)));
				item.setDescription(cursor.getString(cursor.getColumnIndex(ItemDBOpenHelper.COLUMN_DESCRIPTION)));
				item.setDueDate(cursor.getString(cursor.getColumnIndex(ItemDBOpenHelper.COLUMN_DUEDATE)));
				items.add(item);
			}
		}
		Log.d("martas", "Exiting getAllItems method...");
		return items;
	}
	
	public Item updateItem(Item item) {
		String whereClause = ItemDBOpenHelper.COLUMN_ITEMID + "=" + item.getItemId();
		ContentValues cv = new ContentValues();
		cv.put(ItemDBOpenHelper.COLUMN_ICONID, item.getIconId());
		cv.put(ItemDBOpenHelper.COLUMN_DESCRIPTION, item.getDescription());
		cv.put(ItemDBOpenHelper.COLUMN_DUEDATE, item.getDueDate());
		db.update(ItemDBOpenHelper.TABLE_ITEMS, cv, whereClause, null);
		return item;
	}
	
	public void deleteItem(Item item) {
		String whereClause = ItemDBOpenHelper.COLUMN_ITEMID + "=" + item.getItemId();
		db.delete(ItemDBOpenHelper.TABLE_ITEMS, whereClause, null);
	}
	
	public void openDBHandle() {
		Log.i("martas", "database opened");
		db = dbHelper.getWritableDatabase();
	}
	
	public void closeDBHandle() {
		Log.i("martas", "database closed");
		dbHelper.close();
	}
}
