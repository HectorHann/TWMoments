package com.han.moments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.han.moments.entity.TweetsDTO;
import com.han.moments.entity.UserInfoDTO;
import com.han.moments.http.HttpTools;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;


public class MainActivity1 extends AppCompatActivity {
    public static final String USER_NAME = "USER_NAME";
    private String username = "jsmith";
    private UserInfoDTO mUserInfo;
    private List<TweetsDTO> mTweetsList = new ArrayList<>();

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main1);
        imageView = (ImageView) findViewById(R.id.image);
        paraseUserName();
        getUserInfo();
        getUserTweets();
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
                Log.e(this.toString(), "Completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(this.toString(), e.toString());
            }

            @Override
            public void onNext(UserInfoDTO userInfoDTO) {
                mUserInfo = userInfoDTO;
                Log.e(this.toString(), mUserInfo.toString());
            }
        }, username);
    }


    private void getUserTweets() {
        HttpTools.getInstance().getUserTweets(new Subscriber<List<TweetsDTO>>() {
            @Override
            public void onCompleted() {
                Log.e(this.toString(), "Completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(this.toString(), e.toString());
            }

            @Override
            public void onNext(List<TweetsDTO> tweetsDTOs) {
                mTweetsList.addAll(tweetsDTOs);
                Log.e(this.toString(), mTweetsList.toString());
            }
        }, username);
    }

}
