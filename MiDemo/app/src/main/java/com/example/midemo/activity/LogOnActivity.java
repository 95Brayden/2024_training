package com.example.midemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.midemo.R;
import com.example.midemo.entity.User;
import com.example.midemo.dao.UserDAO;

public class LogOnActivity extends Activity implements View.OnClickListener {
    private UserDAO userDAO;
    private EditText userNameEditText;
    private EditText passWordEditText;
    private Button loginButton;
    private Button signUpButton;
    private Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_on);

        initViews();
        initUserDAO();
        setupListeners();
    }

    private void initViews() {
        userNameEditText = findViewById(R.id.UserNameEdit);
        passWordEditText = findViewById(R.id.PassWordEdit);
        loginButton = findViewById(R.id.LoginButton);
        signUpButton = findViewById(R.id.SignUpButton);
        postButton = findViewById(R.id.PostButton);
    }

    private void initUserDAO() {
        userDAO = new UserDAO(this);
        userDAO.open();
    }

    private void setupListeners() {
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LoginButton:
                handleLogin();
                break;
            case R.id.SignUpButton:
                navigateToSignUp();
                break;
            case R.id.PostButton:
                navigateToPostActivity();
                break;
        }
    }

    private void handleLogin() {
        String strUserName = userNameEditText.getText().toString().trim();
        String strPassWord = passWordEditText.getText().toString().trim();

        User user = new User();
        user.setUsername(strUserName);
        user.setPassword(strPassWord);

        if (userDAO.checkUser(user)) {
            showToast("登录成功！");
            navigateToMainActivity(strUserName, strPassWord);
        } else {
            showToast("密码错误！");
        }
    }

    private void navigateToMainActivity(String username, String password) {
        Intent intent = new Intent(LogOnActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(LogOnActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void navigateToPostActivity() {
        Intent intent = new Intent(LogOnActivity.this, PostActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(LogOnActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userDAO.close();
    }
}
