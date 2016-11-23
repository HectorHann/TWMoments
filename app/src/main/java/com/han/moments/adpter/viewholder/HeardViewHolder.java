package com.han.moments.adpter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.han.moments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Han on 2016/11/23.
 */

public class HeardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_user_head)
    public ImageView mUserHeader;
    @BindView(R.id.iv_user_profile)
    public ImageView mUserProfile;
    @BindView(R.id.tv_user_name)
    public TextView mUserName;

    public HeardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

