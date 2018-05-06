package com.yq.uitesting.domeTest;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import com.yq.milk.bTest.BaseTest;
import com.yq.milk.elements.ActivityElement;
import com.yq.milk.elements.ToastElement;
import com.yq.milk.utils.God;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by king on 2018/5/3.
 */

@RunWith(AndroidJUnit4.class)
public class TheTest extends BaseTest{
    private UiDevice devide;
    private Instrumentation instrumentation;
    private Context mContext = null;

//    @Rule
//    public ActivityTestRule<?> matr = new ActivityTestRule(God.getFixedClass("yq.com.eespuai.LoginActivity"),
//            false ,false);

    @Before
    public void setUp01(){
        instrumentation = InstrumentationRegistry.getInstrumentation();
        devide = UiDevice.getInstance(instrumentation);
        mContext = InstrumentationRegistry.getContext();
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("yq.com.eespuai");
        mContext.startActivity(intent);
    }
//    @Before
//    public void setUp(){
//        Intent intent = new Intent();
//        matr.launchActivity(intent);
//    }

    @Test
    public void invalidInput() throws InterruptedException {
        Thread.sleep(3000);
        new ActivityElement().setId("email").goal().perform(replaceText("abc"));
        new ActivityElement().setId("password").goal().perform(replaceText("123456"));

        new ActivityElement().setId("email_sign_in_button").goal().click();

        Thread.sleep(3000);
        System.out.println("hello");

        new ToastElement("This email address is invalid").goal().check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void emptyInput() throws InterruptedException {
        Thread.sleep(3000);

        new ActivityElement().setId("email_sign_in_button").goal().click();

        Thread.sleep(3000);
        System.out.println("hello");

        new ToastElement("This field is required").goal().check(ViewAssertions.matches(isDisplayed()));
    }

}
