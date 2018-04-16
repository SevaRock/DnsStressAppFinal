package com.example.klishinpavel.dnsstressapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import org.junit.Before;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class AbstractTest {

    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.example.klishinpavel.dnsstressapp";

    ViewInteraction timeoutField = onView(withId(R.id.timeoutField));
    ViewInteraction startButton =  onView(withId(R.id.buttonStart));
    ViewInteraction messageField = onView(withId(R.id.timer));

    @Before
    public void startActivity() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();

        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);

        context.startActivity(intent);

        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);

    }
}
