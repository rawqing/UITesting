package com.yq.milk_kotlin

/**
 * Created by king on 2018/2/23.
 */
class Sub() : Super() {

    var fristName: String? = null
        get() = this.fristName

    var lastName: String? = null
    override lateinit var name: String

    init {
        name = add()
    }
    fun add(): String {
        return fristName + lastName
    }
//    override fun say(s: String): Super {
//        print("hello world")
//        return this
//    }
}