package com.codewarriors4.tiffin.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jaiso on 2018-07-15.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fcmmanager.db";


    private static final String TABLE_NAME = "fcm_token";
    private static final String COL1 = "ID";
    private static final String COL2 = "fcmkey";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"+ COL2 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(new StringBuilder().append("DROP IF TABLE EXISTS ").append(TABLE_NAME).toString());
        onCreate(db);
    }

    public boolean addData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(COL2,item);
        Log.d("addData","done");

        long result = db.insert(TABLE_NAME, null ,contentvalues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkFCMExists(String fcmkey){

        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        String sql ="SELECT" + " "+ COL2 +" FROM "+TABLE_NAME+" WHERE "+ COL2 + " = " + "'"+fcmkey+"'";
        cursor= db.rawQuery(sql,null);
        Log.d("Cursor Count : ", String.valueOf(cursor.getCount()));

        if(cursor.getCount()>0){
            cursor.close();

            return true;
        }else{
            cursor.close();

            return false;       }

    }

    public Cursor fetch() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        String sql ="SELECT " + COL2 +" FROM "+TABLE_NAME+" WHERE"+" ID = "+1;
        cursor= db.rawQuery(sql,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }





}
