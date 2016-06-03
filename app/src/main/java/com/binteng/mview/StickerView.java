package com.binteng.mview;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.binteng.R;
import com.nineoldandroids.view.ViewHelper;

public class StickerView extends RelativeLayout {

    private int MAX_OVER_RESISTANCE = 0;

    private ImageView img;
    private ImageView delete;
    private ImageView flip;
    private ImageView scale;

    private boolean isEnable = true;

    private boolean isAnimationRuning;

    private boolean isInit;

    private GestureDetector mDetector;

    private RectF mWidgetRect = new RectF();
    private RectF mImgRect = new RectF();

    private View rootView;

    private int mTranslateX;
    private int mTranslateY;

    public StickerView(Context context) {
        super(context);
        init(context);
    }

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_sticker_view, this, true);
        mDetector = new GestureDetector(getContext(), mGestureListener);

        img = (ImageView) getRootView().findViewById(R.id.img);
        delete = (ImageView) getRootView().findViewById(R.id.delete);
        flip = (ImageView) getRootView().findViewById(R.id.flip);
        scale = (ImageView) getRootView().findViewById(R.id.scale);

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        mWidgetRect.set(0, 0, width, height);

        float density = getResources().getDisplayMetrics().density;
        MAX_OVER_RESISTANCE = (int) (density * 140);

        mTranslateX = 0;
        mTranslateY = 0;

    }

    public RectF getmWidgetRect() {
        return mWidgetRect;
    }

    public void setmWidgetRect(RectF mWidgetRect) {
        this.mWidgetRect = mWidgetRect;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isInit) {
            isInit = true;
            mImgRect.set(getLeft(), getTop(), getRight(), getBottom());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isEnable) {
            mDetector.onTouchEvent(event);
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isAnimationRuning) {

            }

            if (canScrollHorizontallySelf(distanceX)) {
                if (distanceX < 0 && mImgRect.left - distanceX > mWidgetRect.left)
                    distanceX = mImgRect.left;
                if (distanceX > 0 && mImgRect.right - distanceX < mWidgetRect.right)
                    distanceX = mImgRect.right - mWidgetRect.right;

                mTranslateX -= distanceX;
            }

            if (canScrollVerticallySelf(distanceY)) {
                if (distanceY < 0 && mImgRect.top - distanceY > mWidgetRect.top)
                    distanceY = mImgRect.top;
                if (distanceY > 0 && mImgRect.bottom - distanceY < mWidgetRect.bottom)
                    distanceY = mImgRect.bottom - mWidgetRect.bottom;

                mTranslateY -= distanceY;
            }

            float left = getLeft();
            float right = getRight();
//            ViewHelper.setTranslationX(rootView, mTranslateX);
//            ViewHelper.setTranslationY(rootView, mTranslateY);
            return true;
        }

    };


    private float resistanceScrollByX(float overScroll, float detalX) {
        return detalX * (Math.abs(Math.abs(overScroll) - MAX_OVER_RESISTANCE) / (float) MAX_OVER_RESISTANCE);
    }

    private float resistanceScrollByY(float overScroll, float detalY) {
        return detalY * (Math.abs(Math.abs(overScroll) - MAX_OVER_RESISTANCE) / (float) MAX_OVER_RESISTANCE);
    }

    public boolean canScrollHorizontallySelf(float direction) {
        if (mImgRect.width() <= mWidgetRect.width()) return false;
        if (direction < 0 && Math.round(mImgRect.left) - direction >= mWidgetRect.left)
            return false;
        if (direction > 0 && Math.round(mImgRect.right) - direction <= mWidgetRect.right)
            return false;
        return true;
    }

    public boolean canScrollVerticallySelf(float direction) {
        if (mImgRect.height() <= mWidgetRect.height()) return false;
        if (direction < 0 && Math.round(mImgRect.top) - direction >= mWidgetRect.top)
            return false;
        if (direction > 0 && Math.round(mImgRect.bottom) - direction <= mWidgetRect.bottom)
            return false;
        return true;
    }

}