package com.binteng.tools;

public class Sprite {

    private int animationCount;

    private boolean isAnimation;

    private final static int COUNT_MAX = 4;

    private float x;
    private float y;
    private float width;
    private float height;

    private float scaleX;
    private float scaleY;

    public boolean isAnimation(){
        return isAnimation;
    }

   public void addAnimationCount(){
       animationCount++;
       if(animationCount>=COUNT_MAX){
           animationCount = COUNT_MAX;
           isAnimation = true;
       }
   }

    public void lowerAnimationCount(){
        animationCount--;
        if(animationCount<=0){
            isAnimation = false;
            animationCount = 0;
        }
    }

    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setTranslateHeight(float height) {
        changeHeight(height);
    }

    /** Sets the position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
     * changed, it is slightly more efficient to translate after those operations. */
    public void translate (float xAmount, float yAmount) {
        x += xAmount;
        y += yAmount;
    }

    public void changeHeight (float hAmount) {
        height += hAmount;
    }

}
