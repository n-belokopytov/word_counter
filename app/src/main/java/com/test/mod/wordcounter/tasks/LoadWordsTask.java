package com.test.mod.wordcounter.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.test.mod.wordcounter.WordCounterApp;
import com.test.mod.wordcounter.data.models.Word;
import com.test.mod.wordcounter.interfaces.IStreamer;
import com.test.mod.wordcounter.interfaces.IWordStorage;
import com.test.mod.wordcounter.interfaces.IWorder;
import com.test.mod.wordcounter.worders.ScannerWorder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbelokopytov on 15.12.2014.
 */
public class LoadWordsTask extends AsyncTask {
    private static final String TAG = "NB:LoadWordsTask";
    private static final int DEFAULT_BATCH_TIME_NANO = (int) (2 * Math.pow(10, 9));
    private static final int DEFAULT_BATCH_SIZE = 60;


    private final String mInputPath;

    protected final OnRefreshUIListener mUIListener;
    protected final IStreamer mStreamLoader;
    protected IWorder mWorder;
    protected IWordStorage mWordsDataSource;

    public LoadWordsTask(IWordStorage dataSource, IStreamer loader, String path,
                         OnRefreshUIListener uiListener){
        mWordsDataSource = dataSource;
        mStreamLoader = loader;
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
            mWorder = new ScannerWorder(stream, DEFAULT_BATCH_TIME_NANO, DEFAULT_BATCH_SIZE);

            while(!mWorder.hasFinished() && !isCancelled()) {
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
