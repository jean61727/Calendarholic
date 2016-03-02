package dataBaseTest;

/**
 * Created by jeanlee on 2014/12/31.
 */

import android.test.AndroidTestCase;
import android.database.sqlite.SQLiteDatabase;

import sqlite.helper.DataBaseHelper;

public class TestDB extends AndroidTestCase {

    public static final String LOG_TAG = TestDB.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(DataBaseHelper.DATABASE_NAME);
        SQLiteDatabase db = new DataBaseHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }





}