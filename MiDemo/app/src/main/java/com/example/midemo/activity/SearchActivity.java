package com.example.midemo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midemo.R;
import com.example.midemo.adapter.AccountAdapter;
import com.example.midemo.entity.AccountItem;
import com.example.midemo.dao.AccountDAO;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;
    List<AccountItem>mDatas;   //数据源
    AccountAdapter accountAdapter;    //适配器对象
    private AccountDAO accountDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        accountDAO = new AccountDAO(this);  // 初始化 AccountDAO 对象

        initView();
        mDatas = new ArrayList<>();
        accountAdapter = new AccountAdapter(this,mDatas);
        searchLv.setAdapter(accountAdapter);
        searchLv.setEmptyView(emptyTv);   //设置无数局时，显示的控件
    }

    private void initView() {
        searchEt = findViewById(R.id.search_et);
        searchLv = findViewById(R.id.search_lv);
        emptyTv = findViewById(R.id.search_tv_empty);
        setLVLongClickListener();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:   //执行搜索的操作
                String msg = searchEt.getText().toString().trim();
                //判断输入内容是否为空，如果为空，就提示不能搜索
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this,"输入内容不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //开始搜索
                List<AccountItem> list = accountDAO.getAccountListByRemark(msg);
                mDatas.clear();
                mDatas.addAll(list);
                accountAdapter.notifyDataSetChanged();
                break;
        }
    }
    /** 设置ListView的长按事件*/
    private void setLVLongClickListener() {
        searchLv.setOnItemLongClickListener((parent, view, position, id) -> {
            AccountItem clickBean = mDatas.get(position);  //获取正在被点击的这条信息
            //弹出提示用户是否删除的对话框
            showDeleteItemDialog(clickBean);
            return true;
        });
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
                    accountAdapter.notifyDataSetChanged();   //提示适配器更新数据
                });
        builder.create().show();   //显示对话框
    }

}