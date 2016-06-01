package com.binteng.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.binteng.AbsActivity;
import com.binteng.R;
import com.binteng.adapter.ActivityListAdapter;
import com.binteng.bean.ActInfo;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends AbsActivity {

    @BindView(R.id.list_view)
    ListView listView;

    private PackageInfo info;

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        super.initData();

        initActivityData();

        initListView();

    }

    private void initActivityData() {
        String applicationPackage = getApplicationInfo().packageName;
        try {
            info = getPackageManager().getPackageInfo(applicationPackage, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initListView() {

        final ActivityInfo[] data = info == null ? null : info.activities;

        final ActivityListAdapter adapter = new ActivityListAdapter(this, initActInfoData(data));

        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);

        animationAdapter.setAbsListView(listView);

        listView.setAdapter(animationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toActivity(adapter.getItem(position));
            }
        });
    }

    private List initActInfoData(ActivityInfo[] data) {
        if (data == null) {
            return null;
        }
        List list = new ArrayList<>();
        for (ActivityInfo activityInfo : data) {
            String name = activityInfo.name;
            if (!name.contains("MainActivity")&&name.contains(activityInfo.packageName)) {
                ActInfo actInfo = new ActInfo();
                actInfo.setClassName(name);
                String activityName = name.substring(name.lastIndexOf(".") + 1, name.length());
                actInfo.setActivityName(activityName);
                list.add(actInfo);
            }
        }
        return list;
    }

    private void toActivity(ActInfo info) {
        Intent intent = new Intent();
        intent.setClassName(this, info.getClassName());
        startActivity(intent);
    }


}
