package com.example.mynotepad.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mynotepad.R;
import com.example.mynotepad.adapter.MeditationAdapter;
import com.example.mynotepad.manager.MediaManager;
import com.example.mynotepad.util.BackgroundUtil;
import com.example.mynotepad.util.RotateUtil;

public class MeditationActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageView;
    private int[] images = new int[]{
            R.drawable.rain, R.drawable.thunderstorm, R.drawable.river, R.drawable.forest, R.drawable.rain};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        initUi();
    }

    private void initUi() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_layout);
        layout.setBackgroundResource(BackgroundUtil.currBgImageId);

        TextView voiceText = (TextView) findViewById(R.id.voice_textView);
        voiceText.setTextColor(BackgroundUtil.currTextColor);

        imageView = (ImageView) findViewById(R.id.voice_imageView);
        imageView.setImageResource(images[0]);
        imageView.setOnClickListener(this);
        MediaManager.getMediaManager().initMediaPlayer(MeditationActivity.this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.meditation_items_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // 布局管理器
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 横向滚动
        recyclerView.setLayoutManager(layoutManager);
        MeditationAdapter adapter = new MeditationAdapter(imageView, images); // 适配器
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.voice_imageView:
                if (!MediaManager.getMediaManager().isPlaying()) {
                    MediaManager.getMediaManager().start(); // 开始播放
                    RotateUtil.startRotate(this, imageView);
                } else {
                    MediaManager.getMediaManager().pause(); // 暂停播放
                    RotateUtil.stopRotate(imageView);
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.getMediaManager().release();
    }

}
