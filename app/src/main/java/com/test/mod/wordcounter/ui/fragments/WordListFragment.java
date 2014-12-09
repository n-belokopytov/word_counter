package com.test.mod.wordcounter.ui.fragments;

/**
 * Created by nbelokopytov on 04.12.2014.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.test.mod.wordcounter.R;
import com.test.mod.wordcounter.WordCounterApp;
import com.test.mod.wordcounter.data.ILoadMore;
import com.test.mod.wordcounter.data.WordsDataSource;
import com.test.mod.wordcounter.data.models.Word;
import com.test.mod.wordcounter.ui.BaseFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A placeholder fragment containing a simple view.
 */
public class WordListFragment extends BaseFragment {

    private static final String TAG = "NB:WordListFragment";
    private static final String PREFS = "WORDLIST";
    private static final String PREFS_KEY_RECENT = "RECENT";

    private ListView mList = null;
    private ArrayAdapter mAdapter = null;
    private WordsDataSource mWordsDataSource = null;
    private static final String PLACEHOLDER_FILENAME = "input.txt";
    private String mInputName = null;
    private File mInputFile = null;

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public static BaseFragment getInstance(String filename) {
        WordListFragment frag = new WordListFragment();
        frag.mInputName = (filename == null) || (filename.isEmpty()) ?
                PLACEHOLDER_FILENAME : filename;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_counter_frag, container, false);
        mList = (ListView) rootView.findViewById(R.id.list);
        //Get the text file
        mInputFile = new File(Environment.getExternalStorageDirectory(), mInputName);

        SharedPreferences preferences = WordCounterApp.getContext().
                                                        getSharedPreferences(PREFS,
                                                                Context.MODE_PRIVATE);
        String recentFile = preferences.getString(PREFS_KEY_RECENT, "null");

        mWordsDataSource = new WordsDataSource(getActivity());
        mWordsDataSource.open();

        setHasOptionsMenu(true);

        if(recentFile.compareTo(mInputName) == 0) {
            createAdapter();
        }
        else {
            ReadWordsTask task = new ReadWordsTask();
            task.execute();
            //Here we can put this filename to preferences and then open it up
            //But for now I won't be doing it because I don't see a need in that mechanism
            //As soon as the file selection is introduced, this should be used and also
            //Preferences should hold the most recent md5 sum instead of a filename
        }


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWordsDataSource.close();
    }

    public ListAdapter createAdapter() {
        List<Word> values = mWordsDataSource.getAllWords();
        mAdapter = new ArrayAdapter<Word>(getActivity(),
                R.layout.word_list_item, values);
        return mAdapter;
    }

    @Override
    public void onResume() {
        mWordsDataSource.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        mWordsDataSource.close();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.sort_occurrence:
                mWordsDataSource.sortByOccurrence();
                refresh();
                return true;

            case R.id.sort_alphabet:
                mWordsDataSource.sortByAlphabet();
                refresh();
                return true;

            case R.id.sort_count:
                mWordsDataSource.sortByCount();
                refresh();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void refresh(){
        createAdapter();
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private class ReadWordsTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            if(mInputName == null){
                return null;
            }

            mWordsDataSource.open();
            mWordsDataSource.reset();

            LinkedHashMap<String, AtomicLong> words = new LinkedHashMap<String, AtomicLong>();
            Scanner sc2 = null;

            try {
                sc2 = new Scanner(mInputFile);
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            String s = null;

            while (sc2.hasNextLine()) {
                Scanner s2 = new Scanner(sc2.nextLine());

                while (s2.hasNext() && !isCancelled()) {
                    s = s2.next();
                    if(!words.containsKey(s)) {
                        words.put(s, new AtomicLong(0));
                    }
                    words.get(s).incrementAndGet();
                }

                final LinkedHashMap<String, AtomicLong> finalBatch = (LinkedHashMap<String, AtomicLong>) words.clone();

                //This thing here uses an executor with a thread pool, but since db write is
                //synchronized there isn't a lot of benefit.
                WordCounterApp.scheduleTask(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Writing a batch of size " + finalBatch.size());
                        mWordsDataSource.addBatch(finalBatch);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                            }
                        });
                    }
                });

                words.clear();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if((Boolean) o == true) {
                refresh();
            }
        }
    }
}