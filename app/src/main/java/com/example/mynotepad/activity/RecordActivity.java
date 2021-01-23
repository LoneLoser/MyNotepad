package com.example.mynotepad.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotepad.R;
import com.example.mynotepad.dao.Record;
import com.example.mynotepad.manager.DatabaseManager;
import com.example.mynotepad.util.BackgroundUtil;
import com.example.mynotepad.util.DateUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Date;

public class RecordActivity extends BaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, View.OnLongClickListener {

    private TextView dateTimeText;
    private EditText contentEdit;
    private ImageView photoImage;
    private Bitmap photoBitmap = null;
    private boolean hasPhoto = false;

    private Date dateTime;
    private int priority = Record.PRIORITY_GENERAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_record);

        dateTime = new Date();
        initUi();
    }

    private void initUi() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_layout);
        layout.setBackgroundResource(BackgroundUtil.currBgImageId);

        dateTimeText = (TextView) findViewById(R.id.date_time_textView);
        contentEdit = (EditText) findViewById(R.id.content_editText);
        photoImage = (ImageView) findViewById(R.id.photo_imageView);

        findViewById(R.id.back_imageView).setOnClickListener(this);
        findViewById(R.id.add_photo_imageView).setOnClickListener(this);
        findViewById(R.id.cancel_button).setOnClickListener(this);
        findViewById(R.id.confirm_button).setOnClickListener(this);
        RadioGroup priorityGroup = (RadioGroup) findViewById(R.id.priority_group);
        priorityGroup.setOnCheckedChangeListener(this);
        photoImage.setOnLongClickListener(this);

        dateTimeText.setText(DateUtil.dateToString(dateTime));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_imageView:
            case R.id.cancel_button :
                cancelAction();
                break;
            case R.id.confirm_button:
                confirmAction();
                break;
            case R.id.add_photo_imageView:
                addPhoto();
                break;
            default:
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch (id) {
            case R.id.important_radio:
                priority = Record.PRIORITY_IMPORTANT;
                break;
            case R.id.general_radio:
                priority = Record.PRIORITY_GENERAL;
                break;
            case R.id.unimportant_radio:
                priority = Record.PRIORITY_UNIMPORTANT;
                break;
            default:
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.photo_imageView:
                deletePhoto();
                break;
            default:
        }
        return true;
    }

    /**
     * 取消操作事件
     */
    private void cancelAction() {
        finish(); // 返回主界面
    }

    /**
     * 确定操作事件
     */
    private void confirmAction() {
        Record record = new Record(dateTime, contentEdit.getText().toString(), priority, photoBitmap);
        boolean result = DatabaseManager.getDatabaseManager().insertData(record);
        if (result) {
            showTip("记录保存成功");
            finish();
        } else {
            showTip("记录保存失败");
        }
    }

    /**
     * 添加照片
     */
    private void addPhoto() {
        if (hasPhoto) {
            showTip("目前只能添加一张照片喔");
        } else {
            // 是否授权读写SD卡
            if (ContextCompat.checkSelfPermission(RecordActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 申请授权读写SD卡
                ActivityCompat.requestPermissions(RecordActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                openAlbum(); // 打开相册
            }
        }
    }

    /**
     * 弹出删除照片对话框
     */
    private void deletePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
        builder.setTitle("Warning");
        builder.setMessage("您确定要删除这张图片吗[·_·?]");
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                photoBitmap = null;
                photoImage.setImageBitmap(null);
                photoImage.setVisibility(View.GONE);
                hasPhoto = false;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // 获得相册中的照片并添加
                    Uri uri = data.getData();
                    try {
                        photoBitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(uri));
                        photoImage.setImageBitmap(photoBitmap);
                        photoImage.setVisibility(View.VISIBLE);
                        hasPhoto = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得权限，打开相册
                    openAlbum();
                } else {
                    showTip("您没有授予读相册的权限");
                }
                break;
            default:
        }
    }

    /**
     * 显示弹窗
     * @param msg 提示信息
     */
    private void showTip(String msg) {
        Toast.makeText(RecordActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
