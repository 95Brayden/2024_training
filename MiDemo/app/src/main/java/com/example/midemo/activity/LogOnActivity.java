package com.example.midemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.midemo.R;
import com.example.midemo.entity.User;
import com.example.midemo.dao.UserDAO;

public class LogOnActivity extends Activity {
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_on);

        EditText userName = this.findViewById(R.id.UserNameEdit);
        EditText passWord = this.findViewById(R.id.PassWordEdit);
        Button loginButton = this.findViewById(R.id.LoginButton);
        Button signUpButton = this.findViewById(R.id.SignUpButton);

        userDAO = new UserDAO(this);
        userDAO.open();

        loginButton.setOnClickListener(v -> {
            String strUserName = userName.getText().toString().trim();
            String strPassWord = passWord.getText().toString().trim();

            User user = new User();
            user.setUsername(strUserName);
            user.setPassword(strPassWord);

            if (userDAO.checkUser(user)) {
                Toast.makeText(LogOnActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LogOnActivity.this, MainActivity.class);
                intent.putExtra("username", strUserName);
                intent.putExtra("password", strPassWord);
                startActivity(intent);
            } else {
                Toast.makeText(LogOnActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
            }
        });

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogOnActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userDAO.close();
    }
}
