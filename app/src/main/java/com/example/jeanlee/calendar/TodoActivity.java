package com.example.jeanlee.calendar;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import java.lang.Object;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import sqlite.helper.CalendarDBhelper;
import sqlite.helper.TaskDBHelper;
import sqlite.helper.TodoDatabaseHelper;
import sqlite.model.Journal;
import sqlite.model.Task;

public class TodoActivity extends ActionBarActivity {
    protected TodoDatabaseHelper db;
    List<Task> list;
    MyAdapter adapt;
    private ViewGroup mContainerView;
    TextView mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        db=TodoDatabaseHelper.getInstance(this);
        list = db.getAllTasks();
        adapt = new MyAdapter(this, R.layout.list_inner_view, list);
        ListView listTask = (ListView) findViewById(R.id.listView1);
        listTask.setAdapter(adapt);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));

        listTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                Task task=list.get(index);
                db.deleteTask(task.getId());
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
                Intent intent = new Intent(TodoActivity.this, ChangeTaskActivity.class);
                Task task=list.get(position);
                intent.putExtra("id",task.getId());
                startActivityForResult(intent, 100);

            }
        });
    }




    /**this function can be used when you set on click on the button in the same activity **/
/*
    public void addTaskNow(View view) {
        EditText t = (EditText) findViewById(R.id.editText1);
        String s = t.getText().toString();
        if (s.equalsIgnoreCase("")) {
            Toast.makeText(this, "enter the task description first!!",
                    Toast.LENGTH_LONG).show();
        } else {
            Task task = new Task(s, 0);
            db.addTask(task);
            Log.d("tasker", "data added");
            t.setText("");
            adapt.add(task);
            adapt.notifyDataSetChanged();
        }

    }*/
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
                Intent intent = new Intent(this, TodoInputActivity.class);
                startActivityForResult(intent, 100);
                //addTaskNow();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    private class MyAdapter extends ArrayAdapter<Task> {

        Context context;
        List<Task> taskList = new ArrayList<Task>();
        int layoutResourceId;

        public MyAdapter(Context context, int layoutResourceId,
                         List<Task> objects) {
            super(context, layoutResourceId, objects);
            this.layoutResourceId = layoutResourceId;
            this.taskList = objects;
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
                convertView = inflater.inflate(R.layout.list_inner_view,
                        parent, false);
                chk = (CheckBox) convertView.findViewById(R.id.chkStatus);
                convertView.setTag(chk);

                chk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        final Task changeTask = (Task) cb.getTag();
                        changeTask.setStatus(cb.isChecked() == true ? 1 : 0);

                        db.updateTask(changeTask);
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
            final Task current = taskList.get(position);
            chk.setText(current.getTaskName());
            chk.setChecked(current.getStatus() == 1 ? true : false);
            chk.setTag(current);
            mDate = (TextView) convertView.findViewById(R.id.date);
            mDate.setText(current.getDateAt());
            Animation animation = null;
            animation = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 0,
                    (float) 1.0);

            animation.setDuration(750);
            convertView.startAnimation(animation);
            animation = null;

            Log.d("listener", String.valueOf(current.getId()));
              return convertView;
        }

    }


//    private void deleteItemIfMarkedAsDone(View view, Task task,int position) {
//            Animation fadeOutAnimation = new AlphaAnimation(1, 0);
//        fadeOutAnimation.setDuration(1000); // time for animation in milliseconds
//        fadeOutAnimation.setFillAfter(false); // make the transformation persist
//        final Task t=task;
//        final int p=position;
//
//        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                db.deleteTask(t.getId());
//                list.remove(p);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) { }
//
//            @Override
//            public void onAnimationStart(Animation animation) { }
//
//        });
//
//            view.setAnimation(fadeOutAnimation);
//
//            animate(view, fadeOutAnimation);
//    }
//
//
//    private static void animate(View view, Animation animation) {
//        view.startAnimation(animation);
//    }


}
