package com.example.githubuser.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.githubuser.R
import org.junit.*

class MainActivityTest{
    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun checkRecyclerView(){
        onView(withId(R.id.rvUser)).check(matches(isDisplayed()))
    }

    @Test
    fun checkDarkMode(){
        onView(withId(R.id.dark_mode)).check(matches(isDisplayed()))
        onView(withId(R.id.dark_mode)).perform(click())
    }
}