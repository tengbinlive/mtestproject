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
import com.binteng.tools.Point2D;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class StickerView extends RelativeLayout {

    private final static int DURATION_ROTATION_TIME = 300;
    private final static float MAX_SCALE = 1.7f;

    private ImageView img;
    private ImageView delete;
    private ImageView flip;
    private ImageView scaleOrRotation;

    private float mMaxScale;

    private boolean isOverturn;

    private boolean isEnable = true;

    private float angleDiff; //旋转角度

    private float mScale; //缩放比例

    private RectF mWidgetRect = new RectF();//活动区域

    private RectF mDrawRect = new RectF();//this view rect . 用于获取旋转 缩放中心点

    private View rootView;

    private float lastX, lastY; //用于计算触摸距离

    private boolean inInit;

    private GestureDetector mDetector;//移动
    private GestureDetector mRotateOrScaleDetector;//缩放旋转

    private OnStickerListener onStickerListener; //贴纸按钮回调

    public interface OnStickerListener {
        void onDelete();

        void onFlip();

        void onScaleOrRotation();
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
        mRotateOrScaleDetector = new GestureDetector(getContext(), mScaleOrRotationListener);
        img = (ImageView) getRootView().findViewById(R.id.img);
        delete = (ImageView) getRootView().findViewById(R.id.delete);
        flip = (ImageView) getRootView().findViewById(R.id.flip);
        scaleOrRotation = (ImageView) getRootView().findViewById(R.id.scale_rotation);

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
        scaleOrRotation.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isEnable) {
                    mRotateOrScaleDetector.onTouchEvent(event);
                    return true;
                }
                if (null != onStickerListener) {
                    onStickerListener.onScaleOrRotation();
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
            initDrawRect();
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            moveView(e2);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
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

        mDrawRect.offset(distanceX, distanceY);
    }

    private GestureDetector.OnGestureListener mScaleOrRotationListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent event) {
            initDrawRect();
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
        scaleView(event);
    }

    private void rotateView(MotionEvent event) {

        final float pt1[] = new float[]{mDrawRect.centerX(), mDrawRect.centerY()};//中心点
        final float pt2[] = new float[]{mDrawRect.right, mDrawRect.bottom};//旋转按钮点
        final float pt3[] = new float[]{mDrawRect.right + event.getX(), mDrawRect.bottom + event.getY()};//触摸点
        double ang1 = Math.toDegrees(Math.atan2(pt2[1] - pt1[1], pt2[0] - pt1[0]));//旋转按钮 与 中心点角度
        double ang2 = Math.toDegrees(Math.atan2(pt3[1] - pt1[1], pt3[0] - pt1[0]));//触摸点 与 中心点角度

        angleDiff += ang2 - ang1;
        ViewHelper.setRotation(rootView, Point2D.angle360(angleDiff));

    }

    private void scaleView(MotionEvent event) {

        float distanceX = lastX - mDrawRect.centerX();
        float distanceY = lastY - mDrawRect.centerY();

        float distanceRawX = event.getRawX() - mDrawRect.centerX();
        float distanceLastY = event.getRawY() - mDrawRect.centerY();

        float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        float distanceRaw = (float) Math.sqrt(distanceRawX * distanceRawX + distanceLastY * distanceLastY);

        float scale = (distanceRaw - distance) * 0.002f;
        mScale += scale;
        if (mScale < 1) {
            mScale = 1;
        } else if (mScale > mMaxScale) {
            mScale = mMaxScale;
        }
        ViewHelper.setScaleX(rootView, mScale);
        ViewHelper.setScaleY(rootView, mScale);
        lastX = event.getRawX();
        lastY = event.getRawY();
    }

    private void initDrawRect() {
        if (!inInit) {
            inInit = true;
            mDrawRect.set(getLeft(), getTop(), getRight(), getBottom());
        }
    }

}