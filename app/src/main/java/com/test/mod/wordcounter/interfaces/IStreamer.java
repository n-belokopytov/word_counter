package com.test.mod.wordcounter.interfaces;

import java.io.InputStream;

/**
 * Created by nbelokopytov on 15.12.2014.
 */
public interface IStreamer {
    InputStream open(String path);
    void close();
}
