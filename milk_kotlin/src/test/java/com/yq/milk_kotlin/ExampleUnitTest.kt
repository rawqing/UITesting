package com.yq.milk_kotlin

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        var sub : Sub = Sub()
        sub.fristName = "li"
        sub.lastName = "si"

        print(sub.say("hello"))
    }
}