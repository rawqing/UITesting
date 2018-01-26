package com.yq.milk.custom.actions;

import android.support.annotation.Size;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.Tap;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;


import com.yq.milk.constant.enumerate.Direction;

import org.hamcrest.Matcher;

import java.util.Arrays;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by king on 16/10/24.
 */

public class CustomViewAction  {
    private static final String TAG = "jcd_" + CustomViewAction.class.getSimpleName();

    private CustomViewAction(){}

    /**
     * 没什么可做的 , 辅助方法
     * @return
     */
    public static ViewAction nothing(){
       return new ViewAction() {
           @Override
           public Matcher<View> getConstraints() {
               return isDisplayed();
           }

           @Override
           public String getDescription() {
               return "nothing to do .";
           }

           @Override
           public void perform(UiController uiController, View view) {
               Log.i(TAG, "perform: no thing to do .");
           }
       };
    }

    /**
     * 选中 or 取消选中CheckBox
     * @return
     */
    public static ViewAction doCheck(final boolean checkOrUncheck){
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(CheckBox.class));
            }

            @Override
            public String getDescription() {
                if (checkOrUncheck) {
                    return "check CheckBox";
                }
                return "uncheck CheckBox";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                CheckBox checkBox = ((CheckBox) view);
                boolean checked = checkBox.isChecked();
                // 实际的选中情况与预期不相符 , 则将状态设置成预期的状态即可
                if (checked != checkOrUncheck) {
                    checkBox.setChecked(checkOrUncheck);
                }
            }
        };
    }

    /**
     * 自由滑动
     * @param direction 方向
     * @param fuzz 毛刺
     * @param swipe_distance 距离
     * @param checkVisible 是否检查存在
     * @return
     */
    public static ViewAction swipeAsDirection(Direction direction, int fuzz, int swipe_distance, boolean checkVisible){
//        return new SwipeAsDirection(Swipe.FAST, direction, fuzz, swipe_distance, Press.FINGER, checkVisible);
        return null;
    }

    /**
     * 滑动至View可见
     *
     * @param direction 滑动方向
     * @return
     */
    public static ViewAction swipeToVisible(Direction direction){
        return new SwipeToVisible((SwipeAsDirection) swipeAsDirection(direction ,10 ,300 ,false));
    }


    /**
     * 按View的相对坐标click
     * @param x
     * @param y
     * @return
     */
    public static ViewAction clickRelativeXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);
                        Log.d(TAG, String.format("calculateCoordinates: screenPos %s", Arrays.toString(screenPos)));

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};
                        Log.d(TAG, String.format("calculateCoordinates: coordinates %s", Arrays.toString(coordinates)));

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    /**
     * 按绝对坐标点击 , 无关View
     * @param x
     * @param y
     * @return
     */
    public static ViewAction clickXY(final float x, final float y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        return new float[]{x, y};
                    }
                },
                Press.FINGER);
    }
    public static ViewAction clickXY(@Size(2) int[] xy){
        return clickXY(xy[0], xy[1]);
    }
}
