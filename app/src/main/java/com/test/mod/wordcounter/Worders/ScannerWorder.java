package com.test.mod.wordcounter.worders;

import android.util.Log;

import com.test.mod.wordcounter.data.models.Word;
import com.test.mod.wordcounter.interfaces.IWorder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by nbelokopytov on 15.12.2014.
 */
public class ScannerWorder implements IWorder {

    private static final String TAG = "NB:ScannerWorder";

    private long mBatchTimer = 2000000000;

    private Scanner mScanner = null;
    private LinkedHashMap<String, Integer> mIndex = new LinkedHashMap<>();
    private List<Word> mWordsBatch = new ArrayList<>();

    public void init(InputStream path, float batchTimerSeconds) {
        try {
            mScanner = new Scanner(path);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        mBatchTimer = (long)(batchTimerSeconds * 1000000000);
    }

    public List<Word> getNextBatch() {
        String s = null;
        mIndex.clear();
        mWordsBatch.clear();
        long startTime = System.nanoTime();

        while (mScanner.hasNext() || (System.nanoTime() - startTime) > mBatchTimer) {
            s = mScanner.next();
            if (!mIndex.containsKey(s)) {
                mIndex.put(s, new Integer(mWordsBatch.size()));
                mWordsBatch.add(Word.createNewWord(s));
            }
            mWordsBatch.get(mIndex.get(s)).addCount();
        }

        return mWordsBatch;
    }

    @Override
    public boolean hasFinished() {
        return mScanner.hasNext();
    }
}
