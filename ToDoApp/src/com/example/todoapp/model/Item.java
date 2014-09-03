package com.example.todoapp.model;


/*
 * Store information about a TodoItem
 * 
 * TODO: eventually convert String dueDate to java.sql.Date, so user can sort items by date
 *
 */

public class Item {
	private long itemId;
	private int iconId;
	private String description;
	private String dueDate;
	
	
	// Constructors
	public Item () {
		super();
	}
	
	public Item (int iconId, String description, String dueDate) {
		super();
		this.description = description;
		this.iconId = iconId;
		this.dueDate = dueDate;
	}
	
	//Getters and setters
	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	
	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	

}
