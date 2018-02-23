package com.yq.milk_kotlin.shell

import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewInteraction
import android.util.Log

/**
 * Created by king on 2018/2/23.
 */
abstract class ViewInteractionSuper {

    private val TAG: String = this.javaClass.simpleName
    abstract var viewInteraction: ViewInteraction

    fun perform(times: Int, vararg viewActions: ViewAction) : ViewInteractionSuper{
        for (i in 1 until times) {
            Log.i(TAG, String.format("perform: 开始第 %s 次匹配 ' %s ' .", i, this.toString()))

            this.viewInteraction.perform(*viewActions)
            return this
        }
        return this
    }
}