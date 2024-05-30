package com.example.childsheild;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class keyloger extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyloger);


        if (!isAccessibilityServiceEnabled()) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        } else {
            startAccessibilityService();
        }

    }
    private void startAccessibilityService() {
        Intent intent = new Intent(this, keyloger.class);
        ComponentName componentName = startService(intent);
        if (componentName != null) {
//            MyAccessibilityService.setTextView(textView);
//            textView.setText(event);
//            MyAccessibilityService.

        }
    }
    private boolean isAccessibilityServiceEnabled() {
        int accessibilityEnabled = Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED, 0);
        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            return services != null && services.toLowerCase().contains(getPackageName().toLowerCase());
        }
        return false;
    }
}
