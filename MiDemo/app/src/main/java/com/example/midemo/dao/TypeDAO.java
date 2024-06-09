package com.example.midemo.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.midemo.bean.TypeBean;
import com.example.midemo.db.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Range")
public class TypeDAO {
    private SQLiteDatabase db;

    public TypeDAO(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public List<TypeBean> getTypes(int kind) {
        List<TypeBean> list = new ArrayList<>();
        String sql = "select * from typetb where kind = " + kind;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            TypeBean typeBean = new TypeBean(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("typename")),
                    cursor.getInt(cursor.getColumnIndex("imageId")),
                    cursor.getInt(cursor.getColumnIndex("sImageId")),
                    cursor.getInt(cursor.getColumnIndex("kind"))
            );
            list.add(typeBean);
        }
        cursor.close();
        return list;
    }

    // 其他Type相关的方法...
}
