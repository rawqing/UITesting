package com.yq.uitesting.domeTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.yq.milk.elements.ActivityElement;
import com.yq.milk.shell.AdViewInteraction;
import com.yq.milk.utils.God;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BlackTest01  {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule(God.getFixedClass("com.yq.espdemo.LoginActivity"));

    @Test
    public void testLogin01(){
        AdViewInteraction email = new ActivityElement().setId("email").goal();
        AdViewInteraction password = new ActivityElement().setId("password").goal();
        AdViewInteraction submit = new ActivityElement().setId("email_sign_in_button").goal();
        email.replaceText("hello@gmail.com");
        password.replaceText("pwd");
        submit.click();

    }
}
