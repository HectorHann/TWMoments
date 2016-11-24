package com.han.moments.provider;

import android.util.Log;

import com.han.moments.entity.TweetsDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Han-HP on 2016/11/22.
 */

public class DataProvider {
    private static final int PER_PAGER_COUNT = 5;
    private List<TweetsDTO> mTweetsList;

    private DataProvider() {
        mTweetsList = new ArrayList<>();
    }

    public static DataProvider getInstance() {
        return Holder.instance;
    }

    static class Holder {
        private static DataProvider instance = new DataProvider();
    }

    public void setTweetsList(List<TweetsDTO> tweetsList) {
        Log.i(this.getClass().getSimpleName(), "set tweets list size = " + tweetsList.size());
        mTweetsList.clear();
        mTweetsList.addAll(tweetsList);
    }

    public List<TweetsDTO> getTweetsList(int pager) {
        List<TweetsDTO> result = new ArrayList<>();
        if (pager < 0 || pager > mTweetsList.size() / PER_PAGER_COUNT) {
            Log.i(this.getClass().getSimpleName(), "no result. pager = " + pager);
            return result;
        }
        int start, end;
        start = pager * PER_PAGER_COUNT;
        end = start + PER_PAGER_COUNT;
        if (end >= mTweetsList.size()) {
            end = mTweetsList.size() - 1;
        }
        Log.i(DataProvider.class.getSimpleName(), start + "|" + end + "|" + mTweetsList.size());
        result.addAll(mTweetsList.subList(start, end));
        if (result.isEmpty()) {
            return result;
        }
        Iterator<TweetsDTO> iterator = result.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isUsefulTweet()) {
                iterator.remove();
            }
        }

        return result;
    }
}
