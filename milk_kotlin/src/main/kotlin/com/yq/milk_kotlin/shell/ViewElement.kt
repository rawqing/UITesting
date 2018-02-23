package com.yq.milk_kotlin.shell

import android.support.test.espresso.ViewInteraction
import android.view.View
import com.yq.milk_kotlin.shell.selfImp.MIterable
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

/**
 * Created by king on 2018/2/23.
 */
class ViewElement <T>(
        var id: String? = null,
        var text: String? = null,
        var hint: String? = null,
        var children: Array<ViewElement<Matcher<View>>>? = null,
        var siblings: Array<ViewElement<Matcher<View>>>? = null,
        var cousinry: Array<ViewElement<Matcher<View>>>? = null,
        var parent: ViewElement<Matcher<View>>? = null,
        var uncle: ViewElement<Matcher<View>>? = null,
        var descendant: ViewElement<Matcher<View>>? = null,
        var ancestor: ViewElement<Matcher<View>>? = null,
        var index : Int? = -1,
        var assignableClass: Class<out View>? = null,
        var matchers: Array<Matcher<*>>? = null,
        var dis : Boolean = true,
        var content_desc: String? = null,
        var self: ViewElement<T>? = null
): ViewInteractionSuper() {
    override lateinit var viewInteraction: ViewInteraction


    fun all(list: List<Matcher<View>>): Matcher<View> {
        return allOf(MIterable(list))
    }

}