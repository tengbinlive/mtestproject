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
    private final static float MAX_SCALE = 1.5f;
    private final static float FLEX_SLIDE = 15.0f;

    private ImageView img;
    private ImageView delete;
    private ImageView flip;
    private ImageView scaleOrRotation;

    private float mMaxScale;

    private boolean isOverturn;

    private boolean isEnable = true;

    private boolean isOperating;

    private float mScale;

    //活动区域
    private RectF mWidgetRect = new RectF();

    private View rootView;

    private float lastX, lastY;
    private float centerX, centerY;

    private float deltaAngle;

    private boolean inInit;

    private GestureDetector mDetector;
    private GestureDetector mRotateOrScaleDetector;

    private OnStickerListener onStickerListener;

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

//        centerX = mWidgetRect.centerX();
//        centerY = mWidgetRect.centerY();

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
//        centerX = mWidgetRect.centerX();
//        centerY = mWidgetRect.centerY();
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
    }

    private GestureDetector.OnGestureListener mScaleOrRotationListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent event) {
            lastX = event.getRawX()-centerX;
            lastY = event.getRawY()-centerY;
            resetAeltaAngle();
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

        float ang = (float) Math.toDegrees(Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX));

        float angleDiff = ang - deltaAngle;

        ViewHelper.setRotation(rootView, angleDiff);

    }

    private void scaleView(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        float currentDX = x - centerX;
        float currentDY = y - centerY;
        float distanceLast = (float) Math.sqrt(lastX * lastX + lastY * lastY);
        float distanceCurrent = (float) Math.sqrt(currentDX * currentDX + currentDY * currentDY);
        float scale = (distanceCurrent-distanceLast) * 0.002f;
        mScale += scale;
        if (mScale < 1) {
            mScale = 1;
        } else if (mScale > mMaxScale) {
            mScale = mMaxScale;
        }
        ViewHelper.setScaleX(rootView, mScale);
        ViewHelper.setScaleY(rootView, mScale);
        lastX = currentDX;
        lastY = currentDY;
    }

    private int getPositiveAndNegative(float x,float y,float distanceX,float distanceY){
        boolean distance = distanceX<0||distanceY<0;
        if(x<centerX){
            return distance?1:-1;
        }else{
            return distance?-1:1;
        }
    }

    private void resetAeltaAngle() {
        int[] position_scaleOrRotation = new int[2];
        scaleOrRotation.getLocationOnScreen(position_scaleOrRotation);

        int[] position_img = new int[2];
        getLocationOnScreen(position_img);

        float buttonCenterX = position_scaleOrRotation[0] + scaleOrRotation.getWidth() * 0.5f;
        float buttonCenterY = position_scaleOrRotation[1] + scaleOrRotation.getHeight() * 0.5f;
        centerX = position_img[0] + getWidth() * 0.5f;
        centerY = position_img[1] + getHeight() * 0.5f;
        deltaAngle = (float) Math.toDegrees(Math.atan2(buttonCenterY - centerY, buttonCenterX - centerX));
    }

}