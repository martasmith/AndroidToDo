
package com.example.todoapp.activity.model;

/*
 * Store information about a TodoItem
 * 
 * TODO: eventually convert String dueDate to java.sql.Date, so user can sort items by date
 *
 */

public class Item {
    private long itemId;
    private int iconColor;
    private int priority;
    private String iconLetter;
    private String description;
    private String dueDate;
    private int taskDone;

    // Constructors
    public Item() {
        super();
    }

    public Item(int priority, int iconColor, String iconLetter, String description, String dueDate,
            int taskDone) {
        super();
        this.description = description;
        this.priority = priority;
        this.iconColor = iconColor;
        this.iconLetter = iconLetter;
        this.dueDate = dueDate;
        this.taskDone = taskDone;
    }

    // Getters and setters
    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
    }

    public int getIconColor() {
        return iconColor;
    }

    public String getIconLetter() {
        return iconLetter;
    }

    public void setIconLetter(String iconLetter) {
        this.iconLetter = iconLetter;
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

    public int getTaskDone() {
        return taskDone;
    }

    public void setTaskDone(int taskDone) {
        this.taskDone = taskDone;
    }

}
