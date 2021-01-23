package com.example.mynotepad.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mynotepad.R;
import com.example.mynotepad.manager.MediaManager;
import com.example.mynotepad.util.RotateUtil;

/**
 * Created by Administrator on 2020/6/4.
 */

public class MeditationAdapter extends RecyclerView.Adapter<MeditationAdapter.ViewHolder> {

    private int[] images;
    private ImageView imageView;

    /**
     * 内部缓存类
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.voice_item_imageView);
        }
    }

    public MeditationAdapter(ImageView imageView, int[] images) {
        this.imageView = imageView;
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // 加载布局，创建View实例
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meditation_item, parent, false);
        final ViewHolder holder = new ViewHolder(view); // 缓存
        // 绑定点击事件
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                MediaManager.getMediaManager().reset(position);
                imageView.setImageResource(images[position]);
                RotateUtil.startRotate(parent.getContext(), imageView);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }
}
