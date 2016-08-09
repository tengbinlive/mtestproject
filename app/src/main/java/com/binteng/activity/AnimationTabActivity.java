package com.binteng.activity;

import android.view.View;

import com.bin.animationtabview.AnimationTabItem;
import com.bin.animationtabview.AnimationTabView;
import com.bin.animationtabview.ECallOnClick;
import com.binteng.AbsActivity;
import com.binteng.R;
import com.binteng.tools.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AnimationTabActivity extends AbsActivity {

    @BindView(R.id.tab_layout)
    AnimationTabView tabView;

    @Override
    public void initData() {
        super.initData();
        List<AnimationTabItem> items = new ArrayList<>();
        AnimationTabItem item = new AnimationTabItem();

        int whiteColor = getResources().getColor(R.color.white);
        int orangeColor = getResources().getColor(R.color.orange);

        item.setTitle("title0x00");
        item.setTitleColorNormal(whiteColor);
        item.setTitleColorPressed(orangeColor);
        item.setIconNormal(R.mipmap.ic_test1_normal);
        item.setIconPressed(R.mipmap.ic_test1_pressed);
        item.setGravity(AnimationTabItem.GRAVITY_LEFT);
        items.add(item);

        item = new AnimationTabItem();
        item.setTitle("title0x01");
        item.setTitleColorNormal(whiteColor);
        item.setTitleColorPressed(orangeColor);
        item.setIconNormal(R.mipmap.ic_test1_normal);
        item.setIconPressed(R.mipmap.ic_test1_pressed);
        item.setGravity(AnimationTabItem.GRAVITY_LEFT);
        items.add(item);

        item = new AnimationTabItem();
        item.setTitle("title0x02");
        item.setTitleColorNormal(whiteColor);
        item.setTitleColorPressed(orangeColor);
        item.setIconNormal(R.mipmap.ic_test0_normal);
        item.setIconPressed(R.mipmap.ic_test0_pressed);
        item.setGravity(AnimationTabItem.GRAVITY_RIGHT);
        items.add(item);

        tabView.initItem(items);
        tabView.setOnItemClickListener(new ECallOnClick() {
            @Override
            public void callOnClick(View view, AnimationTabItem item, int index) {
                CommonUtil.showToast(item.getTitle());
            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_tabani;
    }

}
