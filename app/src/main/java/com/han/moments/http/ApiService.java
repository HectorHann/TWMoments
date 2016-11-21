package com.han.moments.http;

import com.han.moments.entity.TweetsDTO;
import com.han.moments.entity.UserInfoDTO;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiService {

    @GET("user/{name}")
    Observable<UserInfoDTO> getUserInfo(@Path("name") String name);

    @GET("user/{name}/tweets")
    Observable<List<TweetsDTO>> getUserTweets(@Path("name") String name);


}
