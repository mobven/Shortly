package com.mobven.shortly

import android.view.View
import android.widget.EditText
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`


class HintMatcher {
    fun withHint(substring: String?): Matcher<View?>? {
        return withHint(`is`(substring))
    }

    private fun withHint(stringMatcher: Matcher<String?>): Matcher<View?>? {
        return object : BoundedMatcher<View?, EditText>(EditText::class.java) {
            override fun matchesSafely(view: EditText): Boolean {
                val hint: CharSequence = view.hint
                return stringMatcher.matches(hint.toString())
            }

            override fun describeTo(description: Description) {
                description.appendText("with hint: ")
                stringMatcher.describeTo(description)
            }
        }
    }
}