package com.yq.uitesting.domeTest

import com.yq.milk.custom.assertors.CustomAssert.textEndsWith
import com.yq.milk.custom.assertors.CustomAssert.textEq

/**
 * Created by king on 2018/2/23.
 */

class TheDomeAction {
    var dv = TheDomeView()

    fun enterGeneralPage() {
        dv.generalItem.click()
    }

    fun check_element_exist() {
        dv.displayName.check(textEndsWith("name"))
    }
}
