package com.example.mynotepad.util;

import android.graphics.Color;

import com.example.mynotepad.R;

import java.util.Random;

/**
 * 背景工具类
 * Created by Administrator on 2020/5/30.
 */

public class BackgroundUtil {

    private static final int[] BG_IMAGE_ID = new int[]{R.drawable.snow, R.drawable.city,
            R.drawable.woodland, R.drawable.road, R.drawable.sunset};

    private static final String[] TEXT_STR = new String[]{"水墨雪林", "城市之梦",
            "浪漫之秋", "行路漫漫", "枯草夕阳"};

    private static final int[] TEXT_COLOR = new int[]{Color.WHITE, Color.WHITE,
            Color.rgb(0, 85, 0), Color.WHITE, Color.WHITE};

    public static int currBgImageId; // 当前图片id
    public static String currText;   // 当前文本
    public static int currTextColor; // 当前文本颜色

    /**
     * 背景变量初始化
     */
    public static void backgroundInit() {
        Random random = new Random();
        int index = random.nextInt(BG_IMAGE_ID.length);
        currBgImageId = BG_IMAGE_ID[index];
        currText = TEXT_STR[index];
        currTextColor = TEXT_COLOR[index];
    }
}
