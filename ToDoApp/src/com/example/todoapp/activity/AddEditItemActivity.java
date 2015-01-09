
package com.example.todoapp.activity;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.todoapp.R;

public class AddEditItemActivity extends Activity {


    private final int COLOR_RED = Color.parseColor("#ff4444");
    private final int COLOR_ORANGE = Color.parseColor("#ff9305");
    private final int COLOR_GREEN = Color.parseColor("#75ad3e");

    private EditText etDescription;
    private RadioButton priorityH, priorityL, priorityM;
    private DatePicker dpDueDate;
    private Button btnSave;
    private Intent i;
    private String actionType, letter;
    private int color = 0, priority = 0;
    private int itemPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        // Access UI elements
        etDescription = (EditText) findViewById(R.id.etDescription);
        priorityH = (RadioButton) findViewById(R.id.rbHigh);
        priorityM = (RadioButton) findViewById(R.id.rbMedium);
        priorityL = (RadioButton) findViewById(R.id.rbLow);
        dpDueDate = (DatePicker) findViewById(R.id.dpDueDate);
        btnSave = (Button) findViewById(R.id.btnSave);

        priorityH.setTextColor(COLOR_RED);
        priorityM.setTextColor(COLOR_ORANGE);
        priorityL.setTextColor(COLOR_GREEN);

        // If the intention is edit item, get data elements from intent, and populate view with data
        actionType = getIntent().getStringExtra("actionType");
        Log.d("martas", "actionType: " + actionType);
        if (actionType.equals("edit")) {
            String descStr = getIntent().getStringExtra("descStr");
            int priority = getIntent().getIntExtra("priority", 0);
            int dueDay = getIntent().getIntExtra("dueDay", 0);
            int dueMonth = getIntent().getIntExtra("dueMonth", 0);
            int dueYear = getIntent().getIntExtra("dueYear", 0);
            itemPos = getIntent().getIntExtra("itemPos", -2);

            // convert iconId to it's respective radio button selection
            if (priority == 0) {
                priorityH.setChecked(true);
                color = COLOR_RED;
                letter = "H";

            } else if (priority == 1) {
                priorityM.setChecked(true);
                color = COLOR_ORANGE;
                letter = "M";
            }
            else if (priority == 2) {
                priorityL.setChecked(true);
                color = COLOR_GREEN;
                letter = "L";
            }

            // populate rest of item data from intent
            dpDueDate.updateDate(dueYear, dueMonth-1, dueDay);
            etDescription.setText(descStr);

            // add some niceties
            etDescription.setSelection(descStr.length());
            etDescription.requestFocus();
        } else {
            // if add, set the DatePicker's date to the current date

            Calendar cal = Calendar.getInstance();

            int currentYear = cal.get(Calendar.YEAR);
            int currentMonth = cal.get(Calendar.MONTH);
            int currentDay = cal.get(Calendar.DAY_OF_MONTH);

            //Log.d("martas","I'm in add clause, and the year is: " + currentYear + " the month is: " + currentMonth + " the day is: " + currentDay);

            dpDueDate.updateDate(currentYear, currentMonth, currentDay);

        }

        String activityTitle = getIntent().getStringExtra("activityTitle");
        this.setTitle(activityTitle);

        // Call listeners
        setupSaveBtnListener();

    }

    // Create listeners
    private void setupSaveBtnListener() {
        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // determine icon based on which radio button is clicked
                if (priorityH.isChecked()) {
                    priority = 0;
                    color = COLOR_RED;
                    letter = "H";
                } else if (priorityM.isChecked()) {
                    priority = 1;
                    color = COLOR_ORANGE;
                    letter = "M";
                } else if (priorityL.isChecked()) {
                    priority = 2;
                    color = COLOR_GREEN;
                    letter = "L";
                }

                // get the due date components
                int month = dpDueDate.getMonth()+1;
                int day = dpDueDate.getDayOfMonth();
                int year = dpDueDate.getYear();

                // Construct due date string
                String dueDate = String.valueOf(month) + "-" + String.valueOf(day) + "-"
                        + String.valueOf(year);



                // Set up intent to send data to ToDo Activity
                i = new Intent(AddEditItemActivity.this, TodoActivity.class);
                i.putExtra("descStr", etDescription.getText().toString());
                i.putExtra("priority", priority);
                i.putExtra("iconColor", color);
                i.putExtra("iconLetter", letter);
                i.putExtra("dueDate", dueDate);
                i.putExtra("dueYear", year);
                i.putExtra("dueMonth", month);
                i.putExtra("dueDay", day);
                // pass back whether intention of page is add or edit, as onActivityResult behavior
                // will differ based on it.
                i.putExtra("itemPos", itemPos);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
