package com.example.midemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.midemo.dao.UserDAO;

public class SignUpActivity extends Activity {
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText userName = this.findViewById(R.id.UserNameEdit);
        EditText passWord = this.findViewById(R.id.PassWordEdit);
        Button signUpButton = this.findViewById(R.id.SignUpButton);
        ImageButton backButton = this.findViewById(R.id.BackButton);

        userDAO = new UserDAO(this);
        userDAO.open();

        signUpButton.setOnClickListener(v -> {
            String strUserName = userName.getText().toString().trim();
            String strPassWord = passWord.getText().toString().trim();

            if (!strUserName.isEmpty() && !strPassWord.isEmpty()) {
                userDAO.addUser(strUserName, strPassWord);
                Toast.makeText(SignUpActivity.this, "用户成功注册！", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "请正确输入用户信息！", Toast.LENGTH_SHORT).show();
            }
        });
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userDAO.close();
    }
}
