package com.mrd.hackernews.comments;

import android.support.annotation.NonNull;

import com.mrd.hackernews.data.Item;
import com.mrd.hackernews.data.network.HackerNewsService;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mayurdube on 12/05/17.
 */

public class CommentsPresenter implements CommentsContract.Presenter {

    @NonNull
    HackerNewsService mHackerNewsService;
    @NonNull
    CommentsContract.View mCommentView;
    @NonNull
    Item mItem;
    CompositeDisposable mDisposables = new CompositeDisposable();

    public CommentsPresenter(@NonNull HackerNewsService hackerNewsService,
                             @NonNull CommentsContract.View commentsView,
                             @NonNull Item item) {
        mHackerNewsService = checkNotNull(hackerNewsService);
        mCommentView = checkNotNull(commentsView);
        mItem = checkNotNull(item);
    }

    @Override
    public void getComments(Item newsItem) {
        ArrayList<Item> commentItems = new ArrayList<>();
        mDisposables.add(Observable.fromArray(newsItem.getKids())
                .flatMapIterable(list -> list)
                .flatMap(id -> mHackerNewsService.getItemObservable(id))
                .subscribe(item -> {
                    commentItems.add(item);
                }, throwable -> {
                    mCommentView.showError(throwable);
                    mCommentView.setLoadingIndicator(false);
                }, () -> {
                    mCommentView.showComments(commentItems);
                    mCommentView.setLoadingIndicator(false);
                }));
    }


    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mDisposables.dispose();
    }
}
