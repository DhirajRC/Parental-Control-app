package com.example.childsheild;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SMSBroadcastReceiver extends BroadcastReceiver {


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String UserID = firebaseAuth.getCurrentUser().getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String messageBody = smsMessage.getMessageBody();
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    Log.d(TAG, "Message from " + sender + ": " + messageBody);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();


                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("Message from",sender);
                    hashMap.put("Massage",messageBody);
                    hashMap.put("userID", FirebaseAuth.getInstance().getUid());
                    hashMap.put("date", new Timestamp(new Date()));

                    db.collection("MessageLog").add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                            }
                        }
                    });

//
//                    DatabaseReference userRefAl = database.getReference(UserID).child("users Massagelog");
//
//                    Map<String, Object> userData = new HashMap<>();
//                    userData.put("Masage From",sender);
//                    userData.put(":  ",messageBody);
//
//                    userRefAl.push().setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful())
//                            {
//                                Log.d("MainActivity", "User data stored successfully");
//                            } else {
//                                Log.w("MainActivity", "Error storing user data", task.getException());
//                            }
//                        }
//                    });
                }
            }
        }
    }
}
