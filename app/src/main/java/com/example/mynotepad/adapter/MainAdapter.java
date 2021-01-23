package com.example.mynotepad.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mynotepad.R;
import com.example.mynotepad.activity.MeditationActivity;
import com.example.mynotepad.activity.MemoryActivity;
import com.example.mynotepad.activity.RecordActivity;

/**
 * Created by Administrator on 2020/5/25.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private String[] items;
    private int[] imageId = new int[]{R.drawable.item_green, R.drawable.item_blue, R.drawable.item_red};
    private int[] colors = new int[]{Color.rgb(0, 110, 0), Color.rgb(0, 0, 255), Color.rgb(165, 42, 42)};

    /**
     * 内部缓存类
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;

        public ViewHolder(View itemView) {
            super(itemView);
            itemText = (TextView) itemView.findViewById(R.id.main_item_textView);
        }
    }

    public MainAdapter(String[] items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // 加载布局，创建View实例
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item, parent, false);
        final ViewHolder holder = new ViewHolder(view); // 缓存
        // 绑定点击事件
        holder.itemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                switch (position) {
                    case 0:
                        Intent intent1 = new Intent(parent.getContext(), MemoryActivity.class);
                        parent.getContext().startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(parent.getContext(), RecordActivity.class);
                        parent.getContext().startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(parent.getContext(), MeditationActivity.class);
                        parent.getContext().startActivity(intent3);
                        break;
                    default:
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemText.setText(items[position]);
        holder.itemText.setTextColor(colors[position]);
        holder.itemText.setBackgroundResource(imageId[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

}
