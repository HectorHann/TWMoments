package com.han.moments.http;

import com.han.moments.entity.TweetsDTO;
import com.han.moments.entity.UserInfoDTO;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukun on 16/3/9.
 */
public class HttpTools {

    public static final String BASE_URL = "http://thoughtworks-ios.herokuapp.com/";
    private static final int TIME_OUT = 10 * 1000;
    private Retrofit mRetrofit;
    private ApiService mApiService;
    private OkHttpClient.Builder mOkBuilder;

    private HttpTools() {
        mOkBuilder = new OkHttpClient.Builder();
        mOkBuilder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(mOkBuilder.build())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mApiService = mRetrofit.create(ApiService.class);
    }

    private static class SingletonHolder {
        private static final HttpTools INSTANCE = new HttpTools();
    }

    public static HttpTools getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getUserInfo(Subscriber<UserInfoDTO> subscriber, String name) {

        Observable observable = mApiService.getUserInfo(name);

        toSubscribe(observable, subscriber);
    }

    public void getUserTweets(Subscriber<List<TweetsDTO>> subscriber, String name) {

        Observable observable = mApiService.getUserTweets(name);

        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

}
