package com.example.childsheild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class After_Register extends AppCompatActivity {

    ImageView A,B,c,D,m,ab;
    Button submite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);

        A =findViewById(R.id.AppData);
        B =findViewById(R.id.massage);
        c =findViewById(R.id.calllog);
        D =findViewById(R.id.keys);
        m = findViewById(R.id.map);
        ab = findViewById(R.id.applock);
        submite = findViewById(R.id.movemain);

        submite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent6 = new Intent(After_Register.this,keyloger.class);
                startActivity(intent6);
            }
        });

        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(After_Register.this,AppData.class);
                startActivity(intent5);
            }
        });
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent58 = new Intent(After_Register.this,AppBloack.class);
                startActivity(intent58);
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent54 = new Intent(After_Register.this,Location.class);
                startActivity(intent54);
            }
        });
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent44 = new Intent(After_Register.this,masage.class);
                startActivity(intent44);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(After_Register.this,calllog.class);
                startActivity(intent3);
            }
        });
        D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent48 = new Intent(After_Register.this,Location.class);
                startActivity(intent48);
            }
        });
    }
}