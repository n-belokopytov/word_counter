package com.test.mod.wordcounter.data.models;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by nbelokopytov on 05.12.2014.
 */
public class Word {
    private static final String TAG = "NB:Word";

    private final long mId;
    private AtomicLong mCount;
    private final String mWord;


    public static Word createNewWord(String word) {
        return new Word(0, word, 1);
    }

    public static Word createWordFromData(long position, String word, long count) {
        return new Word(position, word, count);
    }

    private Word(long position, String word, long count) {
        mId = position;
        mWord = word;
        mCount = new AtomicLong(count);
    }

    public long getPosition() {
        return mId;
    }

    public String getWord() {
        return mWord;
    }

    public long getCount() {
        return mCount.get();
    }

    public void addCount() {
        mCount.incrementAndGet();
    }

    @Override
    public String toString() {
        return "\"" + mWord + "\" times encountered : " + mCount;
    }
}
