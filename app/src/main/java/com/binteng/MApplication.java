package com.binteng;

import android.app.Application;

import com.karumi.dexter.Dexter;

public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);
    }
}
