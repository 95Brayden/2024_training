package com.example.midemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midemo.R;
import com.example.midemo.dao.AccountDAO;

public class SettingActivity extends AppCompatActivity {
    private AccountDAO accountDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        accountDAO = new AccountDAO(this);  // 初始化 AccountDAO 对象
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_iv_back:
                finish();
                break;
            case R.id.setting_tv_clear:
                showDeleteDialog();
                break;
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除提示")
                .setMessage("您确定要删除所有记录么？\n注意：删除后无法恢复，请慎重选择！")
                .setPositiveButton("取消",null)
                .setNegativeButton("确定", (dialog, which) -> {
                    accountDAO.deleteAllAccount();  // 使用 AccountDAO 删除所有记录
                    Toast.makeText(SettingActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                });
        builder.create().show();
    }
}