package com.han.moments.adpter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.han.moments.R;
import com.han.moments.adpter.viewholder.CommentItemViewHolder;
import com.han.moments.entity.CommentsDTO;

import java.util.List;

/**
 * Created by Han on 2016/11/23.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentItemViewHolder> {
    private final LayoutInflater mInflater;
    private List<CommentsDTO> mCommentList;
    private Context mContext;


    public CommentAdapter(List<CommentsDTO> commentlist, Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mCommentList = commentlist;
    }


    @Override
    public CommentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.comment_item, parent, false);
        return new CommentItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentItemViewHolder holder, int position) {
        CommentsDTO commentsDTO = mCommentList.get(position);
        String name = commentsDTO.getSender().getDisplayName();
        String content = commentsDTO.getContent();
        mixCommentContent(holder.mComment, name, content);
    }


    @Override
    public int getItemCount() {
        return mCommentList == null ? 0 : mCommentList.size();
    }

    private void mixCommentContent(TextView textView, String name, String content) {
        SpannableString spannableString = new SpannableString(name + ":" + content);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED),
                0, name.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }
}
