package com.example.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;

public class LocalActivity extends MainActivity {

    private TextView playerText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_view);

        playerText = findViewById(R.id.playerText);
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

        Bundle extras = getIntent().getExtras();
        withComputer = (Boolean) extras.get("WITH_COMP");

        final TextView[] squares = {square01, square02, square03, square11, square12, square13, square21, square22, square23};

        for (final TextView square : squares) {
            square.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (square.getText() == "") {
                        if (playerText.getText().toString().contains("turn")) {
                            square.setText(currChar);
                            checkWinner();
                            if (playerText.getText().toString().contains("turn")){
                                changePlayer();
                            }
                        }
                    }
                }
            });
        }
    }

    private void changePlayer() {
        if (currPlayer == "1") {
            currPlayer = "2";
            currChar = "X";
            Log.i("withComp", String.valueOf(withComputer));
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

                if (symbol1 == "O") {
                    playerText.setText("Player 1 won!");
                    return;
                } else {
                    playerText.setText("Player 2 won!");
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

}
