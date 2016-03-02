package com.example.jeanlee.calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sqlite.helper.CalendarDBhelper;
import sqlite.model.Journal;
import sqlite.model.Task;
import sqlite.model.Wish;


public class WishListActivity extends ActionBarActivity {


    protected CalendarDBhelper db;
    List<Wish> list;
    MyAdapter adapt;
    TextView tvDate;
    TextView tvTitle;

    private ViewGroup mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wish);
        db=CalendarDBhelper.getInstance(this);
        list = db.getAllWishes();
        adapt = new MyAdapter(this,R.layout.activity_wish_list,list);
        ListView listTask = (ListView) findViewById(R.id.listView1);
        listTask.setAdapter(adapt);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));


        listTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                Wish wish=list.get(index);
                db.deleteWish(wish.getId());
                list.remove(index);

                adapt.notifyDataSetChanged();
                adapt.notifyDataSetInvalidated();
                return true;
            }
        });
        listTask.setClickable(true);
        listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(WishListActivity.this, ChangeWish.class);
                Wish wish=list.get(position);
                intent.putExtra("id", wish.getId());
                startActivityForResult(intent, 100);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_add_item:
                // Hide the "empty" view since there is now at least one item in the list.
                /*findViewById(android.R.id.empty).setVisibility(View.GONE);
                addItem();*/
                Intent intent = new Intent(this, WishListInputActivity.class);
                startActivityForResult(intent, 100);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private class MyAdapter extends ArrayAdapter<Wish> {

        Context context;
        List<Wish> wishList = new ArrayList<Wish>();
        int layoutResourceId;

        public MyAdapter(Context context, int layoutResourceId,
                         List<Wish> objects) {
            super(context, layoutResourceId, objects);
            this.layoutResourceId = layoutResourceId;
            this.wishList = objects;
            this.context = context;
        }

        /**
         * This method will DEFINe what the view inside the list view will
         * finally look like Here we are going to code that the checkbox state
         * is the status of task and check box text is the task name
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckBox chk = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_wish_list,
                        parent, false);
                chk = (CheckBox) convertView.findViewById(R.id.chkStatus);
                convertView.setTag(chk);
                convertView.setBackgroundColor(Color.argb(150,255,255,255));

                chk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Wish changeWish = (Wish) cb.getTag();
                        changeWish.setStatus(cb.isChecked() == true ? 1 : 0);
                        db.updateWish(changeWish);

//                        Toast.makeText(
//                                getApplicationContext(),
//                                "Clicked on Checkbox: " + cb.getText() + " is "
//                                        + cb.isChecked(), Toast.LENGTH_LONG)
//                                .show();
                    }

                });
            } else {
                chk = (CheckBox) convertView.getTag();
            }
            Wish current = wishList.get(position);
            chk.setText(current.getWishName());
            chk.setChecked(current.getStatus() == 1 ? true : false);
            chk.setTag(current);

            Log.d("listener", String.valueOf(current.getId()));
            return convertView;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            Log.e("here","here");
        }
    }






}
