package com.mrd.hackernews.utils;

import android.app.Activity;
import android.support.test.espresso.IdlingResource;

/**
 * Created by mayurdube on 16/05/17.
 */

public class AcIdlingResource implements IdlingResource {

    private ActivityIdlingResource activity;
    private ResourceCallback callback;

    public AcIdlingResource(Activity activity) {
        this.activity = (ActivityIdlingResource) activity;
    }

    @Override
    public String getName() {
        return "AcIdlingResource";
    }

    @Override
    public boolean isIdleNow() {
        Boolean idle = isIdle();
        if (idle) callback.onTransitionToIdle();
        return idle;
    }

    public boolean isIdle() {
        boolean isIdle = activity.isIdle();
        return activity != null && callback != null && isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}
