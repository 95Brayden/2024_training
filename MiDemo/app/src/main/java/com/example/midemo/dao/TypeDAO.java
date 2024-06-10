package com.example.midemo.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.midemo.bean.TypeBean;
import com.example.midemo.utils.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Range")
public class TypeDAO {
    private SQLiteDatabase db;

    public TypeDAO(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public TypeDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public List<TypeBean> getTypeList(int kind) {
        List<TypeBean> list = new ArrayList<>();
        String sql = "select * from typetb where kind = " + kind;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        cursor.close();
        return list;
    }
}
