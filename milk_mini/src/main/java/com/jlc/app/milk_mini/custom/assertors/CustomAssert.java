package com.jlc.app.milk_mini.custom.assertors;


import android.support.test.espresso.ViewAssertion;

import org.hamcrest.Matchers;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.util.Checks.checkNotNull;

/**
 * Created by king on 17/6/9.
 * 暂时没什么可用价值
 */

public class CustomAssert{



    /**
     * Text 等于指定字符串
     * @param str
     * @return
     */
    public static ViewAssertion textEq(String str){
        return matches(withText(checkNotNull(str)));
    }

    /**
     * Text 以 str 开头
     * @param str
     * @return
     */
    public static ViewAssertion textStartsWith(String str){
        return matches(withText(Matchers.startsWith(checkNotNull(str))));
    }

    /**
     * Text 以 str 结尾
     * @param str
     * @return
     */
    public static ViewAssertion textEndsWith(String str){
        return matches(withText(Matchers.endsWith(checkNotNull(str))));
    }

    /**
     * 判断view与页面可见在 90% 及以上(可见)
     * @return
     */
    public static ViewAssertion isVisible(){
        return matches(isDisplayingAtLeast(90));
    }


}
