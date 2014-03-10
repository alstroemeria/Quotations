package com.jackymok.quotations.app.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Jacky on 08/03/14.
 */
public class QuotationProvider extends ContentProvider {
    private QuotationDatabase database;
    private static final int QUOTATIONS = 10;
    private static final int QUOTATION_ID = 20;

    private static String AUTHORITY = "com.jackymok.quotations.app.provider";
    private static final String BASE_PATH = "quotations";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/quotations";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/quotation";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, QUOTATIONS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", QUOTATION_ID);
    }

    @Override
    public boolean onCreate() {
        database = new QuotationDatabase(getContext());
        return false;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(QuotationContract.TABLE__QUOTATION);

        int uriType = sURIMatcher.match(uri);
        switch (uriType){
            case QUOTATIONS:
                break;
            case QUOTATION_ID:
                queryBuilder.appendWhere(QuotationContract.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db,projection,selection,selectionArgs,null,null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case QUOTATIONS:
                id = db.replace(QuotationContract.TABLE__QUOTATION, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(BASE_PATH + "/"+id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch(uriType){
            case QUOTATIONS:
                rowsDeleted = db.delete(QuotationContract.TABLE__QUOTATION,
                        selection,selectionArgs);
                break;
            case QUOTATION_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    rowsDeleted = db.delete(QuotationContract.TABLE__QUOTATION,
                            QuotationContract.COLUMN_ID + "=" + id + " and " + selection, null);
                }
                else{
                    rowsDeleted = db.delete(QuotationContract.TABLE__QUOTATION,
                            QuotationContract.COLUMN_ID + "=" +id + "and" + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db =database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType){
            case QUOTATIONS:
                rowsUpdated = db.update(QuotationContract.TABLE__QUOTATION,values,selection, selectionArgs);
                break;
            case QUOTATION_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    rowsUpdated = db.update(QuotationContract.TABLE__QUOTATION,
                            values, QuotationContract.COLUMN_ID + "=" + id + " and " + selection, null);
                }
                else{
                    rowsUpdated = db.update(QuotationContract.TABLE__QUOTATION,
                            values, QuotationContract.COLUMN_ID + "=" + id + "and" + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { QuotationContract.COLUMN_CATEGORY,
                QuotationContract.COLUMN_READ, QuotationContract.COLUMN_FAVOURITE,
                QuotationContract.COLUMN_ID, QuotationContract.COLUMN_AUTHOR,
                QuotationContract.COLUMN_TEXT};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}