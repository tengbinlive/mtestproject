package com.binteng.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.bin.AnimatorUtils;
import com.binteng.AbsActivity;
import com.binteng.R;
import com.binteng.mview.ClipRevealFrame;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.BindView;

public class AnimationLaunchActivity extends AbsActivity {

    @BindView(R.id.point)
    ImageView point;
    @BindView(R.id.clip_reveal_frame)
    ClipRevealFrame clipRevealFrame;

    private final BounceInterpolator interpolatorBounce = new BounceInterpolator();
    private boolean isShow;
    private ObjectAnimator startAni;

    private final static int ANIMATION_CLIPREVEAL_TIME = 500;   //内容view 显示动画 duration 时长
    private final static int ANIMATION_TRANSLATION_DURATION = 2 * 1000; //移动动画 duration 时长
    private final static int OFFER_X = 700;// x轴移动偏移量


    //内容动画结束监听回调
    private OnAnimationEnd animationEnd;

    interface OnAnimationEnd {

        void onAnimationEnd();
    }

    @Override
    public void initData() {
        super.initData();
        getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePointAni();
            }
        });
        initAnimation();
    }

    private void togglePointAni() {
        if (!isShow) {
            startAni.start();
        } else {
            toggleShowContent(point);
        }
    }

    private void initAnimation() {
        startAni = createAnimation(point, true);
        createAnimation(point, false);
    }

    private ObjectAnimator createAnimation(View view, boolean is) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(view, AnimatorUtils.TRANSLATION_X, is ? 0 : OFFER_X, is ? OFFER_X : 0);
        animator.setDuration(ANIMATION_TRANSLATION_DURATION);
        animator.setInterpolator(interpolatorBounce);
        if (is) {
            animator.addListener(new com.nineoldandroids.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                    super.onAnimationEnd(animation);
                    toggleShowContent(point);
                }
            });
        } else {
            animationEnd = new OnAnimationEnd() {
                @Override
                public void onAnimationEnd() {
                    animator.start();
                }
            };
        }
        return animator;
    }


    @Override
    public int getLayoutID() {
        return R.layout.activity_launch;
    }

    /**
     * 切换内容view
     */
    public void toggleShowContent(View v) {
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
        int x = viewX - (viewWidth >> 1);
        int y = viewY - (viewHeight >> 1);

        float radiusOf = 0.5f * viewWidth / 2f;
        float radiusFromToRoot = (float) Math.hypot(
                Math.max(x, clipRevealFrame.getWidth() - x),
                Math.max(y, clipRevealFrame.getHeight() - y));
        if (!isShow) {
            isShow = true;
            showMenu(clipRevealFrame, x, y, radiusOf, radiusFromToRoot);
        } else {
            isShow = false;
            hideMenu(clipRevealFrame, x, y, radiusFromToRoot, radiusOf);
        }
    }

    /**
     * 显示内容界面
     */
    private void showMenu(final ClipRevealFrame layout, int cx, int cy, float startRadius, float endRadius) {
        layout.setVisibility(View.VISIBLE);
        Animator revealAnim = createCircularReveal(layout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(ANIMATION_CLIPREVEAL_TIME);
        revealAnim.start();
    }

    /**
     * 隐藏内容界面
     *
     * @param cx
     * @param cy
     * @param startRadius
     * @param endRadius
     */
    private void hideMenu(final ClipRevealFrame layout, int cx, int cy, float startRadius, float endRadius) {
        Animator revealAnim = createCircularReveal(layout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(ANIMATION_CLIPREVEAL_TIME);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layout.setVisibility(View.INVISIBLE);
                if (null != animationEnd)
                    animationEnd.onAnimationEnd();
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
