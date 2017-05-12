package com.mrd.hackernews.top_stories;

import com.mrd.hackernews.data.Item;
import com.mrd.hackernews.utils.BasePresenter;
import com.mrd.hackernews.utils.BaseView;

import java.util.ArrayList;

/**
 * Created by mayurdube on 11/05/17.
 */

public class TopStoriesContract {

    interface View extends BaseView<Presenter> {

        void showStories(ArrayList<Item> items);
        void showNoStoriesError();
        void showError(Throwable throwable, int topStories);
        void showMessage(String message);
        void setProgressDialogState(boolean status);
        boolean isListVisible();

        void setEndlessIndicator(boolean state);
    }

    interface Presenter extends BasePresenter {

        void getStories();
        void refreshStories();
        void getStoriesInfo();

    }
}
