package com.example.midemo.utils;

import android.app.Application;
import android.content.Context;

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        initDB(getApplicationContext());
    }

    private void initDB(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);
        helper.getWritableDatabase(); // 这会触发onCreate和插入模拟数据
    }
}
