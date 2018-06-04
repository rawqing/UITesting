package com.jlc.app.milk_mini.elements;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.jlc.app.milk_mini.constant.Conf;
import com.jlc.app.milk_mini.shell.AdViewInteraction;
import com.jlc.app.milk_mini.utils.God;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jlc.app.milk_mini.custom.matchers.CustomMatcher.withContainText;
import static com.jlc.app.milk_mini.custom.matchers.CustomMatcher.withRegexText;
import static com.jlc.app.milk_mini.custom.matchers.CustomMatcher.withWildcardText;
import static com.jlc.app.milk_mini.elements.ToastElement.MsgType.intact;
import static com.jlc.app.milk_mini.shell.Park.given;
import static com.jlc.app.milk_mini.utils.DoIt.notEmpty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by king on 16/11/16.
 */

public class ToastElement implements Element<ViewInteraction> {

    private String toastMsg;
    private ActivityTestRule activity;
    private MsgType msgType = intact;

    public ToastElement(String toastMsg, ActivityTestRule activity) {
        this.toastMsg = toastMsg;
        this.activity = activity;
    }

    public ToastElement(String toastMsg, MsgType msgType) {
        this.toastMsg = toastMsg;
        this.msgType = msgType;
    }
    public ToastElement(String toastMsg) {
        this.toastMsg = toastMsg;
    }
    public ToastElement(){}

    public ToastElement setToastMsg(String toastMsg) {
        this.toastMsg = toastMsg;
        return this;
    }

    public ToastElement setActivity(ActivityTestRule activity) {
        this.activity = activity;
        return this;
    }

    public ToastElement setMsgType(MsgType msgType) {
        this.msgType = msgType;
        return  this;
    }

    @Override
    public String toString() {
        String string = "{";
        if(notEmpty(toastMsg))   string += "toastMsg='" + toastMsg + "', ";
        if(notEmpty(activity))  string += "activity='" + activity + "', ";
        if(notEmpty(msgType))  string += "msgType='" + msgType.name() + "', ";
        return string += '}';
    }

    @Override
    public ViewInteraction way() {
        if(notEmpty(toastMsg)){
            if(notEmpty(activity)){
                return withMsgType(msgType)
                        .inRoot(withDecorView(not(is(activity.getActivity().getWindow().getDecorView()))));

            }else{
                return withMsgType(msgType)
                        .inRoot(withDecorView(not(is(God.getCurrentActivity(Conf.instrumentation).getWindow().getDecorView()))));
            }
        }
        return null;
    }

    @Override
    public AdViewInteraction goal() {
        return given(this.way());
    }

    /**
     * {@link #way()}的辅助判断方法 ,无特殊意义
     * @param msgType
     * @return
     */
    private ViewInteraction withMsgType(MsgType msgType) {
        switch (msgType) {
            case contain:
                return onView(withContainText(toastMsg));
            case regex:
                return onView(withRegexText(toastMsg));
            case wildcard:
                return onView(withWildcardText(toastMsg));
            default:
                return onView(withText(toastMsg));
        }
    }

    public enum  MsgType{
        // 完整匹配
        intact ,
        // 包含匹配
        contain ,
        // 正则匹配
        regex ,
        // 统配符匹配
        wildcard
    }
}
