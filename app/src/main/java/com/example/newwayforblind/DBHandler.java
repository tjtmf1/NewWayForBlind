package com.example.newwayforblind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    final String DATABASE_TABLE = "user_info";
    final String COLUMN_STRIDE = "stride";
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table if not exists " + DATABASE_TABLE + "(" + COLUMN_STRIDE + " double primary key)";
        db.execSQL(CREATE_TABLE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STRIDE, 0.0);
        db.insert(DATABASE_TABLE, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
        onCreate(db);
    }

    public void setStride(double value){
        SQLiteDatabase db = getWritableDatabase();
        String update = "update " + DATABASE_TABLE + "set " + COLUMN_STRIDE + "=" + value;
    }

    public double getStride() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DATABASE_TABLE, null);
        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        return 0.0;
    }
}
