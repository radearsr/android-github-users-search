package com.radea.githubuser.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.radea.githubuser.R
import com.radea.githubuser.ui.home.MainActivity


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun assertNightMode() {
        onView(withId(R.id.action_change_theme)).check(matches(isDisplayed()))
        onView(withId(R.id.action_change_theme)).perform(click())
        activityScenario.onActivity { activity ->
            val currentNightMode = activity.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
            assertThat(currentNightMode).isEqualTo(android.content.res.Configuration.UI_MODE_NIGHT_YES)
        }
    }
}