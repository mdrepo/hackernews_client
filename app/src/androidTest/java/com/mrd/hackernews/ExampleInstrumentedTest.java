package com.mrd.hackernews;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mrd.hackernews.model.Item;
import com.mrd.hackernews.model.network.HackerNewsService;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import io.reactivex.observers.TestObserver;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        HackerNewsService service = new HackerNewsService(appContext);
        Item item = new Item();
        item.setId(14185314);
        item.setKids(Arrays.asList(14192979,14190647,14193466,14195544,14191583,14190302,14190033,14193102,14190227,14194099));
        item.setBy("zmatilsky");
        item.setScore(36);
        item.setTime(1493049379);
        item.setTitle("A Beginner's Hip Hop Playlist");
        item.setType("story");
        item.setUrl("https://gist.github.com/zmatilsky901/8485ba3c2a06c02e0a031cd52add4de6");
        item.setDescendants(26);

        TestObserver<Item> testSubscriber = new TestObserver<>();
        service.getItemObservable(item.getId()).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertComplete();
        testSubscriber.assertValue(item);

    }
}
