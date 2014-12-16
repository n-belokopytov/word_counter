package com.test.mod.wordcounter.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.test.mod.wordcounter.R;
import com.test.mod.wordcounter.WordCounterApp;
import com.test.mod.wordcounter.ui.BaseFragment;
import com.test.mod.wordcounter.ui.activities.WordCounterActivity;

/**
 * Created by nbelokopytov on 16.12.2014.
 */
public class SelectionFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "NB:SelectionFragment";

    private TextView mDefaultSDCardView = null;
    private TextView mDefaultWebView = null;
    private EditText mPathView = null;
    private Button mProceed = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.selection_frag, container, false);

        mDefaultSDCardView = (TextView) rootView.findViewById(R.id.default_sdcard);
        mDefaultWebView = (TextView) rootView.findViewById(R.id.default_web);
        mPathView = (EditText) rootView.findViewById(R.id.path);
        mProceed = (Button) rootView.findViewById(R.id.proceed);

        mDefaultSDCardView.setOnClickListener(this);
        mDefaultWebView.setOnClickListener(this);
        mProceed.setOnClickListener(this);

        return rootView;
    }

    public static Fragment getInstance() {
        return new SelectionFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.default_sdcard:
                mPathView.setText(R.string.default_sdcard_path);
                break;

            case R.id.default_web:
                mPathView.setText(R.string.default_web_path);
                break;

            case R.id.proceed:
                Intent intent = new Intent(WordCounterApp.getContext(), WordCounterActivity.class);
                intent.putExtra(WordCounterActivity.FILE, mPathView.getText().toString());
                startActivity(intent);
                break;
        }
    }
}
