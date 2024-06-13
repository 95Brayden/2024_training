package com.example.midemo.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.midemo.entity.Type;
import com.example.midemo.utils.DataBaseManager;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Range")
public class TypeDAO {
    private SQLiteDatabase db;

    public TypeDAO(Context context) {
        DataBaseManager helper = new DataBaseManager(context);
        db = helper.getWritableDatabase();
    }

    public TypeDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public List<Type> getTypeList(int kind) {
        List<Type> list = new ArrayList<>();
        String sql = "select * from type where kind = " + kind;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            Type type = new Type(id, typename, imageId, sImageId, kind);
            list.add(type);
        }
        cursor.close();
        return list;
    }
}
