package com.bin.animationtabview;

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
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

public class AnimationTabView extends RelativeLayout {

    private final OvershootInterpolator mInterpolator = new OvershootInterpolator();
    private boolean isAnimation = true; //是否支持动画 默认 true = 支持
    private static final int titleDuration = 300;    //  动画时长

    private Context mContext;

    private int selection = 0;//默认 选中0

    private int itemLayoutIconId; // item icon id
    private int itemLayoutTitleId;// item title id

    private List<AnimationTabItem> tabItems;

    private ECallOnClick onItemClickListener;

    public void setOnItemClickListener(ECallOnClick onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AnimationTabView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.ani_tab_layout, this);

        mContext = context;

    }

    public void initItem(List<AnimationTabItem> items) {
        initItem(items, R.layout.ani_tab_item_left, R.layout.ani_tab_item_right, R.id.tab_icon, R.id.tab_title);
    }

    /**
     * 初始化item
     *
     * @param items             item 集合
     * @param itemLeftLayoutId  左边布局
     * @param itemRightLayoutId 右边布局
     * @param itemLayoutIconId  icon id
     * @param itemLayoutTitleId title id
     */
    public void initItem(List<AnimationTabItem> items, int itemLeftLayoutId, int itemRightLayoutId, int itemLayoutIconId, int itemLayoutTitleId) {
        if (items == null) {
            return;
        }
        int index = 0;
        this.itemLayoutIconId = itemLayoutIconId;
        this.itemLayoutTitleId = itemLayoutTitleId;
        this.tabItems = items;
        this.removeAllViews();
        boolean leftAdd = false;//最左边是否已经有item
        boolean rightAdd = false;//最右边是否已经有item
        int margin_tab_item = (int) mContext.getResources().getDimension(R.dimen.margin_tab_item);
        for (final AnimationTabItem item : items) {
            LinearLayout item_layout;
            boolean isRight = item.getGravity() == AnimationTabItem.GRAVITY_RIGHT;

            if (isRight) {
                item_layout = (LinearLayout) LayoutInflater.from(mContext).inflate(itemRightLayoutId, this, false);
            } else {
                item_layout = (LinearLayout) LayoutInflater.from(mContext).inflate(itemLeftLayoutId, this, false);
            }
            setItemStatus(item, index == 0, item_layout, true);
            item_layout.setId(index + 1);//设置0 无效
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

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(margin_tab_item, margin_tab_item, margin_tab_item, margin_tab_item);

            if (isRight && !rightAdd) {
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                rightAdd = true;
            } else if (!leftAdd) {
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                leftAdd = true;
            } else {
                if (index > 0) {
                    int id = this.getChildAt(index - 1).getId();
                    if (rightAdd && item.getGravity() == AnimationTabItem.GRAVITY_RIGHT) {
                        params.addRule(RelativeLayout.LEFT_OF, id);
                    } else {
                        params.addRule(RelativeLayout.RIGHT_OF, id);
                    }
                }
            }

            this.addView(item_layout, params);
            index++;
        }
    }

    public void setSelection(int index) {
        if (selection == index) {
            return;
        }
        LinearLayout itemLayoutI = (LinearLayout) getChildAt(index);
        LinearLayout itemLayoutS = (LinearLayout) getChildAt(selection);

        setItemStatus(tabItems.get(index), true, itemLayoutI);
        setItemStatus(tabItems.get(selection), false, itemLayoutS);
        startTitleAnimation(selection, index);
        selection = index;
    }

    /**
     * 开始动画
     *
     * @param selection 需要关闭的 title view
     * @param index     需要展开的 title view
     */
    private void startTitleAnimation(int selection, int index) {
        if (!isAnimation) {
            return;
        }
        View expandView = getChildAt(index).findViewById(itemLayoutTitleId);
        View closeView = getChildAt(selection).findViewById(itemLayoutTitleId);
        List<Animator> animList = new ArrayList<>();
        int width = closeView.getWidth();
        animList.add(aniView(closeView, false, width));
        animList.add(aniView(expandView, true, width));
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
    private Animator aniView(View view, final boolean is, int width) {
        float fromValue = is ? 0 : width;
        float toValue = is ? width : 0;
        Animator animator = createScaleAni(view, titleDuration, fromValue, toValue);
        animator.setInterpolator(mInterpolator);
        return animator;
    }

    // 创建 动画
    private Animator createScaleAni(final View view, final int duration, final float fromValue, final float toValue) {
        ValueAnimator animation = ValueAnimator.ofFloat(fromValue, toValue);
        animation.setDuration(duration);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                int width = (int) value;
                if (width < 0) {
                    width = 0;
                }
                params.width = width;
                view.setLayoutParams(params);
            }
        });
        return animation;
    }

    /**
     * 设置item 选中 状态
     *
     * @param item        item属性
     * @param isSelection 是否选中
     * @param itemLayout  item 布局
     */
    private void setItemStatus(AnimationTabItem item, boolean isSelection, ViewGroup itemLayout) {
        setItemStatus(item, isSelection, itemLayout, false);
    }


    /**
     * 设置item 选中 状态
     *
     * @param item        item属性
     * @param isSelection 是否选中
     * @param itemLayout  item 布局
     */
    private void setItemStatus(AnimationTabItem item, boolean isSelection, ViewGroup itemLayout, boolean isInit) {
        ImageView icon = (ImageView) itemLayout.findViewById(itemLayoutIconId);
        TextView title = (TextView) itemLayout.findViewById(itemLayoutTitleId);
        if (isSelection) {
            icon.setImageResource(item.getIconPressed());
            title.setTextColor(item.getTitleColorPressed());
        } else {
            icon.setImageResource(item.getIconNormal());
            title.setTextColor(item.getTitleColorNormal());
            if (isInit)
                title.setWidth(0);
        }
        title.setText(item.getTitle());
    }

    public static int getTitleDuration() {
        return titleDuration;
    }

    public boolean isAnimation() {
        return isAnimation;
    }

    public void setAnimation(boolean animation) {
        isAnimation = animation;
    }

}
