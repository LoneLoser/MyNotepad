package com.example.mynotepad.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynotepad.R;

public class LoginActivity extends BaseActivity {

    private String xmlName = "data";
    private EditText usernameEdit, passwordEdit;
    private CheckBox autoLoginCheck;
    private String username, password;
    private boolean isAutoLogin;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        readFromXml();
        if ("".equals(username)) {
            // 跳转注册
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        } else if (isAutoLogin) {
            // 自动登录
            loginSuccess();
        } else {
            // 手动登录
            usernameEdit = (EditText) findViewById(R.id.username_editText);
            passwordEdit = (EditText) findViewById(R.id.password_editText);
            autoLoginCheck = (CheckBox) findViewById(R.id.auto_login_checkBox);
            findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginAction();
                }
            });   
        }
    }

    /**
     * 登录操作
     */
    private void loginAction() {
        String currUsername = usernameEdit.getText().toString();
        String currPassword = passwordEdit.getText().toString();
        boolean currAutoLogin = autoLoginCheck.isChecked();
        if (username.equals(currUsername) && password.equals(currPassword)) {
            // 登录成功
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isAutoLogin", currAutoLogin);
            editor.apply(); // 提交
            loginSuccess();
        } else {
            showTip("用户名或密码不正确");
            passwordEdit.setText("");
        }
    }

    /**
     * 登录成功
     */
    private void loginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    /**
     * 从SharedPreferences读数据
     */
    private void readFromXml() {
        preferences = getSharedPreferences(xmlName, MODE_PRIVATE);
        username = preferences.getString("username", "");
        password = preferences.getString("password", "");
        isAutoLogin = preferences.getBoolean("isAutoLogin", false);
    }

    /**
     * 显示弹窗
     * @param msg 提示信息
     */
    private void showTip(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
