package com.example.childsheild;


import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyBackgroundService extends Service {

    private static final String TAG = "MyBackgroundService";
    private static final int UPDATE_INTERVAL = 10000; // Update interval in milliseconds (5 seconds)
    private Handler handler;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        handler = new Handler();
        startForegroundService();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            trackAppUsage();
            handler.postDelayed(this, UPDATE_INTERVAL);
        }
    };

    void startForegroundService() {
        // Create a notification for the foreground service
        Notification notification = createNotification();

        // Start the service as a foreground service
        startForeground(2, notification);
        handler.postDelayed(runnable, UPDATE_INTERVAL); // Start the repeating task
    }

    void stopRepeatingTask() {
        handler.removeCallbacks(runnable);
    }

    private void trackAppUsage() {

        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - 12 * 60 * 60 * 1000; // Track usage for the last 24 hours

        // Get the usage stats for the given time range
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, currentTime);

        // Process the usage stats
        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            // Map to store app package names and their total usage time
            SortedMap<String, Long> usageMap = new TreeMap<>();
            for (UsageStats usageStats : usageStatsList) {
                if (usageStats.getTotalTimeInForeground() > 0) {
                    String packageName = usageStats.getPackageName();
                    long totalUsageTime = usageStats.getTotalTimeInForeground();
                    if (usageMap.containsKey(packageName)) {
                        totalUsageTime += usageMap.get(packageName);
                    }
                    usageMap.put(packageName, totalUsageTime);
                }
            }

            List<String> usageInfo = new ArrayList<>();

            // Store the usage data in Firestore
            for (String packageName : usageMap.keySet()) {
                long usageTime = usageMap.get(packageName);
                long hours = usageTime / (1000 * 60 * 60);
                long minutes = (usageTime % (1000 * 60 * 60)) / (1000 * 60);
                long seconds = ((usageTime % (1000 * 60 * 60)) % (1000 * 60)) / 1000;
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                // Here you can store the packageName and formattedTime in Firestore
//                Log.d(TAG, "Package: " + packageName + ", Usage Time: " + formattedTime);

                usageInfo.add(packageName + " - " + formattedTime);


                DocumentReference m = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());



                if (hours >= 1) {
                    Log.d(TAG, "Package: " + packageName + ", Usage Time: " + formattedTime);

//                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Youralready used morthat "+formattedTime, Toast.LENGTH_SHORT).show();
                    usageInfo.add(packageName + " - " + formattedTime);
//                    performGlobalAction(GLOBAL_ACTION_BACK); // Simulate back button press to prevent launch
//                    packageName.back();
//                    showPersistentDialog(packageName, formattedTime);


                    m.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

//                        UserInfoHolder userInfoHolder  = documentSnapshot.toObject(UserInfoHolder.class);
                            Log.d("AGAAAAAAAAAAAAAAAAA", "onSuccess: "+documentSnapshot.getString("mobileNo"));
                            String mobile = documentSnapshot.getString("mobileNo");
//                        Toast.makeText(MainDash.this, ""+userInfoHolder.getMobileNo(), Toast.LENGTH_SHORT).show();{

                                String msg= "hii";
//                    String number= mobile.getText.t;
//                String msg=message.getText().toString();
                                try {
                                    SmsManager smsManager=SmsManager.getDefault();
                                    smsManager.sendTextMessage(documentSnapshot.getString("mobileNo"),null,msg,null,null);
                                    Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_LONG).show();
                                }catch (Exception e)
                                {
                                    Toast.makeText(getApplicationContext(),"Some fiedls is Empty",Toast.LENGTH_LONG).show();
                                }
                            }

                    });


                }



                String UserID = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference = mFirestore.collection("AppData").document(UserID);

                Map<String , Object> usegedata = new HashMap<>();
                usegedata.put("APPDATA", usageInfo);
                documentReference.set(usegedata).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                        Log.d("TAG", "Success");
                        Toast.makeText(MyBackgroundService.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });



//                DocumentReference documentReference1 = mFirestore.collection("Users").document(UserID);
//                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                String mobileNo = document.getString("mobileNo");
//                                if (mobileNo != null) {
//                                    // Use mobileNo as needed
//                                    Log.d("TAG", "Mobile Number: " + mobileNo);
////                                    Toast.makeText(Registration.this, "Mobile Number: " + mobileNo, Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Log.d("TAG", "Mobile Number not found");
////                                    Toast.makeText(Registration.this, "Mobile Number not found", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Log.d("TAG", "No such document");
////                                Toast.makeText(Registration.this, "No such document", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Log.d("TAG", "get failed with ", task.getException());
////                            Toast.makeText(Registration.this, "Failed to get mobile number", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });



            }
        }
    }

    private Notification createNotification() {
        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("App Usage Tracker")
                .setContentText("Tracking app usage...")
                .setSmallIcon(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal);

        // Set the notification to be a foreground service notification
        builder.setPriority(NotificationCompat.PRIORITY_MIN);
        builder.setOngoing(true); // Make the notification ongoing
        return builder.build();
    }


}
