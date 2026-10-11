package com.example.bootstarter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootStarter";
    private static final String SCRIPT = "/data/local/tmp/start_all.sh";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action) ||
            "android.intent.action.QUICKBOOT_POWERON".equals(action)) {
            Log.i(TAG, "Boot completed, starting print services...");
            new Thread(() -> {
                try { Thread.sleep(10000); } catch (InterruptedException ignored) {}
                runAsRoot("sh " + SCRIPT);
            }).start();
        }
    }

    private void runAsRoot(String cmd) {
        try {
            Process su = Runtime.getRuntime().exec("su");
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(su.getOutputStream()));
            w.write(cmd + "\n");
            w.write("exit\n");
            w.flush();
            w.close();
            su.waitFor();
            Log.i(TAG, "Executed: " + cmd + " exit=" + su.exitValue());
        } catch (Exception e) {
            Log.e(TAG, "Root exec failed: " + cmd, e);
        }
    }
}