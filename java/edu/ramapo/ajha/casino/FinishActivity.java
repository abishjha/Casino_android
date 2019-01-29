package edu.ramapo.ajha.casino;

/*
 ************************************************************
 * Name:     Abish Jha                                      *
 * Project:  Project 1 - Konane                             *
 * Class:    CMPS 331 Artificial Intelligence I             *
 * Date:     February 2, 2018                               *
 ************************************************************
*/

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FinishActivity extends AppCompatActivity {

    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        displayStats(this.getIntent());
    }


    //
    // parameters :- intent --> the current intent
    // returns    :- (null)
    //
    public void displayStats(Intent intent){
        int human_s = intent.getIntExtra("human_score", 0);
        int computer_s = intent.getIntExtra("computer_score", 0);

        String message = "Human got " + human_s + " points\nComputer got " + computer_s + " points\n\n";

        if(human_s == computer_s) {
            message += " | IT IS A TIE | ";
        } else if(human_s > computer_s) {
            message += " | Human wins | ";
        } else if(human_s < computer_s) {
            message += " | Computer wins | ";
        }

        ((TextView) findViewById(R.id.stats_display)).setText(message);
    }


    //
    // parameters :- view --> the current view
    // returns    :- (null)
    //
    public void new_game(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //
    // parameters :- view --> the current view
    // returns    :- (null)
    //
    public void restart(View view){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("game", "1"); // means a new game
        intent.putExtra("firstPlayer", Player.HUMAN); // let human play first
        startActivity(intent);
    }
}
