package com.test.mod.wordcounter.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.test.mod.wordcounter.ui.BaseActivity;
import com.test.mod.wordcounter.ui.fragments.WordListFragment;
import com.test.mod.wordcounter.R;


public class WordCounterActivity extends BaseActivity {

    public static final String TAG = "NB:WordCounterActivity";
    public static final String LIST_FRAG_TAG = "WordListFrag";
    public static final String FILE = "FILE";

    @Override
    public android.app.Fragment getNewFragmentInstance() {
        //Here I am parsing the intent extra for filename in case this activity will be refactored
        //and inserted into a workflow, default value is input.txt
        return WordListFragment.getInstance(getIntent().getStringExtra(WordCounterActivity.FILE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_counter, menu);
        return true;
    }

    @Override
    public String getFragmentTag() {
        return LIST_FRAG_TAG;
    }

    @Override
    public int getActionBarTitle() {
        return R.string.actionbar_title;
    }
}
