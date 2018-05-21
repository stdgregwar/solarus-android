package org.solarus_games.solarus;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import org.libsdl.app.SDLActivity;

public class SolarusEngine extends SDLActivity {
    private String quest_absolute_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quest_absolute_path = getIntent().getExtras().getString("quest_path");
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    @Override
    protected String[] getArguments() {
        String[] args_emulator ={"-no-audio",quest_absolute_path};
        String[] args = {quest_absolute_path};
        return isEmulator() ? args_emulator : args;
    }
}
