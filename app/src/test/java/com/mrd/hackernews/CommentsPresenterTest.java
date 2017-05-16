package com.mrd.hackernews;

import android.content.Context;

import com.mrd.hackernews.comments.CommentsContract;
import com.mrd.hackernews.comments.CommentsPresenter;
import com.mrd.hackernews.data.Item;
import com.mrd.hackernews.data.network.HackerNewsService;
import com.mrd.hackernews.top_stories.TopStoriesContract;
import com.mrd.hackernews.top_stories.TopStoriesPresenter;
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
import io.reactivex.internal.operators.observable.ObservableError;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mayurdube on 15/05/17.
 */

public class CommentsPresenterTest {

    @Mock
    HackerNewsService mHackerNewService;

    @Mock
    CommentsContract.View mCommentsView;

    @Mock
    Context mContext;

    CommentsPresenter mPresenter;
    BaseSchedulerProvider mScheduleProvider;
    Item mItem;
    ArrayList<Item> mListOfComments;

    @Before
    public void setupMocksAndView() {
        MockitoAnnotations.initMocks(this);
        mScheduleProvider = new ImmediateSchedulerProvider();
        mItem = new Item();
        mItem.setId(121003);
        ArrayList<Integer> kids = new ArrayList<>();
        kids.add(121016);
        kids.add(121109);
        kids.add(121168);
        mItem.setKids(kids);
        mItem.setText("<i>or</i> HN: the Next Iteration<p>I get the impression that with Arc being released a lot of people who never had time for HN before are suddenly dropping in more often. (PG: what are the numbers on this? I'm envisioning a spike.)<p>Not to say that isn't great, but I'm wary of Diggification. Between links comparing programming to sex and a flurry of gratuitous, ostentatious  adjectives in the headlines it's a bit concerning.<p>80% of the stuff that makes the front page is still pretty awesome, but what's in place to keep the signal/noise ratio high? Does the HN model still work as the community scales? What's in store for (++ HN)?");
        mItem.setTitle("A Beginner's Hip Hop Playlist");
        mPresenter = new CommentsPresenter(mCommentsView,mHackerNewService, mScheduleProvider,mItem);
        mListOfComments = new ArrayList<>();
        mListOfComments.add(mItem);
    }


    @Test
    public void testShowComments() {
        when(mHackerNewService.getItemObservable(Mockito.anyInt())).thenReturn(Observable.just(mItem));
        mPresenter.getComments(mItem);
        verify(mCommentsView).showComments(Mockito.anyList());
    }

    @Test
    public void testShowError() {
        when(mHackerNewService.getItemObservable(Mockito.anyInt())).thenReturn(Observable.error(new IOException()));
        mPresenter.getComments(mItem);
        verify(mCommentsView).showError(Mockito.any());
    }

}
