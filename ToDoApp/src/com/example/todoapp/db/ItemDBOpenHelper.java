package com.example.todoapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemDBOpenHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "items.db";
	private static final int DATABASE_VERSION =  7;
	
	// define table name and columns in the table
	public static final String TABLE_ITEMS =  "items";
	public static final String COLUMN_ITEMID =  "itemId";
	public static final String COLUMN_ICONID =  "iconId";
	public static final String COLUMN_DESCRIPTION =  "description";
	public static final String COLUMN_DUEDATE =  "dueDate";
	
	// table create statement
	private static final String TABLE_CREATE =  
			"CREATE TABLE " + TABLE_ITEMS + " (" +
			COLUMN_ITEMID + " INTEGER PRIMARY KEY, " +
			COLUMN_ICONID + " INTEGER, " +
			COLUMN_DESCRIPTION + " TEXT, " +
			COLUMN_DUEDATE + " TEXT" + ")";
	
	public ItemDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		Log.i("martas", "created table...");
		Log.i("martas", "table create statement: " + TABLE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		onCreate(db);

	}

}
