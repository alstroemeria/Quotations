package com.jackymok.quotations.app.provider;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Jacky on 08/03/14.
 */
public class QuotationContract {

    public static final String TABLE_QUOTATION = "quotation";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_FAVOURITE = "favourite";
    public static final String COLUMN_SEEN = "seen";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_QUOTATION
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_AUTHOR + " text not null,"
            + COLUMN_CATEGORY + " integer not null,"
            + COLUMN_TEXT + " text not null,"
            + COLUMN_FAVOURITE + " integer default 0,"
            + COLUMN_SEEN + " integer default 0"
            + ");";

    public static void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(QuotationContract.class.getName(), "Upgrading database from version "
            + oldVersion + " to " + newVersion
            + ", which will destroy all old data");
        database.execSQL("DROP TABLE ID EXISTS" + TABLE_QUOTATION);
        onCreate(database);
    }
}
