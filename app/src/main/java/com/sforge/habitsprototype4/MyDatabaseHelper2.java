package com.sforge.habitsprototype4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper2 extends SQLiteOpenHelper{
    public Context context;
    public static final String DATABASE_NAME = "calendar.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "calendar_library";
    public static final String COLUMN_ID = "_id";
    public static final String DATE = "date";
    public static final String STATUS = "status";

    public MyDatabaseHelper2(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE + " TEXT, " +
                STATUS + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addHabit(String date, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //day_library has no column named habit_repeat

        cv.put(DATE, date);
        cv.put(STATUS, status);
        long result;
        result = db.insert(TABLE_NAME, null, cv);
        if(result == -1)
            Toast.makeText(context, "Failed " + result, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Success! " + result, Toast.LENGTH_SHORT).show();
    }
    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1)
            Toast.makeText(context, "Failed " + result, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Success! " + result, Toast.LENGTH_SHORT).show();
    }
}
