package com.if_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.if_connect.R;

public class MainActivity extends AppCompatActivity {

    Context context;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        token = getIntent().getStringExtra("AUTH_TOKEN");

    }
}