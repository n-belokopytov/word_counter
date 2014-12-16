package com.test.mod.wordcounter.ui.activities;

import android.app.Fragment;

import com.test.mod.wordcounter.R;
import com.test.mod.wordcounter.ui.BaseActivity;
import com.test.mod.wordcounter.ui.fragments.SelectionFragment;

/**
 * Created by nbelokopytov on 16.12.2014.
 */
public class SelectionActivity extends BaseActivity {

    public static final String TAG = "NB:SelectionActivity";
    public static final String LIST_FRAG_TAG = "SelectionFrag";

    @Override
    public Fragment getNewFragmentInstance() {
        return SelectionFragment.getInstance();
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
