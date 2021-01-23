package com.example.mynotepad.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理类
 * Created by Administrator on 2020/5/17.
 */

public class ActivityCollector {

    private List<Activity> activities = new ArrayList<>();

    /******************类的单例化******************/

    private ActivityCollector() {}

    private static class ActivityCollectorInstance {
        private static final ActivityCollector collector = new ActivityCollector();
    }

    public static ActivityCollector getActivityCollector() {
        return ActivityCollectorInstance.collector;
    }

    /*********************************************/

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public void finishAll() {
        for (Activity activity: activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public List<Activity> getActivities() {
        return activities;
    }
}
