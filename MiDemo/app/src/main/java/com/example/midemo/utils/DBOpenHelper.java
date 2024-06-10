package com.example.midemo.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.midemo.R;
import com.example.midemo.bean.AccountBean;
import com.example.midemo.dao.AccountDAO;


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
        insertMockData(db);//添加模拟的数据
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
    /* 插入模拟数据 */
    private void insertMockData(SQLiteDatabase db) {
        AccountDAO accountDAO = new AccountDAO(db);

        // 检查是否已经有数据，避免重复插入
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM accounttb", null);
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            // 插入模拟数据
            accountDAO.insertAccount(new AccountBean(0, "购物", 2131689478, "买衣服", 150.0f, "2024年06月08日 14:00", 2024, 6, 8, 0));
            accountDAO.insertAccount(new AccountBean(0, "娱乐", 2131689505, "看电影", 60.0f, "2024年06月06日 20:30", 2024, 6, 6, 0));
            accountDAO.insertAccount(new AccountBean(0, "零食", 2131689485, "买零食", 20.0f, "2024年06月05日 15:45", 2024, 6, 5, 0));
            accountDAO.insertAccount(new AccountBean(0, "医疗", 2131689503, "买药", 35.0f, "2024年06月04日 11:00", 2024, 6, 4, 0));
            accountDAO.insertAccount(new AccountBean(0, "交通", 2131689481, "公交车费", 5.0f, "2024年06月03日 08:00", 2024, 6, 3, 0));
            accountDAO.insertAccount(new AccountBean(0, "住宅", 2131689507, "交房租", 800.0f, "2024年06月01日 09:00", 2024, 6, 1, 0));
            accountDAO.insertAccount(new AccountBean(0, "通讯", 2131689497, "手机费", 50.0f, "2024年05月29日 10:30", 2024, 5, 29, 0));
            accountDAO.insertAccount(new AccountBean(0, "餐饮", 2131689474, "外卖", 40.0f, "2024年05月28日 12:00", 2024, 5, 28, 0));
            accountDAO.insertAccount(new AccountBean(0, "服饰", 2131689476, "买鞋", 200.0f, "2024年05月27日 16:00", 2024, 5, 27, 0));
            accountDAO.insertAccount(new AccountBean(0, "人情往来", 2131689489, "送礼", 100.0f, "2024年05月26日 18:00", 2024, 5, 26, 0));
            accountDAO.insertAccount(new AccountBean(0, "薪资", 2131689529, "工资收入", 5000.0f, "2024年06月10日 09:00", 2024, 6, 10, 1));
            accountDAO.insertAccount(new AccountBean(0, "奖金", 2131689515, "季度奖金", 1000.0f, "2024年06月09日 10:00", 2024, 6, 9, 1));
            accountDAO.insertAccount(new AccountBean(0, "投资回报", 2131689527, "投资收益", 800.0f, "2024年06月08日 15:00", 2024, 6, 8, 1));
            accountDAO.insertAccount(new AccountBean(0, "意外所得", 2131689531, "彩票中奖", 200.0f, "2024年06月07日 17:00", 2024, 6, 7, 1));
            accountDAO.insertAccount(new AccountBean(0, "利息收入", 2131689519, "银行利息", 50.0f, "2024年06月06日 09:00", 2024, 6, 6, 1));
            accountDAO.insertAccount(new AccountBean(0, "收债", 2131689525, "朋友还钱", 300.0f, "2024年06月05日 14:00", 2024, 6, 5, 1));
            accountDAO.insertAccount(new AccountBean(0, "借入", 2131689517, "借款", 1000.0f, "2024年06月04日 13:00", 2024, 6, 4, 1));
            accountDAO.insertAccount(new AccountBean(0, "二手交易", 2131689513, "卖旧书", 20.0f, "2024年06月03日 11:00", 2024, 6, 3, 1));
            accountDAO.insertAccount(new AccountBean(0, "其他", 2131689521, "其他收入", 100.0f, "2024年06月02日 16:00", 2024, 6, 2, 1));
            accountDAO.insertAccount(new AccountBean(0, "薪资", 2131689529, "工资收入", 4800.0f, "2024年05月31日 09:00", 2024, 5, 31, 1));
        }
        cursor.close();
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