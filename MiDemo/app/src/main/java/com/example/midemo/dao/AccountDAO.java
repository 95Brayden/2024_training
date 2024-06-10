package com.example.midemo.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.midemo.bean.AccountBean;
import com.example.midemo.bean.BarChartItemBean;
import com.example.midemo.bean.ChartItemBean;
import com.example.midemo.utils.DBOpenHelper;
import com.example.midemo.utils.FloatUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Range")
public class AccountDAO {
    private SQLiteDatabase db;

    public AccountDAO(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }
    public AccountDAO(SQLiteDatabase db) {
        this.db = db;
    }
    // 插入操作
    /**
     * 向记账表中插入一条数据
     *
     * @param bean 需要插入的AccountBean对象
     */
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

    // 更新操作

    /**
     * 根据ID更新金额
     *
     * @param id 记录ID
     * @param newMoney 新的金额
     */
    public void updateMoneyById(int id, float newMoney) {
        ContentValues values = new ContentValues();
        values.put("money", newMoney);
        db.update("accounttb", values, "id=?", new String[]{String.valueOf(id)});
    }

    // 查询操作

    /**
     * 获取某一天的所有支出或收入记录
     *
     * @param year  年份
     * @param month 月份
     * @param day   日期
     * @return 记录的列表
     */
    public List<AccountBean> getAccountListInDay(int year, int month, int day) {
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
    /**
     * 获取某一月的所有支出或收入记录
     *
     * @param year  年份
     * @param month 月份
     * @return 记录的列表
     */
    public List<AccountBean> getAccountListInMonth(int year, int month) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by time desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        // 遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            AccountBean accountBean = new AccountBean(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("typename")),
                    cursor.getInt(cursor.getColumnIndex("sImageId")),
                    cursor.getString(cursor.getColumnIndex("beizhu")),
                    cursor.getFloat(cursor.getColumnIndex("money")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    year, month,
                    cursor.getInt(cursor.getColumnIndex("day")),
                    cursor.getInt(cursor.getColumnIndex("kind"))
            );
            list.add(accountBean);
        }
        cursor.close();
        return list;
    }
    /**
     * 获取某一月按金额排序的所有支出或收入记录
     *
     * @param year      年份
     * @param month     月份
     * @param ascending 是否升序
     * @return 记录的列表
     */
    public List<AccountBean> getAccountListInMonthByMoney(int year, int month,boolean ascending) {
        List<AccountBean> list = new ArrayList<>();
        String order = ascending ? "asc" : "desc";
        String sql = "select * from accounttb where year=? and month=? order by money " + order;
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        // 遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            AccountBean accountBean = new AccountBean(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("typename")),
                    cursor.getInt(cursor.getColumnIndex("sImageId")),
                    cursor.getString(cursor.getColumnIndex("beizhu")),
                    cursor.getFloat(cursor.getColumnIndex("money")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    year, month,
                    cursor.getInt(cursor.getColumnIndex("day")),
                    cursor.getInt(cursor.getColumnIndex("kind"))
            );
            list.add(accountBean);
        }
        cursor.close();
        return list;
    }
    /**
     * 获取某一天的总支出或收入金额
     *
     * @param year  年份
     * @param month 月份
     * @param day   日期
     * @param kind  类型，支出=0，收入=1
     * @return 总金额
     */
    public float getSumMoneyOneDay(int year, int month, int day, int kind) {
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        if (cursor.moveToFirst()) {
            total = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
        }
        cursor.close();
        return total;
    }
    /**
     * 获取某一月的总支出或收入金额
     *
     * @param year  年份
     * @param month 月份
     * @param kind  类型，支出=0，收入=1
     * @return 总金额
     */
    public float getSumMoneyOneMonth(int year, int month,int kind) {
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            total = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
        }
        cursor.close();
        return total;
    }
    /**
     * 获取某月份的记录条数
     *
     * @param year  年份
     * @param month 月份
     * @param kind  类型，支出=0，收入=1
     * @return 记录条数
     */
    public int getCountItemOneMonth(int year,int month,int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("count(money)"));
        }
        return total;
    }

    /**
     * 根据备注搜索记录
     *
     * @param beizhu 备注
     * @return 记录的列表
     */
    public List<AccountBean> getAccountListByRemark(String beizhu) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where beizhu like '%" + beizhu + "%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            AccountBean accountBean = new AccountBean(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("typename")),
                    cursor.getInt(cursor.getColumnIndex("sImageId")),
                    cursor.getString(cursor.getColumnIndex("beizhu")),
                    cursor.getFloat(cursor.getColumnIndex("money")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getInt(cursor.getColumnIndex("year")),
                    cursor.getInt(cursor.getColumnIndex("month")),
                    cursor.getInt(cursor.getColumnIndex("day")),
                    cursor.getInt(cursor.getColumnIndex("kind"))
            );
            list.add(accountBean);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取所有年份
     *
     * @return 年份列表
     */
    public List<Integer>getYearListFromAccount(){
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        cursor.close();
        return list;
    }
    /**
     * 获取某月份每种类型的总金额
     *
     * @param year  年份
     * @param month 月份
     * @param kind  类型,支出=0，收入=1
     * @return 图表项的列表
     */
    public List<ChartItemBean> getChartItemListFromAccount(int year, int month, int kind) {
        List<ChartItemBean> list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);
        String sql = "select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? group by typename order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            float ratio = FloatUtils.div(total, sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取某月中某一天的最高收入或支出金额
     *
     * @param year  年份
     * @param month 月份
     * @param kind  类型，支出=0，收入=1
     * @return 最大金额
     */
    public float getMaxMoneyOneDayInMonth(int year, int month, int kind) {
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            cursor.close();
            return money;
        }
        cursor.close();
        return 0;
    }

    /**
     * 获取某月每一天的收入或支出总金额
     *
     * @param year  年份
     * @param month 月份
     * @param kind  类型，支出=0，收入=1
     * @return 条形图项的列表
     */
    public List<BarChartItemBean> getSumMoneyOneDayInMonth(int year, int month, int kind) {
        List<BarChartItemBean> list = new ArrayList<>();
        String sql = "select day, sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(kind)});
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float sumMoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, sumMoney);
            list.add(itemBean);
        }
        cursor.close();
        return list;
    }

    // 删除操作
    /**
     * 根据ID删除一条记录
     *
     * @param id 记录ID
     */
    public void deleteItemFromAccountById(int id){
        db.delete("accounttb", "id=?", new String[]{id + ""});
    }

    /**
     * 删除所有记录
     */
    public void deleteAllAccount() {
        db.delete("accounttb", null, null);
    }

}
