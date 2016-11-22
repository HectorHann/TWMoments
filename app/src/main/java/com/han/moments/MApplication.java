package com.han.moments;

import android.app.Application;

import com.han.moments.imageloader.ImageLoader;

/**
 * Created by Han on 2016/11/22.
 */

public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.init(this, 720);
    }
}
