package com.jlc.app.milk_mini.idlingResource;

import android.app.Activity;
import android.support.test.espresso.IdlingResource;

/**
 * Created by king on 2016/9/8.
 */
public interface ActivityLifecycleIdlingResource<T> extends IdlingResource {

    void inject(Activity activity, T activityComponent);

    void clear();
}