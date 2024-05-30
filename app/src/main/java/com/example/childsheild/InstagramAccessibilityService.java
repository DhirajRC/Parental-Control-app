package com.example.childsheild;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class InstagramAccessibilityService extends AccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() != null && event.getPackageName().equals("com.instagram.android")) {
            // Instagram app is open
            showToast("Instagram app is open");

        }
    }

    private void showToast(String message) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(InstagramAccessibilityService.this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.packageNames = new String[]{"com.instagram.android"};
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);

    }
}
