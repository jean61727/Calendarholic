package com.example.jeanlee.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
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
import sqlite.model.Task;
import sqlite.model.Wish;


public class WishListInputActivity extends ActionBarActivity {

    private EditText mTitleText;
    protected CalendarDBhelper db;
    List<Task> list;
    private ViewGroup mContainerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_wish_list_input);

        mTitleText = (EditText) findViewById(R.id.todo_edit_description);

        Button saveButton = (Button) findViewById(R.id.save_button);
        Button cancelButton=(Button)findViewById(R.id.cancel_Button);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));



        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addWishNow(mTitleText);
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
        getMenuInflater().inflate(R.menu.menu_wish_list_input, menu);
        return true;
    }

    private static final String ACTIVITY_TAG="INPUT";
    public void addWishNow(EditText t) {
        db=CalendarDBhelper.getInstance(this);
        String s = t.getText().toString();
        if (s.equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter the wish description first!!",
                    Toast.LENGTH_LONG).show();
        } else {
            Wish wish = new Wish(s, 0);
            db=CalendarDBhelper.getInstance(this);
            long listid=db.addWish(wish);
            wish.setId(listid);
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
//        Toast.makeText(WishListInputActivity.this, "Please maintain a summary",
//                Toast.LENGTH_LONG).show();
//    }



}
