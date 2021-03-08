package com.licence.projetalarme;

import android.app.Application;
import android.os.SystemClock;

/**
 * Create by SA E SILVA Eduardo on <DATE-ON-JOUR>
 */
public class Timeapp extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        SystemClock.sleep(2000);
    }
}
