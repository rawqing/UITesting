package com.yq.milk.elements;

import android.view.View;

import com.trubuzz.trubuzz.shell.MIterable;
import com.trubuzz.trubuzz.utils.God;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.trubuzz.trubuzz.feature.custom.matchers.CustomMatcher.hasSiblingNoSelf;
import static com.trubuzz.trubuzz.feature.custom.matchers.CustomMatcher.withCousin;
import static com.trubuzz.trubuzz.feature.custom.matchers.CustomMatcher.withIndex;
import static com.trubuzz.trubuzz.feature.custom.matchers.CustomMatcher.withUncle;
import static com.trubuzz.trubuzz.utils.DoIt.notEmpty;

/**
 * Created by king on 16/12/14.
 */

public class ElementHandle {
    /**
     * 将封装的element 转换成 matcher list , 摒弃null和空值
     * @return
     */
    public List<Matcher<View>> element2matcher(ActivityElement ae ){
        List<Matcher<View>> ms = new ArrayList<Matcher<View>>();

        String id = ae.getId();
        if(notEmpty(id)) ms.add(withResourceName(id));

        String text = ae.getText();
        if(notEmpty(text)) ms.add(withText(text));

        String hint = ae.getHint();
        if(notEmpty(hint)) ms.add(withHint (hint));

        String content_desc = ae.getContent_desc();
        if(notEmpty(content_desc)) ms.add(withContentDescription(content_desc));

        Element[] children = ae.getChildren();
        if(notEmpty(children)) ms.add(allOf(new MIterable(childrenMatch(children))));
//        if(notEmpty(children)) ms.add(withChild(allOf(new MIterable(elements2matcher(children))))); ///- 原来错误的方案

        Element[] siblings = ae.getSiblings();
        if(notEmpty(siblings))
            ms.add(allOf(new MIterable(siblingsMatch(siblings))));
//            ms.add(hasSiblingNoSelf(allOf(new MIterable(elements2matcher(siblings)))));

        Element[] cousinry = ae.getCousinry();
        if(notEmpty(cousinry)) ms.add(allOf(new MIterable(cousinryMatch(cousinry))));
//        if(notEmpty(cousinry)) ms.add(withCousin(allOf(new MIterable(elements2matcher(cousinry)))));

        Element<Matcher<View>> parent = ae.getParent();
        if(notEmpty(parent)) ms.add(withParent(parent.way()));

        Element<Matcher<View>> uncle = ae.getUncle();
        if(notEmpty(uncle)) ms.add(withUncle(uncle.way()));

        Element<Matcher<View>> ancestor = ae.getAncestor();
        if(notEmpty(ancestor)) ms.add(isDescendantOfA(ancestor.way()));

        Element<Matcher<View>> descendant = ae.getDescendant();
        if(notEmpty(descendant)) ms.add(hasDescendant(descendant.way()));

        int index = ae.getIndex();
        if(index >= 0) ms.add(withIndex(index));

        Class<? extends View> assignableClass = ae.getAssignableClass();
        if(notEmpty(assignableClass)) ms.add(isAssignableFrom(assignableClass));

        Matcher[] matchers = ae.getMatchers();
        if(notEmpty(matchers)) ms.addAll(God.<Matcher<View>>array2list(matchers));

        boolean displayed = ae.isDis();
        if(displayed)   ms.add(isDisplayed());

        ActivityElement self = ae.getSelf();
        if (notEmpty(self)) ms.addAll(element2matcher(self));

        return ms;
    }

    /**
     * 多element 的合并
     * @param elements
     * @return
     */
//    private List<Matcher<View>> elements2matcher(Element<Matcher>... elements){
//        List<Matcher<View>> ms = new ArrayList<>();
//        for(Element<Matcher> element : elements){
//            ms.add(element.way());
//        }
//        return ms;
//    }

    private List<Matcher<View>> childrenMatch(Element<Matcher>... elements){
        List<Matcher<View>> ms = new ArrayList<>();
        for(Element<Matcher> element : elements){
            ms.add(withChild(element.way()));
        }
        return ms;
    }
    private List<Matcher<View>> siblingsMatch(Element<Matcher>... elements){
            List<Matcher<View>> ms = new ArrayList<>();
            for(Element<Matcher> element : elements){
                ms.add(hasSiblingNoSelf(element.way()));
            }
            return ms;
    }
    private List<Matcher<View>> cousinryMatch(Element<Matcher>... elements){
            List<Matcher<View>> ms = new ArrayList<>();
            for(Element<Matcher> element : elements){
                ms.add(withCousin(element.way()));
            }
            return ms;
    }

    /**
     * 直接返回set的text值.
     * @param activityElement
     * @return
     */
    public static String getElementText(ActivityElement activityElement) {
        return activityElement.getText();
    }
}
