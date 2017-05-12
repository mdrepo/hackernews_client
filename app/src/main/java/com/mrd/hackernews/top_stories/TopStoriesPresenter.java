package com.mrd.hackernews.top_stories;

import android.support.annotation.NonNull;


import com.mrd.hackernews.data.Item;
import com.mrd.hackernews.data.network.HackerNewsService;
import com.mrd.hackernews.utils.Common;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mayurdube on 11/05/17.
 */

public class TopStoriesPresenter implements TopStoriesContract.Presenter {

    public static final int TOP_STORIES = 1;
    private static final int GET_ITEMS = 2;

    public static final int NEWS_COUNT = 10;

    int currentPage = 1;
    ArrayList<Integer> mNewsIds = new ArrayList<>();


    private final CompositeDisposable disposables = new CompositeDisposable();

    @NonNull
    private TopStoriesContract.View mTopstoriesView;
    @NonNull
    private HackerNewsService mHackerNewsService;


    public TopStoriesPresenter(@NonNull HackerNewsService hackerNewsService,
                               @NonNull TopStoriesContract.View topstoriesView) {
        mTopstoriesView = checkNotNull(topstoriesView);
        mTopstoriesView.setPresenter(this);
        mHackerNewsService = checkNotNull(hackerNewsService);
    }

    @Override
    public void getStories() {

        mTopstoriesView.setEndlessIndicator(true);
        disposables.add(mHackerNewsService
                .getTopStoriesObservable()
                .subscribe(
                        this::setNewsIds
                        , throwable -> {
                            mTopstoriesView.showError(throwable, TOP_STORIES);
                            mTopstoriesView.setEndlessIndicator(false);
                        },
                        () -> {
                            mTopstoriesView.setProgressDialogState(false);
                        }));
    }

    @Override
    public void refreshStories() {
        currentPage = 1;
        getStories();
    }

    private void setNewsIds(ArrayList<Integer> newsIds) {
        this.mNewsIds = newsIds;
        getStoriesInfo();
    }
    @Override
    public void getStoriesInfo() {
        int end = currentPage * NEWS_COUNT;
        int start = end - NEWS_COUNT;
        ArrayList<Item> newsItems = new ArrayList<>();

        disposables.add(Observable.fromArray(mNewsIds.subList(start, end))
                .flatMapIterable(list -> list)
                .concatMap(id -> mHackerNewsService.getItemObservable(id))
                .subscribe(item -> {
                    Common.log(item.toString());
                    newsItems.add(item);
                }, throwable -> {
                    throwable.printStackTrace();
                    mTopstoriesView.showError(throwable, GET_ITEMS);
                }, () -> {
                    mTopstoriesView.showStories(newsItems);
                    currentPage++;
                }));
    }


    @Override
    public void subscribe() {
        if(!mTopstoriesView.isListVisible()) {
            getStories();
        }
    }

    @Override
    public void unsubscribe() {
        disposables.dispose();
    }
}
