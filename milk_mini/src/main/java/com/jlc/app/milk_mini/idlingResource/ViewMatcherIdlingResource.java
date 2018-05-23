package com.jlc.app.milk_mini.idlingResource;

import android.support.test.espresso.IdlingResource;
import android.util.Log;
import android.view.View;

import com.jlc.app.milk_mini.elements.ActivityElement;

import org.hamcrest.Matcher;

import static com.jlc.app.milk_mini.utils.Judge.isMatched;


/**
 * Created by king on 16/12/12.
 */

public class ViewMatcherIdlingResource implements IdlingResource {
    private final String TAG = "jcd_" + this.getClass().getSimpleName();
    private ResourceCallback resourceCallback;
    private boolean isIdle;

    private  View view;
    private ActivityElement activityElement ;
    private final Matcher<View> matcher;

    public ViewMatcherIdlingResource(View view, Matcher<View> matcher) {
        this.view = view;
        this.matcher = matcher;
    }

    public ViewMatcherIdlingResource(ActivityElement activityElement, Matcher<View> matcher) {
        this.activityElement = activityElement;
        this.matcher = matcher;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        if (isIdle) return true;

        if (view != null) {
            isIdle = matcher.matches(view);
        } else if (activityElement != null) {
            isIdle = isMatched(activityElement, matcher);
        }
        Log.i(TAG, "isIdleNow: " + isIdle);
        if (isIdle) {
            resourceCallback.onTransitionToIdle();
        }
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}
