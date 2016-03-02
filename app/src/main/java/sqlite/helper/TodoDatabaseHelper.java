package sqlite.helper;

/**
 * Created by jeanlee on 2015/1/14.
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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
import sqlite.model.Task;
import sqlite.model.TodoTable;
import sqlite.model.Wish;

public class TodoDatabaseHelper extends SQLiteOpenHelper{
    //initialize variable
    private static SharedPreferences pref ;
    private static TodoDatabaseHelper sInstance;
    //Logtag
    public static final String LOG="DatabasaHelper";
    //Database version
    public static final int DATABASE_VERSION =1;
    //Database Name
    public static final String DATABASE_NAME="TodoManager";
    //private static int countTask;
    private SharedPreferences.Editor edit;



    // table name
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_COUNT="count";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "do_date";

    // TASKS Table Columns names
    private static final String KEY_TASKNAME = "taskName";
    private static final String KEY_STATUS = "status";
    private static final String KEY_COUNT="counttask";


    // Table Create Statements
    // Task table create statement
    private static final String CREATE_TABLE_TASK = "CREATE TABLE "
            + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASKNAME
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_DATE
            + " TEXT" + ")";

    private static final String CREATE_TABLE_COUNT = "CREATE TABLE "
            + TABLE_COUNT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COUNT
            + " INTEGER" + ")";


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

    private TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static TodoDatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TodoDatabaseHelper(context.getApplicationContext());
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
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_COUNT);
        //initializing my count
        ContentValues values = new ContentValues();
        values.put(KEY_COUNT, 0); // task name
        long id=db.insert(TABLE_COUNT, null, values);
        Log.e("countid","id"+id);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNT);
        // create new tables
        onCreate(db);
    }



    //Creating a Task
    /**
     * create a todo item in todos table, also assigning the todo to a
     * tag name which inserts a row in todo_tags table
     */

//    public long addCount(int countTask){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_COUNT, countTask); // task name
//        long count_id= db.insert(TABLE_COUNT, null, values);
//        return count_id;
//    }

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
        c.close();
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

    public void updateCount(int count){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COUNT, count);

        // updating row
        db.update(TABLE_COUNT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(1) });

    }

    /*
 * Deleting a task
 */
    public void deleteTask(long task_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Task tk=getTask(task_id);
        if(tk.getStatus()==1){
            int countTask=getCount()+1;
            updateCount(countTask);
            Log.e("update",""+countTask);
        };
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(task_id) });
    }


    public  int getCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery2="SELECT  * FROM " + TABLE_COUNT + " WHERE "
                + KEY_ID + " = " + 1;
        int count=0;
        Cursor cursor1=db.rawQuery(countQuery2,null);
        if (cursor1.moveToFirst()) {

             count=cursor1.getInt(cursor1.getColumnIndex(KEY_COUNT));
            Log.e("countTask",""+count);

        }
        cursor1.close();
        return count;
    }
    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
                + KEY_STATUS + " = " + 1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        String countQuery2="SELECT  * FROM " + TABLE_COUNT + " WHERE "
                + KEY_ID + " = " + 1;


        int count = cursor.getCount()+getCount();
        Log.e("count",""+count);
        cursor.close();


        // return count
        return count;
    }



    public List<Task> getTasksByDate(String date) {
        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
                + KEY_DATE + " = " + "\'"+date+"\'";

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



}