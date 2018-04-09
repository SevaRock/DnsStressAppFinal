package com.example.klishinpavel.dnsstressapp;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    protected static final Logger L = Logger.getLogger("dns_tests");
    StringBuilder log = new StringBuilder();
    BackgroundDnsSpamProcess dnsSpamProcess = new BackgroundDnsSpamProcess();
    ReadLog readLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Button buttonStartDnsSpoofing = findViewById(R.id.buttonStart);
        Button buttonStopDnsSpoofing = findViewById(R.id.buttonStop);

        buttonStartDnsSpoofing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dnsSpamProcess.start();
            }
        });

        buttonStopDnsSpoofing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dnsSpamProcess.stop();
            }
        });
    }

    public long getTimeOut() {
        try {
        EditText timeoutField = findViewById(R.id.timeoutField);
            int time = Integer.parseInt(timeoutField.getText().toString());
            return time;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void startRandomDnsSpoofing() throws InterruptedException {
        isResolvable(randomDnsSpoofingAddress());
        TimeUnit.SECONDS.sleep(getTimeOut());
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String randomDnsSpoofingAddress() {
        char[] chars = "1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder(8);
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString() + ".dns.secure.mos";
    }

    public void isResolvable(String hostname) {
        try {
            InetAddress ip = InetAddress.getByName(hostname);
            L.info(hostname + " successfully resolved to " + ip.getHostAddress());
        } catch (UnknownHostException e) {
            L.info("unknown host: " + hostname);
        }
    }


    public class BackgroundDnsSpamProcess implements Runnable {

        Thread backgroungThread;

        public void start() {

            TextView statusMessage = findViewById(R.id.timer);

            if (getTimeOut() <= 0) {
                statusMessage.setText("Wrong Timeout!");
            } else {
                if (backgroungThread == null) {
                    backgroungThread = new Thread(this);
                    hideKeyboard(MainActivity.this);
                    try {
                        readLog.readToUi();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    statusMessage.setText("Dns resolving with interval of " + getTimeOut() + " seconds started...");
                    backgroungThread.start();
                }
            }
        }

        public void stop() {
            if (backgroungThread != null) {
                TextView statusMessage = findViewById(R.id.timer);
                statusMessage.setText("Dns Resolving Stopped...");
                backgroungThread.interrupt();
            }
        }

        @Override
        public void run() {
            try {
                Log.e("DNS", "Dns Spam Started");
                while (!backgroungThread.isInterrupted()) {
                    startRandomDnsSpoofing();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Log.e("DNS", "Dns Spam Stopped");
                backgroungThread = null;
            }
        }
    }
}

