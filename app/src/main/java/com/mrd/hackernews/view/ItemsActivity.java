package com.mrd.hackernews.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.mrd.hackernews.R;
import com.mrd.hackernews.model.Item;
import com.mrd.hackernews.model.network.HackerNewsService;
import com.mrd.hackernews.presenter.CommentAdapter;
import com.mrd.hackernews.utils.Common;

import java.util.ArrayList;

import io.reactivex.Observable;

public class ItemsActivity extends AppCompatActivity {

    HackerNewsService hackerNewsService;
    Item newsItem;
    ArrayList<Item> commentItems = new ArrayList<>();
    RecyclerView rcComments;
    CommentAdapter commentAdapter;
    ProgressDialog progressDialog;

    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        hackerNewsService = HackerNewsService.getInstance(getApplicationContext());
        if (newsItem == null) {
            newsItem = (Item) getIntent().getSerializableExtra("item");
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(newsItem.getTitle());
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        initUI();

        if (!isLoading) {
            getCommentsforItem(newsItem);
        } else {
            progressDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        rcComments = (RecyclerView) findViewById(R.id.rc_comments);
        rcComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentAdapter = new CommentAdapter(commentItems);
        rcComments.setAdapter(commentAdapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_comments));
    }

    /***
     * This function will iterate on all kid items, fetch them one by one and add the to the commentlist
     *
     * @param newsItem The news items for which the comment have to be fetched
     */
    private void getCommentsforItem(Item newsItem) {
        progressDialog.show();
        if (Common.isNetworkConnected(getApplicationContext())) {
            Observable.fromArray(newsItem.getKids())
                    .flatMapIterable(list -> list)
                    .flatMap(id -> hackerNewsService.getItemObservable(id))
                    .subscribe(item -> {
                        commentItems.add(item);
                    }, throwable -> {
                        showError(throwable);
                        progressDialog.dismiss();
                    }, () -> {
                        commentAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    });
        } else {
            showError(new Throwable(getString(R.string.no_internet_msg)));
        }
    }

    private void showError(Throwable throwable) {
        if (throwable != null) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.activity_main),
                            throwable.getMessage(),
                            Snackbar.LENGTH_LONG)
                    .setAction("RETRY",
                            view -> getCommentsforItem(newsItem));

            snackbar.show();
        }
    }


}
