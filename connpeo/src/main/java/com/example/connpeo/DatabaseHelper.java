package com.example.connpeo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_database";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "my_table";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name String, "
                + "beiyong String, "
                + "tel String, "
                + "img String, "
                + "fenzu String)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 处理数据库升级
    }

    // 向数据库中插入数据
    public void insertData(String name, String beiyong, String tel,String img,String fenzu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("beiyong", beiyong);
        cv.put("tel", tel);
        cv.put("img", img);
        cv.put("fenzu", fenzu);

        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    // 修改数据库中的信息
    public void updateData(String name, String beiyong, String tel,String fenzu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("beiyong", beiyong);
        cv.put("tel", tel);
        cv.put("fenzu", fenzu);
        db.update(TABLE_NAME, cv, "name=?", new String[]{String.valueOf(name)});
        db.close();
    }

    // 删除数据库中的信息
    public void deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "name=?", new String[]{String.valueOf(name)});
        db.close();
    }



    @SuppressLint("Range")
    public List<Fenzued> queryData(String x) {
        List<Fenzued> dataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM my_table WHERE fenzu LIKE '%"+x+"%'", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String beiyong = cursor.getString(cursor.getColumnIndex("beiyong"));
                @SuppressLint("Range") String tel = cursor.getString(cursor.getColumnIndex("tel"));
                @SuppressLint("Range") String img = cursor.getString(cursor.getColumnIndex("img"));
                @SuppressLint("Range") String fenzu = cursor.getString(cursor.getColumnIndex("fenzu"));
                Fenzued model = new Fenzued(name, beiyong,tel,img,fenzu);
                dataList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dataList;
    }

    @SuppressLint("Range")
    public List<RecentlyViewed> queryDataA() {
        List<RecentlyViewed> dataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM my_table", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String beiyong = cursor.getString(cursor.getColumnIndex("beiyong"));
                @SuppressLint("Range") String tel = cursor.getString(cursor.getColumnIndex("tel"));
                @SuppressLint("Range") String img = cursor.getString(cursor.getColumnIndex("img"));
                @SuppressLint("Range") String fenzu = cursor.getString(cursor.getColumnIndex("fenzu"));
                RecentlyViewed model = new RecentlyViewed(name, beiyong,tel,img,fenzu);
                dataList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dataList;
    }

    @SuppressLint("Range")
    public List<Fenzued> queryDataS(String x) {
        List<Fenzued> dataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM my_table WHERE name LIKE '%"+x+"%'", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String beiyong = cursor.getString(cursor.getColumnIndex("beiyong"));
                @SuppressLint("Range") String tel = cursor.getString(cursor.getColumnIndex("tel"));
                @SuppressLint("Range") String img = cursor.getString(cursor.getColumnIndex("img"));
                @SuppressLint("Range") String fenzu = cursor.getString(cursor.getColumnIndex("fenzu"));
                Fenzued model = new Fenzued(name, beiyong,tel,img,fenzu);
                dataList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dataList;
    }
}