package com.example.klishinpavel.dnsstressapp;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class GeneralUiTest extends  AbstractTest {

    @Test
    public void checkGeneralUi() {
        timeoutField.perform(typeText("3"));
        startButton.perform(click());
        messageField.check(matches(withText("Dns resolving with interval of 3 seconds started...")));
    }
}
