package com.example.midemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midemo.R;
import com.example.midemo.adapter.AccountAdapter;
import com.example.midemo.entity.AccountItem;
import com.example.midemo.dao.AccountDAO;
import com.example.midemo.dialog.BudgetDialog;
import com.example.midemo.dialog.EditDialog;
import com.example.midemo.dialog.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;  //展示今日收支情况的ListView
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;

    AccountAdapter adapter;
    private AccountDAO accountDAO;

    int year,month,day;
    //声明数据源
    List<AccountItem> mDatas;
    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        accountDAO = new AccountDAO(this);  // 初始化 AccountDAO 对象

        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        //添加ListView的头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();
        //设置适配器：加载每一行数据到列表当中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }

    private void addLVHeaderView() {
        //将布局转换成View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
    }

    /* 获取今日的具体时间*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
    /** 初始化自带的View的方法*/
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
        // 设置ListView的点击事件
        setLVClickListener();
    }

    /** 设置ListView的长按事件*/
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener((parent, view, position, id) -> {
            if (position == 0) {  //点击了头布局
                return false;
            }
            int pos = position-1;
            AccountItem clickBean = mDatas.get(pos);  //获取正在被点击的这条信息

            //弹出提示用户是否删除的对话框
            showDeleteItemDialog(clickBean);
            return true;
        });
    }

    /** 设置ListView的点击事件*/
    private void setLVClickListener() {
        todayLv.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {  // 点击了头布局
                return;
            }
            int pos = position - 1;
            AccountItem clickBean = mDatas.get(pos);  // 获取正在被点击的这条信息
            // 弹出修改金额对话框
            showEditDialog(clickBean);
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_iv_search:
                Intent it = new Intent(this, SearchActivity.class);  //跳转界面
                startActivity(it);
                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);  //跳转界面
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                // 切换TextView明文和密文
                toggleShow();
                break;
        }
        if (v == headerView) {
            //头布局被点击了
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }

    // 当activity获取焦点时，会调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }

    private void setTopTvShow() {
        //获取今日支出和收入总金额，显示在view当中
        float incomeOneDay = accountDAO.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = accountDAO.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日支出 ￥"+outcomeOneDay+"  收入 ￥"+incomeOneDay;
        topConTv.setText(infoOneDay);
        //获取本月收入和支出总金额
        float incomeOneMonth = accountDAO.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = accountDAO.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("￥"+incomeOneMonth);
        topOutTv.setText("￥"+outcomeOneMonth);
    }

    // 加载数据库数据
    private void loadDBData() {
        List<AccountItem> list = accountDAO.getAccountListInDay(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }
    boolean isShow = true;
    /**
     * 点击头布局眼睛时，如果原来是明文，就加密，如果是密文，就显示出来
     * */
    private void toggleShow() {
        if (isShow) {   //明文====》密文
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topOutTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topbudgetTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;   //设置标志位为隐藏状态
        }else{  //密文---》明文
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod);   //设置隐藏
            topOutTv.setTransformationMethod(hideMethod);   //设置隐藏
            topbudgetTv.setTransformationMethod(hideMethod);   //设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;   //设置标志位为隐藏状态
        }
    }
    /* 弹出是否删除某一条记录的对话框*/
    private void showDeleteItemDialog(final AccountItem clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录么？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", (dialog, which) -> {
                    int click_id = clickBean.getId();
                    //执行删除的操作
                    accountDAO.deleteItemFromAccountById(click_id);
                    mDatas.remove(clickBean);   //实时刷新，移除集合当中的对象
                    adapter.notifyDataSetChanged();   //提示适配器更新数据
                    setTopTvShow();   //改变头布局TextView显示的内容
                });
        builder.create().show();   //显示对话框
    }
    /** 显示运算设置对话框*/
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(money -> {
            //将预算金额写入到共享参数当中，进行存储
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat("bmoney",money);
            editor.apply();
            //计算剩余金额
            float outcomeOneMonth = accountDAO.getSumMoneyOneMonth(year, month, 0);
            float syMoney = money-outcomeOneMonth;//预算剩余 = 预算-支出
            topbudgetTv.setText("￥"+syMoney);
        });
    }
    /** 显示修改金额设置对话框*/
    private void showEditDialog(AccountItem accountItem) {
        EditDialog dialog = new EditDialog(this, accountItem.getId(), accountItem.getMoney());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener((id, money) -> {
            // 更新数据库中的金额
            accountDAO.updateMoneyById(id, money);
            // 刷新数据和界面显示
            loadDBData();
            setTopTvShow();   //改变头布局TextView显示的内容
        });
    }
}