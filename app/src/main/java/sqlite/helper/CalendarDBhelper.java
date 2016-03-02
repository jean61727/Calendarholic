package sqlite.helper;

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

import sqlite.model.Journal;
import sqlite.model.Tag;
import sqlite.model.Task;
import sqlite.model.Todo;
import sqlite.model.Wish;

/**
 * Created by jeanlee on 2014/12/30.
 *
 *
 * Database helper contains all the methods to perform database operations
 * such as openeing connection, insert, update, read, delete etc.
 */
public class CalendarDBhelper extends SQLiteOpenHelper{
    //initialize variable
    private static CalendarDBhelper sInstance;
    //Logtag
    public static final String LOG="DatabasaHelper";
    //Database version
    public static final int DATABASE_VERSION =1;
    //Database Name
    public static final String DATABASE_NAME="calendarManager";



    // table name
    private static final String TABLE_TASKS = "tasks";
    public static final String TABLE_JOURNAL="journal";
    public static final String TABLE_LIST="List_of_day";
    public static final String TABLE_WISH="wish";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "do_date";

    // TASKS Table Columns names
    private static final String KEY_TASKNAME = "taskName";
    private static final String KEY_STATUS = "status";

    //TASKS Wish Column names
    private static final String KEY_WISHNAME="wishName";

    //JOURNAL Table Columns name
    public static final String KEY_TITLE="journal_title";
    public static final String KEY_DESCRIP="journal_descrip";

    // List_of_day Table - column names
    public static final String KEY_TASK_ID="task_id";
    public static final String KEY_JOOURNAL_ID="journal_id";

    // Table Create Statements
    // Task table create statement
    private static final String CREATE_TABLE_TASK = "CREATE TABLE "
            + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASKNAME
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_DATE
            + " TEXT" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_JOURNAL = "CREATE TABLE " + TABLE_JOURNAL
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"+KEY_DESCRIP+" TEXT,"
            + KEY_DATE + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_LIST = "CREATE TABLE "
            + TABLE_LIST + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TASK_ID+ " INTEGER," + KEY_JOOURNAL_ID + " INTEGER,"
            + KEY_DATE + " DATETIME" + ")";
    // Wish table create statement
    private static final String CREATE_TABLE_WISH = "CREATE TABLE "
            + TABLE_WISH + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_WISHNAME
            + " TEXT," + KEY_STATUS + " INTEGER" +")";


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     *
     (Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
     context	to use to open or create the database
     name	    of the database file, or null for an in-memory database
     factory	to use for creating cursor objects, or null for the default
     version	number of the database (starting at 1); if the database is older,
     onUpgrade(SQLiteDatabase, int, int) will be used to upgrade the database;
     if the database is newer, onDowngrade(SQLiteDatabase, int, int) will be used to
     downgrade the database

     */

    private CalendarDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static CalendarDBhelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new CalendarDBhelper(context.getApplicationContext());
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
        db.execSQL(CREATE_TABLE_JOURNAL);
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_LIST);
        db.execSQL(CREATE_TABLE_WISH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISH);

        // create new tables
        onCreate(db);
    }



    //Creating a Task
    /**
     * create a todo item in todos table, also assigning the todo to a
     * tag name which inserts a row in todo_tags table
     */

    // Adding new task
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, task.getTaskName()); // task name
        // status of task- can be 0 for not done and 1 for done
        values.put(KEY_STATUS, task.getStatus());
        values.put(KEY_DATE,task.getDateAt());

        // Inserting Row
       long task_id= db.insert(TABLE_TASKS, null, values);
        // insert row

        return task_id;

    }


    /** get single task
     */
    public Task getTask(long task_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
                + KEY_ID + " = " + task_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Task tk = new Task();
        tk.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        tk.setTaskName((c.getString(c.getColumnIndex(KEY_TASKNAME))));
        tk.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
        tk.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));

        return tk;
    }


    /*
 * getting all task
 * */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS+" ORDER BY "+KEY_DATE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Task tk = new Task();
                tk.setId(c.getLong((c.getColumnIndex(KEY_ID))));
                tk.setTaskName((c.getString(c.getColumnIndex(KEY_TASKNAME))));
                tk.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
                tk.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));

                // adding to todo list
                tasks.add(tk);
            } while (c.moveToNext());
        }
        c.close();
        return tasks;
    }




    /*
 * Updating a task
 */
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, task.getTaskName());
        values.put(KEY_STATUS, task.getStatus());
        values.put(KEY_DATE,task.getDateAt());

        // updating row
        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
    }



    /*
 * Deleting a task
 */
    public void deleteTask(long task_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(task_id) });
    }



    /*
 * Creating journal
 */
    public long createJournal(Journal journal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, journal.getTitle());
        values.put(KEY_DATE, journal.getDateAt());
        values.put(KEY_DESCRIP, journal.getDescrip());

        // insert row
        long journal_id = db.insert(TABLE_JOURNAL, null, values);

        return journal_id;
    }


    /** get single journal
     */
    public Journal getJournal(long journal_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_JOURNAL + " WHERE "
                + KEY_ID + " = " + journal_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Journal journal = new Journal();
        journal.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        journal.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
        journal.setDescrip(c.getString(c.getColumnIndex(KEY_DESCRIP)));
        journal.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
        c.close();
        return journal;
    }


    /**
     * getting all journals
     * */
    public List<Journal> getAllJournals() {
        List<Journal> journals = new ArrayList<Journal>();
        String selectQuery = "SELECT  * FROM " + TABLE_JOURNAL+" ORDER BY "+KEY_DATE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Journal j = new Journal();
                j.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                j.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                j.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
                j.setDescrip(c.getString(c.getColumnIndex(KEY_DESCRIP)));

                // adding to journal
                journals.add(j);
            } while (c.moveToNext());
        }
        c.close();
        return journals;
    }
    /*
 * Updating a tag
 */
    public int updateJournal(Journal journal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, journal.getTitle());
        values.put(KEY_DESCRIP,journal.getDescrip());
        values.put(KEY_DATE,journal.getDateAt());

        // updating row
        return db.update(TABLE_JOURNAL, values, KEY_ID + " = ?",
                new String[] { String.valueOf(journal.getId()) });
    }


    /*
 * Deleting a journal
 */
    public void deleteJournal(long journal_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_JOURNAL, KEY_ID + " = ?",
                new String[] { String.valueOf(journal_id) });
    }

    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }



    public List<Task> getTasksByDate(String date) {
        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
                + KEY_DATE + " = "  +"\'"+ date+"\'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Task tk = new Task();
                tk.setId(c.getLong((c.getColumnIndex(KEY_ID))));
                tk.setTaskName((c.getString(c.getColumnIndex(KEY_TASKNAME))));
                tk.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
                tk.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));

                // adding to todo list
                tasks.add(tk);
            } while (c.moveToNext());
        }
        c.close();
        return tasks;

    }
    public List<Journal> getJournalByDate(String date) {
        List<Journal> journals = new ArrayList<Journal>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_JOURNAL + " WHERE "
                + KEY_DATE + " = " +"\'"+ date+"\'";
        Log.e("journal",date);
        Cursor c = db.rawQuery(selectQuery, null);
        Log.e("journal",""+c.getCount());

        if (c != null)
            c.moveToFirst();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Log.e("journal1","haha");
                Journal journal = new Journal();
                journal.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                journal.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
                journal.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
                journal.setDescrip(c.getString(c.getColumnIndex(KEY_DESCRIP)));
                Log.e("journal",journal.getDateAt());

                // adding to journal list
                journals.add(journal);
            } while (c.moveToNext());
        }
        c.close();
        return journals;
    }




    // Adding new wish
    public long addWish(Wish wish) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WISHNAME, wish.getWishName()); // task name
        // status of task- can be 0 for not done and 1 for done
        values.put(KEY_STATUS, wish.getStatus());

        // Inserting Row
        long wish_id= db.insert(TABLE_WISH, null, values);
        // insert row

        return wish_id;

    }


    /** get single task
     */
    public Wish getWish(long wish_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_WISH + " WHERE "
                + KEY_ID + " = " + wish_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Wish wh = new Wish();
        wh.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        wh.setWishName((c.getString(c.getColumnIndex(KEY_WISHNAME))));
        wh.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
        c.close();
        return wh;
    }


    /*
 * getting all task
 * */
    public List<Wish> getAllWishes() {
        List<Wish> wishes = new ArrayList<Wish>();
        String selectQuery = "SELECT  * FROM " + TABLE_WISH;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Wish wh = new Wish();
                wh.setId(c.getLong((c.getColumnIndex(KEY_ID))));
                wh.setWishName((c.getString(c.getColumnIndex(KEY_WISHNAME))));
                wh.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));

                // adding to wish list
                wishes.add(wh);
            } while (c.moveToNext());
        }
        c.close();
        return wishes;
    }




    /*
 * Updating a wish
 */
    public int updateWish(Wish wish) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WISHNAME, wish.getWishName());
        values.put(KEY_STATUS, wish.getStatus());

        // updating row
        return db.update(TABLE_WISH, values, KEY_ID + " = ?",
                new String[] { String.valueOf(wish.getId()) });
    }



    /*
 * Deleting a wish
 */
    public void deleteWish(long wish_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WISH, KEY_ID + " = ?",
                new String[] { String.valueOf(wish_id) });
    }


}
