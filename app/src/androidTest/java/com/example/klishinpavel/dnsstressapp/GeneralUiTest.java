package com.example.klishinpavel.dnsstressapp;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class GeneralUiTest extends AbstractUiTest {

    private final static String TIMEOUT_INTERVAL = "3";

    @Test
    public void intervalIndicationTest() {
        checkIntervalIndication(TIMEOUT_INTERVAL);
    }
}
