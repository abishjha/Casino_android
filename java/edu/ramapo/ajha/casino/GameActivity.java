package edu.ramapo.ajha.casino;

/*
 ************************************************************
 * Name:     Abish Jha                                      *
 * Project:  Casino                                         *
 * Date:     November 20, 2018                              *
 ************************************************************
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedList;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private Game newGame;
    private LinkedList<String> selections;
    private boolean hand_card;
    private Move help_move;
    private boolean stats_click;

    private static final String HUMAN = "Human";
    private static final String COMPUTER = "Computer";
    private static final String TABLE = "Table";
    private static final String BUTTON = "Button";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // if a new game is selected
        if ((getIntent().getStringExtra("game")).equals("1")) {
            newGame = new Game();
            newGame.set_current_player(getIntent().getStringExtra("firstPlayer"));
        } else if ((getIntent().getStringExtra("game")).equals("2")) {
            String file = getIntent().getStringExtra("filepath");
            newGame = new Game(file);
        }

        selections = new LinkedList<>();

        setup_screen();
        refresh_screen();
    }


    /**
     sets the passed in image to the passed in image button as its content
     @param element_name :-> the name of the element of the GUI whose content is to be changed
     @param img_name :-> the name of the image in the drawable folder of which to add the image
     */
    private void set_image(String element_name, String img_name) {
        ImageButton btn = (ImageButton) findViewById(get_id(element_name));
        img_name = img_name.trim().toLowerCase();
        int resID = getResources().getIdentifier(img_name, "drawable", getPackageName());

        btn.setImageResource(resID);
        btn.setBackgroundResource(0);
    }


    /**
     get id for elements on the screen that need to have listeners registered
     @param name :-> the name of the element of the GUI whose id is to be generated
     @return an integer id for the passed in name
     */
    private int get_id(String name) {
        int val = 0;

        // give them a multiple of 100 value depending on the class of element
        if (name.contains(COMPUTER))
            val = 100;
        else if (name.contains(HUMAN))
            val = 200;
        else if (name.contains(TABLE))
            val = 300;
        else if (name.contains(BUTTON))
            val = 400;

        // add unique number from end
        name = name.replaceAll("[^0-9]", "");
        val += Integer.parseInt(name);

        return val;
    }


    /**
     setup the holders and listeners for both player's cards, set up the console, and the move buttons
     */
    public void setup_screen() {
        setup_computer_cards();
        setup_human_cards();
        setup_move_buttons();
        setup_console();
    }


    /**
     setup the holders and listeners for computer player's cards
     */
    public void setup_computer_cards() {
        TableLayout board = findViewById(R.id.computerLayout);

        TableRow row = new TableRow(this);

        TextView text = new TextView(this);
        text.setText("Computer\nHand");
        text.setTextSize(1, 25);
        text.setTextColor(Color.BLACK);
        row.addView(text);

        for (int i = 0; i < 4; i++) {
            ImageButton btn = new ImageButton(this);
            btn.setId(get_id(COMPUTER + i));
            btn.setOnClickListener(this);

            row.addView(btn); // add button to row
        }

        board.addView(row);
    }


    /**
     setup the holders and listeners for human player's cards
     */
    public void setup_human_cards() {
        TableLayout board = findViewById(R.id.humanLayout);

        TableRow row = new TableRow(this);

        TextView text = new TextView(this);
        text.setText("Human\nHand   ");
        text.setTextSize(1, 25);
        text.setTextColor(Color.BLACK);
        row.addView(text);

        for (int i = 0; i < 4; i++) {
            ImageButton btn = new ImageButton(this);
            btn.setId(get_id(HUMAN + i));
            btn.setOnClickListener(this);

            row.addView(btn); // add button to row
        }

        board.addView(row);
    }


    /**
     setup the holders and listeners for the seven move buttons
     */
    public void setup_move_buttons() {
        TableLayout buttons = findViewById(R.id.buttonLayout);

        // these buttons appear in this order in the display
        String[] button_actions = {
                Move.ACTION_CAPTURE,
                Move.ACTION_BUILD,
                Move.ACTION_EXTEND,
                Move.ACTION_MULTI,
                Move.ACTION_TRAIL,
                "GET  HELP MOVE",
                "MAKE HELP MOVE"
        };

        for (int i = 0; i < button_actions.length; i++) {
            TableRow row = new TableRow(this);

            Button btn = new Button(this);
            btn.setText(button_actions[i]);
            btn.setTextSize(1, 25);
            btn.setLayoutParams(new TableRow.LayoutParams(180, 80));
            btn.setId(get_id(BUTTON + i));
            btn.setOnClickListener(this);

            // disable the make help move button.  only activate when get help move is performed
            if (i == 6) btn.setEnabled(false);

            row.addView(btn);
            buttons.addView(row);
        }
    }


    /**
     setup the holders and listeners for the interactive on-screen console
     */
    public void setup_console() {
        EditText cons = findViewById(R.id.console);
        cons.setOnClickListener(this);
        cons.setWidth(250);
        cons.setBackgroundColor(Color.BLACK);
        cons.setTextColor(Color.WHITE);
    }


    /**
     add text to console and scroll to bottom to make text visible
     @param text :-> the text to be added to the console
     */
    public void add_to_console(String text) {
        EditText cons = findViewById(R.id.console);
        cons.append(text + "\n\n");

        cons.setSelection(cons.getText().length());
        hide_keyboard(this);
    }


    /**
     check the game state and load the appropriate activity i.e. round end, tournament end, continue play, refresh elements on screen
     */
    public void refresh_screen() {
        // 1 -- nothing.  2 -- round has ended.  3 -- tournament has ended
        final String status = newGame.check_game_state();
        if(status.charAt(0) == '2' || status.charAt(0) == '3'){
            String[] round_info = status.split("\\|");

            for(String r : round_info)
                System.out.println(r);

            final String round_end_stats =
                    "+-------------\n" +
                    "Round " + (newGame.get_round_number()-1) + " has ended\n\n" +
                    round_info[1].trim() + "\n" +
                    round_info[2].trim() + "\n\n" +
                    round_info[3].trim() + "\n" +
                    round_info[4].trim() + "\n\n" +
                    round_info[5].trim() + "\n" +
                    round_info[6].trim() + "\n\n" +
                    round_info[7].trim() + "\n" +
                    round_info[8].trim() + "\n\n" +
                    "Human Tournament Score: " + newGame.get_human_score() + "\n" +
                    "Computer Tournament Score: " + newGame.get_computer_score() + "\n" +
                    "+-------------";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("--- Round has ended ---");
            final EditText input = new EditText(this);
            input.setText(round_end_stats);
            input.setEnabled(false);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    add_to_console(round_end_stats);

                    // if tournament end, also move to new activity
                    if(status.charAt(0) == '3') {
                        game_end();
                        return;
                    }
                }
            });

            builder.show();
        }

        if(status.charAt(0) != '3') {
            refresh_human_cards();
            refresh_computer_cards();
            refresh_table_cards();
            refresh_current_player();

            selections.clear();
            hand_card = false;
            stats_click = false;

            int btn_id = 406;
            ((Button) findViewById(btn_id)).setEnabled(false);


            // if computer make automatic move
            if(newGame.get_current_player().equals(Player.COMPUTER)){
                add_to_console("computer making move...");
                final int btnID = 405;
                // perform get help move button click (also prints move to console)
                ((Button) findViewById(btnID)).performClick();
                // perform the make help move button click
                ((Button) findViewById(btnID + 1)).performClick();
                add_to_console("human's move...");
            }
        }
    }


    /**
     refresh the human hand cards to reflect on the holders
     */
    public void refresh_human_cards() {
        // setting up human cards
        LinkedList<String> human_hand = newGame.get_human_hand();
        for (int i = 0; i < 4; i++) {
            if (human_hand.size() <= i)
                set_image(HUMAN + i, "blankcard");
            else
                set_image(HUMAN + i, human_hand.get(i));
        }
    }


    /**
     refresh the computer hand cards to reflect on the holders
     */
    public void refresh_computer_cards() {
        // setting up computer cards
        LinkedList<String> comp_hand = newGame.get_computer_hand();
        for (int i = 0; i < 4; i++) {
            if (comp_hand.size() <= i)
                set_image(COMPUTER + i, "blankcard");
            else
                set_image(COMPUTER + i, comp_hand.get(i));
        }
    }


    /**
     build places for and add the table cards along with the build cards on the table
     */
    public void refresh_table_cards() {
        TableLayout board = findViewById(R.id.tableLayout);
        board.removeAllViews();

        LinkedList<String> loose_cards = newGame.get_loose_cards();
        LinkedList<String> build_cards = newGame.get_build_cards();

        // total number of cards on table
        int total_count = loose_cards.size() + build_cards.size();
        int element_count = 0, row_count = 0;

        TableRow row = new TableRow(this);

        while (element_count < total_count) {
            // make button
            View btn;

            // for the loose cards -- with display for cards
            if (element_count < loose_cards.size()) {
                btn = new ImageButton(this);

                // set the proper image on the button
                String img_name = loose_cards.get(element_count).trim().toLowerCase();
                int resID = getResources().getIdentifier(img_name, "drawable", getPackageName());
                ((ImageButton) btn).setImageResource(resID);
            }
            // for builds -- just text is displayed
            else {
                btn = new Button(this);

                String build = build_cards.get(element_count - loose_cards.size());
                ((Button) btn).setText(build + "\n\nsum: " + newGame.get_build_sum(build));
                ((Button) btn).setTextSize(1, 15);
                ((Button) btn).setTextColor(Color.BLACK);

                ((Button) btn).setHeight(138);
                ((Button) btn).setWidth(80);
            }

            btn.setId(get_id(TABLE + element_count));
            btn.setOnClickListener(this);
            row.addView(btn); // add button to row
            element_count++;

            if (element_count % 4 == 0) {
                board.addView(row);

                row_count++;
                row = new TableRow(this);
            } else if (element_count == total_count)
                board.addView(row);
        }
    }


    /**
     refresh the current player display to display 'H' for human, and 'C' for computer
     */
    public void refresh_current_player(){
        TextView label = (TextView) findViewById(R.id.currentPlayer);
        label.setTextSize(1, 20);
        label.setTextColor(Color.BLACK);
        label.setBackgroundColor(Color.GREEN);
        label.setEnabled(false);

        if(newGame.get_current_player().equals(Player.HUMAN))
            label.setText("H");
        else
            label.setText("C");
    }


    /**
     select the passed in card visually by setting a background border
     @param resID :-> the element's id whose style we are to add
     */
    public void select_card_visual(int resID) {
        View btn = findViewById(resID);
        btn.setBackgroundResource(R.drawable.button_border);
    }


    /**
     deselect the passed in card visually by removing the background border
     @param resID :-> the element's id whose style we are to remove
     */
    public void deselect_card_visual(int resID) {
        View btn = findViewById(resID);
        btn.setBackgroundResource(0);
    }


    /**
     handle on click events
     @param view :-> the view in focus when the event happened
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();

        // only shows stats on subsequent double click
        if (id == R.id.console) {
            if (stats_click) {
                add_to_console("+-------------\nRound: " + newGame.get_round_number());
                add_to_console("Human Pile: " + newGame.get_human_pile());
                add_to_console("Computer Pile: " + newGame.get_computer_pile());
                add_to_console("Deck: " + newGame.get_game_deck());
                add_to_console("Human Score: " + newGame.get_human_score() + "\n" +
                                    "Computer Score: " + newGame.get_computer_score() +
                                    "\n+-------------");

                stats_click = false;
            } else
                stats_click = true;

            hide_keyboard(this);
            return;
        }

        int index;
        String card;
        switch (id / 100) {
            case 1: // computer card
                // dont let human select computers card
                if (newGame.get_current_player().equals(Player.HUMAN)) return;

                index = id % 10;
                if (index >= newGame.get_computer_hand().size()) return;
                card = newGame.get_computer_hand().get(index);

                if (selections.contains(card)) {
                    refresh_screen();
                } else if (hand_card) {
                    return;
                } else {
                    selections.add(card);
                    select_card_visual(id);
                    hand_card = true;
                }
                break;
            case 2: // human card
                // dont let computer select humans card
                if (newGame.get_current_player().equals(Player.COMPUTER)) return;

                index = id % 10;
                if (index >= newGame.get_human_hand().size()) return;
                card = newGame.get_human_hand().get(index);

                if (selections.contains(card)) {
                    refresh_screen();
                } else if (hand_card) {
                    return;
                } else {
                    selections.add(card);
                    select_card_visual(id);
                    hand_card = true;
                }
                break;
            case 3: // table card
                if (!hand_card) {
                    add_to_console("select a hand card first");
                    return;
                }

                index = id % 100;
                card = newGame.get_table_cards().get(index);

                if (selections.contains(card)) {
                    selections.remove(card);
                    deselect_card_visual(id);
                } else {
                    selections.add(card);
                    select_card_visual(id);
                }
                break;
            case 4: // action button
                index = id % 10;

                // "GET  HELP MOVE"
                if (index == 5) {
                    help_move = newGame.get_move();
                    add_to_console(help_move.pretty_str());
                    ((Button) findViewById(id+1)).setEnabled(true);
                    return;
                }

                String[] actions = {
                        Move.ACTION_CAPTURE,
                        Move.ACTION_BUILD,
                        Move.ACTION_EXTEND,
                        Move.ACTION_MULTI,
                        Move.ACTION_TRAIL
                };

                Move to_do;

                // "MAKE HELP MOVE"
                if (index == 6) {
                    to_do = help_move;
                } else {
                    if(selections.isEmpty() || selections.peekFirst().isEmpty() || selections.size() <= 0) {
                        add_to_console("select cards to play");
                        return;
                    }

                    to_do = new Move(actions[index], selections.pop(), selections);
                }

                if (newGame.make_move(to_do))
                    add_to_console("move successful");
                else
                    add_to_console("move unsuccessful");
                refresh_screen();
        }
    }


    /**
     save the current game. exit if save successful, do not if not successful
     @param view :-> the view in focus when the event happened
     */
    public void save_button(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("enter a name for the file");

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filename = input.getText().toString() + ".txt";
                String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + filename;

                if (newGame.save_game(filepath)) {
                    // save was a success
                    add_to_console("save was a success");
                    System.exit(1);
                }
                else
                    add_to_console("save was not a success");
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    /**
     move to the next screen when this function is called. only called when tournament is over
     */
    public void game_end(){
        // move to the next activity
        Intent intent = new Intent(this, FinishActivity.class);
        intent.putExtra("human_score", newGame.get_human_score());
        intent.putExtra("computer_score", newGame.get_computer_score());

        startActivity(intent);
    }


    /**
     hide the soft input key keyboard that appears on the screen after operations on 'EditText' view
     @param activity :-> the current activity
     */
    public static void hide_keyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}