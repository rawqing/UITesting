package com.jlc.app.milk_mini.bTest.common;

import android.util.Log;

import com.jlc.app.milk_mini.elements.ToastElement;
import com.jlc.app.milk_mini.utils.God;

import org.junit.Assert;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.jlc.app.milk_mini.constant.Conf.instrumentation;
import static com.jlc.app.milk_mini.custom.matchers.CustomMatcher.thisString;
import static com.jlc.app.milk_mini.shell.Park.given;

/**
 * Created by king on 17/6/9.
 */

public class CommonAction {
    private static final String TAG = "jlc_" + CommonAction.class.getSimpleName();

    /**
     * 检查给定的activity name 是否属于当前activity
     * @param activityName
     */
    public static void check_current_activity(String activityName ) {
        String currentAName = God.getTopActivityName(instrumentation.getContext());
        Log.i(TAG, String.format("check_current_activity:  current activity name = %s", currentAName ));

        Assert.assertThat(currentAName ,thisString(activityName));
    }

    /**
     * 检查给定的 toast 是否可见
     * @param toastElement
     */
    public static void check_toast_msg(ToastElement toastElement) {
        given(toastElement).check(matches(isDisplayed()));
    }

}
