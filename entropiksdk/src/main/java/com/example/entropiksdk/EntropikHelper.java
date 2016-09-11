package com.example.entropiksdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by prashant on 11/09/16.
 */
public class EntropikHelper {

    private static EntropikHelper _INSTANCE;
    private String activityName;
    private String activityStartTime;
    private String activityStopTime;
    private String uuid;
    private ArrayList<String> touchEventsList;
    Context mContext;

    private EntropikHelper() {
    }

    public static synchronized EntropikHelper getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new EntropikHelper();
        }
        return _INSTANCE;
    }

    public void registerLifecycleCallback(Application application) {
        application.registerActivityLifecycleCallbacks(new ActivityLifeCycleCallbacks());
    }

    void trackActivityName(Activity activity) {
        if (activity == null) return;
        activityName = activity.getClass().getName();
    }

    void onActivityResumed(Activity activity) {
        mContext = activity;
        trackActivityName(activity);
        trackStartTime();
        uuid = getUuid();
    }

    void onActivityPaused(Activity activity) {
        trackStopTime();
        createAndLogEvents();
    }

    private void storeTouchEvents(float x, float y) {
        if (touchEventsList == null) {
            touchEventsList = new ArrayList<>();
        }
        touchEventsList.add("X: " + x + ",Y: " + y);
    }

    private void trackStartTime() {
        activityStartTime = toHumanReadableDate(System.currentTimeMillis());
    }

    private void trackStopTime() {
        activityStopTime = toHumanReadableDate(System.currentTimeMillis());
    }


    private void createAndLogEvents() {
        try {
            JSONObject payload = new JSONObject();
            payload.put(Constants.ACTIVITY_NAME_KEY, activityName);
            payload.put(Constants.TOUCH_DATA, touchEventsList);
            payload.put(Constants.START_TIME, activityStartTime);
            payload.put(Constants.END_TIME, activityStopTime);
            payload.put(Constants.UUID, uuid);
            Log.d("Payload", payload.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    public boolean onTouch(MotionEvent event) {
        storeTouchEvents(event.getX(), event.getY());
        return true;
    }

    public String getUuid() {
        SharedPreferences sp = getSharedPrefs();
        String uid = sp.getString(Constants.PREF_KEY_APP_UUID, "");
        if (TextUtils.isEmpty(uid)) {
            uid = generateAndSaveUniqueId();
        }
        return uid;
    }

    private String generateAndSaveUniqueId() {
        String uid = generateUUID();
        SharedPreferences sp = getSharedPrefs();
        sp.edit().putString(Constants.PREF_KEY_APP_UUID, uid).apply();
        return uid;
    }

    private String generateUUID() {
        UUID generatedUID = UUID.randomUUID();
        return generatedUID.toString();
    }

    private SharedPreferences getSharedPrefs() {
        return mContext.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    private String toHumanReadableDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultdate = new Date(time);
        return sdf.format(resultdate);
    }

}
