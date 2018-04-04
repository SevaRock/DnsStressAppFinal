package com.example.klishinpavel.dnsstressapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
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
        EditText timeoutField = findViewById(R.id.timeoutField);
        int time = Integer.parseInt(timeoutField.getText().toString());
        return time;
    }

    public void startRandomDnsSpoofing() throws IOException, InterruptedException {
        isResolvable(randomDnsSpoofingAddress());
        TimeUnit.SECONDS.sleep(getTimeOut());
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

    public void isResolvable(String hostname) throws InterruptedException {
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
            if (backgroungThread == null) {
                backgroungThread = new Thread(this);
                TextView statusMessage = findViewById(R.id.timer);
                statusMessage.setText("Dns resolving with interval of " + getTimeOut() + " seconds started...");
                backgroungThread.start();
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
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Log.e("DNS", "Dns Spam Stopped");
                backgroungThread = null;
            }
        }
    }
}

