package com.dicoding.soccer.utilities

import androidx.test.espresso.IdlingResource

object PermenEspresso {
    private const val RESOURCE = "GLOBAL"
    private val countIdlingResource = CountingIdlingSource(RESOURCE)
    val idlingResource: IdlingResource get() = countIdlingResource

    fun increase(){
        countIdlingResource.increment()
    }

    fun decrease(){
        countIdlingResource.decrement()
    }
}