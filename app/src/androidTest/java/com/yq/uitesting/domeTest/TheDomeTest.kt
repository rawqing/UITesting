package com.yq.uitesting.domeTest

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import com.yq.milk.bTest.BaseTest
import com.yq.uitesting.SettingsActivity

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by king on 2018/2/23.
 */
//@RunWith(AndroidJUnit4::class)
class TheDomeTest : BaseTest() {
    private val td: TheDomeAction = TheDomeAction()

    @get:Rule
    var matr: ActivityTestRule<*> = ActivityTestRule(SettingsActivity::class.java)

//    @Test
    fun generalTest() {
        td.enterGeneralPage()
        td.check_element_exist()
    }
}
