package com.jlc.app.milk_mini.idlingResource;

import android.support.test.espresso.IdlingResource;
import android.util.Log;
import android.view.View;

import com.jlc.app.milk_mini.utils.Judge;

import org.hamcrest.Matcher;

/**
 * Created by king on 16/12/12.
 */

public class ViewIdlingResource implements IdlingResource {
    private final String TAG = "jcd_" + this.getClass().getSimpleName();
    private ResourceCallback resourceCallback;
    private boolean isIdle;
    private final View view;
    private Matcher<View> matcher = null;

    public ViewIdlingResource(View view) {
        this.view = view;
    }

    public ViewIdlingResource(View view, Matcher<View> matcher) {
        this.view = view;
        this.matcher = matcher;
    }

    @Override
    public String getName() {
        return this.getClass().getName() + " view : " + view.toString();
    }

    @Override
    public boolean isIdleNow() {
        if (isIdle) return true;
        // 如果matcher 不为null , 则优先使用matcher判断
        if(matcher != null){
            return matcher.matches(view);
        }
        isIdle = Judge.isVisible(view, 90);
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
