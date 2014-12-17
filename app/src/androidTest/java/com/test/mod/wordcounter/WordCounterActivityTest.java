package com.test.mod.wordcounter;

import android.content.Intent;
import android.test.suitebuilder.annotation.MediumTest;

import com.test.mod.wordcounter.ui.activities.WordCounterActivity;

import junit.framework.Assert;

/**
 * Created by nbelokopytov on 17.12.2014.
 */
public class WordCounterActivityTest extends android.test.ActivityInstrumentationTestCase2<WordCounterActivity> {

    private WordCounterActivity mActivity = null;

    public WordCounterActivityTest() {
        super(WordCounterActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getContext(), WordCounterActivity.class);
        intent.putExtra(WordCounterActivity.FILE, "input.txt");
        setActivityIntent(intent);
        mActivity = getActivity();
    }

    @MediumTest
    public void testWordCounterActivityCreation() {

        Assert.assertNotNull(mActivity);
    }
}
