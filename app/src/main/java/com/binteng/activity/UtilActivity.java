package com.binteng.activity;

import android.widget.TextView;

import com.binteng.AbsActivity;
import com.binteng.R;
import com.utils.DateTimeUtils;

import butterknife.BindView;

public class UtilActivity extends AbsActivity {

    @BindView(R.id.time_iso8601)
    TextView timeIso8601;

    @Override
    public void initData() {
        super.initData();
        testTime();
    }

    private void testTime() {
        String dateStr = "2016-08-11T16:11:05.733Z";
        String str = DateTimeUtils.formatDateTime(dateStr, DateTimeUtils.FORMAT_ISO8601);
        timeIso8601.setText(str);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_util;
    }

}
