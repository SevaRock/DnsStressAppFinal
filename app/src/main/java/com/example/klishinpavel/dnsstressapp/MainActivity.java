package com.example.klishinpavel.dnsstressapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.StrictMode;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    protected static final Logger L = Logger.getLogger("dns_tests");
    StringBuilder log = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("wifi")) {
                    log.append(line.substring(line.indexOf(": ") + 2)).append("\n");
                }
                this.log.append(log.toString().replace(this.log.toString(), ""));
                TextView resultTextView = findViewById(R.id.resultTextView);
                resultTextView.setText(this.log.toString());
            }
        } catch (IOException e) {
            Log.e("wifiDirectHandler", "Failure reading logcat");
        }

        Button buttonStartDnsSpoofing = findViewById(R.id.buttonStart);

        buttonStartDnsSpoofing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                while (true) {
                    try {
                        startRandomDnsSpoofing();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void startRandomDnsSpoofing() throws IOException, InterruptedException {
        isResolvable(randomDnsSpoofingAddress());
        TimeUnit.SECONDS.sleep(1);
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
}
