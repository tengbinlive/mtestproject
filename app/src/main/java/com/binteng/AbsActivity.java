package com.binteng;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

public abstract class AbsActivity extends AppCompatActivity {

    private View rootView;

    public final MActivityHandler mHandler = new MActivityHandler(this);

    public View getRootView() {
        return rootView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(getLayoutID(), (ViewGroup) getWindow().getDecorView(), false);
        setContentView(rootView);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(this.getLocalClassName());
        initData();
    }

    /**
     * 设置当前activity root view
     * @return
     */
    abstract public int getLayoutID();

    /**
     * 初始化 资源
     * 该函数会在butterknife bind数据之后调用
     */
    public void initData() {
    }

    /**
     * handler回调
     * @param msg  message
     */
    public void handlerCallBack(Message msg) {
    }

    public static class MActivityHandler extends Handler {
        private final WeakReference<AbsActivity> activityWeakReference;

        public MActivityHandler(AbsActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AbsActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.handlerCallBack(msg);
            }
        }
    }


}
