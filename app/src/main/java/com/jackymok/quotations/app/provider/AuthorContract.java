package com.jackymok.quotations.app.provider;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Jacky on 10/05/14.
 */
public class AuthorContract {
    public static final String TABLE_AUTHOR= "author";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FAVOURITE = "favourite";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_AUTHOR
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_FAVOURITE + " integer default 0"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(QuotationContract.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE ID EXISTS" + TABLE_AUTHOR);
        onCreate(database);
    }
}