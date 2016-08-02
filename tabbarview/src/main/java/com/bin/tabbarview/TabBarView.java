package com.bin.tabbarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置 item 间隔位置的tab bar
 */
public class TabBarView extends RelativeLayout {

    private final OvershootInterpolator mInterpolator = new OvershootInterpolator();
    private boolean isAnimation = true; //是否支持动画 默认 true = 支持
    private static int duration = 300;    //  动画时长

    private Context mContext;

    private int selection = 0;//默认 选中0

    private int itemLayoutIconId; // item icon id
    private int itemLayoutTitleId;// item title id

    private List<Model> tabItems;

    private ECallOnClick onItemClickListener;

    private int mModelSize;

    private int mModelNum;

    private int itemLayoutId;

    /**
     * item margin
     */
    private int itemMargin;

    private boolean isBuild;

    public void setOnItemClickListener(ECallOnClick onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TabBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.tabbar_layout, this);

        mContext = context;

    }

    public void initItem(List<Model> items, int index, int dimenID) {
        initItem(items, R.layout.item_model, R.id.tab_icon, R.id.tab_title, dimenID);
        setSelection(index);
    }

    public void initItem(List<Model> items, int dimenID) {
        initItem(items, 0, dimenID);
    }

    public void initItem(List<Model> items) {
        initItem(items, 0, 0);
    }

    /**
     * 初始化item
     *
     * @param items             item 集合
     * @param itemLayoutId      item布局
     * @param itemLayoutIconId  icon id
     * @param itemLayoutTitleId title id
     * @param dimenID           margin_tab_item id
     */
    public void initItem(List<Model> items, int itemLayoutId, int itemLayoutIconId, int itemLayoutTitleId, int dimenID) {
        if (items == null) {
            return;
        }
        this.itemLayoutIconId = itemLayoutIconId;
        this.itemLayoutTitleId = itemLayoutTitleId;
        this.tabItems = items;
        this.itemMargin = dimenID;
        this.itemLayoutId = itemLayoutId;
    }

    private void buildItem() {
        int index = 0;
        this.removeAllViews();
        int margin_tab_item = itemMargin == 0 ? 0 : (int) mContext.getResources().getDimension(itemMargin);
        for (final Model item : tabItems) {
            LinearLayout item_layout;
            boolean isRight = item.getGravity() == Model.GRAVITY_RIGHT;
            int anchor = item.getAnchor();
            item_layout = (LinearLayout) LayoutInflater.from(mContext).inflate(itemLayoutId, this, false);
            setItemStatus(item, index == 0, item_layout);
            item_layout.setId(index + 1);
            item_layout.setTag(index);
            item_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    setSelection(index);
                    if (null != onItemClickListener) {
                        onItemClickListener.callOnClick(v, item, index);
                    }
                }
            });

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mModelSize, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(margin_tab_item, margin_tab_item, margin_tab_item, margin_tab_item);

            if (anchor > 0) {
                params.addRule(item.getGravity(), anchor);
            } else {
                params.addRule(isRight ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            }
            this.addView(item_layout, params);
            index++;
        }
    }

    public void setSelection(int index) {
        if (selection == index) {
            return;
        }
        ViewGroup itemLayoutI = (ViewGroup) getChildAt(index);
        ViewGroup itemLayoutS = (ViewGroup) getChildAt(selection);
        setItemStatus(tabItems.get(index), true, itemLayoutI);
        setItemStatus(tabItems.get(selection), false, itemLayoutS);
        startAnimation(selection, index);
        selection = index;
    }

    /**
     * 开始动画
     *
     * @param selection 需要关闭的  view
     * @param index     需要展开的  view
     */
    private void startAnimation(int selection, int index) {
        if (!isAnimation) {
            return;
        }
        View expandView = getChildAt(index);
        View closeView = getChildAt(selection);
        List<Animator> animList = new ArrayList<>();
        animList.add(aniView(closeView, false));
        animList.add(aniView(expandView, true));
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(animList);
        animSet.start();
    }

    /**
     * 动画
     *
     * @param is 动画状态 true = 展开 | false 关闭
     * @return
     */
    private Animator aniView(View view, final boolean is) {
        float fromValue = is ? 1 : 1.05f;
        float toValue = is ? 1.05f : 1;
        Animator animator = createAni(view, duration, fromValue, toValue);
        animator.setInterpolator(mInterpolator);
        return animator;
    }

    // 创建 动画
    private Animator createAni(final View view, final int duration, final float fromValue, final float toValue) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", fromValue, toValue);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", fromValue, toValue);
        ViewHelper.setPivotX(view, view.getWidth() >> 1);
        ViewHelper.setPivotY(view, view.getHeight() >> 1);
        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY).setDuration(300);
        animator2.setInterpolator(mInterpolator);
        animator2.setDuration(duration);
        return animator2;
    }

    /**
     * 设置item 选中 状态
     *
     * @param item        item属性
     * @param isSelection 是否选中
     * @param itemLayout  item 布局
     */
    private void setItemStatus(Model item, boolean isSelection, ViewGroup itemLayout) {
        ImageView icon = (ImageView) itemLayout.findViewById(itemLayoutIconId);
        TextView title = (TextView) itemLayout.findViewById(itemLayoutTitleId);
        if (isSelection) {
            icon.setImageResource(item.getIconPressed());
            title.setTextColor(item.getTitleColorPressed());
        } else {
            icon.setImageResource(item.getIconNormal());
            title.setTextColor(item.getTitleColorNormal());
        }
        title.setTextSize(item.getTitleSize());
        title.setText(item.getTitle());
    }

    public static int getDuration() {
        return duration;
    }

    public static void setDuration(int duration) {
        TabBarView.duration = duration;
    }

    public boolean isAnimation() {
        return isAnimation;
    }

    public void setAnimation(boolean animation) {
        isAnimation = animation;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Get measure size
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        if (null == tabItems || tabItems.isEmpty() || width == 0 || height == 0) return;


        if (mModelNum <= 0) {
            // Get model size
            mModelSize = width / tabItems.size();
        } else {
            mModelSize = width / mModelNum;
        }

        if (!isBuild) {
            isBuild = true;
            buildItem();
        }

    }

    public int getmModelNum() {
        return mModelNum;
    }

    public void setmModelNum(int mModelNum) {
        this.mModelNum = mModelNum;
    }

    public int getItemMargin() {
        return itemMargin;
    }

    public void setItemMargin(int itemMargin) {
        this.itemMargin = itemMargin;
    }

    public int getSelection() {
        return selection;
    }
}