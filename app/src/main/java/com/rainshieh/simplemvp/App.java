package com.rainshieh.simplemvp;

import android.app.Application;

/**
 * author: xiecong
 * create time: 2017/12/7 15:53
 * lastUpdate time: 2017/12/7 15:53
 */

public class App extends Application {
    private static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
