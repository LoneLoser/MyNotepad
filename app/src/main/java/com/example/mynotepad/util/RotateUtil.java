package com.example.mynotepad.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.mynotepad.R;

/**
 * 图片旋转工具类
 * Created by Administrator on 2020/6/5.
 */

public class RotateUtil {

    /**
     * 开启动画
     * @param context 上下文
     * @param imageView 图片
     */
    public static void startRotate(Context context, ImageView imageView) {
        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.image_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            imageView.startAnimation(operatingAnim);
        }
    }

    /**
     * 关闭动画
     * @param imageView 图片
     */
    public static void stopRotate(ImageView imageView) {
        imageView.clearAnimation();
    }
}
