package com.test.mod.wordcounter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.test.mod.wordcounter.data.db.WordsDBHelper;
import com.test.mod.wordcounter.data.models.Word;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by nbelokopytov on 05.12.2014.
 */
public class WordsDataSource {

    private Object mGetAllLock = new Object();

    private static final String TAG = "NB:WordsDataSource";
    private String mOrder = null;
    // Database fields
    private SQLiteDatabase mDatabase = null;
    private WordsDBHelper mDbHelper = null;
    private String[] allColumns = { WordsDBHelper.COLUMN_ID,
            WordsDBHelper.COLUMN_WORD, WordsDBHelper.COLUMN_COUNT };

    public WordsDataSource(Context context) {
        mOrder = WordsDBHelper.COLUMN_ID;
        mDbHelper = new WordsDBHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDatabase = null;
        mDbHelper.close();
    }

    public boolean isOpen() {
        return mDatabase != null;
    }

    //Batch adding, wrapped in transaction for performance
    public void addBatch(LinkedHashMap<String, AtomicLong> words) {
        synchronized (mGetAllLock) {
            ContentValues values = new ContentValues();
            mDatabase.beginTransaction();
            Cursor cursor = null;
            for (LinkedHashMap.Entry<String, AtomicLong> entry : words.entrySet()) {
                // this is my version of upsert
                values.put(WordsDBHelper.COLUMN_WORD, entry.getKey());
                cursor = mDatabase.query(WordsDBHelper.TABLE_WORDS,
                        allColumns, WordsDBHelper.COLUMN_WORD + "=?", new String[]{entry.getKey()}, null,
                        null, null, null);
                try {
                    if (cursor.moveToFirst()) {
                        values.put(WordsDBHelper.COLUMN_COUNT, entry.getValue().get()
                                + cursor.getLong(1));
                        mDatabase.update(WordsDBHelper.TABLE_WORDS, values,
                                WordsDBHelper.COLUMN_WORD + "=?", new String[]{entry.getKey()});
                    } else {
                        values.put(WordsDBHelper.COLUMN_COUNT, entry.getValue().get());
                        mDatabase.insert(WordsDBHelper.TABLE_WORDS, null, values);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);
                } finally {
                    cursor.close();
                }
            }
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        }
    }

    public void sortByOccurrence() {
        mOrder = WordsDBHelper.COLUMN_ID;
    }

    public void sortByAlphabet() {
        mOrder = WordsDBHelper.COLUMN_WORD;
    }

    public void sortByCount() {
        mOrder = WordsDBHelper.COLUMN_COUNT;
    }

    public List<Word> getAllWords() {
        List<Word> words = new ArrayList<Word>();
        Cursor cursor = null;
        try {
            synchronized (mGetAllLock) {
                cursor = mDatabase.query(WordsDBHelper.TABLE_WORDS,
                        allColumns, null, null, null, null, mOrder);
            }
        }
        catch (Exception e){
            Log.e(TAG, e.getLocalizedMessage(), e);
            return words;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Word word = cursorToWord(cursor);
            words.add(word);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return words;
    }

    private Word cursorToWord(Cursor cursor) {
        return Word.createWordFromData(cursor.getLong(0), cursor.getString(1), cursor.getLong(2));
    }

    public void reset(){
        mDbHelper.reset(mDatabase);
    }
}