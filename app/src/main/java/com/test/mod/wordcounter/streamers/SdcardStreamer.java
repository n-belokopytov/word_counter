package com.test.mod.wordcounter.streamers;

import android.util.Log;

import com.test.mod.wordcounter.interfaces.IStreamer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by nbelokopytov on 15.12.2014.
 */
public class SdcardStreamer implements IStreamer {

    private static final String TAG = "NB:SdcardStreamer";
    private InputStream mStream = null;

    @Override
    public InputStream open(String path) {
        try {
            mStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return mStream;
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
