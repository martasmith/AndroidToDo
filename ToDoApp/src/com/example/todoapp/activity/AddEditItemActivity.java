
package com.example.todoapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

        // If the intention is edit item, get data elements from intent, and populate view with data
        actionType = getIntent().getStringExtra("actionType");
        if (actionType.equals("edit")) {
            String descStr = getIntent().getStringExtra("descStr");
            int priority = getIntent().getIntExtra("priority", 0);
            String dueDateStr = getIntent().getStringExtra("dueDateStr");
            itemPos = getIntent().getIntExtra("itemPos", -2);

            // parse the date string to set the datepicker's value to the correct date
            String[] dueDateArr = dueDateStr.split("-");
            int month = Integer.parseInt(dueDateArr[0])-1;
            int day = Integer.parseInt(dueDateArr[1]);
            int year = Integer.parseInt(dueDateArr[2]);

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
            dpDueDate.updateDate(year, month, day);
            etDescription.setText(descStr);

            // add some niceties
            etDescription.setSelection(descStr.length());
            etDescription.requestFocus();
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
                int month = dpDueDate.getMonth();
                int day = dpDueDate.getDayOfMonth();
                int year = dpDueDate.getYear();

                // Construct due date string
                String dueDate = String.valueOf(month+1) + "-" + String.valueOf(day) + "-"
                        + String.valueOf(year);

                // Set up intent to send data to ToDo Activity
                i = new Intent(AddEditItemActivity.this, TodoActivity.class);
                i.putExtra("descStr", etDescription.getText().toString());
                i.putExtra("priority", priority);
                i.putExtra("iconColor", color);
                i.putExtra("iconLetter", letter);
                i.putExtra("dueDateStr", dueDate);
                // pass back whether intention of page is add or edit, as onActivityResult behavior
                // will differ based on it.
                i.putExtra("itemPos", itemPos);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
