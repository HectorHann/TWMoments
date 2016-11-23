package com.han.moments.adpter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.han.moments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Han on 2016/11/23.
 */


public class TweetItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_sender_head)
    public ImageView mSenderHead;
    @BindView(R.id.tv_sender_name)
    public TextView mSenderName;
    @BindView(R.id.tv_sender_content)
    public TextView mSenderContent;
    @BindView(R.id.rv_image_grid)
    public RecyclerView mImageGrid;
    @BindView(R.id.layout_tweet_content)
    public LinearLayout mTweetContentLayout;

    public TweetItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}