package com.example.jeanlee.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jeanlee on 2014/12/29.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="MyDataBase";
    private static final int DATABASE_VERSION=1;

    public SqliteHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql="CREATE TABLE users"+"(journal TEXT)";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i,int i2){
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

}
