package com.test.mod.wordcounter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.test.mod.wordcounter.data.db.WordsDBHelper;
import com.test.mod.wordcounter.data.models.Word;
import com.test.mod.wordcounter.interfaces.IWordStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbelokopytov on 05.12.2014.
 */
public class WordsDataSource implements IWordStorage {

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
        mDatabase = mDbHelper.getWritableDatabase();
    }

    @Override
    public void cleanup() {
        mDatabase = null;
        mDbHelper.close();
    }

    //Batch adding, wrapped in transaction for performance
    @Override
    public void addBatch(List<Word> words) {
        if(mDatabase == null){
            mDatabase = mDbHelper.getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        Cursor cursor = null;

        mDatabase.beginTransaction();
        for (Word entry : words) {
            // this is my version of upsert
            values.put(WordsDBHelper.COLUMN_WORD, entry.getWord());
            cursor = mDatabase.query(WordsDBHelper.TABLE_WORDS,
                    allColumns, WordsDBHelper.COLUMN_WORD + "=?", new String[]{entry.getWord()}, null,
                    null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    values.put(WordsDBHelper.COLUMN_COUNT, entry.getCount()
                            + cursor.getLong(1));
                    mDatabase.update(WordsDBHelper.TABLE_WORDS, values,
                            WordsDBHelper.COLUMN_WORD + "=?", new String[]{entry.getWord()});
                } else {
                    values.put(WordsDBHelper.COLUMN_COUNT, entry.getCount());
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

    @Override
    public void sortByOccurrence() {
        mOrder = WordsDBHelper.COLUMN_ID;
    }

    @Override
    public void sortByAlphabet() {
        mOrder = WordsDBHelper.COLUMN_WORD;
    }

    @Override
    public void sortByCount() {
        mOrder = WordsDBHelper.COLUMN_COUNT;
    }

    @Override
    public List<Word> getAll() {
        if(mDatabase == null){
            mDatabase = mDbHelper.getWritableDatabase();
        }
        List<Word> words = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = mDatabase.query(WordsDBHelper.TABLE_WORDS,
                    allColumns, null, null, null, null, mOrder);
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

    @Override
    public void reset() {
        if(mDatabase == null){
            mDatabase = mDbHelper.getWritableDatabase();
        }
        mDbHelper.reset(mDatabase);
    }
}