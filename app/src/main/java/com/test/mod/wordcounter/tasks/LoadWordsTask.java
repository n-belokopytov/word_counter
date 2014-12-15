package com.test.mod.wordcounter.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.test.mod.wordcounter.WordCounterApp;
import com.test.mod.wordcounter.data.models.Word;
import com.test.mod.wordcounter.interfaces.IStreamer;
import com.test.mod.wordcounter.interfaces.IWordStorage;
import com.test.mod.wordcounter.interfaces.IWorder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by nbelokopytov on 15.12.2014.
 */
public class LoadWordsTask extends AsyncTask {
    private static final String TAG = "NB:LoadWordsTask";
    private static final float DEFAULT_BATCH_TIME = 2f;

    private final IWorder mWorder;
    private final IStreamer mStreamLoader;
    private final String mInputPath;
    private final OnRefreshUIListener mUIListener;
    //READER class implementation
    //get characters one by one

    IWordStorage mWordsDataSource;

    public LoadWordsTask(IWordStorage dataSource, IStreamer loader, IWorder worder,
                                                    String path, OnRefreshUIListener uiListener){
        mWordsDataSource = dataSource;
        mStreamLoader = loader;
        mWorder = worder;
        mInputPath = path;
        mUIListener = uiListener;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        if(mInputPath == null){
            return null;
        }

        mWordsDataSource.reset();

        InputStream stream = null;
        try {
            stream = mStreamLoader.open(mInputPath);
            mWorder.init(stream, DEFAULT_BATCH_TIME);

            while(!mWorder.hasFinished()) {
                final List<Word> finalBatch = new ArrayList<>(mWorder.getNextBatch());

                WordCounterApp.scheduleTask(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Writing a batch of size " + finalBatch.size());
                        mWordsDataSource.addBatch(finalBatch);
                        mUIListener.onRefreshUI();
                    }
                });
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return false;
        }
        finally {
            mStreamLoader.close();
        }

        return true;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if((Boolean) o) {
            mUIListener.onRefreshUI();
        }
    }

    public interface OnRefreshUIListener {
        void onRefreshUI();
    }
}
