package com.binteng.activity;

import com.bin.AnimationSearchView;
import com.bin.TextWatcherAdapter;
import com.binteng.AbsActivity;
import com.binteng.R;

import butterknife.BindView;

public class AnimationSeachActivity extends AbsActivity {

    @BindView(R.id.searchView)
    AnimationSearchView searchView;

    @Override
    public void initData() {
        super.initData();
        searchView.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_seach;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isAnimationOpen()) {
            searchView.closeAnimation();
            return;
        }
        super.onBackPressed();
    }
}
