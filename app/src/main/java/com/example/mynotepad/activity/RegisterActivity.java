package com.example.mynotepad.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynotepad.R;

public class RegisterActivity extends BaseActivity {

    private String xmlName = "data";
    private EditText usernameEdit, passwordEdit, repasswordEdit;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEdit = (EditText) findViewById(R.id.username_editText);
        passwordEdit = (EditText) findViewById(R.id.password_editText);
        repasswordEdit = (EditText) findViewById(R.id.repassword_editText);
        findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAction();
            }
        });
    }

    /**
     * 注册操作
     */
    private void registerAction() {
        username = usernameEdit.getText().toString();
        password = passwordEdit.getText().toString();
        String repassword = repasswordEdit.getText().toString();
        if ("".equals(username)) {
            showTip("用户名不能为空");
        } else if ("".equals(password)) {
            showTip("密码不能为空");
        } else if (!password.equals(repassword)) {
            showTip("密码不一致");
            passwordEdit.setText("");
            repasswordEdit.setText("");
        } else {
            // 注册成功
            writeToXml();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 存储到SharedPreferences（共享首选项）
     */
    private void writeToXml() {
        SharedPreferences.Editor editor = getSharedPreferences(xmlName, MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("isAutoLogin", false);
        editor.apply(); // 提交数据
    }

    /**
     * 显示弹窗
     * @param msg 提示信息
     */
    private void showTip(String msg) {
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
