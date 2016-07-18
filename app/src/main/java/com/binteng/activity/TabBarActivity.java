package com.binteng.activity;

import android.view.View;

import com.bin.animationtabview.AnimationTabItem;
import com.bin.tabbarview.ECallOnClick;
import com.bin.tabbarview.Model;
import com.bin.tabbarview.TabBarView;
import com.binteng.AbsActivity;
import com.binteng.R;
import com.binteng.tools.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TabBarActivity extends AbsActivity {

    @BindView(R.id.tab_layout)
    TabBarView tabView;

    @Override
    public void initData() {
        super.initData();
        List<Model> items = new ArrayList<>();
        Model item = new Model();

        int whiteColor = getResources().getColor(R.color.white);
        int orangeColor = getResources().getColor(R.color.orange);

        item.setTitle("title0x00");
        item.setTitleColorNormal(whiteColor);
        item.setTitleColorPressed(orangeColor);
        item.setIconNormal(R.mipmap.ic_test1_normal);
        item.setIconPressed(R.mipmap.ic_test1_pressed);
        item.setGravity(AnimationTabItem.GRAVITY_LEFT);
        items.add(item);

        item = new Model();
        item.setTitle("title0x01");
        item.setAnchor(1);
        item.setTitleColorNormal(whiteColor);
        item.setTitleColorPressed(orangeColor);
        item.setIconNormal(R.mipmap.ic_test1_normal);
        item.setIconPressed(R.mipmap.ic_test1_pressed);
        item.setGravity(AnimationTabItem.GRAVITY_RIGHT);
        items.add(item);

        item = new Model();
        item.setTitle("title0x02");
        item.setAnchor(4);
        item.setTitleColorNormal(whiteColor);
        item.setTitleColorPressed(orangeColor);
        item.setIconNormal(R.mipmap.ic_test0_normal);
        item.setIconPressed(R.mipmap.ic_test0_pressed);
        item.setGravity(AnimationTabItem.GRAVITY_LEFT);
        items.add(item);

        item = new Model();
        item.setTitle("title0x03");
        item.setTitleColorNormal(whiteColor);
        item.setTitleColorPressed(orangeColor);
        item.setIconNormal(R.mipmap.ic_test0_normal);
        item.setIconPressed(R.mipmap.ic_test0_pressed);
        item.setGravity(AnimationTabItem.GRAVITY_RIGHT);
        items.add(item);

        tabView.initItem(items);
        tabView.setmModelNum(5);
        tabView.setOnItemClickListener(new ECallOnClick() {
            @Override
            public void callOnClick(View view, Model item, int index) {
                CommonUtil.showToast(item.getTitle());
            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_tabbar;
    }

}
