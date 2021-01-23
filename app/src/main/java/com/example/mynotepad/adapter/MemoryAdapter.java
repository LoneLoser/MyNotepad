package com.example.mynotepad.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotepad.manager.DatabaseManager;
import com.example.mynotepad.util.DateUtil;
import com.example.mynotepad.R;
import com.example.mynotepad.dao.Record;
import com.example.mynotepad.activity.DetailedMemoryActivity;

import java.util.List;

/**
 * Created by Administrator on 2020/5/26.
 */

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private List<Record> recordList;
    private Context context;

    /**
     * 内部缓存类
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView; // 子项外层布局
        TextView dateItem, priorityItem, contentItem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            dateItem = (TextView) itemView.findViewById(R.id.dateTime_item_textView);
            priorityItem = (TextView) itemView.findViewById(R.id.priority_item_textView);
            contentItem = (TextView) itemView.findViewById(R.id.content_item_textView);
        }
    }

    public MemoryAdapter(List<Record> recordList, Context context) {
        this.recordList = recordList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 加载布局，创建View实例
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memory_item, parent, false);
        final ViewHolder holder = new ViewHolder(view); // 缓存
        // 绑定点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                clickAction(position);
            }
        });
        // 绑定长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = holder.getAdapterPosition();
                longClickAction(position);
                return true;
            }
        });
        return holder;
    }

    /**
     * 点击列表项触发事件，查看具体记录
     * @param position 列表项索引
     */
    private void clickAction(int position) {
        Intent intent = new Intent(context, DetailedMemoryActivity.class);
        intent.putExtra("record", recordList.get(position));
        ((Activity) context).startActivityForResult(intent, 1);
    }

    /**
     * 长按列表项触发事件，删除记录
     * @param position 列表项索引
     */
    private void longClickAction(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning");
        builder.setMessage("您确定要删除这条记录吗[·_·?]");
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int result = DatabaseManager.getDatabaseManager().deleteData(recordList.get(position));
                if (result > 0) {
                    recordList.remove(position);
                    notifyDataSetChanged();
                    showTip("记录删除成功");
                } else {
                    showTip("记录删除失败");
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        Record record = recordList.get(position);
        holder.dateItem.setText(DateUtil.dateToString(record.getDateTime()));
        holder.priorityItem.setText(record.getPriorityStr());
        holder.contentItem.setText(record.getContent());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    /**
     * 更新列表项
     * @param recordList 新的记录列表
     */
    public void updateItem(List<Record> recordList) {
        this.recordList = recordList;
        notifyDataSetChanged();
    }

    /**
     * 显示弹窗
     * @param msg 提示信息
     */
    private void showTip(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
