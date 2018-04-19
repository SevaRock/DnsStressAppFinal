package com.example.klishinpavel.dnsstressapp;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class GeneralUiTest extends AbstractUiTest {

    //TODO: Make test parametrized

    private final static String TIMEOUT_INTERVAL_SMALL = "3";
    private final static String TIMEOUT_INTERVAL_MIDDLE = "10";
    private final static String TIMEOUT_INTERVAL_LARGE = "30";

    @Test
    public void simpleUiTest() {
        checkIntervalIndication(TIMEOUT_INTERVAL_SMALL);

        checkIntervalIndication(TIMEOUT_INTERVAL_MIDDLE);

        checkIntervalIndication(TIMEOUT_INTERVAL_LARGE);

        checkStopIndication();
    }
}
