package com.test.mod.wordcounter.streamers;

import android.util.Log;

import com.test.mod.wordcounter.interfaces.IStreamer;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by nbelokopytov on 15.12.2014.
 */
public class HttpStreamer implements IStreamer {

    private static final String TAG = "NB:HttpStreamer";
    InputStream mStream = null;

    @Override
    public InputStream open(String path) {
        URL url = null;
        InputStream httpStream = null;
        try {
            url = new URL(path);
            httpStream = url.openStream();
        }
        catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }

        return httpStream;
    }

    @Override
    public void close() {
        try {
            mStream.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
    }
}
