package com.example.midemo.utils;

import android.content.Context; // 导入Context类，用于获取上下文环境
import android.database.sqlite.SQLiteDatabase; // 导入SQLiteDatabase类，用于操作数据库
import android.database.sqlite.SQLiteOpenHelper; // 导入SQLiteOpenHelper类，用于创建和管理数据库

public class DatabaseHelper extends SQLiteOpenHelper {
    // 数据库名称和版本号
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // 用户表和列的定义
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // 创建用户表的SQL语句
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT);";

    // 构造函数，接受Context参数
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 数据库第一次创建时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE); // 执行创建表的SQL语句
    }

    // 数据库版本升级时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER); // 删除旧的用户表
        onCreate(db); // 重新创建新的用户表
    }
}
