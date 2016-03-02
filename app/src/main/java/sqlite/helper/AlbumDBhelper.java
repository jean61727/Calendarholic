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

import sqlite.model.Album;
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
public class AlbumDBhelper extends SQLiteOpenHelper{
    //initialize variable
    private static AlbumDBhelper sInstance;
    //Logtag
    public static final String LOG="DatabasaHelper";
    //Database version
    public static final int DATABASE_VERSION =1;
    //Database Name
    public static final String DATABASE_NAME="albumManager";



    // table name
    private static final String TABLE_ALBUM = "album";
    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "do_date";

    //ALBUM Table Columns name
    public static final String KEY_TITLE="album_title";
    public static final String KEY_DESCRIP="album_descrip";
    public static final String KEY_PHOTO="album_photo";


    // Table Create Statements
    // Task table create statement

    // Tag table create statement
    private static final String CREATE_TABLE_ALBUM = "CREATE TABLE " + TABLE_ALBUM
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"+KEY_DESCRIP+" TEXT,"
            + KEY_DATE + " TEXT," + KEY_PHOTO+" BLOB NOT NULL"+")";

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

    private AlbumDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static AlbumDBhelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new AlbumDBhelper(context.getApplicationContext());
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
        db.execSQL(CREATE_TABLE_ALBUM);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
        // create new tables
        onCreate(db);
    }


    /*
 * Creating Album
 */
    public long createAlbum(Album album) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("albumdb","jkhjk");
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, album.getTitle());
        values.put(KEY_DATE, album.getDateAt());
        values.put(KEY_DESCRIP, album.getDescrip());
        values.put(KEY_PHOTO,album.getPhoto());

        // insert row
        long album_id = db.insert(TABLE_ALBUM, null, values);

        return album_id;
    }


    /** get single journal
     */
    public Album getAlbum(long album_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ALBUM + " WHERE "
                + KEY_ID + " = " + album_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Album album = new Album();
        album.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        album.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
        album.setDescrip(c.getString(c.getColumnIndex(KEY_DESCRIP)));
        album.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
        album.setPhoto(c.getBlob(c.getColumnIndex(KEY_PHOTO)));
        c.close();
        return album;
    }


    /**
     * getting all albums
     * */
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<Album>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALBUM+" ORDER BY "+KEY_DATE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
               Album a = new Album();
                a.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                a.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                a.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
                a.setDescrip(c.getString(c.getColumnIndex(KEY_DESCRIP)));
                a.setPhoto(c.getBlob(c.getColumnIndex(KEY_PHOTO)));

                // adding to journal
                albums.add(a);
            } while (c.moveToNext());
        }
        c.close();
        return albums;
    }
    /*
 * Updating a tag
 */
    public int updateAlbum(Album album) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, album.getTitle());
        values.put(KEY_DESCRIP,album.getDescrip());
        values.put(KEY_DATE,album.getDateAt());
        values.put(KEY_PHOTO,album.getPhoto());

        // updating row
        return db.update(TABLE_ALBUM, values, KEY_ID + " = ?",
                new String[] { String.valueOf(album.getId()) });
    }


    /*
 * Deleting a journal
 */
    public void deleteAlbum(long album_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALBUM, KEY_ID + " = ?",
                new String[] { String.valueOf(album_id) });
    }


    public List<Album> getAlbumByDate(String date) {
        List<Album> albums = new ArrayList<Album>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ALBUM + " WHERE "
                + KEY_DATE + " = "  +"\'"+ date+"\'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Album a = new Album();
                a.setId(c.getLong((c.getColumnIndex(KEY_ID))));
                a.setDescrip((c.getString(c.getColumnIndex(KEY_DESCRIP))));
                a.setDateAt(c.getString(c.getColumnIndex(KEY_DATE)));
                a.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                a.setPhoto(c.getBlob(c.getColumnIndex(KEY_PHOTO)));

                // adding to todo list
                albums.add(a);
            } while (c.moveToNext());
        }
        c.close();
        return albums;

    }


}
