package com.mrd.hackernews.top_stories;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.mrd.hackernews.R;
import com.mrd.hackernews.data.Item;
import com.mrd.hackernews.data.network.HackerNewsService;
import com.mrd.hackernews.ui_components.EndlessRecyclerOnScrollListener;
import com.mrd.hackernews.utils.OnItemClickListener;
import com.mrd.hackernews.comments.CommentsActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class TopStoriesActivity extends AppCompatActivity implements OnItemClickListener, TopStoriesContract.View {


    TopStoriesContract.Presenter mPresenter;
    TopStoriesAdapter mTopStoriesAdapter;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new TopStoriesPresenter(new HackerNewsService(getApplicationContext()), this);
        initUI();
    }

    private void initUI() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        SwipeRefreshLayout swipeRefreshLayout = ButterKnife.findById(this, R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.refreshStories();
        });
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        RecyclerView rcNews = ButterKnife.findById(this, R.id.rc_news);
        rcNews.setLayoutManager(manager);
        mTopStoriesAdapter = new TopStoriesAdapter(new ArrayList<>());
        mTopStoriesAdapter.setListener(this);
        rcNews.setAdapter(mTopStoriesAdapter);
        rcNews.addOnScrollListener(new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int current_page) {
                mTopStoriesAdapter.loadingLastPage();
                mPresenter.getStoriesInfo();
            }
        });
    }


    @Override
    public void onItemClicked(int position, Object value) {
        Item item = (Item) value;
        if (item != null && item.getKids() != null && item.getDescendants() > 0) {
            Intent showCommentsScreen = new Intent(TopStoriesActivity.this, CommentsActivity.class);
            showCommentsScreen.putExtra("item", (Item) value);
            startActivity(showCommentsScreen);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void showStories(ArrayList<Item> items) {
        mTopStoriesAdapter.enableFooter(false);
        SwipeRefreshLayout swipeRefreshLayout = ButterKnife.findById(this,R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(false);
        mTopStoriesAdapter.setNews(items);
    }

    @Override
    public void showNoStoriesError() {
        showMessage("No stories to show");
    }

    @Override
    public void showError(Throwable throwable, int errorFrom) {
        if (throwable != null) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.activity_main),
                            throwable.getMessage(),
                            Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY",
                            view -> {
                                if (errorFrom == TopStoriesPresenter.TOP_STORIES)
                                    mPresenter.getStories();
                                else mPresenter.getStoriesInfo();
                            });

            snackbar.show();
        }
    }

    @Override
    public void showMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.activity_items),
                            message,
                            Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


    @Override
    public void setProgressDialogState(boolean status) {
        if (status) mProgressDialog.show();
        else mProgressDialog.dismiss();
    }

    @Override
    public boolean isListVisible() {
        return mTopStoriesAdapter.getItemCount() > 0;
    }

    @Override
    public void setEndlessIndicator(boolean state) {
        if (state) {
            mTopStoriesAdapter.loadingLastPage();
        }
        mTopStoriesAdapter.enableFooter(state);
        SwipeRefreshLayout swipeRefreshLayout = ButterKnife.findById(this,R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void setPresenter(TopStoriesContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
