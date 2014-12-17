package com.test.mod.wordcounter.interfaces;

import com.test.mod.wordcounter.data.models.Word;
import java.io.InputStream;
import java.util.List;

/**
 * Created by nbelokopytov on 15.12.2014.
 */
public interface IWorder {
    List<Word> getNextBatch();
    boolean hasFinished();
}
