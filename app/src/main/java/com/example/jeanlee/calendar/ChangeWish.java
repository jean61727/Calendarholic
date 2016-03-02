package com.example.jeanlee.calendar;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import sqlite.helper.CalendarDBhelper;
import sqlite.model.Journal;
import sqlite.model.Task;
import sqlite.model.Wish;


public class ChangeWish extends ActionBarActivity {

    private EditText mTitleText;
    protected CalendarDBhelper db;
    static List<Wish> list;
    private ViewGroup mContainerView;
    private DatePickerDialog dialog = null;
    Wish wish;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_input);
        mTitleText = (EditText) findViewById(R.id.todo_edit_description);
        Button saveButton = (Button) findViewById(R.id.save_button);
        Button cancelButton=(Button)findViewById(R.id.cancel_Button);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));


       /* Get values from Intent */
        intent=getIntent();
        long id=intent.getLongExtra("id",-1);
        db=CalendarDBhelper.getInstance(this);
        Log.e("where are you2","whereare you ");
        wish=db.getWish(id);
        Log.e("where are you2","whereare you "+wish);

        mTitleText.setText(wish.getWishName());


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addWishNow(mTitleText,wish);
                setResult(100, intent);
                Log.e("chanfe","change");
                finish();

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Log.e("chanfe","change2");
                finish();
                return;
            }

        });

    }
    private static final String ACTIVITY_TAG="INPUT";


    public void addWishNow(EditText newTask,Wish w) {
        String sTitle = newTask.getText().toString();
        if (sTitle.equalsIgnoreCase("")) {
            Toast.makeText(this, "enter the Wish description first!!",
                    Toast.LENGTH_LONG).show();
        } else {
            w.setWishName(sTitle);
            w.setStatus(0);
            db.updateWish(w);
            Log.e("chanfeinnnnn","changeinnnnnn");
            //get the id of the created journal
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wish_list_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}