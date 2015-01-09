
package com.example.todoapp.activity.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.todoapp.activity.model.Item;

public class ItemDataSource {

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase db;

    private static final String[] dbColumns = {
        ItemDBOpenHelper.COLUMN_ITEMID,
        ItemDBOpenHelper.COLUMN_PRIORITY,
        ItemDBOpenHelper.COLUMN_ICONCOLOR,
        ItemDBOpenHelper.COLUMN_ICONLETTER,
        ItemDBOpenHelper.COLUMN_DESCRIPTION,
        ItemDBOpenHelper.COLUMN_DUEYEAR,
        ItemDBOpenHelper.COLUMN_DUEMONTH,
        ItemDBOpenHelper.COLUMN_DUEDAY,
        ItemDBOpenHelper.COLUMN_TASKDONE
    };

    public ItemDataSource(Context context) {
        dbHelper = new ItemDBOpenHelper(context);
    }

    public Item insertItem(Item item) {
        ContentValues cv = new ContentValues();
        cv.put(ItemDBOpenHelper.COLUMN_PRIORITY, item.getPriority());
        cv.put(ItemDBOpenHelper.COLUMN_ICONCOLOR, item.getIconColor());
        cv.put(ItemDBOpenHelper.COLUMN_ICONLETTER, item.getIconLetter());
        cv.put(ItemDBOpenHelper.COLUMN_DESCRIPTION, item.getDescription());
        cv.put(ItemDBOpenHelper.COLUMN_DUEYEAR, item.getDueYear());
        cv.put(ItemDBOpenHelper.COLUMN_DUEMONTH, item.getDueMonth());
        cv.put(ItemDBOpenHelper.COLUMN_DUEDAY, item.getDueDay());
        cv.put(ItemDBOpenHelper.COLUMN_TASKDONE, item.getTaskDone());
        long insertId = db.insert(ItemDBOpenHelper.TABLE_ITEMS, null, cv);
        item.setItemId(insertId);
        return item;
    }

    public List<Item> fetchAllItems(int prioritySort, int dateSort) {
        Log.d("martas", "Entering getAllItems method...");
        List<Item> items = new ArrayList<Item>();
        String orderBy = "";
        if (prioritySort == 1) {
            orderBy = ItemDBOpenHelper.COLUMN_PRIORITY + ", "+
                    ItemDBOpenHelper.COLUMN_DUEYEAR + ", " +
                    ItemDBOpenHelper.COLUMN_DUEMONTH + ", " +
                    ItemDBOpenHelper.COLUMN_DUEDAY;
        } else if (dateSort == 1) {
            orderBy = ItemDBOpenHelper.COLUMN_DUEYEAR + ", "+
                    ItemDBOpenHelper.COLUMN_DUEMONTH + ", " +
                    ItemDBOpenHelper.COLUMN_DUEDAY + ", " +
                    ItemDBOpenHelper.COLUMN_PRIORITY;
        }


        String qry = SQLiteQueryBuilder.buildQueryString(false, ItemDBOpenHelper.TABLE_ITEMS, dbColumns,
                null, null, null, orderBy, null);

        Log.d("martas","fetch checked query: " + qry);


        Cursor cursor = db.query(ItemDBOpenHelper.TABLE_ITEMS, dbColumns, null, null, null,null, orderBy);

        // Cursor cursor = db.rawQuery("SELECT * FROM items", null);
        int resCount = cursor.getCount();
        Log.i("martas", "results count is: " + resCount);
        if (resCount > 0) {
            while (cursor.moveToNext()) {
                Item item = new Item();
                item.setItemId(cursor.getLong(cursor.getColumnIndex(ItemDBOpenHelper.COLUMN_ITEMID)));
                item.setPriority(cursor.getInt(cursor.getColumnIndex(ItemDBOpenHelper.COLUMN_PRIORITY)));
                item.setIconColor(cursor.getInt(cursor
                        .getColumnIndex(ItemDBOpenHelper.COLUMN_ICONCOLOR)));
                item.setIconLetter(cursor.getString(cursor
                        .getColumnIndex(ItemDBOpenHelper.COLUMN_ICONLETTER)));
                item.setDescription(cursor.getString(cursor
                        .getColumnIndex(ItemDBOpenHelper.COLUMN_DESCRIPTION)));
                item.setDueYear(cursor.getInt(cursor
                        .getColumnIndex(ItemDBOpenHelper.COLUMN_DUEYEAR)));
                item.setDueMonth(cursor.getInt(cursor
                        .getColumnIndex(ItemDBOpenHelper.COLUMN_DUEMONTH)));
                item.setDueDay(cursor.getInt(cursor
                        .getColumnIndex(ItemDBOpenHelper.COLUMN_DUEDAY)));
                item.setTaskDone(cursor.getInt(cursor
                        .getColumnIndex(ItemDBOpenHelper.COLUMN_TASKDONE)));
                items.add(item);
            }
        }
        return items;
    }

    public Item updateItem(Item item) {
        String whereClause = ItemDBOpenHelper.COLUMN_ITEMID + "=" + item.getItemId();
        ContentValues cv = new ContentValues();
        cv.put(ItemDBOpenHelper.COLUMN_PRIORITY, item.getPriority());
        cv.put(ItemDBOpenHelper.COLUMN_ICONCOLOR, item.getIconColor());
        cv.put(ItemDBOpenHelper.COLUMN_ICONLETTER, item.getIconLetter());
        cv.put(ItemDBOpenHelper.COLUMN_DESCRIPTION, item.getDescription());
        cv.put(ItemDBOpenHelper.COLUMN_DUEYEAR, item.getDueYear());
        cv.put(ItemDBOpenHelper.COLUMN_DUEMONTH, item.getDueMonth());
        cv.put(ItemDBOpenHelper.COLUMN_DUEDAY, item.getDueDay());
        cv.put(ItemDBOpenHelper.COLUMN_TASKDONE, item.getTaskDone());
        db.update(ItemDBOpenHelper.TABLE_ITEMS, cv, whereClause, null);
        return item;
    }

    public void deleteItem(Item item) {
        String whereClause = ItemDBOpenHelper.COLUMN_ITEMID + "=" + item.getItemId();
        db.delete(ItemDBOpenHelper.TABLE_ITEMS, whereClause, null);
    }

    public void deleteAllChecked() {
        // for testing, select all with taskdone = 1

        Log.d("martas", "deleteAllCheck is being called!");

        String whereClause = ItemDBOpenHelper.COLUMN_TASKDONE + "=" + 1;
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
