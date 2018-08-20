package org.solarus_games.solarus;

import android.app.Application;
import android.content.Context;

public class SolarusApp extends Application {
    private static SolarusApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }
}
