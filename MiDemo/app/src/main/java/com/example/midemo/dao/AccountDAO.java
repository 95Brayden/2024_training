package com.example.midemo.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.midemo.bean.AccountBean;
import com.example.midemo.db.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Range")
public class AccountDAO {
    private SQLiteDatabase db;

    public AccountDAO(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    /*
     * 向记账表当中插入一条元素
     * */
    public void insertAccount(AccountBean bean) {
        ContentValues values = new ContentValues();
        values.put("typename", bean.getTypename());
        values.put("sImageId", bean.getsImageId());
        values.put("beizhu", bean.getBeizhu());
        values.put("money", bean.getMoney());
        values.put("time", bean.getTime());
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("kind", bean.getKind());
        db.insert("accounttb", null, values);
    }
    /*
     * 获取记账表当中某一天的所有支出或者收入情况
     * */
    public List<AccountBean> getAccountsByDay(int year, int month, int day) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        while (cursor.moveToNext()) {
            AccountBean accountBean = new AccountBean(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("typename")),
                    cursor.getInt(cursor.getColumnIndex("sImageId")),
                    cursor.getString(cursor.getColumnIndex("beizhu")),
                    cursor.getFloat(cursor.getColumnIndex("money")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    year, month, day,
                    cursor.getInt(cursor.getColumnIndex("kind"))
            );
            list.add(accountBean);
        }
        cursor.close();
        return list;
    }
}
