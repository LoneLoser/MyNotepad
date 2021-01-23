package com.example.mynotepad.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mynotepad.R;
import com.example.mynotepad.adapter.MainAdapter;
import com.example.mynotepad.util.BackgroundUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private String[] items = new String[]{"回忆美好", "记录生活", "静心冥想"};
    private TextView quitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
    }

    private void initUi() {
        BackgroundUtil.backgroundInit();
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_layout);
        TextView titleText = (TextView) findViewById(R.id.title_textView);
        TextView littleTitleText = (TextView) findViewById(R.id.little_title_textView);
        layout.setBackgroundResource(BackgroundUtil.currBgImageId);
        titleText.setText(BackgroundUtil.currText);
        titleText.setTextColor(BackgroundUtil.currTextColor);
        littleTitleText.setTextColor(BackgroundUtil.currTextColor);

        String username = getIntent().getStringExtra("username");
        TextView usernameText = (TextView) findViewById(R.id.username_textView);
        usernameText.setText("Hi，" + username);
        usernameText.setTextColor(BackgroundUtil.currTextColor);
        usernameText.setOnClickListener(this);
        quitText = (TextView) findViewById(R.id.quit_textView);
        quitText.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_items_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // 布局管理器
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 横向滚动
        recyclerView.setLayoutManager(layoutManager);
        MainAdapter adapter = new MainAdapter(items); // 适配器
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.username_textView:
                if (quitText.getVisibility() == View.VISIBLE) {
                    quitText.setVisibility(View.GONE);
                } else {
                    quitText.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.quit_textView:
                updateSharedPref(); // 修改自动登录为false
                // 发送退出登录广播，广播注册在BaseActivity
                Intent intent = new Intent();
                intent.setAction("FORCE_OFFLINE");
                sendBroadcast(intent);
                break;
            default:
        }
    }

    /**
     * 退出时修改自动登录为false
     */
    private void updateSharedPref() {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putBoolean("isAutoLogin", false);
        editor.apply(); // 提交
    }
}
