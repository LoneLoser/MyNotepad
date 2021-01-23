package com.example.mynotepad.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.litepal.crud.LitePalSupport;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2020/5/26.
 */

public class Record extends LitePalSupport implements Serializable {

    private static final String[] PRIORITY_STR = new String[]{"重要", "普通", "不重要"};
    public static final int PRIORITY_IMPORTANT = 1, PRIORITY_GENERAL = 2, PRIORITY_UNIMPORTANT = 3;

    private Date dateTime;  // 记录创建日期时间
    private String content; // 记事项内容
    private int priority;   // 优先级
    private byte[] photo;   // 照片（bytes）

    public Record() {}

    public Record(Date dateTime, String content, int priority, byte[] photo) {
        this.dateTime = dateTime;
        this.content = content;
        this.priority = priority;
        this.photo = photo;
    }

    public Record(Date dateTime, String content, int priority, Bitmap photo) {
        this.dateTime = dateTime;
        this.content = content;
        this.priority = priority;
        this.photo = bitmapToBytes(photo);
    }

    /**
     * Bitmap转换为byte[]
     * @param bitmap Bitmap格式的图片
     * @return byte[]
     */
    public byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } else {
            return new byte[0];
        }
    }

    /**
     * byte[]转换为Bitmap
     * @param bytes byte[]格式的图片
     * @return Bitmap
     */
    public Bitmap bytesToBitmap(byte[] bytes) {
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPriority() {
        return priority;
    }

    public String getPriorityStr() {
        return PRIORITY_STR[priority - 1];
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public byte[] getPhotoBytes() {
        return photo;
    }

    public void setPhotoBytes(byte[] photo) {
        this.photo = photo;
    }

    public Bitmap getPhotoBitmap() {
        return bytesToBitmap(photo);
    }

    public void setPhotoBitmap(Bitmap photo) {
        this.photo = bitmapToBytes(photo);
    }
}
