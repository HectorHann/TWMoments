package com.han.moments.adpter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.han.moments.R;
import com.han.moments.entity.ImagesDTO;
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

    private int mScreenWidth;


    public RefreshAdapter(Context context, List<TweetsDTO> datas, UserInfoDTO user) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mUser = user;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        mScreenWidth = point.x;
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
            if (tweetsDTO.isUsefulTweet()) {
                tweetItemViewHolder.mSenderHead.setTag(tweetsDTO.getSender().getAvatar());
                tweetItemViewHolder.mSenderName.setText(tweetsDTO.getSender().getDisplayName());
                tweetItemViewHolder.mSenderContent.setText(tweetsDTO.getContent());
                ImageLoader.getInstance().load(tweetItemViewHolder.mSenderHead, tweetsDTO.getSender().getAvatar());

                if (tweetsDTO.getImages() != null && !tweetsDTO.getImages().isEmpty()) {
                    ViewGroup.LayoutParams gridParams = tweetItemViewHolder.mImageGrid.getLayoutParams();
                    gridParams.width = (int) (mScreenWidth * 0.85);
                    gridParams.height = (int) ((gridParams.width / 3) * Math.ceil((double) tweetsDTO.getImages().size() / 3.0));
                    tweetItemViewHolder.mImageGrid.setLayoutParams(gridParams);
                    tweetItemViewHolder.mImageGrid.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
                    tweetItemViewHolder.mImageGrid.setAdapter(new HorizontalAdapter(tweetsDTO.getImages()));
                    tweetItemViewHolder.mImageGrid.setTag(tweetsDTO.toString());
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

    public class TweetItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_sender_head)
        ImageView mSenderHead;
        @BindView(R.id.tv_sender_name)
        TextView mSenderName;
        @BindView(R.id.tv_sender_content)
        TextView mSenderContent;
        @BindView(R.id.rv_image_grid)
        RecyclerView mImageGrid;
        @BindView(R.id.layout_tweet_content)
        LinearLayout mTweetContentLayout;

        public TweetItemViewHolder(View itemView) {
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


    public class HorizontalAdapter extends RecyclerView.Adapter {

        private List<ImagesDTO> mImageUrllist;


        public HorizontalAdapter(List<ImagesDTO> images) {
            mImageUrllist = images;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.image_item, parent, false);
            return new ImageItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageItemViewHolder imageItemViewHolder = (ImageItemViewHolder) holder;
            String url = mImageUrllist.get(position).getUrl();
            imageItemViewHolder.mimageitem.setTag(url);
            ImageLoader.getInstance().load(imageItemViewHolder.mimageitem, url);
        }


        @Override
        public int getItemCount() {
            return mImageUrllist == null ? 0 : mImageUrllist.size();
        }

        public class ImageItemViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_imageview_item)
            ImageView mimageitem;

            public ImageItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                ViewGroup.LayoutParams layoutParams = mimageitem.getLayoutParams();
                layoutParams.width = layoutParams.height = (int) (mScreenWidth * 0.7 /3);
                mimageitem.setLayoutParams(layoutParams);
            }
        }
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
}
