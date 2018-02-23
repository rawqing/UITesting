package com.yq.milk_kotlin.viewHandler

import android.view.View


import com.yq.milk_kotlin.shell.ViewElement

import org.hamcrest.Matcher

import java.util.ArrayList

import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withChild
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withHint
import android.support.test.espresso.matcher.ViewMatchers.withParent
import android.support.test.espresso.matcher.ViewMatchers.withResourceName
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.yq.milk.custom.matchers.CustomMatcher.hasSiblingNoSelf
import com.yq.milk.custom.matchers.CustomMatcher.withCousin
import com.yq.milk.custom.matchers.CustomMatcher.withIndex
import com.yq.milk.custom.matchers.CustomMatcher.withUncle
import com.yq.milk.utils.DoIt.notEmpty
import com.yq.milk.utils.God
import com.yq.milk_kotlin.shell.selfImp.MIterable
import org.hamcrest.core.AllOf.allOf

/**
 * Created by king on 16/12/14.
 */

class ElementHandle {
    /**
     * 将封装的element 转换成 matcher list , 摒弃null和空值
     * @return
     */
    fun element2matcher(ve: ViewElement<*>): List<Matcher<View>> {
        val ms = ArrayList<Matcher<View>>()

        val id = ve.id
        if (notEmpty(id)) ms.add(withResourceName(id))

        val text = ve.text
        if (notEmpty(text)) ms.add(withText(text))

        val hint = ve.hint
        if (notEmpty(hint)) ms.add(withHint(hint))

        val content_desc = ve.content_desc
        if (notEmpty(content_desc)) ms.add(withContentDescription(content_desc))

        val children = ve.children
        if (notEmpty(children)) ms.add(allOf(MIterable(childrenMatch(children))))
        //        if(notEmpty(children)) ms.add(withChild(allOf(new MIterable(elements2matcher(children))))); ///- 原来错误的方案

        val siblings = ve.siblings
        if (notEmpty(siblings))
            ms.add(allOf(MIterable(siblingsMatch(siblings))))
        //            ms.add(hasSiblingNoSelf(allOf(new MIterable(elements2matcher(siblings)))));

        val cousinry = ve.cousinry
        if (notEmpty(cousinry)) ms.add(allOf(MIterable(cousinryMatch(cousinry))))

        val parent = ve.parent
        if (notEmpty(parent)) ms.add(withParent(parent.way()))

        val uncle = ve.uncle
        if (notEmpty(uncle)) ms.add(withUncle(uncle.way()))

        val ancestor = ve.ancestor
        if (notEmpty(ancestor)) ms.add(isDescendantOfA(ancestor.way()))

        val descendant = ve.descendant
        if (notEmpty(descendant)) ms.add(hasDescendant(descendant.way()))


        if (ve.index!! >= 0) ms.add(withIndex(ve.index!!))

        val assignableClass = ve.assignableClass
        if (notEmpty(assignableClass)) ms.add(isAssignableFrom(assignableClass))

        val matchers = ve.matchers
        if (notEmpty(matchers)) ms.addAll(matchers)

        val displayed = ve.dis
        if (displayed) ms.add(isDisplayed())

        val self = ve.self
        if (notEmpty(self)) ms.addAll(element2matcher(self))

        return ms
    }

    private fun allOf(mIterable: MIterable): Matcher<View> {
        for (any in mIterable.iterator()) {

        }
        allOf()
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

    private fun childrenMatch(vararg elements: Element<Matcher<*>>): List<Matcher<View>> {
        val ms = ArrayList<Matcher<View>>()
        for (element in elements) {
            ms.add(withChild(element.way()))
        }
        return ms
    }

    private fun siblingsMatch(vararg elements: Element<Matcher<*>>): List<Matcher<View>> {
        val ms = ArrayList<Matcher<View>>()
        for (element in elements) {
            ms.add(hasSiblingNoSelf(element.way()))
        }
        return ms
    }

    private fun cousinryMatch(vararg elements: Element<Matcher<*>>): List<Matcher<View>> {
        val ms = ArrayList<Matcher<View>>()
        for (element in elements) {
            ms.add(withCousin(element.way()))
        }
        return ms
    }

    fun all(list: List<Matcher<View>>): Matcher<View> {
        return allOf(MIterable(list))
    }

    companion object {

        /**
         * 直接返回set的text值.
         * @param activityElement
         * @return
         */
        fun getElementText(activityElement: ActivityElement): String {
            return activityElement.getText()
        }
    }
}
