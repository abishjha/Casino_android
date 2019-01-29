package edu.ramapo.ajha.casino;

/*
 ************************************************************
 * Name:     Abish Jha                                      *
 * Project:  Casino                                         *
 * Date:     November 20, 2018                              *
 ************************************************************
 */

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    //
    // parameters :- view --> the current view
    // returns    :- (null)
    //
    // finish the current activity to return to the main activity
    public void back_button(View view){
        finish();
    }

}
