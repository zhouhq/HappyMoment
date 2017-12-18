package com.ryo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/12/18.
 */

public class RyoDataBaseHelper extends SQLiteOpenHelper {
    private static final String CRTATE_RECOMMDNE = "create table recommend( "+
                                                    "id integer primary key autoincrement,"+
                                                    "title text,"+
                                                    "content text"+
                                                    ")";
                                                            ;
    public RyoDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version)
    {
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CRTATE_RECOMMDNE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
