package com.example.mynotepad.manager;

import android.util.Log;

import com.example.mynotepad.util.DateUtil;
import com.example.mynotepad.dao.Record;

import org.litepal.LitePal;

import java.util.List;

/**
 * 数据库操作类
 * Created by Administrator on 2020/5/26.
 */

public class DatabaseManager {

    /******************类的单例化******************/

    private DatabaseManager() {
        LitePal.getDatabase(); // 获得数据库
    }

    private static class DatabaseManagerInstance {
        private static final DatabaseManager manager = new DatabaseManager();
    }

    public static DatabaseManager getDatabaseManager() {
        return DatabaseManagerInstance.manager;
    }

    /*********************************************/

    /**
     * 插入数据
     * @param record 一条记录
     * @return boolean 插入成功与否
     */
    public boolean insertData(Record record) {
        return record.save();
    }

    /**
     * 查询所有数据
     * @param isOrderByDate 是否优先根据日期排序
     * @return List<Record> 记录列表
     */
    public List<Record> selectAll(boolean isOrderByDate) {
        if (isOrderByDate) {
            return LitePal.order("dateTime desc, priority asc").find(Record.class);
        } else {
            return LitePal.order("priority asc, dateTime desc").find(Record.class);
        }
    }

    /**
     * 根据日期查询数据
     * @param isOrderByDate 是否优先根据日期排序
     * @param startYear 开始年份
     * @param startMonth 开始月份
     * @param startDay 开始日期
     * @param endYear 结束年份
     * @param endMonth 结束月份
     * @param endDay 结束日期
     * @return List<Record> 记录列表
     */
    public List<Record> selectByDate(boolean isOrderByDate,
            int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        String startDateStr = startYear + "/" + startMonth + "/" + startDay + " 0:0";
        String endDateStr = endYear + "/" + endMonth + "/" + endDay + " 23:59";
        String startDate = Long.toString(DateUtil.stringToLong(startDateStr));
        String endDate = Long.toString(DateUtil.stringToLong(endDateStr));
        List<Record> recordList;
        if (isOrderByDate) {
            recordList = LitePal.where("dateTime >= ? and dateTime <= ?",
                    startDate, endDate).order("dateTime desc, priority asc").find(Record.class);
        } else {
            recordList = LitePal.where("dateTime >= ? and dateTime <= ?",
                    startDate, endDate).order("priority asc, dateTime desc").find(Record.class);
        }
        return recordList;
    }

    /**
     * 删除数据
     * @param record 一条记录
     * @return 删除条数
     */
    public int deleteData(Record record) {
        String dateStr = Long.toString(DateUtil.dateToLong(record.getDateTime()));
        String content = record.getContent();
        return LitePal.deleteAll(Record.class, "dateTime = ? and content = ?", dateStr, content);
    }

    /**
     * 更新数据
     * @param record 一条记录
     * @return 更新条数
     */
    public int updateDate(Record record) {
        String dateStr = Long.toString(DateUtil.dateToLong(record.getDateTime()));
        return record.updateAll("dateTime = ?", dateStr);
    }
}
