package com.viveret.safemessage;

import android.app.Application;

/**
 * Created by viveret on 1/14/17.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // FacebookSdk.sdkInitialize(getApplicationContext());
        // AppEventsLogger.activateApp(this);
    }


}
