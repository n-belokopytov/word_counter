package com.test.mod.wordcounter.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.test.mod.wordcounter.R;
import com.test.mod.wordcounter.WordCounterApp;

/**
 * Created by nbelokopytov on 30/10/14.
 */
//Base fragment class in case we would need something common for all of them, like a timer
//or common Options Menu
public class BaseFragment extends Fragment {

    public Context mContext = WordCounterApp.getContext();
    protected long mStartTime = 0;
    protected Dialog mCurrentDialog = null;;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);

        //This is for possible analytics in future
        mStartTime = System.currentTimeMillis();
    }

    //I know I should've done that through DialogFragments, but it seemed like an overhead
    protected void showProgressDialog() {
        dismissDialogs();
        ProgressDialog pd = new ProgressDialog(new ContextThemeWrapper(getActivity(), R.style.CustomAlertDialog));
        this.mCurrentDialog = pd;
        pd.setMessage(getActivity().getString(R.string.msg_working));
        pd.show();
    }

    protected void dismissDialogs() {
        if (mCurrentDialog != null) {
            if (mCurrentDialog.isShowing()) {
                mCurrentDialog.dismiss();
            }
            mCurrentDialog = null;
        }
    }
}
