package com.yq.milk.custom.actions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.PrecisionDescriber;
import android.support.test.espresso.action.Swiper;
import android.view.View;


import com.yq.milk.constant.enumerate.Direction;

import org.hamcrest.Matcher;

import static com.yq.milk.constant.Env.VISIBILITY;
import static com.yq.milk.custom.matchers.CustomMatcher.withAny;
import static com.yq.milk.utils.God.getScreenRectangle;
import static com.yq.milk.utils.Judge.isVisible;


/**
 * Created by king on 2017/7/4.
 * 按指定的方向,距离等滑动.
 */

public class SwipeAsDirection implements ViewAction {
    private final Swiper swiper;
    private final Direction direction;
    private final int fuzz;
    private final int swipe_distance;
    private final PrecisionDescriber precisionDescriber;
    private final boolean checkVisible;

    /**
     *
     * @param swiper 速度
     * @param direction 方向
     * @param fuzz  边缘毛刺
     * @param swipe_distance  滑动距离 ( start -> end 的像素长度 )
     * @param precisionDescriber    精度 ( 拇指 / 食指 等 )
     * @param checkVisible  是否需要检查可见
     */
    public SwipeAsDirection(Swiper swiper, Direction direction, int fuzz, int swipe_distance,
                            PrecisionDescriber precisionDescriber, boolean checkVisible) {
        this.swiper = swiper;
        this.direction = direction;
        this.fuzz = fuzz;
        this.swipe_distance = swipe_distance;
        this.precisionDescriber = precisionDescriber;
        this.checkVisible = checkVisible;
    }

    @Override

    public Matcher<View> getConstraints() {
        return withAny();
    }

    @Override
    public String getDescription() {
        return "swipe as " + direction;
    }

    @Override
    public void perform(UiController uiController, View view) {
        if(checkVisible){
            if(isVisible(view , VISIBILITY)) return;
        }
        float[] precision = precisionDescriber.describePrecision();
        float[][] positions = direction.getPosition(fuzz, swipe_distance, getScreenRectangle(view));

        new SwipeAs(swiper, positions[0], positions[1], precision)
                .perform(uiController, view);
    }
}