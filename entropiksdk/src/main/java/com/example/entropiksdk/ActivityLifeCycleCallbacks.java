package com.example.entropiksdk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by prashant on 11/09/16.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ActivityLifeCycleCallbacks implements Application.ActivityLifecycleCallbacks {

    EntropikHelper helper;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (helper == null) {
            helper = EntropikHelper.getInstance();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        helper.onActivityResumed(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        helper.onActivityPaused(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //Do Nothing
    }
}