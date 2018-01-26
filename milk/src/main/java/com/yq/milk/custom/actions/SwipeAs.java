package com.yq.milk.custom.actions;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.Swiper;
import android.support.test.espresso.util.HumanReadables;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;

import org.hamcrest.Matcher;

import java.util.Arrays;

import static com.trubuzz.trubuzz.constant.Env.TAG;
import static com.trubuzz.trubuzz.feature.custom.matchers.CustomMatcher.withAny;

/**
 * Created by king on 2017/7/4.
 * 这是一个较原始的方法 , 根据坐标轴来滑动
 * 如: startCoordinates = {10 ,720} -> endCoordinates = {400 ,720}
 * 则说明是从左向右滑动
 */

public class SwipeAs implements ViewAction {
    /** Maximum number of times to attempt sending a swipe action. */
    private static final int MAX_TRIES = 3;

    private final Swiper swiper;
    private final float[] startCoordinates;
    private final float[] endCoordinates;
    private final float[] precision ;

    public SwipeAs(Swiper swiper, float[] startCoordinates, float[] endCoordinates, float[] precision) {
        this.swiper = swiper;
        this.startCoordinates = startCoordinates;
        this.endCoordinates = endCoordinates;
        this.precision = precision;
    }

    @Override
    public Matcher<View> getConstraints() {
        return withAny();
    }

    @Override
    public String getDescription() {
        return "swipe .";
    }

    @Override
    public void perform(UiController uiController, View view) {
        Swiper.Status status = Swiper.Status.FAILURE;

        for (int tries = 0; tries < MAX_TRIES && status != Swiper.Status.SUCCESS; tries++) {
            try {
                Log.i(TAG, "SwipeAs perform .swipe as : "+ Arrays.toString(startCoordinates)
                        + " -> "+ Arrays.toString(endCoordinates) +
                        " ; precision : "+ Arrays.toString(precision));

                status = swiper.sendSwipe(uiController, startCoordinates, endCoordinates, precision);
            } catch (RuntimeException re) {
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(re)
                        .build();
            }

            int duration = ViewConfiguration.getPressedStateDuration();
            // ensures that all work enqueued to process the swipe has been run.
            if (duration > 0) {
                uiController.loopMainThreadForAtLeast(duration);
            }
        }

        if (status == Swiper.Status.FAILURE) {
            throw new PerformException.Builder()
                    .withActionDescription(getDescription())
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(new RuntimeException(String.format(
                            "Couldn't swipe from: %s,%s to: %s,%s precision: %s, %s . Swiper: %s "
                                    + "start coordinate provider: %s . Tried %s times",
                            startCoordinates[0],
                            startCoordinates[1],
                            endCoordinates[0],
                            endCoordinates[1],
                            precision[0],
                            precision[1],
                            swiper,
                            Arrays.toString(startCoordinates),
                            MAX_TRIES)))
                    .build();
        }
    }
}