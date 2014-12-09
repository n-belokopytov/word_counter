package com.test.mod.wordcounter.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.test.mod.wordcounter.R;
import com.test.mod.wordcounter.ui.util.ForegroundActivityCounter;
import com.test.mod.wordcounter.ui.util.Utils;


/**
 * Created by nbelokopytov on 30/10/14.
 */

//Base activity that handles the foreground counter and other things that should be common across
//all activities
public abstract class BaseActivity extends Activity {

    private static final String TAG = "NB:BaseActivity";
    private Fragment mFragment = null;
    private ActionBar mActionBar = null;

    public abstract Fragment getNewFragmentInstance();
    public abstract String getFragmentTag();
    public abstract int getActionBarTitle();

    public int getContentLayout(){
        return R.layout.frag_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentLayout());

        if(savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            mFragment = getNewFragmentInstance();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_holder, mFragment, getFragmentTag());
            ft.commit();
        }else {
            mFragment = getFragmentManager().findFragmentByTag(getFragmentTag());
        }
        mActionBar = getActionBar();
        if (mActionBar != null) {
            if(getActionBarTitle() != 0){
                mActionBar.setTitle(getActionBarTitle());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ForegroundActivityCounter counter = ForegroundActivityCounter.getInstance();
        counter.countUp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ForegroundActivityCounter counter = ForegroundActivityCounter.getInstance();
        counter.countDown();
    }

}
