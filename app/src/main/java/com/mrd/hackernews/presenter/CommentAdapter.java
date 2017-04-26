package com.mrd.hackernews.presenter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrd.hackernews.R;
import com.mrd.hackernews.model.Item;
import com.mrd.hackernews.ui_components.HNTextView;
import com.mrd.hackernews.utils.Common;

import java.util.ArrayList;

/**
 * Created by mayurdube on 26/04/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final ArrayList<Item> comments;

    public CommentAdapter(ArrayList<Item> comments) {
        this.comments = comments;
    }


    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Item item = comments.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        HNTextView txtCommentTxt;
        HNTextView txtCommentInfo;

        public CommentViewHolder(View itemView) {
            super(itemView);
            txtCommentInfo = (HNTextView) itemView.findViewById(R.id.txt_comment_info);
            txtCommentTxt = (HNTextView) itemView.findViewById(R.id.txt_comment_title);
        }

        public void bind(Item item) {
            if(!TextUtils.isEmpty(item.getText())) {
                txtCommentTxt.setVisibility(View.VISIBLE);
                txtCommentTxt.setText(Html.fromHtml(item.getText()));
            } else {
                txtCommentTxt.setVisibility(View.GONE);
            }
            txtCommentInfo.setText(item.getBy() + " " + Common.getTimeSpan(item.getTime()));
        }
    }
}
