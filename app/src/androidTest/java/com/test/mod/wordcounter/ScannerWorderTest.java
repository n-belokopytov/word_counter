package com.test.mod.wordcounter;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.test.mod.wordcounter.data.WordsDataSource;
import com.test.mod.wordcounter.data.models.Word;
import com.test.mod.wordcounter.interfaces.IStreamer;
import com.test.mod.wordcounter.interfaces.IWordStorage;
import com.test.mod.wordcounter.interfaces.IWorder;
import com.test.mod.wordcounter.worders.ScannerWorder;

import java.io.InputStream;
import java.util.List;

/**
 * Created by nbelokopytov on 18.12.2014.
 */
public class ScannerWorderTest extends InstrumentationTestCase {

    private Context mContext = null;
    private IWordStorage mWDS = null;
    private IWorder mWorder = null;
    private final static int DEFAULT_BATCH_SIZE = 60;
    private final static int DEFAULT_BATCH_DURATION_NANO = (int) (2 * Math.pow(10, 9));
    private final static String MOCK_FILE_PATH = "input.txt";
    private final static String PREFIX = "Test";
    private final static String FIRST_TEST_WORD = "ipsum";
    private final static int FIRST_TEST_COUNT = 1;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), PREFIX);
        mWDS = new WordsDataSource(mContext);
    }

    @MediumTest
    public void testScannerWorder() {
        assertNotNull(mWDS);
        InputStream stream = null;
        try {
            stream = mContext.getResources().openRawResource(R.raw.input);
        }
        catch (Exception e) {
            fail();
        }
        mWorder = new ScannerWorder(stream, DEFAULT_BATCH_DURATION_NANO, DEFAULT_BATCH_SIZE);
        assertNotNull(mWorder);
        assertEquals(false, mWorder.hasFinished());
        List<Word> words = mWorder.getNextBatch();
        assertNotNull(words);
        assertEquals(DEFAULT_BATCH_SIZE, words.size());
        assertEquals(FIRST_TEST_WORD, words.get(1).getWord());
        assertEquals(FIRST_TEST_COUNT, words.get(0).getCount());
    }

    @SmallTest
    public void testScannerWorderEmptyFile() {
        assertNotNull(mWDS);
        InputStream stream = null;
        try {
            stream = mContext.getResources().openRawResource(R.raw.empty);
        }
        catch (Exception e) {
            fail();
        }
        mWorder = new ScannerWorder(stream, DEFAULT_BATCH_DURATION_NANO, DEFAULT_BATCH_SIZE);
        assertNotNull(mWorder);
        assertEquals(true, mWorder.hasFinished());
    }

    @SmallTest
    public void testScannerWorderNullInput() {
        mWorder = new ScannerWorder(null, DEFAULT_BATCH_DURATION_NANO, DEFAULT_BATCH_SIZE);
        assertNotNull(mWorder);
        assertNull(mWorder.getNextBatch());
        assertTrue(mWorder.hasFinished());
    }
}
