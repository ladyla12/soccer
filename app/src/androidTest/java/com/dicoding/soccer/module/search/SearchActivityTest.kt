package com.dicoding.soccer.module.search

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.dicoding.soccer.R
import com.dicoding.soccer.utilities.PermenEspresso
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<SearchActivity> = ActivityTestRule(SearchActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(PermenEspresso.idlingResource)
    }

    @Test
    fun testSearchView() {
        onView(withId(R.id.src_bar)).perform(click())
        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("Arsenal"))
            .perform(clearText())
            .perform(typeText("Juventus"))
            .perform(clearText())
            .perform(typeText("Real Madrid"))
    }
}