package com.ryo.happymoment.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Administrator on 2017/12/21.
 */

public  class RecommendProvider extends ContentProvider {
    private static UriMatcher uriMatcher;
    private static final int RECOMMEND_DIR = 0;
    private static final int RECOMMEND_ITEM = 1;
    private static final String RECOMMEND_TABLE = "recommend";
    public static final String AUTHORITY = "com.ryo.happymoment.provider";
    public static final String Recommend_Uri="content://" + AUTHORITY + "/" + RECOMMEND_TABLE;
    private RyoDataBaseHelper dataBaseHelper;

    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "recommend", RECOMMEND_DIR);
        uriMatcher.addURI(AUTHORITY, "recommend/#", RECOMMEND_ITEM);

    }

    @Override
    public boolean onCreate() {
        try {
            dataBaseHelper = new RyoDataBaseHelper(this.getContext(), "recommend.db", null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] column, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case RECOMMEND_DIR:
                cursor = database.query(RECOMMEND_TABLE, column, selection, selectionArgs, null, null, sortOrder);
                break;

            case RECOMMEND_ITEM:
                String recommendID = uri.getPathSegments().get(1);
                cursor = database.query(RECOMMEND_TABLE, column, "id= ?", new String[]{recommendID}, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case RECOMMEND_DIR:
                return "vnd.android.cursor.dir/vnd.com.ryo.com.ryo.happymoment.provider" + RECOMMEND_TABLE;
            case RECOMMEND_ITEM:
                return "vnd.android.cursor.item/vnd.com.ryo.com.ryo.happymoment.provider" + RECOMMEND_TABLE;
            default:
                return null;

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri resultUri = null;
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case RECOMMEND_DIR:
            case RECOMMEND_ITEM:
                long recommend = database.insert(RECOMMEND_TABLE, null, contentValues);
                resultUri = Uri.parse("content://" + AUTHORITY + "/" + RECOMMEND_TABLE + recommend);
                break;
            default:
                break;
        }
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase database= dataBaseHelper.getReadableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case RECOMMEND_DIR:
            case RECOMMEND_ITEM:
                count=database.delete(RECOMMEND_TABLE,s,strings);
                break;
            default:
                break;
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase database= dataBaseHelper.getReadableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case RECOMMEND_DIR:
            case RECOMMEND_ITEM:
                count=database.update(RECOMMEND_TABLE,contentValues,s,strings);
                break;
            default:
                break;
        }
        return count;
    }
}
