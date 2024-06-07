package com.example.midemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.main_iv_search) {
            // 执行搜索操作
        } else if (id == R.id.main_btn_edit) {
            Intent it1 = new Intent(this, RecordActivity.class);  // 跳转界面
            startActivity(it1);
        } else if (id == R.id.main_btn_more) {
            // 执行更多操作
        } else if (id == R.id.item_mainlv_top_tv_budget) {
            // 处理预算相关操作
        } else if (id == R.id.item_mainlv_top_iv_hide) {
            // 切换TextView明文和密文
        } else {
            // 处理未知ID
        }
    }
}