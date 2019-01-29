package edu.ramapo.ajha.casino;

/*
 ************************************************************
 * Name:     Abish Jha                                      *
 * Project:  Casino                                         *
 * Date:     November 20, 2018                              *
 ************************************************************
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    //
    // parameters :- view --> the current view
    // returns    :- (null)
    //
    public void start_button(View view) {
        final Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("game", "1"); // means a new game

        chooseBlackPlayer(intent);
    }


    //
    // parameters :- intent --> the intent for the new game
    // returns    :- (null)
    //
    private void chooseBlackPlayer(final Intent intent) {
        final Dialog tilePicker = new Dialog(this);
        tilePicker.setTitle("guess which stone is black to play first");
        tilePicker.setContentView(R.layout.dialog_black_player);

        Random randomGenerator = new Random();
        final int choice = randomGenerator.nextInt(2) + 1;

        final ImageButton button1 = tilePicker.findViewById(R.id.choice1);
        final ImageButton button2 = tilePicker.findViewById(R.id.choice2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choice == 1) {
                    intent.putExtra("firstPlayer", Player.HUMAN);
                    ((TextView) tilePicker.findViewById(R.id.choiceDisplay)).setText("Congrats! Human plays first");
                    button1.setBackgroundResource(R.drawable.black_piece);
                    button2.setBackgroundResource(R.drawable.white_piece);
                } else {
                    intent.putExtra("firstPlayer", Player.COMPUTER);
                    ((TextView) tilePicker.findViewById(R.id.choiceDisplay)).setText("Sorry! Computer plays first");
                    button1.setBackgroundResource(R.drawable.white_piece);
                    button2.setBackgroundResource(R.drawable.black_piece);
                }

                button1.setClickable(false);
                button2.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                }, 1000);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choice == 2) {
                    intent.putExtra("firstPlayer", Player.HUMAN);
                    ((TextView) tilePicker.findViewById(R.id.choiceDisplay)).setText("Congrats! Human plays first");
                    button1.setBackgroundResource(R.drawable.white_piece);
                    button2.setBackgroundResource(R.drawable.black_piece);
                } else {
                    intent.putExtra("firstPlayer", Player.COMPUTER);
                    ((TextView) tilePicker.findViewById(R.id.choiceDisplay)).setText("Sorry! Computer plays first");
                    button1.setBackgroundResource(R.drawable.black_piece);
                    button2.setBackgroundResource(R.drawable.white_piece);
                }

                button1.setClickable(false);
                button2.setClickable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                }, 1000);
            }
        });

        tilePicker.show();
    }


    //
    // parameters :- view --> the current view
    // returns    :- (null)
    //
    public void load_button(View view) {
        final String[] files = getFiles();

        AlertDialog.Builder filePicker = new AlertDialog.Builder(this);
        filePicker.setTitle("pick a file (../Documents)");
        filePicker.setItems(files, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String fileName = files[i];
                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + fileName;

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("game", "2"); // means a loaded game
                intent.putExtra("filepath", filePath);
                startActivity(intent);
            }
        });
        filePicker.show();
    }


    //
    // parameters :- (null)
    // returns    :- an array of String with the filenames for all files in the specified directory
    //
    private String[] getFiles() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString());
        String[] fileNames = directory.list();
        return fileNames;
    }


    //
    // parameters :- view --> the current view
    // returns    :- (null)
    //
    public void question_button(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}
