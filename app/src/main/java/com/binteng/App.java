package com.binteng;

import android.app.Application;

import com.karumi.dexter.Dexter;

public class App extends Application {

    private static App instance;

    /**
     * 获得本类的一个实例
     */
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Dexter.initialize(this);
    }
}
