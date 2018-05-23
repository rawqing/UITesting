package com.jlc.app.milk_mini.utils;

/**
 * Created by king on 16/12/9.
 * 除开消息栏  和 action栏的 净屏幕可操作窗口矩形
 */

public class ScreenRectangle {
    private int top;
    private int bottom;
    private int left;
    private int right;

    public ScreenRectangle(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }
}
