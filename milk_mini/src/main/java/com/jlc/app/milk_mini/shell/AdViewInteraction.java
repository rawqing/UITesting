package com.jlc.app.milk_mini.shell;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.util.Log;
import android.view.View;

import com.jlc.app.milk_mini.custom.actions.CustomViewAction;
import com.jlc.app.milk_mini.elements.Element;
import com.jlc.app.milk_mini.idlingResource.different.Idle;
import com.jlc.app.milk_mini.idlingResource.different.WaitFor;
import com.jlc.app.milk_mini.utils.God;
import com.yq.allure2_androidj.common.Allure;

import org.hamcrest.Matcher;

import java.util.Date;

import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static com.jlc.app.milk_mini.utils.DoIt.sleep;
import static com.jlc.app.milk_mini.utils.Judge.isNone;

/**
 * Created by king on 16/10/24.
 */

public class AdViewInteraction {
    private static final String TAG = "jlc_AdViewInteraction";
    private Element viewElement ;
    private final ViewInteraction viewInteraction;


    public AdViewInteraction(Element viewElement){
        this.viewElement = viewElement;
        this.viewInteraction = Park.getViewInteraction(viewElement);
    }

    public AdViewInteraction(ViewInteraction viewInteraction) {
        this.viewInteraction = viewInteraction;
    }

    public ViewInteraction getViewInteraction() {
        return this.viewInteraction;
    }
    /**
     * 改良后的perform , 加入等待与错误截图
     * @param times
     * @param viewActions
     * @return
     */
    public AdViewInteraction perform(final int times ,final ViewAction... viewActions) {
        String imagePath = "" ;
        sleep(500);
        for(int i = 1; i<times ; i++){
            Log.i(TAG, String.format("perform: 开始第 %s 次匹配 ' %s ' .", i,this.toString()));
            if(canPerform(this.viewInteraction, viewActions)){
                if (! isNone(imagePath)) Allure.removeAttachment(imagePath);
                return this;
            }else if(i==2 ){
                //执行截图并保存文件名
                imagePath = Allure.addAttachment(God.getDateFormat(new Date()) + " | error take screenshot");
            }
            Log.w(TAG, String.format("perform: 第 %s 次未匹配到元素 ' %s ' .",i , this.toString()));
        }
        Log.i(TAG, String.format("perform: 开始第 %s 次匹配 ' %s ' .", times,this.toString()));
        this.viewInteraction.perform(viewActions);
        if (! isNone(imagePath)) Allure.removeAttachment(imagePath);

        return this;
    }
    public AdViewInteraction perform(final ViewAction... viewActions) {
        return this.perform(5 ,viewActions);
    }
    public AdViewInteraction perform(boolean isTry ,final ViewAction... viewActions) {
        if(isTry){
            try {
                return perform(viewActions);
            }catch (Throwable e ){
                return this;
            }
        }else{
            return perform(viewActions);
        }
    }

    /**
     * 改良后的check , 加入等待与错误截图
     * @param times
     * @param viewAssert
     * @return
     */
    public AdViewInteraction check(final int times ,final ViewAssertion viewAssert){
        sleep(500);
        String imagePath = "" ;
        for(int i=1; i<times ; i++){
            Log.i(TAG, String.format("check: 开始第 %s 次匹配 ' %s ' .",i,this.toString()));
            if(checkRight(viewAssert)){
                if (! isNone(imagePath)) Allure.removeAttachment(imagePath);
                return this;
            }else if(i==2 ){
                //执行截图并保存文件名
                imagePath = Allure.addAttachment(God.getDateFormat(new Date()) + " | error take screenshot");
            }
            Log.w(TAG, String.format("check: 第 %s 次未匹配到元素 ' %s ' .",i ,this.toString()));
        }
        Log.i(TAG, String.format("check: 开始第 %s 次匹配 ' %s ' .",times,this.toString()));
        this.viewInteraction.check(viewAssert);
        if (! isNone(imagePath)) Allure.removeAttachment(imagePath);
        return this;
    }
    /**
     * 默认匹配 5 次
     * @param viewAssert
     * @return
     */
    public AdViewInteraction check(final ViewAssertion viewAssert){
        return check(5 , viewAssert);
    }

    public AdViewInteraction check(final Matcher<View> matcher) {
        return check(matches(matcher));
    }
    /**
     * 尝试check
     * @param isTry
     * @param viewAssert
     * @return
     */
    public AdViewInteraction check(boolean isTry ,final ViewAssertion viewAssert){
        if(isTry){
            try {
                return check(viewAssert);
            }catch (Throwable e ){
                return this;
            }
        }else
            return check(viewAssert);
    }

    /**
     * 判断断言是否成功
     * @param viewAssert
     * @return
     */
    public boolean checkRight(final ViewAssertion viewAssert){
        try {
            this.viewInteraction.check(viewAssert);
            return true;
        } catch (NoMatchingViewException e) {
            Log.e(TAG, "checkRight: check "+ viewAssert.toString() +" not match ", e);
            e.printStackTrace();
            return false;
        } catch (Error error){
            Log.e(TAG, "checkRight: check " + viewAssert.toString() + " assertion failed", error );
            return false;
        }
    }






    /**
     * 点击
     * @return
     */
    public  AdViewInteraction click(){
        return this.perform(ViewActions.click());
    }

    /**
     * 注入字符串
     * @param text
     * @return
     */
    public  AdViewInteraction replaceText(String text){
        return this.perform(ViewActions.replaceText(text));
    }

    /**
     * 使用键盘输入字符串
     * @param text
     * @return
     */
    public  AdViewInteraction typeText(String text){
        return this.perform(ViewActions.typeText(text));
    }

    /**
     * 判断元素在布局存在
     * @return true 存在于布局
     */
    public  boolean isExist(){
        try {
            this.check(2, doesNotExist());
            Log.i(TAG, "dose not exist: " + this.toString());
            return false;
        } catch (Throwable e) {
            Log.i(TAG, "is exist: " + this.toString());
            return true;
        }
    }

    /**
     * 判断元素是否禁用
     * @return
     */
    public boolean isEnabled(int times){
        try {
            this.check(times,matches((ViewMatchers.isEnabled())));
            Log.i(TAG, "is isEnabled: " + this.toString());
            return true;
        } catch (Throwable e ){
            e.printStackTrace();
            Log.i(TAG, "dose not isEnabled: " + this.toString());
            return false;
        }
    }
    public boolean isEnabled(){
        return isEnabled(2);
    }

    /**
     * 判断元素是否可见
     * @return
     */
    public boolean isVisible(int times){
        try {
            this.perform(times,CustomViewAction.nothing());
            Log.i(TAG, "is visible: " + this.toString());
            return true;
        } catch (Throwable e ){
            e.printStackTrace();
            Log.i(TAG, "dose not visible: " + this.toString());
            return false;
        }
    }
    public boolean isVisible(){
        return isVisible(2);
    }

    public boolean isDisplayed(int times){
        try {
            this.check(times,matches((ViewMatchers.isDisplayed())));
            Log.i(TAG, "is displayed: " + this.toString());
            return true;
        } catch (Throwable e ){
            e.printStackTrace();
            Log.i(TAG, "dose not displayed: " + this.toString());
            return false;
        }
    }
    /**
     * 判断操作是否成功
     * @param v
     * @param viewActions
     * @return
     */
    public static boolean canPerform(final ViewInteraction v , final ViewAction... viewActions ) {
        try {
            v.perform(viewActions);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 智能等待一下
     * @param idle
     * @return
     */
    public AdViewInteraction waitFor(Idle idle){
        waitFor(idle, 500, 5000);
        return this;
    }
    public AdViewInteraction waitFor(Idle idle ,long step, long time){
        //执行截图
        Allure.addAttachment(God.getDateFormat(new Date()) + " | take screenshot with before wait.");
        WaitFor.waitForIdle(idle ,step,time);
        return this;
    }

    /**
     * 暂停time毫秒
     * @param time
     * @return
     */
    public AdViewInteraction pause(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String toString() {
        return viewElement == null ? viewInteraction.toString() : viewElement.toString();
    }

}
