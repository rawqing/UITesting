package com.yq.milk.custom.actions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;

import static com.trubuzz.trubuzz.constant.Env.TAG;
import static com.trubuzz.trubuzz.constant.Env.VISIBILITY;
import static com.trubuzz.trubuzz.feature.custom.matchers.CustomMatcher.withAny;
import static com.trubuzz.trubuzz.utils.Judge.isVisible;

/**
 * Created by king on 2017/7/4.
 * 滑动至view可见
 */

public class SwipeToVisible implements ViewAction {
    private final int MAX_TRIES = 5;
    private final SwipeAsDirection swiper;

    public SwipeToVisible(SwipeAsDirection swipeAs) {
        this.swiper = swipeAs;
    }

    @Override
    public Matcher<View> getConstraints() {
        return withAny();
    }

    @Override
    public String getDescription() {
        return "swipe to visible .";
    }

    @Override
    public void perform(UiController uiController, View view) {
        for(int i=0;i<MAX_TRIES;i++) {
            if (isVisible(view,VISIBILITY)) return;
            Log.i(TAG, String.format("perform: swipe %s times",i ));
            swiper.perform(uiController ,view);
        }
    }


}
