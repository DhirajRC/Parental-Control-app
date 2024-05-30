package com.example.childsheild;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AppData extends AppCompatActivity {

    private static final int USAGE_STATS_PERMISSION_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_data);

        // Request permission if not granted
        if (!hasUsageStatsPermission()) {
            requestUsageStatsPermission();
        }
    }

    public void startBackgroundService(View view) {
        if (hasUsageStatsPermission()) {
            Intent serviceIntent = new Intent(this, MyBackgroundService.class);
            startService(serviceIntent);
        } else {
            Toast.makeText(this, "Please grant usage stats permission to track app usage.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void requestUsageStatsPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivityForResult(intent, USAGE_STATS_PERMISSION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USAGE_STATS_PERMISSION_REQUEST) {
            if (hasUsageStatsPermission()) {
                // Permission granted, start the background service
                Intent intent1 = new Intent(AppData.this,After_Register.class);
                startActivity(intent1);
                Intent serviceIntent = new Intent(this, MyBackgroundService.class);
                startService(serviceIntent);
            } else {
                Toast.makeText(this, "Usage stats permission not granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
