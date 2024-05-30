package com.example.childsheild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText ULname,ULpass;
    Button Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ULname = findViewById(R.id.LU_Name);
        ULpass = findViewById(R.id.L_Pass);
        Login = findViewById(R.id.LOGIN);
        Button SwitchR = findViewById(R.id.switer);

        SwitchR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1  =new Intent(Login.this,Registration.class);
                startActivity(intent1);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogineUser();
            }
        });

    }
    public void LogineUser()
    {
        String email, pass;
        email = ULname.getText().toString();
        pass = ULpass.getText().toString();

        if (TextUtils.isEmpty(email)) {
            ULname.setError("Email cannot be empty");
            ULpass.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            ULpass.setError("Password cannot be empty");
            ULpass.requestFocus();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, After_Register.class));
                } else {
                    Toast.makeText(Login.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}