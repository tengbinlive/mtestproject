package com.binteng.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.PointF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.bin.AnimatorUtils;
import com.binteng.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import butterknife.BindView;

public class AnimationLaunchActivity extends AbsActivity {

    @BindView(R.id.point)
    ImageView point;
    @BindView(R.id.clip_reveal_frame)
    ClipRevealFrame clipRevealFrame;

    private final OvershootInterpolator mInterpolator = new OvershootInterpolator();
    private boolean isShow;

    @Override
    public void initData() {
        super.initData();
        initAnimation();
    }

    private void initAnimation() {

        int tanslationX_time = 5*1000;
        int tanslationY_count = 2;
        int tanslationY_time = tanslationX_time/tanslationY_count;

        int tanslationY_offer = -50;

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels >> 1;

        final ObjectAnimator tanslationX = ObjectAnimator.ofFloat(point, AnimatorUtils.TRANSLATION_X, 0, width);
        tanslationX.setDuration(tanslationX_time);

        final ObjectAnimator tanslationY = ObjectAnimator.ofFloat(point, AnimatorUtils.TRANSLATION_Y, 0 ,tanslationY_offer,0);
        tanslationY.setDuration(700);
        tanslationY.setRepeatCount(tanslationY_count);
        tanslationY.setRepeatMode(ValueAnimator.REVERSE);

        final ObjectAnimator endAnimation = ObjectAnimator.ofFloat(point, AnimatorUtils.TRANSLATION_Y, tanslationY_offer, tanslationY_offer*4);
        endAnimation.setDuration(1000);
        endAnimation.setInterpolator(mInterpolator);
        endAnimation.addListener(new com.nineoldandroids.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                super.onAnimationEnd(animation);
                toggleShowSetting(point);
            }
        });

//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(tanslationX,tanslationY);
//        set.addListener(new com.nineoldandroids.animation.AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
//                super.onAnimationEnd(animation);
//                endAnimation.start();
//            }
//        });
//        set.start();
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(point, "rotationX", 0, 360),
                ObjectAnimator.ofFloat(point, "rotationY", 0, 180),
                ObjectAnimator.ofFloat(point, "rotation", 0, -90),
                ObjectAnimator.ofFloat(point, "translationX", 0, 90),
                ObjectAnimator.ofFloat(point, "translationY", 0, 90),
                ObjectAnimator.ofFloat(point, "scaleX", 1, 1.5f),
                ObjectAnimator.ofFloat(point, "scaleY", 1, 0.5f),
                ObjectAnimator.ofFloat(point, "alpha", 1, 0.25f, 1)
        );
        set.setDuration(5 * 1000).start();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_launch;
    }

    /**
     * 切换约定界面
     */
    public void toggleShowSetting(View v) {
        int viewWidth = 0;
        int viewHeight = 0;
        int viewX = 0;
        int viewY = 0;
        if (v != null) {
            viewWidth = v.getWidth();
            viewHeight = v.getHeight();
            viewX = (int) v.getX();
            viewY = (int) v.getY();
        }
        int x = viewX-(viewWidth>>1);
        int y = viewY-(viewHeight>>1);

        float radiusOf = 0.5f * viewWidth / 2f;
        float radiusFromToRoot = (float) Math.hypot(
                Math.max(x, clipRevealFrame.getWidth() - x),
                Math.max(y, clipRevealFrame.getHeight() - y));
        if (!isShow) {
            isShow = true;
            showMenu(clipRevealFrame,x, y, radiusOf, radiusFromToRoot);
        } else {
            isShow = false;
            hideMenu(clipRevealFrame,x, y, radiusFromToRoot, radiusOf);
        }
    }

    /**
     * 显示约定界面
     *
     */
    private void showMenu(final ClipRevealFrame layout,int cx, int cy, float startRadius, float endRadius) {
        layout.setVisibility(View.VISIBLE);
        Animator revealAnim = createCircularReveal(layout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(500);
        revealAnim.start();
    }

    /**
     * 隐藏约定界面
     *
     * @param cx
     * @param cy
     * @param startRadius
     * @param endRadius
     */
    private void hideMenu(final ClipRevealFrame layout,int cx, int cy, float startRadius, float endRadius) {
        Animator revealAnim = createCircularReveal(layout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(500);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layout.setVisibility(View.INVISIBLE);
            }
        });
        revealAnim.start();
    }

    /**
     * view animation
     *
     * @param view
     * @param x
     * @param y
     * @param startRadius
     * @param endRadius
     * @return
     */
    private Animator createCircularReveal(final ClipRevealFrame view, int x, int y, float startRadius,
                                          float endRadius) {
        final Animator reveal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            reveal = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        } else {
            view.setClipOutLines(true);
            view.setClipCenter(x, y);
            reveal = android.animation.ObjectAnimator.ofFloat(view, "ClipRadius", startRadius, endRadius);
            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setClipOutLines(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return reveal;
    }

}
