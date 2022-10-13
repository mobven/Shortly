package com.mobven.shortly

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.mobven.shortly.ui.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private var mIdlingResource: IdlingResource? = null

    @Before
    fun registerIdlingResource() {
        val activityScenario: ActivityScenario<*> =
            ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity {
            mIdlingResource = (it as MainActivity).getIdlingResource()
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @Test
    fun displayHomeTexts() {
        onView(withText(R.string.title_main)).check(matches(isDisplayed()))
        onView(withText(R.string.text_description)).check(matches(isDisplayed()))
        onView(withId(R.id.shorten_it_button)).perform(click())
        onView(withId(R.id.shorten_link_edt)).check(matches(HintMatcher().withHint("Please add a link here")))
    }

    @Test
    fun showWrongLinkPopup() {
        onView(withId(R.id.shorten_link_edt)).perform(typeText("asdfgh"))
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.shorten_it_button)).perform(click())
        onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
        onView(withText("An error occurred")).inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }


}