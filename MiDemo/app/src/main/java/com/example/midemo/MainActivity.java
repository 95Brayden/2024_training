package com.example.midemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeTextView = this.findViewById(R.id.WelcomeTextView);

        // 获取Intent传递过来的数据
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        // 设置欢迎信息
        String welcomeMessage = "欢迎, " + username + "! 你的密码是 " + password;
        welcomeTextView.setText(welcomeMessage);
    }
}
