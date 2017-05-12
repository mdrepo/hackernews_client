package com.mrd.hackernews.data.network;

import android.content.Context;

import com.mrd.hackernews.data.Item;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import com.mrd.hackernews.utils.Common;

/**
 * Created by mayurdube on 25/04/17.
 * Controller class for fetching and returning data from HackerNews Webservices
 */

public class HackerNewsService {

    private static HackerNewsService sHackerNewsService;
    private final Retrofit retrofit;
    private HackerNewsInterface orderInterFace;

    public HackerNewsService(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Common.Urls.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(Common.getHttpClient(context).build())
                .build();
        orderInterFace = retrofit.create(HackerNewsInterface.class);
    }

    public Observable<Item> getItemObservable(int id) {
        return getInterface()
                .getItem(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<Integer>> getTopStoriesObservable() {
        return getInterface().getTopNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static HackerNewsService getInstance(Context context) {
        if (sHackerNewsService == null) {
            sHackerNewsService = new HackerNewsService(context);
        }
        return sHackerNewsService;
    }

    public HackerNewsInterface getInterface() {
        return orderInterFace;
    }


    public interface HackerNewsInterface {
        @GET(Common.Urls.GET_TOP_STORIES_API)
        public Observable<ArrayList<Integer>> getTopNews();

        @GET(Common.Urls.GET_HACKERNEWS_ITEM)
        public Observable<Item> getItem(@Path("itemid") int itemid);
    }
}
