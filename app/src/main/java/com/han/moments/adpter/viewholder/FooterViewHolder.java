package com.han.moments.adpter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.han.moments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Han on 2016/11/23.
 */


public class FooterViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.layout_more)
    public LinearLayout mLoadmore_layout;
    @BindView(R.id.layout_nomore)
    public RelativeLayout mLoadnomore_layout;
    @BindView(R.id.layout_loading)
    public LinearLayout mLoading_layout;

    public FooterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

