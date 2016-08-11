package com.binteng.activity;

import android.view.View;

import com.bin.animationtabview.AnimationTabItem;
import com.bin.animationtabview.AnimationTabView;
import com.bin.animationtabview.ECallOnClick;
import com.binteng.AbsActivity;
import com.binteng.R;
import com.binteng.tools.CommonUtil;
import com.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class UtilActivity extends AbsActivity {

    @Override
    public void initData() {
        super.initData();
        testTime();
    }

    private void testTime(){
        String dateStr = "2016-08-11T16:11:05.733Z";
        Date date = DateTimeUtils.parse(dateStr);
        String str = DateTimeUtils.formatEN(dateStr,DateTimeUtils.FORMAT_SHORT_EN);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_util;
    }

}
