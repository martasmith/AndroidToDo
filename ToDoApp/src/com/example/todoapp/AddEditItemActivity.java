package com.example.todoapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

public class AddEditItemActivity extends Activity {
	
	private EditText etDescription;
	private RadioButton priorityH, priorityL, priorityM;
	private DatePicker dpDueDate;
	private Button btnSave;
	private Intent i;
	private String intention;
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
		intention = getIntent().getStringExtra("intention");
		if (intention.equals("edit")) {
			String descStr = getIntent().getStringExtra("descStr");
			int iconId = getIntent().getIntExtra("iconId",0);
			String dueDateStr = getIntent().getStringExtra("dueDateStr");
			itemPos = getIntent().getIntExtra("itemPos",-2);
			
			// parse the date string to set the datepicker's value   to the correct date
			String[] dueDateArr  = dueDateStr.split("-");
			int month = Integer.parseInt(dueDateArr[0]);
			int day = Integer.parseInt(dueDateArr[1]);
			int year = Integer.parseInt(dueDateArr[2]);
			
			// convert iconId to it's respective radio button selection
			if (iconId == R.drawable.ic_high) {
				priorityH.setChecked(true);
			} else if (iconId == R.drawable.ic_medium) {
				priorityM.setChecked(true);
			}
			else if (iconId == R.drawable.ic_low) {
				priorityL.setChecked(true);
			}
				
			// populate rest of item data from intent
			dpDueDate.updateDate(year, month, day);
			etDescription.setText(descStr);
			
			// add some niceties
			etDescription.setSelection(descStr.length());
			etDescription.requestFocus();
		}
		
		// Call listeners
		setupSaveBtnListener();
		
	}
	
	// Create listeners
	private void setupSaveBtnListener() {
		btnSave.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				// determine iconId based on which radio button is clicked
				int iconId = 0;
				if (priorityH.isChecked()) {
					iconId = R.drawable.ic_high;
				} else if (priorityM.isChecked()) {
					iconId = R.drawable.ic_medium;
				} else if (priorityL.isChecked()) {
					iconId = R.drawable.ic_low;
				}
				
				// get the due date components
				int month = dpDueDate.getMonth();
				int day = dpDueDate.getDayOfMonth();
				int year = dpDueDate.getYear();
				
				//Construct due date string
				String dueDate = String.valueOf(month) + "-" + String.valueOf(day) + "-" + String.valueOf(year);
				
				// Set up intent to send data to ToDo Activity
				i = new Intent(AddEditItemActivity.this, TodoActivity.class);
				i.putExtra("descStr",etDescription.getText().toString());
				i.putExtra("iconId", iconId);
				i.putExtra("dueDateStr", dueDate);
				// pass back whether intention of page is add or edit, as onActivityResult behavior will differ based on it.
				Log.d("martas", "saveBtnListener intention: "+intention+ " Sending Item position: " + itemPos);
				i.putExtra("intention", intention);
				i.putExtra("itemPos", itemPos);
				setResult(RESULT_OK, i);
				finish();
				
			}
			
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
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
}
