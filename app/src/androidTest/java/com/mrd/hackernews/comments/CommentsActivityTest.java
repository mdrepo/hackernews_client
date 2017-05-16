package com.mrd.hackernews.comments;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.TextView;

import com.mrd.hackernews.R;
import com.mrd.hackernews.data.Item;
import com.mrd.hackernews.utils.AcIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by mayurdube on 16/05/17.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CommentsActivityTest {

    @Rule
    public ActivityTestRule<CommentsActivity> commentsActivityTestRule =
            new ActivityTestRule<CommentsActivity>(CommentsActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Item item = new Item();
                    item.setId(14185314);
                    item.setTitle("A Beginner's Hip Hop Playlist");
                    ArrayList<Integer> kids = new ArrayList<>();
                    kids.add(121016);
                    kids.add(121109);
                    kids.add(121168);
                    item.setKids(kids);
                    item.setText("<i>or</i> HN: the Next Iteration<p>I get the impression that with Arc being released a lot of people who never had time for HN before are suddenly dropping in more often. (PG: what are the numbers on this? I'm envisioning a spike.)<p>Not to say that isn't great, but I'm wary of Diggification. Between links comparing programming to sex and a flurry of gratuitous, ostentatious  adjectives in the headlines it's a bit concerning.<p>80% of the stuff that makes the front page is still pretty awesome, but what's in place to keep the signal/noise ratio high? Does the HN model still work as the community scales? What's in store for (++ HN)?");
                    item.setTitle("A Beginner's Hip Hop Playlist");
                    Intent i = super.getActivityIntent();
                    i.putExtra("item", item);
                    return i;
                }
            };

    AcIdlingResource idlingResource;

    @Before
    public void setup() {
        CommentsActivity commentsActivity = commentsActivityTestRule.getActivity();
        idlingResource = new AcIdlingResource(commentsActivity);
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void commentsActivityTest() {

        onView(allOf(withId(R.id.rc_comments),
                withParent(withParent(withId(R.id.activity_items))),
                isDisplayed()));
    }

    @After
    public void tearDown() {
        Espresso.unregisterIdlingResources(idlingResource);
    }

}

