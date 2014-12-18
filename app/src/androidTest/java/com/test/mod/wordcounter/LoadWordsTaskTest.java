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
import com.test.mod.wordcounter.streamers.FileSystemStreamer;
import com.test.mod.wordcounter.tasks.LoadWordsTask;
import com.test.mod.wordcounter.worders.ScannerWorder;

import java.io.InputStream;
import java.util.List;

/**
 * Created by nbelokopytov on 18.12.2014.
 */
public class LoadWordsTaskTest extends InstrumentationTestCase {

    private Context mContext = null;
    private IWordStorage mWDS = null;
    private IStreamer mStreamer = null;
    private IWorder mWorder = null;
    private final static int DEFAULT_BATCH_SIZE = 60;
    private final static int DEFAULT_BATCH_DURATION_NANO = (int) (2 * Math.pow(10, 9));
    private final static String MOCK_FILE_PATH = "input.txt";
    private final String PREFIX = "Test";
    private LoadWordsTask mLoadFSTask = null;
    private LoadWordsTask.OnRefreshUIListener mMockListener = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), PREFIX);
        mWDS = new WordsDataSource(mContext);
        mStreamer = new FileSystemStreamer();
        mMockListener = new LoadWordsTask.OnRefreshUIListener() {
            @Override
            public void onRefreshUI() {
                List<Word> words = mWDS.getAll();
                assertNotNull(words);
            }
        };
        mWorder = new ScannerWorder(mContext.getResources().openRawResource(R.raw.input),
                                                                    DEFAULT_BATCH_DURATION_NANO,
                                                                    DEFAULT_BATCH_SIZE);
    }

    @MediumTest
    public void testLoadWordsTask() {
        mMockListener = new LoadWordsTask.OnRefreshUIListener() {
            @Override
            public void onRefreshUI() {
                List<Word> words = mWDS.getAll();
                assertNotNull(words);
            }
        };
        assertNotNull(mWDS);
        assertNotNull(mWorder);
        mLoadFSTask = new LoadWordsTask(mWDS, mWorder, mMockListener);
        mLoadFSTask.execute();
    }

    @SmallTest
    public void testLoadWordsTaskNullInput() {
        mMockListener = new LoadWordsTask.OnRefreshUIListener() {
            @Override
            public void onRefreshUI() {
                List<Word> words = mWDS.getAll();
                assertEquals(true, words.isEmpty());
            }
        };
        mLoadFSTask = new LoadWordsTask(null, null, mMockListener);
        mLoadFSTask.execute();
    }
}
