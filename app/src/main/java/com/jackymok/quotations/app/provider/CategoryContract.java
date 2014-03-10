package com.jackymok.quotations.app.provider;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Jacky on 09/03/14.
 */
public class CategoryContract {
    public static final String TABLE_CATEGORY= "category";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_CATEGORY
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CATEGORY + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(QuotationContract.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE ID EXISTS" + TABLE_CATEGORY);
        onCreate(database);
    }
}