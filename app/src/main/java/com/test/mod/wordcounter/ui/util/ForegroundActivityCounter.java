package com.test.mod.wordcounter.ui.util;

/**
 * Created by nbelokopytov on 30/10/14.
 */
import android.os.Looper;


//This is a foreground activity counter, a pretty handy tool for analytics
public class ForegroundActivityCounter {

    private static final ForegroundActivityCounter sInstance = new ForegroundActivityCounter();

    public static ForegroundActivityCounter getInstance() {
        return sInstance;
    }

    private int mForegroundActivities;

    private ForegroundActivityCounter() {}

    public void countUp() {
        checkOnMainThread();
        mForegroundActivities++;
    }

    public void countDown() {
        checkOnMainThread();
        mForegroundActivities--;
        if(mForegroundActivities < 0) {
            throw new IllegalStateException("ForegroundActivityCounter is negative");
        }
    }

    public boolean isAppInForeground() {
        return mForegroundActivities > 0;
    }

    private void checkOnMainThread() {
        if(Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
            throw new IllegalStateException("Calling ForegroundActivityCounter from non-main thread");
        }
    }
}
