package com.test.mod.wordcounter;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContext;
import android.test.suitebuilder.annotation.MediumTest;

import com.test.mod.wordcounter.data.WordsDataSource;
import com.test.mod.wordcounter.data.models.Word;
import com.test.mod.wordcounter.interfaces.IWordStorage;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbelokopytov on 17.12.2014.
 */
public class WordsDataSourceTest extends InstrumentationTestCase {

    private Context mContext = null;
    private IWordStorage mWDS = null;
    private final String PREFIX = "Test";
    private List<Word> mTestBatch = null;
    private List<Word> mWordsFromDB = null;
    private final int DEFAULT_TEST_BATCH_SIZE = 20;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "Test");
        mWDS = new WordsDataSource(mContext);

        mTestBatch = new ArrayList<>(DEFAULT_TEST_BATCH_SIZE);
        Word toAdd = null;
        for(int i = 0; i < DEFAULT_TEST_BATCH_SIZE; ++i) {
            toAdd = Word.createNewWord("" + i);
            toAdd.addCount();
            mTestBatch.add(toAdd);
        }
    }

    @MediumTest
    public void testWordsDataSource() {
        Assert.assertNotNull(mWDS);
        mWDS.addBatch(mTestBatch);
        mWordsFromDB = mWDS.getAll();
        assertEquals(mWordsFromDB.size(), mTestBatch.size());
        for(int i = 0; i < mWordsFromDB.size(); ++i) {
            assertEquals(mWordsFromDB.get(i).toString(), mTestBatch.get(i).toString());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mWDS.reset();
    }
}
