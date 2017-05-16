package com.mrd.hackernews;

import android.content.Context;

import com.mrd.hackernews.data.Item;
import com.mrd.hackernews.data.network.HackerNewsService;
import com.mrd.hackernews.top_stories.TopStoriesContract;
import com.mrd.hackernews.top_stories.TopStoriesPresenter;
import com.mrd.hackernews.utils.Common;
import com.mrd.hackernews.utils.schedulers.BaseSchedulerProvider;
import com.mrd.hackernews.utils.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mayurdube on 12/05/17.
 */

public class TopStoriesPresenterTest {

    @Mock
    HackerNewsService mHackerNewService;

    @Mock
    TopStoriesContract.View mTopStoriesView;

    @Mock
    Context mContext;

    TopStoriesPresenter mPresenter;
    BaseSchedulerProvider mScheduleProvider;

    @Before
    public void setupMocksAndView() {
        MockitoAnnotations.initMocks(this);
        when(mTopStoriesView.isVisible()).thenReturn(true);
        mScheduleProvider = new ImmediateSchedulerProvider();
        mPresenter = new TopStoriesPresenter(mTopStoriesView,mHackerNewService, mScheduleProvider);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        mPresenter = new TopStoriesPresenter(mTopStoriesView,mHackerNewService, mScheduleProvider);

        // Then the presenter is set to the view
        verify(mTopStoriesView).setPresenter(mPresenter);
    }


    @Test
    public void testExceptionHandling() throws Exception {
        // create or mock response object
        when(mHackerNewService.getTopStoriesObservable()).thenReturn(Observable.error(new IOException()));
        mPresenter.getStories();
        verify(mTopStoriesView).showError(Mockito.any(),Mockito.anyInt());
    }

    @Test
    public void testGetStories() {
        ArrayList<Integer> newsIds = new ArrayList<>();
        newsIds.add(14338328);
        newsIds.add(14338411);
        newsIds.add(14337871);
        newsIds.add(14338328);
        newsIds.add(14338411);
        newsIds.add(14337871);
        newsIds.add(14338328);
        newsIds.add(14338411);
        newsIds.add(14337871);
        newsIds.add(14338328);
        newsIds.add(14338411);
        newsIds.add(14337871);
        newsIds.add(14338328);
        newsIds.add(14338411);
        newsIds.add(14337871);

        Item item = new Item();
        item.setId(14185313);
        item.setTitle("A Beginner's Hip Hop Playlist");
        when(mHackerNewService.getItemObservable(Mockito.anyInt())).thenReturn(Observable.just(item));
        when(mHackerNewService.getTopStoriesObservable()).thenReturn(Observable.just(newsIds));
        mPresenter.getStories();
        verify(mTopStoriesView).showStories(Mockito.any());
    }

    @Test
    public void testRefreshStories() {
        ArrayList<Integer> newsIds = new ArrayList<>();
        newsIds.add(14338328);
        newsIds.add(14338411);
        newsIds.add(14337871);
        newsIds.add(14338328);
        newsIds.add(14338411);
        newsIds.add(14337871);
        newsIds.add(14338328);
        newsIds.add(14338411);
        newsIds.add(14337871);
        newsIds.add(14338328);
        newsIds.add(14338411);
        newsIds.add(14337871);
        newsIds.add(14338328);
        newsIds.add(14338411);
        newsIds.add(14337871);

        Item item = new Item();
        item.setId(14185313);
        item.setTitle("A Beginner's Hip Hop Playlist");
        when(mHackerNewService.getItemObservable(Mockito.anyInt())).thenReturn(Observable.just(item));
        when(mHackerNewService.getTopStoriesObservable()).thenReturn(Observable.just(newsIds));
        mPresenter.refreshStories();
        verify(mTopStoriesView).showStories(Mockito.any());
    }


}
