package com.example.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.metrics.LogSessionId;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OnlineActivity extends MainActivity {
    // TODO: handle no enemy situation ?
    // TODO: revange ?
    private TextView playerText;
    private TextView counter;
    private TextView square01;
    private TextView square02;
    private TextView square03;
    private TextView square11;
    private TextView square12;
    private TextView square13;
    private TextView square21;
    private TextView square22;
    private TextView square23;

    private String currPlayer;
    private String currChar;

    private boolean withComputer;

    private DatabaseReference dbref;
    private DatabaseReference dbref2;
    private String game_user;
    private String game_key;
    private String game_enemy;
    private String game_user_id;
    private boolean isgoing;
    private boolean game_online_going;

    private String url = "https://tictactoe-tctcte-default-rtdb.europe-west1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_view);

        dbref = FirebaseDatabase.getInstance(url)
                .getReference("data");

        dbref2 = FirebaseDatabase.getInstance(url)
                .getReference("moves");

        List<String> a = Arrays.asList("Hello", "World");

        dbref.child("1").setValue(a);

        setupDbListener();

        playerText = findViewById(R.id.playerText);
        counter = findViewById(R.id.counter);
        square01 = findViewById(R.id.square01);
        square02 = findViewById(R.id.square02);
        square03 = findViewById(R.id.square03);
        square11 = findViewById(R.id.square11);
        square12 = findViewById(R.id.square12);
        square13 = findViewById(R.id.square13);
        square21 = findViewById(R.id.square21);
        square22 = findViewById(R.id.square22);
        square23 = findViewById(R.id.square23);

        currPlayer = "1";
        currChar = "O";
        withComputer = false;
        isgoing = false;
        game_online_going = false;
        game_key = "";
        game_enemy = "";
        game_user_id = "";

        TextView[] squares = {square01, square02, square03, square11, square12, square13, square21, square22, square23};

        for (int i = 0; i < squares.length; i++) {
            TextView square = squares[i];
            int finalI = i;
            square.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (square.getText().toString().equals("") && isgoing) {
                        if (playerText.getText().toString().contains("turn")) {
                            square.setText(currChar);
                            checkWinner();
                            if (playerText.getText().toString().contains("turn")){
                                changePlayer();
                            }
                        }
                    }
                    else if(square.getText().toString().equals("") && game_online_going){
                        int id_player = Integer.parseInt(game_user_id)+1;
                        if (playerText.getText().toString().contains("turn") && playerText.getText().toString().contains(String.valueOf(id_player))) {
                            dbref2.child(game_key).setValue(currChar + String.valueOf(finalI));
                        }
                    }
                }
            });
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int counting = Integer.valueOf((String) counter.getText());
                counting = counting - 1;

                int finalCounting = counting;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        counter.setText(String.valueOf(finalCounting));

                        if(game_enemy.equals("")) {
                            if (finalCounting == 0) {
                                counter.setText("Play with computer");
                                withComputer = true;
                                isgoing = true;
                                dbref.child(game_key).removeValue();
                                dbref2.child(game_key).removeValue();
                                timer.cancel();
                            }
                        }
                        else{
                            timer.cancel();
                            counter.setTextSize(20);
                            counter.setText("You are Player " + String.valueOf(Integer.valueOf(game_user_id)+1));
                            game_online_going = true;
                        }
                    }
                });
            }
        }, 0, 1000);

        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!game_key.equals("")){
                            dbref.child(game_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        if(dataSnapshot.getValue() != null){
                                            List<String> value = (List<String>) dataSnapshot.getValue();
                                            if(!game_enemy.equals("") && value.size() < 2){
                                                displayAlert();
                                                timer2.cancel();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("ERROR", "enemy checker error");
                                }
                            });
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void revange(){
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Revange");
            builder.setMessage("Do you want a revange with this player?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    dbref2.child(game_key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Object value = snapshot.getValue();
                            if(value != null){
                                String v = value.toString();
                                if(v.equals("revange")){
                                    dbref2.child(game_key).setValue("start");
                                }
                                else{
                                    dbref2.child(game_key).setValue("revange");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("REVANGE-DATABASE","Database error");
                        }
                    });
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    backToMainMenu();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void displayAlert() {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No enemy");
            builder.setMessage("Your enemy is not here anymore.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    dbref.child(game_key).removeValue();
                    dbref2.child(game_key).removeValue();
                    backToMainMenu();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void setupDbListener(){
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    long list_len = dataSnapshot.getChildrenCount();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        List<String> value = (List<String>) childSnapshot.getValue();
                        if (key != null) {
                            if (value != null && value.size() < 2) {
                                DatabaseReference newChildRef = dbref.push();
                                String user = newChildRef.getKey();
                                game_user = user;
                                List<String> users = new ArrayList<>(value);
                                users.add(user);
                                dbref.child(key).setValue(users);
                                game_key = key;
                                break;
                            } else {
                                if (key.equals(String.valueOf(list_len))) {
                                    DatabaseReference newChildRef = dbref.push();
                                    String user = newChildRef.getKey();
                                    game_user = user;
                                    List<String> users = new ArrayList<>();
                                    users.add(user);
                                    dbref.child(String.valueOf(Integer.valueOf(key) + 1)).setValue(users);
                                    game_key = String.valueOf(Integer.valueOf(key) + 1);
                                    break;
                                } else {
                                    continue;
                                }
                            }

                        } else {
                            DatabaseReference newChildRef = dbref.push();
                            String user = newChildRef.getKey();
                            game_user = user;
                            List<String> users = new ArrayList<>();
                            users.add(user);
                            dbref.child("1").setValue(users);
                            game_key = "1";
                            break;
                        }
                    }

                    if (dataSnapshot.getChildrenCount() == 0) {
                        DatabaseReference newChildRef = dbref.push();
                        String user = newChildRef.getKey();
                        game_user = user;
                        List<String> users = new ArrayList<>();
                        users.add(user);
                        dbref.child("1").setValue(users);
                        game_key = "1";
                    }

                    waitForPlayer();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Error: " + error.getMessage());
            }
        });


    }

    private void waitForPlayer(){
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if(childSnapshot.getValue() != null && childSnapshot.getKey() != null) {
                        String key = childSnapshot.getKey();
                        List<String> value = (List<String>) childSnapshot.getValue();
                        if (key.equals(game_key)) {
                            if (value.size() == 2) {
                                for (String v : value) {
                                    if (!v.equals(game_user)) {
                                        game_enemy = v;
                                        if (game_user_id.equals("")) {
                                            game_user_id = "1";
                                        }
                                        setupDbListenerMoves();
                                        break;
                                    }
                                }
                            } else if (value.size() == 1) {
                                if (game_user_id.equals("")) {
                                    game_user_id = "0";
                                }
                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Error: " + error.getMessage());
            }
        });
    }

    private void changePlayer() {
        if (currPlayer.equals("1")) {
            currPlayer = "2";
            currChar = "X";
            if (withComputer) {
                compTurn();
            }
        } else {
            currPlayer = "1";
            currChar = "O";
        }

        if (playerText.getText().toString().contains("turn")){
            playerText.setText("Player " + currPlayer + " turn");
        }
    }

    private void checkWinner() {
        final TextView[] squares = {square01, square02, square03, square11, square12, square13, square21, square22, square23};
        playerText = findViewById(R.id.playerText);

        int[][] winCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},  // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},  // Columns
                {0, 4, 8}, {2, 4, 6}              // Diagonals
        };

        for (int[] combo : winCombinations) {
            String symbol1 = squares[combo[0]].getText().toString();
            String symbol2 = squares[combo[1]].getText().toString();
            String symbol3 = squares[combo[2]].getText().toString();

            if (!symbol1.isEmpty() && symbol1.equals(symbol2) && symbol2.equals(symbol3)) {
                squares[combo[0]].setBackgroundColor(Color.parseColor("#00FF00"));
                squares[combo[1]].setBackgroundColor(Color.parseColor("#00FF00"));
                squares[combo[2]].setBackgroundColor(Color.parseColor("#00FF00"));

                if (symbol1.equals("O")) {
                    playerText.setText("Player 1 won!");
                    revange();
                    return;
                } else {
                    playerText.setText("Player 2 won!");
                    revange();
                    return;
                }
            }
        }

        for (TextView square : squares) {
            if (square.getText().toString().isEmpty()) {
                return;
            }
        }

        playerText.setText("Tie!");
        revange();
    }

    private void compTurn(){
        if (playerText.getText().toString().contains("turn")) {
            TextView square = think();
            square.setText(currChar);
            checkWinner();
            if (playerText.getText().toString().contains("turn")){
                changePlayer();
            }
        }
    }

    private TextView think() {

        TextView[] squares = {square01, square02, square03, square11, square12, square13, square21, square22, square23};

        for (int i = 0; i < 9; i += 3) {
            if (squares[i].getText().equals("X") && squares[i+1].getText().equals("X") && squares[i + 2].getText()
                    .equals("")) {
                return squares[i + 2];
            }
            if (squares[i + 1].getText()
                    .equals("X") && squares[i + 2].getText()
                    .equals("X") && squares[i].getText()
                    .equals("")) {
                return squares[i];
            }
            if (squares[i].getText()
                    .equals("X") && squares[i + 2].getText()
                    .equals("X") && squares[i + 1].getText()
                    .equals("")) {
                return squares[i + 1];
            }
        }
        for (int i = 0; i < 3; i++) {
            if (squares[i].getText()
                    .equals("X") && squares[i + 3].getText()
                    .equals("X") && squares[i + 6].getText()
                    .equals("")) {
                return squares[i + 6];
            }
            if (squares[i + 3].getText()
                    .equals("X") && squares[i + 6].getText()
                    .equals("X") && squares[i].getText()
                    .equals("")) {
                return squares[i];
            }
            if (squares[i].getText()
                    .equals("X") && squares[i + 6].getText()
                    .equals("X") && squares[i + 3].getText()
                    .equals("")) {
                return squares[i + 3];
            }
        }
        if (squares[0].getText()
                .equals("X") && squares[4].getText()
                .equals("X") && squares[8].getText()
                .equals("")) {
            return squares[8];
        }
        if (squares[4].getText()
                .equals("X") && squares[8].getText()
                .equals("X") && squares[0].getText()
                .equals("")) {
            return squares[0];
        }
        if (squares[8].getText()
                .equals("X") && squares[0].getText()
                .equals("X") && squares[4].getText()
                .equals("")) {
            return squares[4];
        }
        if (squares[2].getText()
                .equals("X") && squares[4].getText()
                .equals("X") && squares[6].getText()
                .equals("")) {
            return squares[6];
        }
        if (squares[4].getText()
                .equals("X") && squares[6].getText()
                .equals("X") && squares[2].getText()
                .equals("")) {
            return squares[2];
        }
        if (squares[6].getText()
                .equals("X") && squares[2].getText()
                .equals("X") && squares[4].getText()
                .equals("")) {
            return squares[4];
        }
        for (int i = 0; i < 9; i += 3) {
            if (squares[i].getText()
                    .equals("O") && squares[i + 1].getText()
                    .equals("O") && squares[i + 2].getText()
                    .equals("")) {
                return squares[i + 2];
            }
            if (squares[i + 1].getText()
                    .equals("O") && squares[i + 2].getText()
                    .equals("O") && squares[i].getText()
                    .equals("")) {
                return squares[i];
            }
            if (squares[i].getText()
                    .equals("O") && squares[i + 2].getText()
                    .equals("O") && squares[i + 1].getText()
                    .equals("")) {
                return squares[i + 1];
            }
        }
        for (int i = 0; i < 3; i++) {
            if (squares[i].getText()
                    .equals("O") && squares[i + 3].getText()
                    .equals("O") && squares[i + 6].getText()
                    .equals("")) {
                return squares[i + 6];
            }
            if (squares[i + 3].getText()
                    .equals("O") && squares[i + 6].getText()
                    .equals("O") && squares[i].getText()
                    .equals("")) {
                return squares[i];
            }
            if (squares[i].getText()
                    .equals("O") && squares[i + 6].getText()
                    .equals("O") && squares[i + 3].getText()
                    .equals("")) {
                return squares[i + 3];
            }
        }
        if (squares[4].getText()
                .equals("O") && squares[8].getText()
                .equals("O") && squares[0].getText()
                .equals("")) {
            return squares[0];
        }
        if (squares[8].getText()
                .equals("O") && squares[0].getText()
                .equals("O") && squares[4].getText()
                .equals("")) {
            return squares[4];
        }
        if (squares[2].getText()
                .equals("O") && squares[4].getText()
                .equals("O") && squares[6].getText()
                .equals("")) {
            return squares[6];
        }
        if (squares[4].getText()
                .equals("O") && squares[6].getText()
                .equals("O") && squares[2].getText()
                .equals("")) {
            return squares[2];
        }
        if (squares[6].getText()
                .equals("O") && squares[2].getText()
                .equals("O") && squares[4].getText()
                .equals("")) {
            return squares[4];
        }
        if (squares[4].getText()
                .equals("")) {
            return squares[4];
        }
        if ((squares[0].getText()
                .equals("O") && squares[8].getText()
                .equals("O")) || (squares[2].getText()
                .equals("O") && squares[6].getText()
                .equals("O"))) {
            for (int i = 1; i < 8; i += 2) {
                if (squares[i].getText()
                        .equals("")) {
                    return squares[i];
                }
            }
        }
        if (squares[0].getText()
                .equals("O") && squares[5].getText()
                .equals("O") && squares[2].getText()
                .equals("")) {
            return squares[2];
        }
        if (squares[0].getText()
                .equals("O") && squares[7].getText()
                .equals("O") && squares[6].getText()
                .equals("")) {
            return squares[6];
        }
        if (squares[2].getText()
                .equals("O") && squares[3].getText()
                .equals("O") && squares[0].getText()
                .equals("")) {
            return squares[0];
        }
        if (squares[2].getText()
                .equals("O") && squares[7].getText()
                .equals("O") && squares[8].getText()
                .equals("")) {
            return squares[8];
        }
        if (squares[6].getText()
                .equals("O") && squares[1].getText()
                .equals("O") && squares[0].getText()
                .equals("")) {
            return squares[0];
        }
        if (squares[6].getText()
                .equals("O") && squares[5].getText()
                .equals("O") && squares[8].getText()
                .equals("")) {
            return squares[8];
        }
        if (squares[8].getText()
                .equals("O") && squares[1].getText()
                .equals("O") && squares[2].getText()
                .equals("")) {
            return squares[2];
        }
        if (squares[8].getText()
                .equals("O") && squares[3].getText()
                .equals("O") && squares[6].getText()
                .equals("")) {
            return squares[6];
        }
        if (squares[1].getText()
                .equals("O") && squares[3].getText()
                .equals("O") && squares[0].getText()
                .equals("")) {
            return squares[0];
        }
        if (squares[1].getText()
                .equals("O") && squares[5].getText()
                .equals("O") && squares[2].getText()
                .equals("")) {
            return squares[2];
        }
        if (squares[7].getText()
                .equals("O") && squares[3].getText()
                .equals("O") && squares[6].getText()
                .equals("")) {
            return squares[6];
        }
        if (squares[7].getText()
                .equals("O") && squares[5].getText()
                .equals("O") && squares[8].getText()
                .equals("")) {
            return squares[8];
        }
        if (squares[0].getText()
                .equals("X") && squares[5].getText()
                .equals("X") && squares[2].getText()
                .equals("")) {
            return squares[2];
        }
        if (squares[0].getText()
                .equals("X") && squares[7].getText()
                .equals("X") && squares[6].getText()
                .equals("")) {
            return squares[6];
        }
        if (squares[2].getText()
                .equals("X") && squares[3].getText()
                .equals("X") && squares[0].getText()
                .equals("")) {
            return squares[0];
        }
        if (squares[2].getText()
                .equals("X") && squares[7].getText()
                .equals("X") && squares[8].getText()
                .equals("")) {
            return squares[8];
        }
        if (squares[6].getText()
                .equals("X") && squares[1].getText()
                .equals("X") && squares[0].getText()
                .equals("")) {
            return squares[0];
        }
        if (squares[6].getText()
                .equals("X") && squares[5].getText()
                .equals("X") && squares[8].getText()
                .equals("")) {
            return squares[8];
        }
        if (squares[8].getText()
                .equals("X") && squares[1].getText()
                .equals("X") && squares[2].getText()
                .equals("")) {
            return squares[2];
        }
        if (squares[8].getText()
                .equals("X") && squares[3].getText()
                .equals("X") && squares[6].getText()
                .equals("")) {
            return squares[6];
        }
        if (squares[4].getText()
                .equals("X")) {
            for (int i = 0; i < 9; i++) {
                if (squares[i].getText()
                        .equals("") && squares[8 - i].getText()
                        .equals("")) {
                    return squares[8 - i];
                }
            }
        }

        for(int i = 0; i<9; i++){
            if (squares[i].getText()
                    .equals("")) {
                return squares[i];
            }
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // usuwanie gracza gdy wyjdzie
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    List<String> value = (List<String>) childSnapshot.getValue();
                    if(key.equals(game_key)){
                        if(value.size() > 0){
                            for (String v: value) {
                                if(v.equals(game_user)){
                                    if(game_user_id.equals("0")){
                                        dbref.child(game_key).child("0").removeValue();
                                        break;
                                    }
                                    else if(game_user_id.equals("1")){
                                        dbref.child(game_key).child("1").removeValue();
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Error: " + error.getMessage());
            }
        });
        super.onDestroy();
    }

    private void backToMainMenu(){
        Intent mainAct = new Intent(OnlineActivity.this, MainActivity.class);
        startActivity(mainAct);
    }

    private void setupDbListenerMoves(){
        TextView[] squares = {square01, square02, square03, square11, square12, square13, square21, square22, square23};

        dbref2.child(game_key).setValue("start");

        dbref2.child(game_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Object value = dataSnapshot.getValue();
                    if(value != null) {
                        if (value.toString().contains("O") || value.toString().contains("X")) {
                            String v = value.toString();
                            String c = String.valueOf(v.charAt(0));
                            int n = v.charAt(1) - '0';
                            if (squares[n].getText().toString().equals("") && game_online_going) {
                                squares[n].setText(c);
                                checkWinner();
                                if (playerText.getText().toString().contains("turn")) {
                                    changePlayer();
                                }
                            }
                        }
                        else if(value.toString().contains("start")){
                            for (int i = 0; i < squares.length; i++) {
                                TextView square = squares[i];
                                square.setBackgroundColor(Color.WHITE);
                                square.setText("");
                                game_online_going = true;
                                playerText.setText("Player 1 turn");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR","Moves database error.");

            }
        });
    }
}
