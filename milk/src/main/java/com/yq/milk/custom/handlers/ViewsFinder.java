package com.yq.milk.custom.handlers;

import android.support.test.espresso.AmbiguousViewMatcherException;
import android.util.Log;
import android.view.View;

import com.trubuzz.trubuzz.shell.Element;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static com.trubuzz.trubuzz.feature.custom.actions.CustomViewAction.nothing;

/**
 * Created by king on 16/12/6.
 * 专注于获得多个view
 */

public class ViewsFinder {
    private List<View> views = new ArrayList<>();
    private final String TAG = "jcd_" + this.getClass().getSimpleName();

    private Matcher<View> thisMatcher(final Matcher<View> matcher){
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("this is a view finder .");
            }

            @Override
            protected boolean matchesSafely(View view) {
                if(matcher.matches(view)){
                    views.add(view);
                    return true;
                }
                return false;
            }
        };
    }

    private boolean hasViews(Matcher<View> matcher){
        try {
            onView(thisMatcher(matcher)).perform(nothing());
            return true;
        } catch (AmbiguousViewMatcherException e) {
            return true;
        } catch (Exception ne){
            Log.e(TAG, "hasViews: ", ne);
            return false;
        }
    }

    public List<View> getViews(Matcher<View> matcher) {
        if(this.hasViews(matcher))
            return views;
        else {
            Log.w(TAG, "getViews: 没有匹配的view" );
            return null;
        }
    }
    public List<View> getViews(Element<Matcher<View>> matcher) {
        return getViews(matcher.way());
    }
}
