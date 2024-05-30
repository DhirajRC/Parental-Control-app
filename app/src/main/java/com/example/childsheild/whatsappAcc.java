package com.example.childsheild;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.telephony.SmsManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class whatsappAcc extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() != null && event.getPackageName().equals("com.whatsapp")) {
            // Instagram app is open
            Toast.makeText(this, "Whatsapp app is open", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "Instagram app is open", Toast.LENGTH_SHORT).show();
            String msg="Whatsapp app is open";
            String number= "9284855957";
//                String msg=message.getText().toString();
            try {
                SmsManager smsManager=SmsManager.getDefault();
                smsManager.sendTextMessage(number,null,msg,null,null);
                Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_LONG).show();
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Some fiedls is Empty",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.packageNames = new String[]{"com.whatsapp"};
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }
}
