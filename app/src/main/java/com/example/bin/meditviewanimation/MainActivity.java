package com.example.bin.meditviewanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bin.AnimationSearchView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (AnimationSearchView) findViewById(R.id.searchView);
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
