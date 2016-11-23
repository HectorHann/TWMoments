package com.han.moments.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.han.moments.R;
import com.han.moments.entity.TweetsDTO;
import com.han.moments.entity.UserInfoDTO;
import com.han.moments.imageloader.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 刘楠 on 2016/9/10 0010.18:06
 */
public class RefreshAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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


    public RefreshAdapter(Context context, List<TweetsDTO> datas, UserInfoDTO user) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mUser = user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View itemView = mInflater.inflate(R.layout.recylerview_item, parent, false);
            return new ItemViewHolder(itemView);
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
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            TweetsDTO tweetsDTO = mDatas.get(position -1);
            if (tweetsDTO.isUsefulTweet()) {
                itemViewHolder.mSenderHead.setTag(tweetsDTO.getSender().getAvatar());
                itemViewHolder.mSenderName.setText(tweetsDTO.getSender().getDisplayName());
                itemViewHolder.mSenderContent.setText(tweetsDTO.getContent());
                ImageLoader.getInstance().load(itemViewHolder.mSenderHead, tweetsDTO.getSender().getAvatar());
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
            if (mUser == null){
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

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_sender_head)
        ImageView mSenderHead;
        @BindView(R.id.tv_sender_name)
        TextView mSenderName;
        @BindView(R.id.tv_sender_content)
        TextView mSenderContent;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_more)
        LinearLayout mLoadmore_layout;
        @BindView(R.id.layout_nomore)
        RelativeLayout mLoadnomore_layout;
        @BindView(R.id.layout_loading)
        LinearLayout mLoading_layout;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public class HeardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user_head)
        ImageView mUserHeader;
        @BindView(R.id.iv_user_profile)
        ImageView mUserProfile;
        @BindView(R.id.tv_user_name)
        TextView mUserName;

        public HeardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        notifyDataSetChanged();
    }

    public int getMoreStatus() {
        return mLoadMoreStatus;
    }

    public void setUserInfo(UserInfoDTO userInfo){
        mUser = userInfo;
        notifyDataSetChanged();
    }
}
