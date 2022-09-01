package com.anne.linger.go4lunch;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.espresso.contrib.DrawerActions;

import ui.activities.PlacesActivity;

/**
*Instrumented tests for Go4Lunch
*/

@RunWith(JUnit4.class)
public class MainInstrumentedTest {

    @Rule
    public ActivityTestRule<PlacesActivity> mRule = new ActivityTestRule<>(PlacesActivity.class);
    private PlacesActivity mPlacesActivity;

    @Before
    public void init() {
        mPlacesActivity = mRule.getActivity();
    }

    //Test map fragment display
    @Test
    public void checkIfMapFragmentIsDisplayedWhenLaunchingApp() {
        onView(withId(R.id.activity_places_frame_layout)).check(matches(isDisplayed()));
    }

    //Test the bottom navigation
}
