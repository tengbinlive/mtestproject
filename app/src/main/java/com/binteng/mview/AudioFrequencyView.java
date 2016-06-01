package com.binteng.mview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.binteng.R;
import com.binteng.tools.Sprite;
import com.binteng.tools.SpriteAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;

/**
 * 自定义声音波浪
 */
public class AudioFrequencyView extends View {

    private final TweenManager tweenManager = new TweenManager();
    private final long startTime = System.currentTimeMillis();
    private long privTime = System.currentTimeMillis();
    private float delta = 0.0f;

    private Paint mPaint = new Paint();// 画笔
    private int mCurrentSpectrumNum;

    private float mLevelLine = 0;// 水位线 默认view 中间
    private boolean isMeasured = false;
    private float mArticleWidth;// article宽
    private float mGap;// 间隔
    private float mArticleHeight = 20;// article 高度
    private int mViewWidth;

    private Sprite[] mArticles;

    public AudioFrequencyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioFrequencyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        mArticleWidth = getResources().getDimension(R.dimen.article_width);
        mGap = mArticleWidth;
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#E54B4C"));
    }

    /**
     * 初始化 article
     *
     * @param size 数量
     */
    private void initArticle(int size) {
        if (null == mArticles) {
            mCurrentSpectrumNum = size;
            mArticles = new Sprite[mCurrentSpectrumNum];
            for (int i = size - 1; i >= 0; i--) {
                float x = (mGap * 1.5f) + (mArticleWidth + mGap) * i;
                float y = mLevelLine;
                Sprite sprite = new Sprite();
                sprite.setX(x);
                sprite.setY(y);
                sprite.setHeight(mArticleHeight);
                sprite.setWidth(mArticleWidth);
                mArticles[i] = sprite;
            }
        }
    }

    public void updateVisualizer(int index) {
        if (null != mArticles && index > 0 && index < mCurrentSpectrumNum) {
            final Sprite sprite = mArticles[index];
            if (sprite.isAnimation()) {
                return;
            }
            sprite.addAnimationCount();
            float toHeight = sprite.getHeight() + mArticleHeight;
            float mArticleHeightMAX = 600;
            toHeight = toHeight > mArticleHeightMAX ? mArticleHeightMAX : toHeight;
            Timeline.createSequence()
                    .push(Tween.to(mArticles[index], SpriteAccessor.HEIGHT, 0.6f).target(toHeight).ease(Back.OUT))
                    .push(Tween.to(mArticles[index], SpriteAccessor.HEIGHT, 0.3f).target(mArticleHeight).ease(Back.OUT))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            sprite.lowerAnimationCount();
                        }
                    })
                    .start(tweenManager);
        }
    }

    /**
     * 根据声波数组更新波浪的数据
     * 为了显示效果随意 写的.
     *
     * @param fft
     */
    public void updateVisualizer(byte[] fft) {
        for (int i = 2, j = 1; j < 7; ) {
            byte model = (byte) Math.hypot(fft[i], fft[i + 1]);
            if (model < 0) {
                model = 127;
            }
            updateVisualizer(model);
            i += 2;
            j++;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            isMeasured = true;

            mViewWidth = getMeasuredWidth();
            int mViewHeight = getMeasuredHeight();

            // 水位线从最底下开始上升
            mLevelLine = mViewHeight >> 1;

            int size = mViewWidth / ((int) mArticleWidth << 1);
            int mMaxsize = 70;
            size = size < mMaxsize ? size : mMaxsize;
            initArticle(size - 1);
        }
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        render();
        if (mCurrentSpectrumNum > 0) {
            for (Sprite sprite : mArticles) {
                float left = sprite.getX();
                float top = sprite.getY() - sprite.getHeight();
                float right = sprite.getX() + sprite.getWidth();
                float bottom = sprite.getY();
                canvas.drawRect(left, top, right, bottom, mPaint);

                //反向
                top = sprite.getY() + mGap;
                bottom = sprite.getY() + sprite.getHeight() + mGap;
                left = mViewWidth - sprite.getX() - mGap;
                right = mViewWidth - sprite.getX() + sprite.getWidth() - mGap;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }

        }
        delta = (float) ((System.currentTimeMillis() - privTime) / 1000.0);
        privTime = System.currentTimeMillis();
        invalidate();// 重绘
    }

    public void render() {
        if (startTime < System.currentTimeMillis()) {
            tweenManager.update(delta);
        }
    }

    public void dispose() {
        tweenManager.killAll();
    }

}
