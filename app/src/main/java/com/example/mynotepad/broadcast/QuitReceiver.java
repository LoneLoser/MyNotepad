package com.example.mynotepad.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.mynotepad.activity.LoginActivity;
import com.example.mynotepad.manager.ActivityCollector;

/**
 * 退出登录广播接收器
 * Created by Administrator on 2020/5/17.
 */

public class QuitReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        // 弹出对话框，提示退出登录
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning");
        builder.setMessage("你确定要退出登录吗[·_·?]");
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 点击确定按钮，关闭所有活动，启动登录活动
                ActivityCollector.getActivityCollector().finishAll();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

}
