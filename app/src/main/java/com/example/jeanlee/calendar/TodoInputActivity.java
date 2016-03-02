package com.example.jeanlee.calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import sqlite.helper.CalendarDBhelper;
import sqlite.helper.TaskDBHelper;
import sqlite.helper.TodoDatabaseHelper;
import sqlite.model.Task;

/*
 * TodoDetailActivity allows to enter a new todo item
 * or to change an existing
 */
public class TodoInputActivity extends ActionBarActivity {

    private EditText mTitleText;
    private static EditText mDate;
    protected TodoDatabaseHelper db;
    List<Task> list;
    private ViewGroup mContainerView;


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            view.updateDate(year, month, day);
            month++;
            mDate.setText(year+"/"+month+"/"+day);

        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }




    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_todo_input);

        mTitleText = (EditText) findViewById(R.id.todo_edit_description);
        mDate=(EditText)findViewById(R.id.datetext);
        Button saveButton = (Button) findViewById(R.id.save_button);
        Button cancelButton=(Button)findViewById(R.id.cancel_Button);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));



        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addTaskNow(mTitleText, mDate);
                if (TextUtils.isEmpty(mTitleText.getText().toString())) {
                   // makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }



        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_input, menu);
        return true;
    }

    private static final String ACTIVITY_TAG="INPUT";
    public void addTaskNow(EditText t,EditText date) {
        db=TodoDatabaseHelper.getInstance(this);
        String s = t.getText().toString();
        String sdate=date.getText().toString();
        if (s.equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter the task title first!!",
                    Toast.LENGTH_LONG).show();
        } else {
            Task task = new Task(s, 0,sdate);
            db=TodoDatabaseHelper.getInstance(this);
            long listid=db.addTask(task);
            task.setId(listid);
            t.setText("");
//            adapt.add(task);
//            adapt.notifyDataSetChanged();
            Intent intent = new Intent(this, TodoActivity.class);
            setResult(100, intent);
            finish();
        }

    }


//
//    private void makeToast() {
//        Toast.makeText(TodoInputActivity.this, "Please maintain a summary",
//                Toast.LENGTH_LONG).show();
//    }
//


}
