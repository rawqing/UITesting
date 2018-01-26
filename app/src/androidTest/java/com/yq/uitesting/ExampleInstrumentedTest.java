package com.yq.uitesting;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.yq.milk.bTest.BaseTest;
import com.yq.milk.elements.ActivityElement;
import com.yq.milk.elements.Element;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.yq.milk.shell.Park.given;
import static com.yq.milk.utils.DoIt.sleep;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest extends BaseTest{

    @Rule
    public ActivityTestRule<?> matr = new ActivityTestRule<>(SettingsActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.yq.uitesting", appContext.getPackageName());

        Element e = new ActivityElement().setChildren(new ActivityElement().setId("title").setText("General"));
        given(e).click();

        sleep(2000);
    }
}
