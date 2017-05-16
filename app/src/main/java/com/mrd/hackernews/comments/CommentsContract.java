package com.mrd.hackernews.comments;

import com.mrd.hackernews.data.Item;
import com.mrd.hackernews.utils.BasePresenter;
import com.mrd.hackernews.utils.BaseView;

import java.util.List;

/**
 * Created by mayurdube on 12/05/17.
 */

public class CommentsContract {

    public interface View extends BaseView<Presenter> {
        public void showComments(List<Item> comments);
        public void showError(Throwable throwable);
        void setLoadingIndicator(boolean state);
    }

    interface Presenter extends BasePresenter {
        public void getComments(Item newsItem);
    }
}
