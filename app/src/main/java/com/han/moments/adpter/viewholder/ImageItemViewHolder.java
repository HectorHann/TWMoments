package com.han.moments.adpter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.han.moments.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Han on 2016/11/23.
 */

public class ImageItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_imageview_item)
    public ImageView mimageitem;

    public ImageItemViewHolder(View itemView, int width) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        ViewGroup.LayoutParams layoutParams = mimageitem.getLayoutParams();
        layoutParams.width = layoutParams.height = width;
        mimageitem.setLayoutParams(layoutParams);
    }
}