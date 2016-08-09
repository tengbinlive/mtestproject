package com.bin.tabbarview;

import java.io.Serializable;

public class Model implements Serializable {

    public static final int GRAVITY_LEFT = 0; //左
    public static final int GRAVITY_RIGHT = GRAVITY_LEFT + 1;//右

    private int gravity; //如果没有依赖对象 anchor, 则相对于tab layout中的位置.  是在左边还是右边

    private String title;
    private int iconPressed;
    private int iconNormal;
    private int titleColorPressed;
    private int titleColorNormal;
    private float titleSize = 12;

    private int anchor; // 依赖对象(item 位于集合中的位置index+1,因为id为0无效);

    public int getTitleColorPressed() {
        return titleColorPressed;
    }

    public void setTitleColorPressed(int titleColorPressed) {
        this.titleColorPressed = titleColorPressed;
    }

    public int getTitleColorNormal() {
        return titleColorNormal;
    }

    public void setTitleColorNormal(int titleColorNormal) {
        this.titleColorNormal = titleColorNormal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconPressed() {
        return iconPressed;
    }

    public void setIconPressed(int iconPressed) {
        this.iconPressed = iconPressed;
    }

    public int getIconNormal() {
        return iconNormal;
    }

    public void setIconNormal(int iconNormal) {
        this.iconNormal = iconNormal;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public float getTitleSize() {
        return titleSize;
    }

    public int getAnchor() {
        return anchor;
    }

    public void setAnchor(int anchor) {
        this.anchor = anchor;
    }
}
