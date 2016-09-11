package com.example.prashant.entropik;

import android.app.Application;

import com.example.entropiksdk.EntropikHelper;

/**
 * Created by prashant on 11/09/16.
 */
public class EntropikDemo extends Application  {

    @Override
    public void onCreate() {
        super.onCreate();
        EntropikHelper.getInstance().registerLifecycleCallback(this);
    }
}
