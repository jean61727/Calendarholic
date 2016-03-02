package com.example.jeanlee.calendar;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
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


public class JournalActivity extends ActionBarActivity {


    protected CalendarDBhelper db;
    List<Journal> list;
    MyAdapter adapt;
    TextView tvDate;
    TextView tvTitle;
    TextView tvDesrip;

    private ViewGroup mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journal);
        db=CalendarDBhelper.getInstance(this);
        list = db.getAllJournals();
        adapt = new MyAdapter(this, R.layout.journal_row, list);
        ListView listTask = (ListView) findViewById(R.id.listView1);
        listTask.setAdapter(adapt);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));

        Log.e("jourrr","jourrr");

        listTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                Journal journal=list.get(index);
                db.deleteJournal(journal.getId());
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
                Intent intent = new Intent(JournalActivity.this, ChangeJournal.class);
                Journal journal=list.get(position);
                intent.putExtra("id",journal.getId());
                Log.e("activity","heyyy"+journal.getId());
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
                Intent intent = new Intent(this, JournalInputActivity.class);
                startActivityForResult(intent, 100);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /***private void addItem() {
        // Instantiate a new "row" view.
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.list_item_example, mContainerView, false);

        // Set the text in the new row to a random country.
        ((TextView) newView.findViewById(android.R.id.text1)).setText("gotothell");

        // Set a click listener for the "X" button in the row that will remove the row.
        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the row from its parent (the container view).
                // Because mContainerView has android:animateLayoutChanges set to true,
                // this removal is automatically animated.
                mContainerView.removeView(newView);

                // If there are no rows remaining, show the empty view.
                if (mContainerView.getChildCount() == 0) {
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                }
            }
        });

        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        mContainerView.addView(newView, 0);
    }*/
    private class MyAdapter extends ArrayAdapter<Journal> {

        Context context;
        List<Journal> journalList = new ArrayList<Journal>();
        int layoutResourceId;

        public MyAdapter(Context context, int layoutResourceId,
                         List<Journal> objects) {
            super(context, layoutResourceId, objects);
            this.layoutResourceId = layoutResourceId;
            this.journalList = objects;
            this.context = context;
        }

        /**
         * This method will DEFINe what the view inside the list view will
         * finally look like Here we are going to code that the checkbox state
         * is the status of task and check box text is the task name
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Journal journal= getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.journal_row, parent, false);
            }
            // Lookup view for data population
            tvDate = (TextView) convertView.findViewById(R.id.date);
            tvTitle = (TextView) convertView.findViewById(R.id.title);
            tvDesrip=(TextView)convertView.findViewById(R.id.descrip);

            // Populate the data into the template view using the data object
            tvDate.setText(journal.getDateAt());
            tvDesrip.setText(journal.getDescrip());
            tvTitle.setText(journal.getTitle());
            // Return the completed view to render on screen
            Animation animation = null;
            animation = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 0,
                    (float) 1.0);

            animation.setDuration(750);
            convertView.startAnimation(animation);
            animation = null;



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
