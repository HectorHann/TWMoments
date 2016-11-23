package com.han.moments.adpter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.han.moments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Han on 2016/11/23.
 */

public class CommentItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_comment)
    public TextView mComment;

    public CommentItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}