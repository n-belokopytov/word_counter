package com.test.mod.wordcounter.interfaces;

import com.test.mod.wordcounter.data.models.Word;

import java.util.List;

/**
 * Created by nbelokopytov on 15.12.2014.
 */
public interface IWordStorage {

    public void addBatch(List<Word> batch);

    public List<Word> getAll();

    public void reset();

    public void sortByOccurrence();

    public void sortByAlphabet();

    public void sortByCount();

    public void cleanup();
}
