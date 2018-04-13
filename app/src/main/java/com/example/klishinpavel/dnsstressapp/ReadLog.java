package com.example.klishinpavel.dnsstressapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadLog extends MainActivity {

    public void readToUi() throws Exception {

        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            TextView statusMessage = findViewById(R.id.textView);
            statusMessage.setText(log.toString());
        } catch (IOException e) {
            // Handle Exception
        }
    }
}
