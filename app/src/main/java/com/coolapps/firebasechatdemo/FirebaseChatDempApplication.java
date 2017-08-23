package com.coolapps.firebasechatdemo;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by rupam.ghosh on 23/08/17.
 */

public class FirebaseChatDempApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
