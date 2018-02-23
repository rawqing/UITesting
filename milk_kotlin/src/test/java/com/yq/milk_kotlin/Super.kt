package com.yq.milk_kotlin

/**
 * Created by king on 2018/2/23.
 */
abstract class Super {
    val age = 19
    abstract var name : String

    open fun say(s: String): Super{
        print("$s $name $age")
        return this
    }
}