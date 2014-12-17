package com.test.mod.wordcounter.ui.fragments;

/**
 * Created by nbelokopytov on 04.12.2014.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.test.mod.wordcounter.R;
import com.test.mod.wordcounter.WordCounterApp;
import com.test.mod.wordcounter.data.WordsDataSource;
import com.test.mod.wordcounter.data.models.Word;
import com.test.mod.wordcounter.interfaces.IStreamer;
import com.test.mod.wordcounter.interfaces.IWordStorage;
import com.test.mod.wordcounter.streamers.FileSystemStreamer;
import com.test.mod.wordcounter.streamers.HttpStreamer;
import com.test.mod.wordcounter.tasks.LoadWordsTask;
import com.test.mod.wordcounter.ui.BaseFragment;
import com.test.mod.wordcounter.worders.ScannerWorder;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class WordListFragment extends BaseFragment implements LoadWordsTask.OnRefreshUIListener{

    private static final String TAG = "NB:WordListFragment";
    private static final String PREFS = "WORDLIST";
    private static final String PREFS_KEY_RECENT = "RECENT";

    private IStreamer mStreamer;
    private ListView mList = null;
    private ArrayAdapter mAdapter = null;
    private IWordStorage mWordsDataSource = null;
    private static final String PLACEHOLDER_FILENAME = "input.txt";
    private String mInputName = null;
    private AsyncTask mLoadTask;


    public static BaseFragment getInstance(String filename) {
        WordListFragment frag = new WordListFragment();
        frag.mInputName = (filename == null) || (filename.isEmpty()) ?
                PLACEHOLDER_FILENAME : filename;
        if(frag.mInputName.contains("http://")) {
            frag.mStreamer = new HttpStreamer();
        }
        else {
            frag.mStreamer = new FileSystemStreamer();
            frag.mInputName = Environment.getExternalStorageDirectory().getPath()
                                                                    + "/" + frag.mInputName;
        }

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_counter_frag, container, false);
        mList = (ListView) rootView.findViewById(R.id.list);

//        SharedPreferences preferences = WordCounterApp.getContext().
//                                                        getSharedPreferences(PREFS,
//                                                                Context.MODE_PRIVATE);
//        String recentFile = preferences.getString(PREFS_KEY_RECENT, "null");

        mWordsDataSource = new WordsDataSource(getActivity());

        setHasOptionsMenu(true);

        return rootView;
    }

    public void createAdapter() {
        WordCounterApp.scheduleTask(new Runnable() {
            @Override
            public void run() {
                List<Word> values = mWordsDataSource.getAll();
                mAdapter = new ArrayAdapter<Word>(getActivity(),
                        R.layout.word_list_item, values);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mList.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        mLoadTask = new LoadWordsTask(mWordsDataSource, mStreamer, mInputName, this);
        mLoadTask.execute();
        super.onResume();
    }

    @Override
    public void onPause() {
        mLoadTask.cancel(true);
        mWordsDataSource.cleanup();
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

    @Override
    public void onRefreshUI() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            });
        }
        catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
    }

    void refresh() {
        createAdapter();
    }


}