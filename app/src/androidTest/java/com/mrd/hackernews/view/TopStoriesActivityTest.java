package com.mrd.hackernews.view;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.mrd.hackernews.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TopStoriesActivityTest {

    @Rule
    public ActivityTestRule<TopStoriesActivity> mActivityTestRule = new ActivityTestRule<>(TopStoriesActivity.class);

    @Test
    public void topStoriesActivityTest() {

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rc_news),
                        withParent(allOf(withId(R.id.swiperefresh),
                                withParent(withId(R.id.activity_main)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));


        ViewInteraction imageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        imageButton.perform(click());



    }

}
