package com.yq.milk.custom.assertors;

import android.support.test.espresso.web.assertion.WebAssertion;
import android.support.test.espresso.web.assertion.WebViewAssertions;
import android.support.test.espresso.web.model.Atom;
import android.webkit.WebView;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static kotlin.jvm.internal.Intrinsics.checkNotNull;

/**
 * Created by king on 2016/10/21.
 */

public class CustomWebAssert {
    private CustomWebAssert(){}

    private static final WebViewAssertions.ResultDescriber<Object> TO_STRING_DESCRIBER = new WebViewAssertions.ResultDescriber<Object>() {
        public String apply(Object input) {
            return input.toString();
        }
    };

    /**
     * A WebAssertion which asserts that the given Atom's result is accepted by the provided matcher.
     *  改良 : 比较过程中过滤多个连续空格 , 使用单个空格代替
     * @param atom an atom to evaluate on the webview
     * @param resultMatcher a matcher to apply to the result of the atom.
     */
    public static <E> WebAssertion<E> customWebMatches(Atom<E> atom, final Matcher<E> resultMatcher) {
        return customWebMatches(atom, resultMatcher, TO_STRING_DESCRIBER);
    }

    /**
     * A WebAssertion which asserts that the given Atom's result is accepted by the provided matcher.
     * 改良 : 比较过程中过滤多个连续空格 , 使用单个空格代替
     * @param atom an atom to evaluate on the webview
     * @param resultMatcher a matcher to apply to the result of the atom.
     * @param resultDescriber a describer that converts the result to a string.
     */
    public static <E> WebAssertion<E> customWebMatches(Atom<E> atom, final Matcher<E> resultMatcher,
                                                       final WebViewAssertions.ResultDescriber<? super E> resultDescriber) {
        checkNotNull(resultMatcher);
        checkNotNull(resultDescriber);
        checkNotNull(atom);
        return new WebAssertion<E>(atom) {
            @Override
            public void checkResult(WebView view, E result) {

                StringDescription description = new StringDescription();
                description.appendText("'");
                resultMatcher.describeTo(description);
                description.appendText("' doesn't match: ");
                description.appendText(null == result ? "null" : resultDescriber.apply(result));
                if(result instanceof String){
                    String newRes = "";
                    Matcher<String> rm;
                     newRes = (String) result;
                    rm =(Matcher<String>) resultMatcher;
                    assertThat(description.toString() , newRes.replaceAll("\\x20+"," ") , rm);
                }else
                    assertThat(description.toString(), result, resultMatcher);
            }
        };
    }

}
