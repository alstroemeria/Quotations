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
    private static final int CATEGORIES = 30;
    private static final int CATEGORY_ID = 40;
    private static final int AUTHORS = 50;
    private static final int AUTHOR_ID = 60;

    private static String AUTHORITY = "com.jackymok.quotations.app.provider";

    private static final String PATH_QUOTATIONS = "quotations";
    private static final String PATH_CATEGORIES = "categories";
    private static final String PATH_AUTHORS = "authors";


    public static final Uri CONTENT_URI_QUOTATIONS = Uri.parse("content://" + AUTHORITY + "/" + PATH_QUOTATIONS);
    public static final Uri CONTENT_URI_CATEGORIES = Uri.parse("content://" + AUTHORITY + "/" + PATH_CATEGORIES);
    public static final Uri CONTENT_URI_AUTHORS = Uri.parse("content://" + AUTHORITY + "/" + PATH_AUTHORS);


    public static final String CONTENT_TYPE_QUOTATION = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/quotations";
    public static final String CONTENT_ITEM_TYPE_QUOTATION = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/quotation";
    public static final String CONTENT_TYPE_CATEGORY = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/categories";
    public static final String CONTENT_ITEM_TYPE_CATEGORY = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/category";
    public static final String CONTENT_TYPE_AUTHOR = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/authors";
    public static final String CONTENT_ITEM_TYPE_AUTHOR = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/author";


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, PATH_QUOTATIONS, QUOTATIONS);
        sURIMatcher.addURI(AUTHORITY, PATH_QUOTATIONS + "/#", QUOTATION_ID);
        sURIMatcher.addURI(AUTHORITY, PATH_CATEGORIES, CATEGORIES);
        sURIMatcher.addURI(AUTHORITY, PATH_CATEGORIES + "/#", CATEGORY_ID);
        sURIMatcher.addURI(AUTHORITY, PATH_AUTHORS, AUTHORS);
        sURIMatcher.addURI(AUTHORITY, PATH_AUTHORS + "/#", AUTHOR_ID);
    }

    @Override
    public boolean onCreate() {
        database = new QuotationDatabase(getContext());

        return false;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        //checkColumns(projection);

        int uriType = sURIMatcher.match(uri);


        switch (uriType){
            case QUOTATIONS:
                //:TODO refactor implicit logic. Where joint parts are currently specified in the projection
                String table = String.format("%s LEFT OUTER JOIN %s ON (%s.%s = %s.%s)",
                        QuotationContract.TABLE_QUOTATION,
                        AuthorContract.TABLE_AUTHOR,
                        QuotationContract.TABLE_QUOTATION,
                        QuotationContract.COLUMN_AUTHOR,
                        AuthorContract.TABLE_AUTHOR,
                        AuthorContract.COLUMN_ID );
                queryBuilder.setTables(table);
                break;
            case QUOTATION_ID:
                queryBuilder.setTables(QuotationContract.TABLE_QUOTATION);
                queryBuilder.appendWhere(QuotationContract.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case CATEGORIES:
                queryBuilder.setTables(CategoryContract.TABLE_CATEGORY);
                break;
            case CATEGORY_ID:
                queryBuilder.setTables(CategoryContract.TABLE_CATEGORY);
                queryBuilder.appendWhere(CategoryContract.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case AUTHORS:
                queryBuilder.setTables(AuthorContract.TABLE_AUTHOR);
                break;
            case AUTHOR_ID:
                queryBuilder.setTables(AuthorContract.TABLE_AUTHOR);
                queryBuilder.appendWhere(AuthorContract.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
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
        Uri returnValue = null;
        switch (uriType) {
            case QUOTATIONS:
                id = db.replace(QuotationContract.TABLE_QUOTATION, null, values);
                returnValue =  Uri.parse(PATH_QUOTATIONS + "/"+id);
                break;
            case CATEGORIES:
                id = db.replace(CategoryContract.TABLE_CATEGORY, null, values);
                returnValue =  Uri.parse(PATH_QUOTATIONS + "/"+id);
                break;
            case AUTHORS:
                id = db.replace(AuthorContract.TABLE_AUTHOR, null, values);
                returnValue =  Uri.parse(PATH_QUOTATIONS + "/"+id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnValue;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        int numInserted = 0;
        String table = "";

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case QUOTATIONS:
                table = QuotationContract.TABLE_QUOTATION;
                break;
            case CATEGORIES:
                table = CategoryContract.TABLE_CATEGORY;
                break;
            case AUTHORS:
                table = AuthorContract.TABLE_AUTHOR;
                break;
        }
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        sqlDB.beginTransaction();
        try {
            for (ContentValues cv : values) {
                long newID = sqlDB.replace(table, null, cv);
//                if (newID <= 0) {
//                    throw new SQLException("Failed to insert row into " + uri);
//                }
            }
            sqlDB.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            numInserted = values.length;
        } finally {
            sqlDB.endTransaction();
        }
        return numInserted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch(uriType){
            case QUOTATIONS:
                rowsDeleted = db.delete(QuotationContract.TABLE_QUOTATION,
                        selection,selectionArgs);
                break;
            case QUOTATION_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    rowsDeleted = db.delete(QuotationContract.TABLE_QUOTATION,
                            QuotationContract.COLUMN_ID + "=" + id + " and " + selection, null);
                }
                else{
                    rowsDeleted = db.delete(QuotationContract.TABLE_QUOTATION,
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
                rowsUpdated = db.update(QuotationContract.TABLE_QUOTATION,values,selection, selectionArgs);
                break;
            case QUOTATION_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    rowsUpdated = db.update(QuotationContract.TABLE_QUOTATION,
                            values, QuotationContract.COLUMN_ID + "=" + id + " and " + selection, null);
                }
                else{
                    rowsUpdated = db.update(QuotationContract.TABLE_QUOTATION,
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
                QuotationContract.COLUMN_SEEN, QuotationContract.COLUMN_FAVOURITE,
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