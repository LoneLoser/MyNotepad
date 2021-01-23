package com.example.mynotepad.manager;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.mynotepad.R;

/**
 * MediaPlayer操作类
 * Created by Administrator on 2020/6/5.
 */

public class MediaManager {

    private MediaPlayer mediaPlayer;
    private Context context;
    private int[] voices = new int[]{R.raw.rain, R.raw.thunderstorm, R.raw.river, R.raw.forest, R.raw.rain};

    /******************类的单例化******************/

    private MediaManager() {}

    private static class MediaManagerInstance {
        private static final MediaManager manager = new MediaManager();
    }

    public static MediaManager getMediaManager() {
        return MediaManager.MediaManagerInstance.manager;
    }

    /*********************************************/

    /**
     * 初始化播放器
     * @param context 上下文
     */
    public void initMediaPlayer(Context context) {
        this.context = context;
        try {
            mediaPlayer = MediaPlayer.create(context, voices[0]);
            // mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否播放中
     * @return boolean
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * 开始播放
     */
    public void start() {
        mediaPlayer.start();
    }

    /**
     * 暂停播放
     */
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * 重置播放器
     * （重新选择播放资源并播放）
     * @param position 点击的资源/列表索引
     */
    public void reset(int position) {
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(context, voices[position]);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放并且释放
     */
    public void release() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
