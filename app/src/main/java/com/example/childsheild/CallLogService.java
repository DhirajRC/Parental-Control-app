package com.example.childsheild;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CallLogService extends Service {

    private static final String TAG = "CallLogService";
    private static final String CHANNEL_ID = "CallLogServiceChannel";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String UserID = firebaseAuth.getCurrentUser().getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private ContentObserver callLogObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(4, createNotification());

        ContentResolver contentResolver = getContentResolver();
        callLogObserver = new CallLogObserver(new Handler());
        contentResolver.registerContentObserver(CallLog.Calls.CONTENT_URI, true, callLogObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callLogObserver != null) {
            getContentResolver().unregisterContentObserver(callLogObserver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class CallLogObserver extends ContentObserver {
        public CallLogObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            getLastCallDetails();
        }
    }

    private void getLastCallDetails() {
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
            int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

            if (numberIndex != -1 && typeIndex != -1 && dateIndex != -1 && durationIndex != -1) {
                String number = cursor.getString(numberIndex);
                String type = cursor.getString(typeIndex);
                long dateMillis = cursor.getLong(dateIndex);
                int durationSeconds = cursor.getInt(durationIndex);

                // Format the date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedDate = dateFormat.format(new Date(dateMillis));

                // Format the duration
                String formattedDuration = String.format("%02d:%02d:%02d",
                        TimeUnit.SECONDS.toHours(durationSeconds),
                        TimeUnit.SECONDS.toMinutes(durationSeconds) % TimeUnit.HOURS.toMinutes(1),
                        durationSeconds % TimeUnit.MINUTES.toSeconds(1));

                Log.d(TAG, "Number: " + number + ", Type: " + type + ", Date: " + formattedDate + ", Duration: " + formattedDuration);


                FirebaseFirestore db = FirebaseFirestore.getInstance();


                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("Numeber",number);
                hashMap.put("Date",formattedDate);
                hashMap.put("Duration",formattedDuration);
                hashMap.put("userID", FirebaseAuth.getInstance().getUid());
                hashMap.put("date", new Timestamp(new Date()));

                db.collection("CallLOG").add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Log.d("MainActivity", "User data stored successfully");
                        }
                    }
                });

//                DatabaseReference userRefAl = database.getReference(UserID).child("users CAllLog ");
//
//                Map<String, Object> userData = new HashMap<>();
//                userData.put("Number",number);
//                userData.put("Date",formattedDate);
//                userData.put("Duration", formattedDuration);
//                userRefAl.push().setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful())
//                        {
//                            Log.d("MainActivity", "User data stored successfully");
//                        } else {
//                            Log.w("MainActivity", "Error storing user data", task.getException());
//                        }
//                    }
//                });

                // Here, you can handle the call log data (e.g., send to a server, store locally, etc.)
            } else {
                Log.e(TAG, "Error retrieving call log details. Column index not found.");
            }
            cursor.close();
        } else {
            Log.e(TAG, "Cursor is null or empty.");
        }
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Call Log Service")
                .setContentText("Monitoring call logs")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Call Log Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
