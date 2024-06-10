package com.example.midemo.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.midemo.R;
import com.example.midemo.adapter.AccountAdapter;
import com.example.midemo.entity.AccountItem;
import com.example.midemo.dao.AccountDAO;
import com.example.midemo.dialog.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    ListView historyLv;
    TextView timeTv;
    List<AccountItem> mDatas;
    AccountAdapter adapter;
    int year,month;
    int dialogSelPos = -1;
    int dialogSelMonth = -1;
    Spinner sortSpinner;
    private AccountDAO accountDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);
        sortSpinner=findViewById(R.id.sort_spinner);
        accountDAO = new AccountDAO(this);  // 初始化 AccountDAO 对象

        mDatas = new ArrayList<>();
        // 设置适配器
        adapter = new AccountAdapter(this,mDatas);
        historyLv.setAdapter(adapter);
        initTime();
        timeTv.setText(year+"年"+month+"月");
        loadData(year,month);
        setLVClickListener();
        setSpinnerListener();
    }
    /*设置ListView每一个item的长按事件*/
    private void setLVClickListener() {
        historyLv.setOnItemLongClickListener((parent, view, position, id) -> {
            AccountItem accountItem = mDatas.get(position);
            deleteItem(accountItem);
            return false;
        });
    }
    private void setSpinnerListener() {
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    loadData(year,month);
                }
                else if (position == 1) {
                    loadData(year, month, true); // 按金额升序加载数据
                } else if (position == 2) {
                    loadData(year, month, false); // 按金额降序加载数据
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 什么也不做
            }
        });
    }

    private void deleteItem(final AccountItem accountItem) {
        final int delId = accountItem.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录么？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", (dialog, which) -> {
                    accountDAO.deleteItemFromAccountById(delId);
                    mDatas.remove(accountItem);   //实时刷新，从数据源删除
                    adapter.notifyDataSetChanged();
                });
        builder.create().show();
    }

    /* 获取指定年份月份收支情况的列表*/
    private void loadData(int year,int month) {
        List<AccountItem> list = accountDAO.getAccountListInMonth(year, month);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }
    private void loadData(int year, int month, boolean ascending) {
        List<AccountItem> list = accountDAO.getAccountListInMonthByMoney(year, month,ascending);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_iv_calendar:
                CalendarDialog dialog = new CalendarDialog(this,dialogSelPos,dialogSelMonth);
                dialog.show();
                dialog.setDialogSize();
                dialog.setOnRefreshListener((selPos, selectedYear, selectedMonth) -> {
                    timeTv.setText(selectedYear+"年"+selectedMonth+"月");
                    loadData(selectedYear,selectedMonth);
                    dialogSelPos = selPos;
                    dialogSelMonth = month;
                    // 更新全局变量，以便后续操作中使用
                    year = selectedYear;
                    month = selectedMonth;
                });
                break;
        }
    }
}