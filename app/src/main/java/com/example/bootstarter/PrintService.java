package com.example.bootstarter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class PrintService extends Service {
    private static final String TAG = "PrintService";
    private static final String CHANNEL_ID = "printservice_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Print Service")
                .setContentText("Starting background services...")
                .setSmallIcon(R.drawable.ic_launcher)
                .build();

        startForeground(1, notification);
        Log.i(TAG, "PrintService created");
        startPythonServices();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startPythonServices() {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "sh /data/local/tmp/start_all.sh"});
                int exitCode = process.waitFor();
                Log.i(TAG, "start_all.sh exitCode=" + exitCode);
            } catch (Exception e) {
                Log.e(TAG, "Failed to start Python services", e);
            }
        }).start();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Print Service",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "PrintService destroyed");
    }
}
