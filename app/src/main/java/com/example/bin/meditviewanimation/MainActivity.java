package com.example.bin.meditviewanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;

import com.bin.AnimationSearchView;
import com.bin.TextWatcherAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (AnimationSearchView) findViewById(R.id.searchView);

        searchView.addTextChangedListener(new TextWatcherAdapter(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }
        });
    }

    private AnimationSearchView searchView;

    @Override
    public void onBackPressed() {
        if (searchView.isAnimationOpen()) {
            searchView.closeAnimation();
            return;
        }
        super.onBackPressed();
    }
}
