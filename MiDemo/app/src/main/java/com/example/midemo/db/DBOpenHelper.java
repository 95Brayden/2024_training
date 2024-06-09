package com.example.midemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.midemo.R;


public class DBOpenHelper extends SQLiteOpenHelper {
    // 数据库名称和版本号
    private static final String DATABASE_NAME = "midemo.db";
    private static final int DATABASE_VERSION = 1;
    // 用户表和列的定义
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    // 创建用户表的SQL语句
    private static final String TABLE_CREATE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT);";
    // 类型表和记账表的定义
    private static final String TABLE_TYPE = "typetb";
    private static final String TABLE_ACCOUNT = "accounttb";

    // 创建类型表的SQL语句
    private static final String TABLE_CREATE_TYPE =
            "CREATE TABLE " + TABLE_TYPE + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "typename VARCHAR(10), " +
                    "imageId INTEGER, " +
                    "sImageId INTEGER, " +
                    "kind INTEGER);";

    // 创建记账表的SQL语句
    private static final String TABLE_CREATE_ACCOUNT =
            "CREATE TABLE " + TABLE_ACCOUNT + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "typename VARCHAR(10), " +
                    "sImageId INTEGER, " +
                    "beizhu VARCHAR(80), " +
                    "money FLOAT, " +
                    "time VARCHAR(60), " +
                    "year INTEGER, " +
                    "month INTEGER, " +
                    "day INTEGER, " +
                    "kind INTEGER);";
    public DBOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // 数据库第一次创建时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USER); // 创建用户表
        db.execSQL(TABLE_CREATE_TYPE); // 创建类型表
        db.execSQL(TABLE_CREATE_ACCOUNT); // 创建记账表
        insertType(db); // 插入类型数据
    }
    // 插入类型数据的方法
    private void insertType(SQLiteDatabase db) {
        String sql = "INSERT INTO " + TABLE_TYPE + " (typename, imageId, sImageId, kind) VALUES (?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{"其他", R.mipmap.ic_qita, R.mipmap.ic_qita_fs, 0});
        db.execSQL(sql, new Object[]{"餐饮", R.mipmap.ic_canyin, R.mipmap.ic_canyin_fs, 0});
        db.execSQL(sql, new Object[]{"交通", R.mipmap.ic_jiaotong, R.mipmap.ic_jiaotong_fs, 0});
        db.execSQL(sql, new Object[]{"购物", R.mipmap.ic_gouwu, R.mipmap.ic_gouwu_fs, 0});
        db.execSQL(sql, new Object[]{"服饰", R.mipmap.ic_fushi, R.mipmap.ic_fushi_fs, 0});
        db.execSQL(sql, new Object[]{"日用品", R.mipmap.ic_riyongpin, R.mipmap.ic_riyongpin_fs, 0});
        db.execSQL(sql, new Object[]{"娱乐", R.mipmap.ic_yule, R.mipmap.ic_yule_fs, 0});
        db.execSQL(sql, new Object[]{"零食", R.mipmap.ic_lingshi, R.mipmap.ic_lingshi_fs, 0});
        db.execSQL(sql, new Object[]{"烟酒茶", R.mipmap.ic_yanjiu, R.mipmap.ic_yanjiu_fs, 0});
        db.execSQL(sql, new Object[]{"学习", R.mipmap.ic_xuexi, R.mipmap.ic_xuexi_fs, 0});
        db.execSQL(sql, new Object[]{"医疗", R.mipmap.ic_yiliao, R.mipmap.ic_yiliao_fs, 0});
        db.execSQL(sql, new Object[]{"住宅", R.mipmap.ic_zhufang, R.mipmap.ic_zhufang_fs, 0});
        db.execSQL(sql, new Object[]{"水电煤", R.mipmap.ic_shuidianfei, R.mipmap.ic_shuidianfei_fs, 0});
        db.execSQL(sql, new Object[]{"通讯", R.mipmap.ic_tongxun, R.mipmap.ic_tongxun_fs, 0});
        db.execSQL(sql, new Object[]{"人情往来", R.mipmap.ic_renqingwanglai, R.mipmap.ic_renqingwanglai_fs, 0});

        db.execSQL(sql, new Object[]{"其他", R.mipmap.in_qt, R.mipmap.in_qt_fs, 1});
        db.execSQL(sql, new Object[]{"薪资", R.mipmap.in_xinzi, R.mipmap.in_xinzi_fs, 1});
        db.execSQL(sql, new Object[]{"奖金", R.mipmap.in_jiangjin, R.mipmap.in_jiangjin_fs, 1});
        db.execSQL(sql, new Object[]{"借入", R.mipmap.in_jieru, R.mipmap.in_jieru_fs, 1});
        db.execSQL(sql, new Object[]{"收债", R.mipmap.in_shouzhai, R.mipmap.in_shouzhai_fs, 1});
        db.execSQL(sql, new Object[]{"利息收入", R.mipmap.in_lixifuji, R.mipmap.in_lixifuji_fs, 1});
        db.execSQL(sql, new Object[]{"投资回报", R.mipmap.in_touzi, R.mipmap.in_touzi_fs, 1});
        db.execSQL(sql, new Object[]{"二手交易", R.mipmap.in_ershoushebei, R.mipmap.in_ershoushebei_fs, 1});
        db.execSQL(sql, new Object[]{"意外所得", R.mipmap.in_yiwai, R.mipmap.in_yiwai_fs, 1});
    }

    // 数据库版本升级时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER); // 删除旧的用户表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE); // 删除旧的类型表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT); // 删除旧的记账表
        onCreate(db); // 重新创建所有表
    }
}