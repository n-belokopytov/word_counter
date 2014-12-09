package com.test.mod.wordcounter.ui.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

/**
 * Created by nbelokopytov on 30/10/14.
 */

//A few assorted utils I carry around from project to project
public class Utils {

    private static final String TAG = "NB:Utils";

    public static boolean isNetworkUnreachable(Context context) {
        NetworkInfo networkInfo = null;
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            Log.e(TAG,"isNetworkUnreachable()",e);
        }

        return ((networkInfo == null || !networkInfo.isConnected())? true:false);
    }

    public static void crossfade(final View to, final View from, int duration) {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        from.setAlpha(0f);
        from.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        from.animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        to.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        to.setVisibility(View.GONE);
                    }
                });
    }

}
