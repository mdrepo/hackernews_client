package com.mrd.hackernews.presenter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mrd.hackernews.R;
import com.mrd.hackernews.model.Item;
import com.mrd.hackernews.ui_components.HNTextView;
import com.mrd.hackernews.utils.Common;
import com.mrd.hackernews.utils.OnItemClickListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mayurdube on 25/04/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int LOAD_MORE = -1;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_PROGRESS = 0;
    private ArrayList<Item> items;
    private OnItemClickListener listener;
    private boolean isFooterEnabled = false;

    public NewsAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news, parent, false);
            vh = new NewsViewHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progressbar, parent, false);

            vh = new NewsProgress(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolder) {
            Item item = items.get(position);
            NewsViewHolder vh = (NewsViewHolder) holder;
            vh.bind(item);
            if (position == items.size() - 1) {
                listener.onItemClicked(LOAD_MORE, null);
            }
        }

    }

    public void enableFooter(boolean status) {
        isFooterEnabled = status;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : isFooterEnabled ? items.size() + 1 : items.size();
    }

    public void setNews(ArrayList<Item> news) {
        this.items = news;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        return isFooterEnabled && position == items.size() ? VIEW_TYPE_PROGRESS : VIEW_TYPE_ITEM;
    }

    public class NewsProgress extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public NewsProgress(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private HNTextView txtItemTitle;
        private HNTextView txtItemInfo;
        private HNTextView txtItemSource;

        public NewsViewHolder(View itemView) {
            super(itemView);
            txtItemTitle = (HNTextView) itemView.findViewById(R.id.txt_item_title);
            txtItemInfo = (HNTextView) itemView.findViewById(R.id.txt_item_info);
            txtItemSource = (HNTextView) itemView.findViewById(R.id.txt_item_source);
            itemView.setOnClickListener(this);
        }

        public void bind(Item item) {
            if (item != null && !TextUtils.isEmpty(item.getTitle())) {
                txtItemTitle.setText(getAdapterPosition() + 1 + ". " + item.getTitle());

                try {
                    URL url = new URL(item.getUrl());
                    txtItemSource.setText("(" + url.getHost().replaceAll("www.", "") + ")");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    txtItemSource.setVisibility(View.GONE);
                }
                String infoString = item.getScore() + " points | by "
                        + item.getBy() + " | " + Common.getTimeSpan(item.getTime())
                        + " | " + item.getDescendants() + " comments";
                txtItemInfo.setText(infoString);
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                Item item = items.get(getAdapterPosition());
                listener.onItemClicked(getAdapterPosition(), item);
            }
        }
    }
}
