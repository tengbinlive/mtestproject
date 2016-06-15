package com.bin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

public class AnimationSearchView extends RelativeLayout {

    //搜索框动画
    private float EDITEXT_OFFER;
    private final OvershootInterpolator mInterpolator = new OvershootInterpolator();
    private boolean isAnimationOpen = false; //动画是否处于展开状态
    private EditText searchEt;
    private TextView searchTitle;
    private ImageView searchIcon;
    private boolean isAnimation = true; //搜索框是否支持动画 默认 true = 支持
    private int iconDuration = 200;     //icon 动画时长
    private int editDuration = 400;    //输入框 动画时长
    private int titleDuration = 200;    //title 动画时长
    //textIcon 动画移动偏移量
    private static int iconOfferY = 0;

    public AnimationSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.ani_search_layout, this, true);

        init();
    }

    private void init() {
        initData();
        initView();
        initListener();
    }

    /**
     * 初始化 默认数据
     */
    private void initData() {
        EDITEXT_OFFER = getResources().getDimension(R.dimen.search_animation_offer);
    }

    /**
     * 初始化 view数据
     */
    private void initView() {
        searchEt = (EditText) findViewById(R.id.search_edit);
        searchTitle = (TextView) findViewById(R.id.search_title);
        searchIcon = (ImageView) findViewById(R.id.search_icon);
    }

    /**
     * 初始化 监听事件
     */
    private void initListener() {
        searchEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && isAnimation) {
                    if (!isAnimationOpen) {
                        startAnimation(true);
                        isAnimationOpen = true;
                    }
                }
            }
        });
    }

    /**
     * 设置icon 点击事件
     *
     * @param listener 事件回调
     */
    public void setSearchIconOnClickListener(OnClickListener listener) {
        if (null != listener)
            searchIcon.setOnClickListener(listener);
    }

    /**
     * 搜索 icon
     *
     * @param res_drawble drawble 资源id
     */
    public void setSearchIcon(int res_drawble) {
        searchIcon.setImageResource(res_drawble);
    }

    /**
     * 搜索框默认显示内容
     *
     * @param res_string string 资源id
     */
    public void setSearchTitle(int res_string) {
        searchTitle.setTextColor(res_string);
    }

    /**
     * 搜索框默认显示内容字体颜色
     *
     * @param res_color colors 资源id
     */
    public void setSearchTitleColor(int res_color) {

        searchTitle.setTextColor(res_color);
    }

    /**
     * 搜索框显示内容字体颜色
     *
     * @param res_color colors 资源id
     */
    public void setSearchContentColor(int res_color) {
        searchEt.setTextColor(res_color);
    }


    /**
     * 搜索框背景
     *
     * @param res_drawable drawable 资源id
     */
    public void setSearchContentBackgroundColor(int res_drawable) {
        searchEt.setBackgroundResource(res_drawable);
    }

    /**
     * 设置回车键类型
     *
     * @param imeOptions
     */
    public void setImeOptions(int imeOptions) {
        searchEt.setImeOptions(imeOptions);
    }

    /**
     * 动画是否处于展开状态
     *
     * @return true 展开状态 | false 关闭状态
     */
    public boolean isAnimationOpen() {
        return isAnimationOpen;
    }

    /**
     * 搜索框添加内容改变监听事件
     *
     * @param watcher 输入框内容监听事件
     */
    public void addTextChangedListener(TextWatcherAdapter watcher) {
        searchEt.addTextChangedListener(watcher);
    }

    /**
     * 搜索框回车键监听事件
     *
     * @param listener 搜索按钮监听事件
     */
    public void addEditorActionListener(TextView.OnEditorActionListener listener) {
        searchEt.setOnEditorActionListener(listener);
    }

    /**
     * 展开搜索框动画
     */
    public void openAnimation() {
        if (!isAnimation) {
            return;
        }
        if (!isAnimationOpen) {
            startAnimation(true);
            isAnimationOpen = true;
        }
    }

    /**
     * 关闭搜索框动画
     */
    public void closeAnimation() {
        if (!isAnimation) {
            return;
        }
        if (isAnimationOpen) {
            startAnimation(false);
            isAnimationOpen = false;
            searchEt.clearFocus();
            searchEt.setText("");
        }
    }

    /**
     * 搜索框动画幅度大小 默认10dp
     *
     * @param offer dp
     */
    public void setAnimationOffer(float offer) {
        EDITEXT_OFFER = offer;
    }


    public int getEditDuration() {
        return editDuration;
    }

    public void setEditDuration(int editDuration) {
        this.editDuration = editDuration;
    }

    public int getIconDuration() {
        return iconDuration;
    }

    public void setIconDuration(int iconDuration) {
        this.iconDuration = iconDuration;
    }

    public int getTitleDuration() {
        return titleDuration;
    }

    public void setTitleDuration(int titleDuration) {
        this.titleDuration = titleDuration;
    }

    /**
     * 输入框的动画
     *
     * @param is 动画状态 true = 展开 | false 关闭
     * @return
     */
    private ValueAnimator aniEditView(final boolean is) {
        ValueAnimator animation = ValueAnimator.ofFloat(is ? 0 : EDITEXT_OFFER, is ? EDITEXT_OFFER : 0);
        animation.setDuration(editDuration);
        animation.setInterpolator(mInterpolator);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                LayoutParams lp = (LayoutParams) searchEt.getLayoutParams();
                int margin = (int) value;
                lp.setMargins(margin, margin, margin, 0);
                searchEt.setLayoutParams(lp);
            }
        });
        return animation;
    }


    /**
     * icon 动画
     *
     * @param is 动画状态 true = 展开 | false 关闭
     * @return
     */
    private Animator aniSearchIcon(final boolean is) {
        //只记录一次
        if (iconOfferY <= 0) {
            iconOfferY = (searchEt.getWidth() >> 1);
        }
        return createTranslationXAni(searchIcon, iconDuration, is ? iconOfferY : 0);
    }

    /**
     * title 动画
     *
     * @param is 动画状态 true = 展开 | false 关闭
     * @return
     */
    private Animator aniSearchTitle(final boolean is) {
        float fromAlpha = is ? 1.0f : 0.0f;
        float toAlpha = is ? 0.0f : 1.0f;
        return createAlphaAni(searchTitle, titleDuration, fromAlpha, toAlpha);
    }

    // 创建 X 位移动画
    private Animator createTranslationXAni(final View view, final int duration, final int offer) {
        Animator animation = ObjectAnimator.ofPropertyValuesHolder(
                view,
                AnimatorUtils.translationX(offer)
        );
        animation.setDuration(duration);

        return animation;
    }

    // 创建 alpha动画
    private Animator createAlphaAni(final View view, final int duration, final float fromAlpha, final float toAlpha) {
        Animator animation = ObjectAnimator.ofPropertyValuesHolder(
                view,
                AnimatorUtils.alpha(fromAlpha, toAlpha)
        );
        animation.setDuration(duration);
        return animation;
    }

    /**
     * 开始动画
     *
     * @param is true 展开 | false 关闭
     */
    private void startAnimation(final boolean is) {
        List<Animator> animList = new ArrayList<>();
        animList.add(aniEditView(is));
        animList.add(aniSearchIcon(is));
        animList.add(aniSearchTitle(is));
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(animList);
        animSet.start();
    }

}
