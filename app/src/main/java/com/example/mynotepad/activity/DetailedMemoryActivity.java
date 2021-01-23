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

import java.io.FileNotFoundException;

public class DetailedMemoryActivity extends BaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, View.OnLongClickListener {

    private Record record;
    private int newPriority;

    private EditText contentEdit;
    private ImageView photoImage;
    private Bitmap photoBitmap;
    private boolean hasPhoto = false;

    private boolean isChanged = false; // 是否有更改（包括删除、更新）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_detailed_memory);

        Intent intent = getIntent();
        record = (Record) intent.getSerializableExtra("record");
        newPriority = record.getPriority();
        photoBitmap = record.getPhotoBitmap();

        initUi();
    }

    private void initUi() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_layout);
        layout.setBackgroundResource(BackgroundUtil.currBgImageId);

        TextView dateText = (TextView) findViewById(R.id.date_time_textView);
        contentEdit = (EditText) findViewById(R.id.content_editText);
        dateText.setText(DateUtil.dateToString(record.getDateTime()));
        contentEdit.setText(record.getContent());
        RadioGroup priorityGroup = (RadioGroup) findViewById(R.id.priority_group);
        int[] priorityId = new int[]{R.id.important_radio, R.id.general_radio, R.id.unimportant_radio};
        priorityGroup.check(priorityId[newPriority - 1]);

        photoImage = (ImageView) findViewById(R.id.photo_imageView);
        photoImage.setOnLongClickListener(this);
        if (photoBitmap != null) {
            photoImage.setImageBitmap(photoBitmap);
            photoImage.setVisibility(View.VISIBLE);
            hasPhoto = true;
        }

        findViewById(R.id.back_imageView).setOnClickListener(this);
        findViewById(R.id.add_photo_imageView).setOnClickListener(this);
        findViewById(R.id.delete_button).setOnClickListener(this);
        findViewById(R.id.update_button).setOnClickListener(this);
        priorityGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_imageView:
                backAction();
                break;
            case R.id.delete_button:
                deleteAction();
                break;
            case R.id.update_button:
                updateAction();
                break;
            case R.id.add_photo_imageView:
                addPhoto();
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
     * 返回事件
     */
    private void backAction() {
        Intent intent = new Intent();
        intent.putExtra("isChanged", isChanged);
        setResult(RESULT_OK, intent); // 向上一级返回数据
        finish();
    }

    /**
     * 删除按钮事件
     */
    private void deleteAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedMemoryActivity.this);
        builder.setTitle("Warning");
        builder.setMessage("您确定要删除这条记录吗[·_·?]");
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int result = DatabaseManager.getDatabaseManager().deleteData(record);
                if (result > 0) {
                    showTip("记录删除成功");
                    isChanged = true;
                    backAction(); // 返回
                } else {
                    showTip("记录删除失败");
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 更新按钮事件
     */
    private void updateAction() {
        String newContent = contentEdit.getText().toString();
        Record newRecord = new Record(record.getDateTime(), newContent, newPriority, photoBitmap);
        int result = DatabaseManager.getDatabaseManager().updateDate(newRecord);
        if (result > 0) {
            showTip("记录更新成功");
            isChanged = true;
        } else {
            showTip("记录更新失败");
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch (id) {
            case R.id.important_radio:
                newPriority = Record.PRIORITY_IMPORTANT;
                break;
            case R.id.general_radio:
                newPriority = Record.PRIORITY_GENERAL;
                break;
            case R.id.unimportant_radio:
                newPriority = Record.PRIORITY_UNIMPORTANT;
                break;
            default:
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
            if (ContextCompat.checkSelfPermission(DetailedMemoryActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 申请授权读写SD卡
                ActivityCompat.requestPermissions(DetailedMemoryActivity.this,
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedMemoryActivity.this);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isChanged", isChanged);
        setResult(RESULT_OK, intent); // 向上一级返回数据
        super.onBackPressed();
    }

    /**
     * 显示弹窗
     * @param msg 提示信息
     */
    private void showTip(String msg) {
        Toast.makeText(DetailedMemoryActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
