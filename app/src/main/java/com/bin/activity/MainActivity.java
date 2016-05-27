package com.bin.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bin.R;
import com.bin.adapter.ActivityListAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

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

        final ActivityListAdapter adapter = new ActivityListAdapter(this, data);

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

    private void toActivity(ActivityInfo info) {
        Intent intent = new Intent(this, info.getClass());
        startActivity(intent);
    }


}
