package com.mrd.hackernews.comments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.mrd.hackernews.R;
import com.mrd.hackernews.data.Item;
import com.mrd.hackernews.utils.ActivityIdlingResource;
import com.mrd.hackernews.utils.Injection;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CommentsActivity extends AppCompatActivity implements CommentsContract.View,
        ActivityIdlingResource {

    CommentAdapter mCommentAdapter;
    ProgressDialog progressDialog;
    CommentsContract.Presenter mPresenter;

    boolean isLoading = false;
    private Item newsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        if (newsItem == null) {
            newsItem = (Item) checkNotNull(getIntent().getSerializableExtra("item"));
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(newsItem.getTitle());
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        initUI();
        mPresenter = new CommentsPresenter(this,
                Injection.provideHackernewsService(getApplicationContext()),
                Injection.provideSchedulerProvider(),
                newsItem);

        if (!isLoading) {
            mPresenter.getComments(newsItem);
        }
        setLoadingIndicator(true);
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
        RecyclerView rcComments = (RecyclerView) findViewById(R.id.rc_comments);
        rcComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCommentAdapter = new CommentAdapter(new ArrayList<>());
        rcComments.setAdapter(mCommentAdapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_comments));
    }


    @Override
    public void showComments(List<Item> comments) {
        mCommentAdapter.setComments(comments);
        isLoading = false;
    }

    @Override
    public void showError(Throwable throwable) {
        if (throwable != null) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.activity_items),
                            throwable.getMessage(),
                            Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY",
                            view -> mPresenter.getComments(newsItem));

            snackbar.show();
        }
    }

    @Override
    public void setLoadingIndicator(boolean state) {
        if (state) progressDialog.show();
        else progressDialog.dismiss();
    }


    @Override
    public void setPresenter(CommentsContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public boolean isIdle() {
        return !isLoading;
    }
}
