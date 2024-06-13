package com.example.midemo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.midemo.entity.User;
import com.example.midemo.utils.DataBaseManager;

// 定义UserDAO类，用于数据库操作
public class UserDAO {
    private SQLiteDatabase database; // SQLiteDatabase对象，用于数据库操作
    private DataBaseManager dbHelper; // DataBaseManager对象，用于创建和管理数据库

    public UserDAO(Context context) { // UserDAO构造函数，接收Context对象
        dbHelper = new DataBaseManager(context); // 初始化DataBaseManager对象
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase(); // 获取可写的数据库实例
        // 打印数据库文件路径
        Log.d("Database Path", database.getPath()); // 记录数据库文件路径
    }

    public void close() {
        dbHelper.close();
    }

    public void addUser(User user) { // 添加用户方法，接收UserBean对象
        ContentValues values = new ContentValues(); // 创建ContentValues对象
        values.put(DataBaseManager.COLUMN_USERNAME, user.getUsername()); // 将用户名放入ContentValues
        values.put(DataBaseManager.COLUMN_PASSWORD, user.getPassword()); // 将密码放入ContentValues
        database.insert(DataBaseManager.TABLE_USER, null, values); // 插入新行到用户表中
    }

    // 检查用户方法，接收UserBean对象
    public boolean checkUser(User user) {
        String[] columns = {
                DataBaseManager.COLUMN_ID
        };
        String selection = DataBaseManager.COLUMN_USERNAME + " = ?" + " AND " + DataBaseManager.COLUMN_PASSWORD + " = ?"; // 查询条件
        String[] selectionArgs = {user.getUsername(), user.getPassword()}; // 查询条件的参数

        Cursor cursor = database.query(DataBaseManager.TABLE_USER, // 执行查询操作
                columns, // 要查询的列
                selection, // 查询条件
                selectionArgs, // 查询条件的参数
                null, // 不进行分组
                null, // 不进行筛选
                null); // 不进行排序

        int cursorCount = cursor.getCount(); // 获取查询结果的行数
        cursor.close();
        return cursorCount > 0; // 如果查询结果行数大于0，则表示用户存在
    }
}
