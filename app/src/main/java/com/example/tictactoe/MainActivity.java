package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button onlineBtn;
    private Button localBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onlineBtn = findViewById(R.id.onlineBtn);
        localBtn = findViewById(R.id.localBtn);

        localBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocal();
            }
        });
    }

    private void startLocal(){
        Intent localAct = new Intent(MainActivity.this, LocalActivity.class);
        startActivity(localAct);
    }
}