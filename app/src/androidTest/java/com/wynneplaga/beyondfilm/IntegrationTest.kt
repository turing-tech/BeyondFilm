package com.wynneplaga.beyondfilm

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class BottomNavigation(@IdRes private val id: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(BottomNavigationView::class.java)
    }

    override fun getDescription(): String {
        return ""
    }

    override fun perform(uiController: UiController?, view: View?) {
        (view as? BottomNavigationView)?.menu?.performIdentifierAction(id, 0)
    }
}

@RunWith(AndroidJUnit4::class)
@LargeTest
class HelloWorldEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test fun favorites() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.filmsRecyclerView)).perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText("The Shawshank Redemption"))))
        Espresso.onView(withText("The Shawshank Redemption")).perform(click())
        Espresso.onView(withId(R.id.favoriteButton)).perform(click())
        Thread.sleep(500)
        Espresso.pressBack()
        Espresso.onView(withId(R.id.nav_view)).perform(BottomNavigation(R.id.navigation_favorites))
        Espresso.onView(withText("The Shawshank Redemption")).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.nav_view)).perform(BottomNavigation(R.id.navigation_home))
        Espresso.onView(withId(R.id.filmsRecyclerView)).perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText("The Shawshank Redemption"))))
        Espresso.onView(withText("The Shawshank Redemption")).perform(click())
        Espresso.onView(withId(R.id.favoriteButton)).perform(click())
        Thread.sleep(500)
        Espresso.pressBack()
        Espresso.onView(withId(R.id.nav_view)).perform(BottomNavigation(R.id.navigation_favorites))
        Espresso.onView(withText("The Shawshank Redemption")).check(doesNotExist())
    }
}