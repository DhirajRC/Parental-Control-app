package com.example.childsheild;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.provider.Settings;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AppBloack extends AppCompatActivity {
    CheckBox insta, snap, whats;
    Button buttonOrder;
    private static final int ACCESSIBILITY_SERVICE_ENABLE_REQUEST = 101;
    private Intent instagramServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bloack);



        insta = findViewById(R.id.checkbox_instagram);
        snap = findViewById(R.id.checkbox_snapchat);
        whats = findViewById(R.id.checkbox_whatsapp);
        buttonOrder = findViewById(R.id.btn_start_service);

        // Create intent for Instagram Accessibility Service
        instagramServiceIntent = new Intent(AppBloack.this, InstagramAccessibilityService.class);

        // Check if the AccessibilityService is enabled
        if (!isAccessibilityServiceEnabled()) {
            // Ask the user to enable the AccessibilityService
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent, ACCESSIBILITY_SERVICE_ENABLE_REQUEST);
        }

        // Set click listener for the button to start services
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (insta.isChecked()) {
                    startService(instagramServiceIntent);
                }
                if (whats.isChecked()) {
                    Intent intent = new Intent(AppBloack.this, whatsappAcc.class);
                    startService(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACCESSIBILITY_SERVICE_ENABLE_REQUEST) {
            if (!isAccessibilityServiceEnabled()) {
                Toast.makeText(this, "Accessibility service is not enabled. App will not work properly.", Toast.LENGTH_SHORT).show();
            }
            // You can remove the else block here because starting services is now handled in the button click listener
        }
    }

    private boolean isAccessibilityServiceEnabled() {
        ComponentName componentName = new ComponentName(this, InstagramAccessibilityService.class);
        String enabledServices = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        return enabledServices != null && enabledServices.contains(componentName.flattenToString());
    }

}