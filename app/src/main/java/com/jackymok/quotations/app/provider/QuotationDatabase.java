package com.jackymok.quotations.app.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jacky on 08/03/14.
 */
public class QuotationDatabase  extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "quotation.db";
    private static final int DATABASE_VERSION = 1;


    public QuotationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        QuotationContract.onCreate(db);
        CategoryContract.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        QuotationContract.onUpgrade(db,oldVersion,newVersion);
        CategoryContract.onUpgrade(db,oldVersion,newVersion);
    }
}
