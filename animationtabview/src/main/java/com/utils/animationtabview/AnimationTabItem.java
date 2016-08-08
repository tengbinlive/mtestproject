package com.utils.animationtabview;

import java.io.Serializable;

public class AnimationTabItem implements Serializable {

    public static final int GRAVITY_LEFT = 0; //左
    public static final int GRAVITY_RIGHT = GRAVITY_LEFT + 1;//右

    private int gravity; //相对于tab layout中的位置.  是在左边还是右边

    private String title;
    private int iconPressed;
    private int iconNormal;
    private int titleColorPressed;
    private int titleColorNormal;

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

}
