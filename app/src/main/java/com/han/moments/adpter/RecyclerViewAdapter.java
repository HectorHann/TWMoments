package com.han.moments.adpter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.han.moments.R;
import com.han.moments.adpter.viewholder.FooterViewHolder;
import com.han.moments.adpter.viewholder.HeardViewHolder;
import com.han.moments.adpter.viewholder.TweetItemViewHolder;
import com.han.moments.entity.CommentsDTO;
import com.han.moments.entity.ImagesDTO;
import com.han.moments.entity.TweetsDTO;
import com.han.moments.entity.UserInfoDTO;
import com.han.moments.imageloader.ImageLoader;
import com.han.moments.utils.ScreenUtil;

import java.util.List;

/**
 * Created by Han on 2016/11/22.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    LayoutInflater mInflater;
    List<TweetsDTO> mDatas;
    UserInfoDTO mUser;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_HEADER = 2;

    public static final int LOAD_FINISH = 0;
    public static final int LOADING = 1;
    public static final int LOAD_NO_MORE = 2;

    private int mLoadMoreStatus = LOAD_FINISH;


    public RecyclerViewAdapter(Context context, List<TweetsDTO> datas, UserInfoDTO user) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mUser = user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View itemView = mInflater.inflate(R.layout.recylerview_item, parent, false);
            return new TweetItemViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = mInflater.inflate(R.layout.load_more_view, parent, false);

            return new FooterViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = mInflater.inflate(R.layout.head_view, parent, false);
            return new HeardViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Log.i(this.getClass().getSimpleName(), "hoder = " + holder.toString());
        if (holder instanceof TweetItemViewHolder) {
            TweetItemViewHolder tweetItemViewHolder = (TweetItemViewHolder) holder;
            TweetsDTO tweetsDTO = mDatas.get(position - 1);
            tweetItemViewHolder.mSenderHead.setTag(tweetsDTO.getSender().getAvatar());
            tweetItemViewHolder.mSenderName.setText(tweetsDTO.getSender().getDisplayName());
            tweetItemViewHolder.mSenderContent.setText(tweetsDTO.getContent());
            ImageLoader.getInstance().load(tweetItemViewHolder.mSenderHead, tweetsDTO.getSender().getAvatar());

            List<ImagesDTO> imagesDTOs = tweetsDTO.getImages();
            if (imagesDTOs != null && !imagesDTOs.isEmpty()) {
                ViewGroup.LayoutParams gridParams = tweetItemViewHolder.mImageGrid.getLayoutParams();
                gridParams.width = (int) (ScreenUtil.getScreenWidth(mContext) * 0.85);
                int itemwidth = (gridParams.width - ScreenUtil.dip2px(mContext, 2) * 6) / 3;
                gridParams.height = (int) ((itemwidth + ScreenUtil.dip2px(mContext, 2) * 2) * Math.ceil((double) tweetsDTO.getImages().size() / 3.0));

                tweetItemViewHolder.mImageGrid.setLayoutParams(gridParams);
                tweetItemViewHolder.mImageGrid.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
                tweetItemViewHolder.mImageGrid.setAdapter(new GridViewAdapter(tweetsDTO.getImages(), mContext, itemwidth));
                tweetItemViewHolder.mImageGrid.setTag(tweetsDTO.toString());
            }
            List<CommentsDTO> commentsDTOs = tweetsDTO.getComments();
            if (commentsDTOs != null && !commentsDTOs.isEmpty()) {
                tweetItemViewHolder.mCommentLayout.removeAllViews();
                for (CommentsDTO item : commentsDTOs) {
                    View commentview = mInflater.inflate(R.layout.comment_item, null);
                    TextView tv_comment = (TextView) commentview.findViewById(R.id.tv_comment);
                    mixCommentContent(tv_comment, item.getSender().getDisplayName(), item.getContent());
                    tweetItemViewHolder.mCommentLayout.addView(commentview);
                }
            }
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (mLoadMoreStatus) {
                case LOAD_FINISH:
                    footerViewHolder.mLoadmore_layout.setVisibility(View.GONE);
                    break;
                case LOADING:
                    footerViewHolder.mLoadmore_layout.setVisibility(View.VISIBLE);
                    footerViewHolder.mLoading_layout.setVisibility(View.VISIBLE);
                    footerViewHolder.mLoadnomore_layout.setVisibility(View.GONE);
                    break;
                case LOAD_NO_MORE:
                    footerViewHolder.mLoadmore_layout.setVisibility(View.VISIBLE);
                    footerViewHolder.mLoading_layout.setVisibility(View.GONE);
                    footerViewHolder.mLoadnomore_layout.setVisibility(View.VISIBLE);
                    break;

            }
        } else if (holder instanceof HeardViewHolder) {
            if (mUser == null) {
                return;
            }
            HeardViewHolder heardViewHolder = (HeardViewHolder) holder;
            heardViewHolder.mUserHeader.setTag(mUser.getAvatar());
            heardViewHolder.mUserProfile.setTag(mUser.getProfile_image());
            ImageLoader.getInstance().load(heardViewHolder.mUserHeader, mUser.getAvatar());
            ImageLoader.getInstance().load(heardViewHolder.mUserProfile, mUser.getProfile_image());
            heardViewHolder.mUserName.setText(mUser.getDisplayName());
        }

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (position >= mDatas.size() + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }


    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        notifyDataSetChanged();
    }

    public int getMoreStatus() {
        return mLoadMoreStatus;
    }

    public void setUserInfo(UserInfoDTO userInfo) {
        mUser = userInfo;
        notifyDataSetChanged();
    }

    private void mixCommentContent(TextView textView, String name, String content) {
        SpannableString spannableString = new SpannableString(name + ":" + content);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.username)),
                0, name.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }
}
