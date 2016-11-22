package com.han.moments.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.han.moments.R;
import com.han.moments.entity.TweetsDTO;
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
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public static final int PULLUP_LOAD_MORE = 0;
    public static final int LOADING_MORE = 1;
    public static final int NO_LOAD_MORE = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;


    public RefreshAdapter(Context context, List<TweetsDTO> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View itemView = mInflater.inflate(R.layout.recylerview_item, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = mInflater.inflate(R.layout.load_more_view, parent, false);

            return new FooterViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            TweetsDTO tweetsDTO = mDatas.get(position);
            if (tweetsDTO.isUsefulTweet()) {
                itemViewHolder.mSenderHead.setTag(tweetsDTO.getSender().getAvatar());
                itemViewHolder.mSenderName.setText(tweetsDTO.getSender().getDisplayName());
                itemViewHolder.mSenderContent.setText(tweetsDTO.getContent());
                ImageLoader.getInstance().load(itemViewHolder.mSenderHead, tweetsDTO.getSender().getAvatar());
            }

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    footerViewHolder.mTvLoadText.setText("refresh ...");
                    break;
                case LOADING_MORE:
                    footerViewHolder.mTvLoadText.setText("loading ...");
                    break;
                case NO_LOAD_MORE:
                    footerViewHolder.mLoadLayout.setVisibility(View.GONE);
                    break;

            }
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() + 1) {
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
        @BindView(R.id.pbLoad)
        ProgressBar mPbLoad;
        @BindView(R.id.tvLoadText)
        TextView mTvLoadText;
        @BindView(R.id.loadLayout)
        LinearLayout mLoadLayout;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        notifyDataSetChanged();
    }
}
