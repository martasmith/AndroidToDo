
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
    private int dueDay;
    private int dueMonth;
    private int dueYear;
    private int taskDone;

    // Constructors
    public Item() {
        super();
    }

    public Item(int priority, int iconColor, String iconLetter, String description, int dueYear,
            int dueMonth, int dueDay, int taskDone) {
        super();
        this.description = description;
        this.priority = priority;
        this.iconColor = iconColor;
        this.iconLetter = iconLetter;
        this.dueYear = dueYear;
        this.dueMonth = dueMonth;
        this.dueDay = dueDay;
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

    public int getDueYear() {
        return dueYear;
    }

    public void setDueYear(int dueYear) {
        this.dueYear = dueYear;
    }

    public int getDueMonth() {
        return dueMonth;
    }

    public void setDueMonth(int dueMonth) {
        this.dueMonth = dueMonth;
    }

    public int getDueDay() {
        return dueDay;
    }

    public void setDueDay(int dueDay) {
        this.dueDay = dueDay;
    }

    public int getTaskDone() {
        return taskDone;
    }

    public void setTaskDone(int taskDone) {
        this.taskDone = taskDone;
    }

}
