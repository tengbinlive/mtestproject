package com.binteng.tools;

import aurelienribon.tweenengine.TweenAccessor;

/**
 */
public class SpriteAccessor implements TweenAccessor<Sprite> {
    public static final int HEIGHT = 1;
    public static final int TINT = 6;

    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case HEIGHT:
                returnValues[0] = target.getHeight();
                return 1;
            case TINT:
                returnValues[0] = target.getColor().r;
                returnValues[1] = target.getColor().g;
                returnValues[2] = target.getColor().b;
                return 3;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case HEIGHT:
                target.setHeight(newValues[0]);
                break;
            case TINT:
                Color c = target.getColor();
                c = target.getColor();
                c.set(newValues[0], newValues[1], newValues[2], c.a);
                target.setColor(c);
                break;
            default:
                assert false;
        }
    }
}
