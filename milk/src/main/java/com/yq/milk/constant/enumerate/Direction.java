package com.yq.milk.constant.enumerate;

import com.trubuzz.trubuzz.utils.ScreenRectangle;

/**
 * Created by king on 16/12/9.
 * 这是滑动时候的方向 ,
 * 栗子: 向左时当然是从右向左, 那么{x ,y}={宽-毛刺 ,0}
 *      向右 -> { 0 + 毛刺 , 0}
 */

public enum Direction {

    LEFT() {
        /**
         *
         * @param fuzz  毛刺
         * @param distance  距离
         * @param rectangle 屏幕矩形
         * @return  创建好的 star -> end 位置
         */
        @Override
        public float[][] getPosition(int fuzz, int distance, ScreenRectangle rectangle) {
            float sx = rectangle.getRight() - fuzz;
            float sy = rectangle.getBottom() / 2;
            return createDirection(sx, sy, sx - distance, sy);
        }
    },
    RIGHT() {
        @Override
        public float[][] getPosition(int fuzz, int distance, ScreenRectangle rectangle) {
            float sx = rectangle.getLeft() + fuzz;
            float sy = rectangle.getBottom()  / 2;
            return createDirection(sx, sy, sx + distance, sy);
        }
    },
    UP () {
        @Override
        public float[][] getPosition(int fuzz, int distance, ScreenRectangle rectangle) {
            float sx = rectangle.getRight()  / 2;
            float sy = rectangle.getBottom() - fuzz;
            return createDirection(sx, sy, sx, sy - distance);
        }
    },
    DOWN () {
        @Override
        public float[][] getPosition(int fuzz, int distance, ScreenRectangle rectangle) {
            float sx = rectangle.getRight()  / 2;
            float sy = rectangle.getTop() + fuzz;
            return createDirection(sx, sy, sx, sy + distance);
        }
    },
    CENTER_RIGHT(){
        @Override
        public float[][] getPosition(int fuzz, int distance, ScreenRectangle rectangle) {
            float sx = rectangle.getRight() / 2;
            float sy = rectangle.getBottom()  / 2;
            return createDirection(sx, sy, sx + distance, sy);
        }
    },
    CENTER_LEFT(){
        @Override
        public float[][] getPosition(int fuzz, int distance, ScreenRectangle rectangle) {
            float sx = rectangle.getRight() / 2;
            float sy = rectangle.getBottom() / 2;
            return createDirection(sx, sy, sx - distance, sy);
        }
    },
    CENTER_UP(){
        @Override
        public float[][] getPosition(int fuzz, int distance, ScreenRectangle rectangle) {
            float sx = rectangle.getRight()  / 2;
            float sy = rectangle.getBottom() / 2;
            return createDirection(sx, sy, sx, sy - distance);
        }
    },
    CENTER_DOWN(){
        @Override
        public float[][] getPosition(int fuzz, int distance, ScreenRectangle rectangle) {
            float sx = rectangle.getRight()  / 2;
            float sy = rectangle.getBottom() / 2;
            return createDirection(sx, sy, sx, sy + distance);
        }
    };

    static float[][] createDirection(float sx ,float sy ,float ex ,float ey){
        return new float[][]{new float[]{sx, sy}, new float[]{ex, ey}};
    }

    abstract public float[][] getPosition(int fuzz, int distance, ScreenRectangle rectangle);
}
