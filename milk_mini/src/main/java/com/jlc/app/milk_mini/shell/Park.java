package com.jlc.app.milk_mini.shell;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.web.model.Atom;
import android.view.View;

import com.jlc.app.milk_mini.elements.ActivityElement;
import com.jlc.app.milk_mini.elements.AtomElement;
import com.jlc.app.milk_mini.elements.Element;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

/**
 * Created by king on 16/10/24.
 */

public class Park {

    public static <T> ViewInteraction getViewInteraction(Element<T> element) {
        T ele = element.way();
        if(ele instanceof ViewInteraction)  return (ViewInteraction) ele;
        if(ele instanceof Matcher)  return onView((Matcher<View>) ele);

        return null;
    }

    /**
     * @deprecated use {@link #given(Element)} instead .
     * 已将toast 封装入Element .
     * so , 后期将不推荐单独getToast
     * @param viewInteraction
     * @return
     */
    public static AdViewInteraction given(ViewInteraction viewInteraction){
        return new AdViewInteraction(viewInteraction);
    }

    /**
     * 使用matcher的封装
     * @param matcher
     * @return
     */
    public static AdViewInteraction given(Matcher<View> matcher){
        return new AdViewInteraction(new ActivityElement().setMatchers(matcher).setDis(false));
    }

    /**
     * 较常用的方式
     * @param element
     * @param <T>
     * @return
     */
    public static <T> AdViewInteraction given(Element<T> element){
        return new AdViewInteraction(element);
    }
    public static AdViewInteraction given(AdViewInteraction element){
        return element;
    }

    /****************** web view ********************/


    /**
     *
     * @param atomElement
     * @return
     */
    public static Atom find(AtomElement atomElement){
        return atomElement.way();
    }

    /**
     * 封装onWebView
     * @return
     */
    public static AdWebInteraction webGiven(){
        return new AdWebInteraction(onWebView());
    }

    /**
     * 在有多个webView的情况下使用
     * 一般来说一个activity中只会有一个webView , so ,使用onWebView()即可匹配
     * @param viewMatcher
     * @return
     */
    public static AdWebInteraction webGiven(Matcher<View> viewMatcher) {
        return new AdWebInteraction(onWebView(viewMatcher));
    }


/********************** onData ***********************/
    /**
     * Uses {@link Espresso#onData(Matcher)} to get a reference to a specific row.
     * <p>
     * Note: A custom matcher can be used to match the content and have more readable code.
     * See the Custom Matcher Sample.
     * </p>
     *
     * @param value the content of the field
     * @return a {@link DataInteraction} referencing the row
     */
    public static DataInteraction onRow(String key , String value) {
        return onData(hasEntry(equalTo(key), is(value)));
    }

//
//    /********************** then ***********************/
//    public static <E> CustomAssert then(Element<E> ele) {
//        return new CustomAssert(ele);
//    }
//
//
//    /********************** wait ***********************/
//    public static void waitFor(WaitStrategy waitStrategy ,int timesout) {
//        int out = timesout * 2;
//        do {
//            if (waitStrategy.isUntilNow()) {
//                return;
//            }
//            sleep(500);    // 等待0.5秒
//            out --;
//        } while (out == 0);
//    }
}
