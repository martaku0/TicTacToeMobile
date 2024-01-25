package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button onlineBtn;
    private Button localBtn;
    private Button computerBtn;

    protected Boolean withComputer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onlineBtn = findViewById(R.id.onlineBtn);
        localBtn = findViewById(R.id.localBtn);
        computerBtn = findViewById(R.id.computerBtn);

        withComputer = false;

        localBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withComputer = false;
                startLocal();
            }
        });

        computerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withComputer = true;
                startComputer();
            }
        });

        onlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOnline();
            }
        });
    }

    private void startLocal(){
        Intent localAct = new Intent(MainActivity.this, LocalActivity.class);
        localAct.putExtra("WITH_COMP", withComputer);
        startActivity(localAct);
    }

    private void startComputer(){
        Intent localAct = new Intent(MainActivity.this, LocalActivity.class);
        localAct.putExtra("WITH_COMP", withComputer);
        startActivity(localAct);
    }

    private void startOnline(){
        Intent onlineAct = new Intent(MainActivity.this, OnlineActivity.class);
        onlineAct.putExtra("WITH_COMP", false);
        startActivity(onlineAct);
    }
}