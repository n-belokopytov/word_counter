package com.test.mod.wordcounter.data.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nbelokopytov on 05.12.2014.
 */
public class WordsDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "NB:WordsDBHelper";

    public static final String TABLE_WORDS = "words";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_COUNT = "count";

    private static final String DATABASE_NAME = "words.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_WORDS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_WORD
            + " text not null, " + COLUMN_COUNT + " integer );";

    public WordsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(WordsDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        reset(db);
    }

    public void reset(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
        onCreate(db);
    }
}
