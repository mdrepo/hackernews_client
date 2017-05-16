package com.mrd.hackernews.top_stories;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.mrd.hackernews.R;
import com.mrd.hackernews.utils.AcIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TopStoriesActivityTest {

    @Rule
    public ActivityTestRule<TopStoriesActivity> mActivityTestRule = new ActivityTestRule<>(TopStoriesActivity.class);

    AcIdlingResource idlingResource;
    @Before
    public void setup() {
        TopStoriesActivity topStoriesActivity = mActivityTestRule.getActivity();
        idlingResource = new AcIdlingResource(topStoriesActivity);
        Espresso.registerIdlingResources(idlingResource);

    }
    @Test
    public void topStoriesActivityTest() {

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rc_news),
                        withParent(allOf(withId(R.id.swiperefresh),
                                withParent(withId(R.id.activity_main)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

    }

    @Test
    public void endlessScrollTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rc_news),
                        withParent(allOf(withId(R.id.swiperefresh),
                                withParent(withId(R.id.activity_main)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(2, scrollTo()));
        onView(allOf(withId(R.id.progress),isDisplayed()));
    }


    @After
    public void tearDown() {
        Espresso.unregisterIdlingResources(idlingResource);
    }

}
