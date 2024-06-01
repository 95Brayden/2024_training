package com.example.midemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LogOnActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_on); // 注意这里应是 LogOnActivity 对应的布局文件

        EditText userName = this.findViewById(R.id.UserNameEdit);
        EditText passWord = this.findViewById(R.id.PassWordEdit);
        Button loginButton = this.findViewById(R.id.LoginButton);
        Button signUpButton = this.findViewById(R.id.SignUpButton);

        loginButton.setOnClickListener(v -> {
            String strUserName = userName.getText().toString().trim();
            String strPassWord = passWord.getText().toString().trim();

            Intent intent = new Intent(LogOnActivity.this, MainActivity.class);
            intent.putExtra("username", strUserName);
            intent.putExtra("password", strPassWord);
            startActivity(intent);
        });

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogOnActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
