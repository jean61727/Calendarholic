package sqlite.helper;

/**
 * Created by jeanlee on 2015/1/20.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sqlite.model.Calendar;
import sqlite.model.Journal;
import sqlite.model.Task;
import sqlite.model.TodoTable;
import sqlite.model.Wish;

public class CalendarAllDBhelper extends SQLiteOpenHelper{
    //initialize variable
    private static CalendarAllDBhelper sInstance;
    //Logtag
    public static final String LOG="DatabasaHelper";
    //Database version
    public static final int DATABASE_VERSION =1;
    //Database Name
    public static final String DATABASE_NAME="allCalendarManager";



    // table name
    private static final String TABLE_CALENDAR = "calendar";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "do_date";

    //CALENDAR Table Columns names
    private static final String KEY_TASKNAME = "taskName";
    private static final String KEY_TIME="time";

    // Table Create Statements
    // Task table create statement
    private static final String CREATE_TABLE_CALENDAR = "CREATE TABLE "
            + TABLE_CALENDAR + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASKNAME
            + " TEXT," + KEY_TIME + " TEXT," + KEY_DATE
            + " TEXT" + ")";



    private CalendarAllDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static CalendarAllDBhelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new CalendarAllDBhelper(context.getApplicationContext());
        }
        return sInstance;
    }


    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */



    @Override
    public void onCreate(SQLiteDatabase db) {
        //execSQL(sql)->the SQL statement to be executed. Multiple statements separated by semicolons are not supported.

        // creating required tables
        db.execSQL(CREATE_TABLE_CALENDAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR);
        // create new tables
        onCreate(db);
    }



    //Creating a Task
    /**
     * create a todo item in todos table, also assigning the todo to a
     * tag name which inserts a row in todo_tags table
     */

    // Adding new calendar
    public long addCalendar(Calendar calendar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, calendar.getTaskName()); // task name
        // status of task- can be 0 for not done and 1 for done
        values.put(KEY_TIME, calendar.getTime());
        values.put(KEY_DATE,calendar.getDateAt());

        // Inserting Row
        long calendar_id= db.insert(TABLE_CALENDAR, null, values);
        // insert row

        return calendar_id;

    }


    /** get single task
     */
    public Calendar getCalendar(long calendar_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CALENDAR + " WHERE "
                + KEY_ID + " = " + calendar_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Calendar cal = new Calendar();
        cal.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        cal.setTaskName((c.getString(c.getColumnIndex(KEY_TASKNAME))));
        cal.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
        cal.setTime(c.getString(c.getColumnIndex(KEY_TIME)));
        c.close();
        return cal;
    }


    /*
 * getting all task
 * */
    public List<Calendar> getAllCalendar() {
        List<Calendar> calendars = new ArrayList<Calendar>();
        String selectQuery = "SELECT  * FROM " + TABLE_CALENDAR+" ORDER BY "+KEY_DATE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Calendar cal = new Calendar();
                cal.setId(c.getLong((c.getColumnIndex(KEY_ID))));
                cal.setTaskName((c.getString(c.getColumnIndex(KEY_TASKNAME))));
                cal.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
                cal.setTime(c.getString(c.getColumnIndex(KEY_TIME)));

                // adding to todo list
                calendars.add(cal);
            } while (c.moveToNext());
        }
        c.close();
        return calendars;
    }




    /*
 * Updating a task
 */
    public int updateCalendar(Calendar calendar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, calendar.getTaskName());
        values.put(KEY_TIME, calendar.getTime());
        values.put(KEY_DATE,calendar.getDateAt());

        // updating row
        return db.update(TABLE_CALENDAR, values, KEY_ID + " = ?",
                new String[] { String.valueOf(calendar.getId()) });
    }



    /*
 * Deleting a task
 */
    public void deleteCalendar(long calendar_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CALENDAR, KEY_ID + " = ?",
                new String[] { String.valueOf(calendar_id) });
    }



    public List<Calendar> getCalendarsByDate(String date) {
        List<Calendar> calendars = new ArrayList<Calendar>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CALENDAR + " WHERE "
                + KEY_DATE + " = " + "\'"+date+"\'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();


        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Calendar cal = new Calendar();
                cal.setId(c.getLong((c.getColumnIndex(KEY_ID))));
                cal.setTaskName((c.getString(c.getColumnIndex(KEY_TASKNAME))));
                cal.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
                cal.setTime(c.getString(c.getColumnIndex(KEY_TIME)));

                // adding to todo list
                calendars.add(cal);
            } while (c.moveToNext());
        }
        c.close();
        return calendars;

    }



}