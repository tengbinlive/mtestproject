package com.bin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class AbsActivity extends AppCompatActivity {

    private View rootView;

    public View getRootView() {
        return rootView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(getLayoutID(), (ViewGroup) getWindow().getDecorView(), false);
        setContentView(rootView);
        ButterKnife.bind(this);
        initData();
    }

    abstract public int getLayoutID();

    public void initData() {

    }


}
