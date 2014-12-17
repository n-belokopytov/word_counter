package com.test.mod.wordcounter;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContext;
import android.test.suitebuilder.annotation.MediumTest;

import com.test.mod.wordcounter.data.WordsDataSource;
import com.test.mod.wordcounter.interfaces.IWordStorage;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by nbelokopytov on 17.12.2014.
 */
public class WordsDataSourceTest extends InstrumentationTestCase {

    private Context mContext = null;
    private IWordStorage mWDS = null;
    private final String PREFIX = "Test";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "Test");
        mWDS = new WordsDataSource(mContext);
    }

    @MediumTest
    public void testWordsDataSource() {
        Assert.assertNotNull(mWDS);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mWDS.reset();
    }
}
