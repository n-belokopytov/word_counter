package com.test.mod.wordcounter;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.mock.MockContext;
import android.test.suitebuilder.annotation.MediumTest;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<WordCounterApp> {
    public ApplicationTest() {
        super(WordCounterApp.class);
    }

    @MediumTest
    public void testApp() {
        createApplication();
    }

}