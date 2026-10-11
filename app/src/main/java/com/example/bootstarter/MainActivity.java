package com.example.bootstarter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent = new Intent();
            String pkg = getPackageName();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + pkg));
            } else {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + pkg));
            }
            startActivity(intent);
        } catch (Exception e) { e.printStackTrace(); }
        finish();
    }
}