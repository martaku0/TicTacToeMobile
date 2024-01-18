package com.example.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class LocalActivity extends MainActivity{

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

        final TextView[] squares = { square01, square02, square03 ,square11,square12,square13, square21,square22, square23};

        for (final TextView square : squares) {
            square.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(square.getText() == ""){
                        if(playerText.getText().toString().contains("turn")){
                            square.setText(currChar);
                            changePlayer();
                            checkWinner();
                        }
                    }
                }
            });
        }
    }

    private void changePlayer(){
        if(currPlayer == "1"){
            currPlayer = "2";
            currChar = "X";
        }
        else{
            currPlayer = "1";
            currChar = "O";
        }

        playerText.setText("Player " + currPlayer + " turn");
    }

    private void checkWinner(){
        final TextView[] squares = { square01, square02, square03 ,square11,square12,square13, square21,square22, square23};
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

                if(symbol1 == "O"){
                    playerText.setText("Player 1 won!");
                    return;
                }
                else{
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
}
