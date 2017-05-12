package com.mrd.hackernews;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mrd.hackernews.data.Item;
import com.mrd.hackernews.data.network.HackerNewsService;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ItemObservableInstrumentedTest {
    @Test
    public void checkItemObservable() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        HackerNewsService service = new HackerNewsService(appContext);
        Item item = new Item();
        item.setId(14185314);
        item.setTitle("A Beginner's Hip Hop Playlist");

        Item testitem = service.getItemObservable(item.getId()).blockingFirst();
        assertEquals(item.getTitle(),testitem.getTitle());

    }

    @Test
    public void checkItemObservableTestSubscriber() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        HackerNewsService service = new HackerNewsService(appContext);

        Item item = new Item();
        item.setId(14185313);
        item.setTitle("A Beginner's Hip Hop Playlist");

        TestObserver<Item> testSubscriber = new TestObserver<>();
        Observable<Item> itemObservable = service.getItemObservable(14185314);
        itemObservable.subscribe(testSubscriber);

        testSubscriber.assertSubscribed();

        itemObservable.subscribe(item1 -> {
            assertThat(item1,notNullValue());
            assertEquals(item.getTitle(),item1.getTitle());
        },throwable -> {
            fail();
        });
    }


}
