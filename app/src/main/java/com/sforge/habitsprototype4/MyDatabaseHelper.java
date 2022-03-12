package com.sforge.habitsprototype4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public Context context;
    public static final String DATABASE_NAME = "dayLibrary.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "day_library";
    public static final String COLUMN_ID = "_id";
    public static final String NAME = "name";
    public static final String TAG = "tag";
    public static final String REPEAT = "repeat";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                TAG + " TEXT, " +
                REPEAT + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addHabit(String name, String tag, String repeat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TAG, tag);
        cv.put(NAME, name);
        cv.put(REPEAT, repeat);
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
    public void updateData(String row_id,String name, String tag, String repeat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NAME, name);
        cv.put(TAG, tag);
        cv.put(REPEAT, repeat);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1)
            Toast.makeText(context, "Failed " + result, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Success! " + result, Toast.LENGTH_SHORT).show();
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
