package com.mhksoft.jsontemplate;

import android.app.Application;

import com.mhksoft.jsonreciever.Activities.JsonReceiver;

/**
 * Created by MHK on 10/12/2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // JSON Receiver
        JsonReceiver.initializeAll(this,
                "ADMOB_APP_ID_MUSTCHANGE",
                "ADMOB_INTERSTITIAL_ID_MUSTCHANGE",
                "ADMOB_VIDEO_ID_MUSTCHANGE",
                "ADUNITY_GAME_ID_MUSTCHANGE");
    }
}
