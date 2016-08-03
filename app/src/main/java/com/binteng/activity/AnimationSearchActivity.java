package com.binteng.activity;

import com.bin.AnimationSearchView;
import com.bin.TextWatcherAdapter;
import com.binteng.AbsActivity;
import com.binteng.R;

import butterknife.BindView;

public class AnimationSearchActivity extends AbsActivity {

    @BindView(R.id.searchView)
    AnimationSearchView searchView;

    @Override
    public void initData() {
        super.initData();
        searchView.setSearchTitle(R.string.search_title);
        searchView.setSearchHint(R.string.search_hint);
        searchView.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }
        });

        searchView.setAnimationChange(new AnimationSearchView.OnSearchAnimationChange() {
            @Override
            public void openAni() {

            }

            @Override
            public void closeAni() {

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
