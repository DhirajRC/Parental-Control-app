package com.example.childsheild;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Registration extends AppCompatActivity {

    EditText adress , name, mobileno,usename, password;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Button reg;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth =FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        reg = findViewById(R.id.Register);
        adress= findViewById(R.id.Adress);
        name = findViewById(R.id.FullNAme);
        mobileno = findViewById(R.id.Mobile);
        usename = findViewById(R.id.Uname);
        password =findViewById(R.id.Pass);
        Button SwitchL = findViewById(R.id.switel);

        SwitchL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1  =new Intent(Registration.this,Login.class);
                startActivity(intent1);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent  =new Intent(Registration.this,Login.class);
//                startActivity(intent);
                CreateUser();
            }
        });
    }
    public void CreateUser()
    {
        String email, pass,mobile,full_Name,Full_Adress;
        email= usename.getText().toString();
        pass = password.getText().toString();
        mobile= mobileno.getText().toString();
        full_Name=name.getText().toString();
        Full_Adress=adress.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Enter Email Id", Toast.LENGTH_SHORT).show();
//            return;
        }
        if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
//            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(Registration.this, "Successful", Toast.LENGTH_SHORT).show();
                    userID=firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("fullName", full_Name);
                    user.put("mobileNo", mobile);
                    user.put("fullAdress", Full_Adress);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Log.d("TAG", "Success"+userID);
                            Intent intent = new Intent(Registration.this, After_Register.class);
                            startActivity(intent);

//                            String str = mobileno.getText().toString();
//                            Intent intent2 = new Intent(Registration.this,After_Register.class);
//                            intent2.putExtra("m_no", str);
                        }
                    });
                }
                else
                {
                    Toast.makeText(Registration.this, "Faild", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
