package com.yq.uitesting.domeTest

import com.yq.milk.elements.ActivityElement
import com.yq.milk.shell.AdViewInteraction

/**
 * Created by king on 2018/2/23.
 */

class TheDomeView {
    internal var generalItem = ActivityElement().setChildren(ActivityElement().setId("title").setText("General")).goal()
    internal var displayName = ActivityElement().setText("Display name1").goal()
}
