package com.example.mynotepad.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mynotepad.R;
import com.example.mynotepad.adapter.MemoryAdapter;
import com.example.mynotepad.dao.Record;
import com.example.mynotepad.manager.DatabaseManager;
import com.example.mynotepad.util.BackgroundUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MemoryActivity extends BaseActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private List<Record> recordList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemoryAdapter adapter;
    private LinearLayout orderLayout;
    private Button startDateBtn, endDateBtn;

    private int startYear = 0, startMonth = 0, startDay = 0; // 初始日期
    private int endYear = 0, endMonth = 0, endDay = 0; // 结束日期
    private boolean currIsOrderByDate; // 当前是否按时间排序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        currIsOrderByDate = true;
        recordList = DatabaseManager.getDatabaseManager().selectAll(true); // 初始化记录列表

        initUi();
    }

    private void initUi() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_layout);
        layout.setBackgroundResource(BackgroundUtil.currBgImageId);

        recyclerView = (RecyclerView) findViewById(R.id.memory_items_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // 布局管理器
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MemoryAdapter(recordList, MemoryActivity.this); // 适配器
        recyclerView.setAdapter(adapter);

        startDateBtn = (Button) findViewById(R.id.choose_startDate_button);
        startDateBtn.setOnClickListener(this);
        endDateBtn = (Button) findViewById(R.id.choose_endDate_button);
        endDateBtn.setOnClickListener(this);
        findViewById(R.id.choose_all_button).setOnClickListener(this);

        findViewById(R.id.order_icon_image).setOnClickListener(this);
        orderLayout = (LinearLayout) findViewById(R.id.order_layout);
        RadioGroup orderGroup = (RadioGroup) findViewById(R.id.order_group);
        orderGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_startDate_button:
                chooseDate(true);
                break;
            case R.id.choose_endDate_button:
                chooseDate(false);
                break;
            case R.id.choose_all_button:
                chooseAllAction();
                break;
            case R.id.order_icon_image:
                if (orderLayout.getVisibility() == View.VISIBLE) {
                    orderLayout.setVisibility(View.INVISIBLE);
                } else {
                    orderLayout.setVisibility(View.VISIBLE);
                }
                break;
            default:
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch (id) {
            case R.id.order_date_radio:
                radioCheckAction(true);
                break;
            case R.id.order_priority_radio:
                radioCheckAction(false);
                break;
            default:
        }
    }

    /**
     * 选择全部按钮事件
     */
    private void chooseAllAction() {
        recordList = DatabaseManager.getDatabaseManager().selectAll(currIsOrderByDate);
        adapter.updateItem(recordList); // 刷新列表项
        startDateBtn.setText(R.string.choose_startDate);
        endDateBtn.setText(R.string.choose_endDate);
        startYear = 0;
        endYear = 0;
    }

    /**
     * 弹出日期选择对话框
     * @param isStart 是否选择开始日期
     */
    private void chooseDate(final boolean isStart) {
        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        int currMonth = calendar.get(Calendar.MONTH);
        int currDay = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(MemoryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                if (isStart) {
                    startDateSetAction(year, month, day);
                } else {
                    endDateSetAction(year, month, day);
                }
            }
        }, currYear, currMonth, currDay).show();
    }

    /**
     * 开始日期选择后执行操作
     * @param year 选择的年份
     * @param month 选择的月份
     * @param day 选择的日期
     */
    private void startDateSetAction(int year, int month, int day) {
        startYear = year;
        startMonth = month + 1;
        startDay = day;
        startDateBtn.setText("开始：" + startYear + "/" + startMonth + "/" + startDay);
        if (endYear != 0) {
            // 查询记录
            recordList = DatabaseManager.getDatabaseManager().selectByDate(currIsOrderByDate,
                    startYear, startMonth, startDay, endYear, endMonth, endDay);
            adapter.updateItem(recordList); // 刷新列表项
        } else {
            showTip("请选择结束日期");
        }
    }

    /**
     * 结束日期选择后执行操作
     * @param year 选择的年份
     * @param month 选择的月份
     * @param day 选择的日期
     */
    private void endDateSetAction(int year, int month, int day) {
        endYear = year;
        endMonth = month + 1;
        endDay = day;
        endDateBtn.setText("结束：" + endYear + "/" + endMonth + "/" + endDay);
        if (startYear != 0) {
            // 查询记录
            recordList = DatabaseManager.getDatabaseManager().selectByDate(currIsOrderByDate,
                    startYear, startMonth, startDay, endYear, endMonth, endDay);
            adapter.updateItem(recordList); // 刷新列表项
        } else {
            showTip("请选择开始日期");
        }
    }

    /**
     * 单选按钮事件
     * @param isOrderByDate 是否按时间排序
     */
    private void radioCheckAction(boolean isOrderByDate) {
        updateRecordList(isOrderByDate);
        currIsOrderByDate = isOrderByDate;
        orderLayout.setVisibility(View.GONE);
    }

    /**
     * 更新记录列表
     * （点击单选按钮，从其他页面返回，需要保留时间等选项信息时触发的事件）
     * @param isOrderByDate 是否按时间排序
     */
    private void updateRecordList(boolean isOrderByDate) {
        if (startYear != 0 && endYear != 0) {
            recordList = DatabaseManager.getDatabaseManager().selectByDate(isOrderByDate,
                    startYear, startMonth, startDay, endYear, endMonth, endDay);
        } else {
            recordList = DatabaseManager.getDatabaseManager().selectAll(isOrderByDate);
        }
        adapter.updateItem(recordList); // 刷新列表项
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: // 请求代码为1
                if (resultCode == RESULT_OK) { // 正确获得结果
                    boolean isChanged = intent.getBooleanExtra("isChanged", false);
                    if (isChanged) {
                        updateRecordList(currIsOrderByDate);
                    }
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
        Toast.makeText(MemoryActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
