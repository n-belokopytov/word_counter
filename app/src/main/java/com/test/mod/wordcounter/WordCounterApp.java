package com.test.mod.wordcounter;

/**
 * Created by nbelokopytov on 05.12.2014.
 */
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by nbelokopytov on 30/10/14.
 */
public class WordCounterApp extends Application {

    private static final String TAG = "NB:WordCounterApp";
    private static final String PREFS = "WordCounterApp_prefs";
    private static final String PREFS_KEY_INSTALL = "WordCounterApp_installed";

    private static Context sContext = null;;

    //A static thread pool for possible bg tasks
    private static ExecutorService mThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {
        int mCounter = 1;

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "NB-bgThread-" + mCounter++);
        }
    });

    private static String sBuildVersion;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        try {
            sBuildVersion = sContext.getPackageManager()
                    .getPackageInfo(sContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Error reading build version", e);
        }

        boolean alreadyInstalled = prefs.getBoolean(PREFS_KEY_INSTALL, false);

        if (!alreadyInstalled){
            //Do some first time stuff like welcome/tutorial screen or Analytics event
        }
    }

    public static void scheduleTask(final Runnable task) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    Log.w(TAG, "Error in background task: " + task, e);

                    if (sContext.getResources().getBoolean(R.bool.Crash_bg_threads)) {
                        throw new RuntimeException(e);
                    }
                } finally {
                }
            }
        });
    }

    public static String getBuildVersion() {
        return sBuildVersion;
    }

}
