package com.binteng.activity;

import android.graphics.RectF;
import android.widget.ImageView;

import com.binteng.AbsActivity;
import com.binteng.R;
import com.binteng.mview.StickerView;

import butterknife.BindView;

public class StickerActivity extends AbsActivity {


    @BindView(R.id.sticker_view)
    StickerView stickerView;
    @BindView(R.id.active_rect)
    ImageView activeRect;

    @Override
    public int getLayoutID() {
        return R.layout.activity_sticker;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 这里来获取容器的宽和高
        if (hasFocus) {
            RectF rectF = new RectF();
            rectF.set(activeRect.getLeft(), activeRect.getTop(), activeRect.getRight(), activeRect.getBottom());
            stickerView.setmWidgetRect(rectF);
        }
    }

}