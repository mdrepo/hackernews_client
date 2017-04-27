package com.mrd.hackernews.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mrd.hackernews.R;
import com.mrd.hackernews.model.Item;
import com.mrd.hackernews.model.network.HackerNewsService;
import com.mrd.hackernews.presenter.NewsAdapter;
import com.mrd.hackernews.utils.Common;
import com.mrd.hackernews.utils.EndlessRecyclerOnScrollListener;
import com.mrd.hackernews.utils.OnItemClickListener;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class TopStoriesActivity extends AppCompatActivity implements OnItemClickListener {

    private static final int TOP_STORIES = 1;
    private static final int GET_ITEMS = 2;

    HackerNewsService hackerNewsService;
    ArrayList<Integer> newsIds = new ArrayList<>();
    ArrayList<Item> newsItems = null;
    ProgressDialog progressDialog = null;
    int page = 1;
    public static final int NEWS_COUNT = 10;

    RecyclerView rcNews;
    NewsAdapter newsAdapter;
    private Disposable detailStoriesObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        hackerNewsService = HackerNewsService.getInstance(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        getTopStories();

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(() -> {
            page = 1;
            getDetailStories();
        });


    }

    private void initUI() {
        rcNews = (RecyclerView) findViewById(R.id.rc_news);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rcNews.setLayoutManager(manager);
        newsAdapter = new NewsAdapter(newsItems);
        newsAdapter.setListener(this);
        rcNews.setAdapter(newsAdapter);
        rcNews.addOnScrollListener(new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int current_page) {
                newsAdapter.enableFooter(true);
                newsAdapter.notifyItemInserted(newsItems.size());
                getDetailStories();
            }
        });
    }


    private void getTopStories() {
        if(Common.isNetworkConnected(getApplicationContext())) {
            hackerNewsService
                    .getTopStoriesObservable()
                    .subscribe(integers -> {
                        progressDialog.dismiss();
                        newsIds = integers;
                    }, throwable -> {
                        progressDialog.dismiss();
                        showError(TOP_STORIES, throwable);
                    }, () -> {
                        getDetailStories();
                        progressDialog.show();
                    });
        } else {
            showError(TOP_STORIES,new Throwable(getString(R.string.no_internet_msg)));
        }
    }

    private void getDetailStories() {
        int end = page * NEWS_COUNT;
        int start = end - NEWS_COUNT;
        if (page == 1) {
            newsItems = new ArrayList<>();
        }
        detailStoriesObservable = Observable.fromArray(newsIds.subList(start, end))
                .flatMapIterable(list -> list)
                .concatMap(id -> hackerNewsService.getItemObservable(id))
                .subscribe(item -> {
                    Common.log(item.toString());
                    newsItems.add(item);
                }, throwable -> {
                    throwable.printStackTrace();
                    showError(GET_ITEMS,throwable);
                }, () -> {
                    progressDialog.dismiss();
                    newsAdapter.enableFooter(false);
                    newsAdapter.setNews(newsItems);
                    if (page == 1) {
                        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    page++;

                });
    }


    private void showError(int errorFrom, Throwable throwable) {
        if (throwable != null) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.activity_items),
                            throwable.getMessage(),
                            Snackbar.LENGTH_LONG)
                    .setAction("RETRY",
                            view -> {
                                if (errorFrom == TOP_STORIES)
                                    getTopStories();
                                else getDetailStories();
                            });

            snackbar.show();
        }
    }

    @Override
    public void onItemClicked(int position, Object value) {
        Item item = (Item) value;
        if (item != null && item.getKids() != null && item.getDescendants() > 0) {
            Intent showCommentsScreen = new Intent(TopStoriesActivity.this, ItemsActivity.class);
            showCommentsScreen.putExtra("item", (Item) value);
            startActivity(showCommentsScreen);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailStoriesObservable.dispose();

    }
}
