package com.example.childsheild;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class keylogerService extends AccessibilityService {
//    TextView textView;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//         textView = textView.findViewById(R.id.textView123);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String UserID = firebaseAuth.getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        List<String> keywords = Arrays.asList("love", "nude");
        List<String> keywords = Arrays.asList(
                "abuse", "addict", "alcohol", "attack", "bully", "cheat", "crash", "crime",
                "cruel", "danger", "death", "depress", "disaster", "drug", "fail", "famine",
                "fight", "fire", "fraud", "gamble", "gun", "hate", "hit", "kill", "lie",
                "lose", "murder", "nude", "pain", "panic", "poison", "porn", "rape",
                "rob", "sex", "shoot", "stab", "steal", "suicide", "terror", "threat",
                "trap", "violence", "war", "weapon"
        );

//        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);


        final int eventType = event.getEventType();
        final int packageevent = event.getEventType();
        String eventText = null;
        String packagename = null;


        switch (eventType) {

            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                eventText = "Typed: ";
                break;
        }

        eventText = eventText + event.getText();
        packagename = String.valueOf(event.getPackageName());

        System.out.println("ACCESSIBILITY SERVICE : " + packagename + "Typed TEXT" + eventText);
//        Toast.makeText(this, "ACCESSIBILITY SERVICE "+eventText, Toast.LENGTH_SHORT).show();
//        textView.setText(eventText);
        List<String> usageInfo = new ArrayList<>();

        for (String keyword : keywords) {
            if (eventText.toString().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println("ALEREt" + keyword);

//                DatabaseReference userRefAl = database.getReference(UserID).child("users Alert ");


                FirebaseFirestore db = FirebaseFirestore.getInstance();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("keyword", keyword);
                hashMap.put("packagename", packagename);
                hashMap.put("userID", FirebaseAuth.getInstance().getUid());
                hashMap.put("date", new Timestamp(new Date()));

                db.collection("KeyAlert").add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful())
                        {
                            Log.d("MainActivity", "User data stored successfully");
                        }
                    }
                });

//                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                DatabaseReference root = db.getReference().child("ASSIGNMENT");
//
//
//                usageInfo.add(keyword + " - " + packagename);
//                Map<String, Object> userData = new HashMap<>();
//                userData.put(keyword,packagename);
////                userData.put("Alert",packagename);
//
//                root.push().setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful())
//                        {
//                            Log.d("MainActivity", "User data stored successfully");
//                        } else {
//                            Log.w("MainActivity", "Error storing user data", task.getException());
//                        }
//                    }
//                })
                break;
            }


        }

    }


    @Override
    public void onInterrupt() {

    }

    @Override
    public void onServiceConnected() {
        AccessibilityServiceInfo info=getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        this.setServiceInfo(info);
    }

}