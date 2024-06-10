package com.example.midemo.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.Cursor;

import com.example.midemo.bean.AccountBean;
import com.example.midemo.bean.BarChartItemBean;
import com.example.midemo.bean.ChartItemBean;
import com.example.midemo.bean.TypeBean;
import com.example.midemo.utils.FloatUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * 负责管理数据库的类
 *   主要对于表当中的内容进行操作，增删改查
 * */
@SuppressLint("Range")
public class DBManager {
    private static SQLiteDatabase db;
    /* 初始化数据库对象*/
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);  //得到帮助类对象
        db = helper.getWritableDatabase();      //得到数据库对象
        // 插入模拟数据
        insertMockData();
    }
    /* 插入模拟数据 */
    private static void insertMockData() {
        // 检查是否已经有数据，避免重复插入
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM accounttb", null);
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            // 插入模拟数据
            insertItemToAccounttb(new AccountBean(0, "购物", 2131689478, "买衣服", 150.0f, "2024年06月08日 14:00", 2024, 6, 8, 0));
            insertItemToAccounttb(new AccountBean(0, "娱乐", 2131689505, "看电影", 60.0f, "2024年06月06日 20:30", 2024, 6, 6, 0));
            insertItemToAccounttb(new AccountBean(0, "零食", 2131689485, "买零食", 20.0f, "2024年06月05日 15:45", 2024, 6, 5, 0));
            insertItemToAccounttb(new AccountBean(0, "医疗", 2131689503, "买药", 35.0f, "2024年06月04日 11:00", 2024, 6, 4, 0));
            insertItemToAccounttb(new AccountBean(0, "交通", 2131689481, "公交车费", 5.0f, "2024年06月03日 08:00", 2024, 6, 3, 0));
            insertItemToAccounttb(new AccountBean(0, "住宅", 2131689507, "交房租", 800.0f, "2024年06月01日 09:00", 2024, 6, 1, 0));
            insertItemToAccounttb(new AccountBean(0, "通讯", 2131689497, "手机费", 50.0f, "2024年05月29日 10:30", 2024, 5, 29, 0));
            insertItemToAccounttb(new AccountBean(0, "餐饮", 2131689474, "外卖", 40.0f, "2024年05月28日 12:00", 2024, 5, 28, 0));
            insertItemToAccounttb(new AccountBean(0, "服饰", 2131689476, "买鞋", 200.0f, "2024年05月27日 16:00", 2024, 5, 27, 0));
            insertItemToAccounttb(new AccountBean(0, "人情往来", 2131689489, "送礼", 100.0f, "2024年05月26日 18:00", 2024, 5, 26, 0));
            insertItemToAccounttb(new AccountBean(0, "薪资", 2131689529, "工资收入", 5000.0f, "2024年06月10日 09:00", 2024, 6, 10, 1));
            insertItemToAccounttb(new AccountBean(0, "奖金", 2131689515, "季度奖金", 1000.0f, "2024年06月09日 10:00", 2024, 6, 9, 1));
            insertItemToAccounttb(new AccountBean(0, "投资回报", 2131689527, "投资收益", 800.0f, "2024年06月08日 15:00", 2024, 6, 8, 1));
            insertItemToAccounttb(new AccountBean(0, "意外所得", 2131689531, "彩票中奖", 200.0f, "2024年06月07日 17:00", 2024, 6, 7, 1));
            insertItemToAccounttb(new AccountBean(0, "利息收入", 2131689519, "银行利息", 50.0f, "2024年06月06日 09:00", 2024, 6, 6, 1));
            insertItemToAccounttb(new AccountBean(0, "收债", 2131689525, "朋友还钱", 300.0f, "2024年06月05日 14:00", 2024, 6, 5, 1));
            insertItemToAccounttb(new AccountBean(0, "借入", 2131689517, "借款", 1000.0f, "2024年06月04日 13:00", 2024, 6, 4, 1));
            insertItemToAccounttb(new AccountBean(0, "二手交易", 2131689513, "卖旧书", 20.0f, "2024年06月03日 11:00", 2024, 6, 3, 1));
            insertItemToAccounttb(new AccountBean(0, "其他", 2131689521, "其他收入", 100.0f, "2024年06月02日 16:00", 2024, 6, 2, 1));
            insertItemToAccounttb(new AccountBean(0, "薪资", 2131689529, "工资收入", 4800.0f, "2024年05月31日 09:00", 2024, 5, 31, 1));
        }
        cursor.close();
    }

    /**
     * 读取数据库当中的数据，写入内存集合里
     *   kind :表示收入或者支出
     * */
    public static List<TypeBean> getTypeList(int kind){
        List<TypeBean>list = new ArrayList<>();
        //读取typetb表当中的数据
        String sql = "select * from typetb where kind = "+kind;
        Cursor cursor = db.rawQuery(sql, null);
        //        循环读取游标内容，存储到对象当中
        while (cursor.moveToNext()) {
            String typename=cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        return list;
    }
    /**
     * 向记账表中插入一条数据
     *
     * @param bean 需要插入的AccountBean对象
     */
    public static void insertItemToAccounttb(AccountBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("sImageId",bean.getsImageId());
        values.put("beizhu",bean.getBeizhu());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        db.insert("accounttb",null,values);
    }
    /*
     * 获取记账表当中某一天的所有支出或者收入情况
     * */
    public static List<AccountBean>getAccountListOneDayFromAccounttb(int year,int month,int day){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /**
     * 获取某一天的支出或者收入的总金额   kind：支出==0    收入===1
     * */
    public static float getSumMoneyOneDay(int year,int month,int day,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            total = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
        }
        return total;
    }
    /*
     * 获取记账表当中某一月的所有支出或者收入情况
     * */
    public static List<AccountBean> getAccountListOneMonthFromAccounttb(int year, int month) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by time desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /*
     * 获取记账表当中某一月的所有支出或者收入情况(按金额排序)
     */
    public static List<AccountBean> getAccountListOneMonthFromAccounttbByAsc(int year, int month, boolean ascending) {
        List<AccountBean> list = new ArrayList<>();
        String order = ascending ? "asc" : "desc";
        String sql = "select * from accounttb where year=? and month=? order by money " + order;
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(year), String.valueOf(month)});

        // 遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }

        cursor.close(); // 记得关闭 cursor
        return list;
    }
    /**
     * 获取某一月的支出或者收入的总金额   kind：支出==0    收入===1
     * */
    public static float getSumMoneyOneMonth(int year,int month,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            total = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
        }
        return total;

    }
    /** 统计某月份支出或者收入情况有多少条  收入-1   支出-0*/
    public static int getCountItemOneMonth(int year,int month,int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("count(money)"));
        }
        return total;
    }
    /*
     * 根据传入的id，删除accounttb表当中的一条数据
     * */
    public static void deleteItemFromAccounttbById(int id){
         db.delete("accounttb", "id=?", new String[]{id + ""});
    }
    /**
     * 根据备注搜索收入或者支出的情况列表
     * */
    public static List<AccountBean>getAccountListByRemarkFromAccounttb(String beizhu){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where beizhu like '%"+beizhu+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /**
     * 查询记账的表当中有几个年份信息
     * */
    public static List<Integer>getYearListFromAccounttb(){
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }
    /*
     * 删除accounttb表格当中的所有数据
     * */
    public static void deleteAllAccount(){
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }
    /**
     * 查询指定年份和月份的收入或者支出每一种类型的总钱数
     * */
    public static List<ChartItemBean>getChartListFromAccounttb(int year, int month, int kind){
        List<ChartItemBean>list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);  //求出支出或者收入总钱数
        String sql = "select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            //计算所占百分比  total /sumMonth
            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }
        return list;
    }
    /**
     * 获取这个月当中某一天收入支出最大的金额，金额是多少
     * */
    public static float getMaxMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float sum_money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return sum_money;
        }
        return 0;
    }
}