package com.han.moments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.han.moments.adpter.RefreshAdapter;
import com.han.moments.entity.TweetsDTO;
import com.han.moments.entity.UserInfoDTO;
import com.han.moments.http.HttpTools;
import com.han.moments.imageloader.ImageLoader;
import com.han.moments.provider.DataProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String USER_NAME = "USER_NAME";
    private String username = "jsmith";
    private UserInfoDTO mUserInfo;
    private List<TweetsDTO> mTweetsList = new ArrayList<>();


    @BindView(R.id.view_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout_swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    RefreshAdapter mRefreshAdapter;

    private int cur_pager = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        ImageLoader.init(this, 720);
        ButterKnife.bind(this);
        initSwipeView();
        initRecycleView();
        initListener();
        paraseUserName();
        getUserInfo();
        getUserTweets();
    }

    private void initListener() {

        initPullRefresh();

        initLoadMoreListener();

    }

    private void initRecycleView() {
        mRefreshAdapter = new RefreshAdapter(this, mTweetsList);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mRefreshAdapter);
    }

    private void initSwipeView() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void paraseUserName() {
        if (null != getIntent()) {
            username = getIntent().getStringExtra(USER_NAME);
        }
        if (TextUtils.isEmpty(username)) {
            username = "jsmith";
        }
    }

    private void getUserInfo() {
        HttpTools.getInstance().getUserInfo(new Subscriber<UserInfoDTO>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
            }

            @Override
            public void onNext(UserInfoDTO userInfoDTO) {
                Log.i(TAG, "getUserInfo finish");
                mUserInfo = userInfoDTO;
            }
        }, username);
    }


    private void getUserTweets() {
        HttpTools.getInstance().getUserTweets(new Subscriber<List<TweetsDTO>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
            }

            @Override
            public void onNext(List<TweetsDTO> tweetsDTOs) {
                Log.i(TAG, "getUserTweets finish. size = " + tweetsDTOs.size());
                DataProvider.getInstance().setTweetsList(tweetsDTOs);
                cur_pager = 0;
                mTweetsList.addAll(DataProvider.getInstance().getTweetsList(cur_pager));
                mRefreshAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, username);
    }

    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Refresh");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTweetsList.clear();
                        cur_pager = 0;
                        mTweetsList.addAll(DataProvider.getInstance().getTweetsList(cur_pager));
                        mRefreshAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });
    }

    private void initLoadMoreListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (totalItemCount < (lastVisibleItem + 1)) {
                    mRefreshAdapter.changeMoreStatus(mRefreshAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mTweetsList.addAll(DataProvider.getInstance().getTweetsList(++cur_pager));
                            mRefreshAdapter.changeMoreStatus(mRefreshAdapter.PULLUP_LOAD_MORE);
                        }
                    }, 3000);
                }
            }
        });

    }
}
