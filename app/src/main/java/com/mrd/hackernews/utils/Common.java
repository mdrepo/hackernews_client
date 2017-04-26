package com.mrd.hackernews.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import com.mrd.hackernews.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by mayurdube on 25/04/17.
 */

public class Common {

    public static class Urls {
        public static final String BaseURL = "https://hacker-news.firebaseio.com";
        public static final String GET_TOP_STORIES_API = "/v0/topstories.json";
        public static final String GET_HACKERNEWS_ITEM = "/v0/item/{itemid}.json";
    }

    public static OkHttpClient.Builder getHttpClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging)
                .readTimeout(150, TimeUnit.SECONDS)
                .connectTimeout(150, TimeUnit.SECONDS);
        return httpClient;
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static String getTimeSpan(long time) {
        String str = DateUtils
                .getRelativeTimeSpanString(time * 1000,
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS).toString();
        return str;
    }

    public static void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d("HackerNewsClient", message);
        }
    }
}
