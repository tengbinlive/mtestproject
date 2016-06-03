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
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class StickerView extends RelativeLayout {

    private final static int DURATION_ROTATION_TIME = 300;
    private final static float MAX_SCALE = 3.0f;

    private ImageView img;
    private ImageView delete;
    private ImageView flip;
    private ImageView scale;

    private float mMaxScale;

    private boolean isOverturn;

    private boolean isEnable = true;

    private boolean isOperating;

    //活动区域
    private RectF mWidgetRect = new RectF();

    private View rootView;

    private float lastX, lastY;

    private float prevDegrees;

    private GestureDetector mDetector;
    private GestureDetector mRotateOrScaleDetector;

    private OnStickerListener onStickerListener;

    public interface OnStickerListener {
        void onDelete();

        void onFlip();

        void onScale();
    }

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
        mRotateOrScaleDetector = new GestureDetector(getContext(), mScaleListener);
        img = (ImageView) getRootView().findViewById(R.id.img);
        delete = (ImageView) getRootView().findViewById(R.id.delete);
        flip = (ImageView) getRootView().findViewById(R.id.flip);
        scale = (ImageView) getRootView().findViewById(R.id.scale);


        //默认活动区域 屏幕内
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        mWidgetRect.set(0, 0, width, height);

        mMaxScale = MAX_SCALE;

        initListener();
    }

    private void initListener() {
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onStickerListener) {
                    onStickerListener.onDelete();
                }
            }
        });
        flip.getParent().requestDisallowInterceptTouchEvent(true);
        flip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggerImage();
                if (null != onStickerListener) {
                    onStickerListener.onFlip();
                }
            }
        });
        scale.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isEnable) {
                    mRotateOrScaleDetector.onTouchEvent(event);
                    return true;
                }
                return false;
            }
        });

        img.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isEnable) {
                    mDetector.onTouchEvent(event);
                    return true;
                }
                return false;
            }
        });
    }

    private void toggerImage() {
        rotationView(img, "rotationY", isOverturn ? 180 : 0, isOverturn ? 0 : 180);
    }

    private void rotationView(View view, String type, float fromValues, float toValues) {
        isOverturn = !isOverturn;
        ObjectAnimator.ofFloat(view, type, fromValues, toValues).setDuration(DURATION_ROTATION_TIME).start();

    }

    public void setOnStickerListener(OnStickerListener onStickerListener) {
        this.onStickerListener = onStickerListener;
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

    public float getmMaxScale() {
        return mMaxScale;
    }

    public void setmMaxScale(float mMaxScale) {
        this.mMaxScale = mMaxScale;
    }

    /**
     * 不能移出活动区域
     *
     * @param nextX
     * @return
     */
    private float calculateActiveX(float nextX) {
        if (nextX < mWidgetRect.left)
            nextX = mWidgetRect.left;
        else if (nextX > mWidgetRect.right - getWidth())
            nextX = mWidgetRect.right - getWidth();
        return nextX;
    }

    /**
     * 不能移出活动区域
     *
     * @param nextY
     * @return
     */
    private float calculateActiveY(float nextY) {
        if (nextY < mWidgetRect.top)
            nextY = mWidgetRect.top;
        else if (nextY > mWidgetRect.bottom - getWidth())
            nextY = mWidgetRect.bottom - getWidth();
        return nextY;
    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent event) {
            lastX = event.getRawX();
            lastY = event.getRawY();
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            moveView(e2);
            return true;
        }

    };

    private void moveView(MotionEvent event) {
        float distanceX = lastX - event.getRawX();
        float distanceY = lastY - event.getRawY();

        float nextY = calculateActiveY(getY() - distanceY);
        float nextX = calculateActiveX(getX() - distanceX);

        ViewHelper.setX(rootView, nextX);
        ViewHelper.setY(rootView, nextY);

        lastX = event.getRawX();
        lastY = event.getRawY();
    }

    private GestureDetector.OnGestureListener mScaleListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent event) {
            lastX = event.getRawX();
            lastY = event.getRawY();
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            rotateOrScale(e2);
            return true;
        }

    };

    private void rotateOrScale(MotionEvent event) {
        rotateView(event);
//        scaleView();
        lastX = event.getRawX();
        lastY = event.getRawY();

    }

    private void rotateView(MotionEvent event) {

        float distanceX = event.getRawX() - lastX;
        float distanceY = event.getRawY() - lastY;

        float k = distanceY / distanceX;   //斜率

        float currDegrees = (float) Math.toDegrees(Math.atan(k));

        prevDegrees += currDegrees;
        ViewHelper.setRotation(rootView, prevDegrees);
    }

    private void scaleView(float scale) {
        if (scale < 1) {
            scale = 1;
        } else if (scale > mMaxScale) {
            scale = mMaxScale;
        }
        ViewHelper.setScaleX(rootView, scale);
        ViewHelper.setScaleX(rootView, scale);
    }

    private float caculateSlope(MotionEvent event) {
        float x1 = lastX;
        float y1 = lastY;
        float x2 = event.getRawX();
        float y2 = event.getRawY();
        return (y2 - y1) / (x2 - x1);
    }

}