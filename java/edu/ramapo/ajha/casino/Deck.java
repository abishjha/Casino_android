package edu.ramapo.ajha.casino;

/*
 ************************************************************
 * Name:     Abish Jha                                      *
 * Project:  Casino                                         *
 * Class:    CMPS 366 Organization of Programming Languages *
 * Date:     November 20, 2018                              *
 ************************************************************
 */

import android.os.Environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Deck {
    static final char SPADE = 'S';
    static final char HEART = 'H';
    static final char DIAMOND = 'D';
    static final char CLUB = 'C';

    private LinkedList<String> cards;
    private final boolean LOAD_FROM_FILE = false;
    private final String DECK_FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/deck.txt";


    /**
     deck class constructor
     */
    public Deck(){
        cards = new LinkedList<>();

        if(LOAD_FROM_FILE){
            if(load_deck_from_file(DECK_FILE_PATH))
               return;
        }

        System.out.println("loading a random fresh deck");
        String[] suite = { SPADE+"", HEART+"", DIAMOND+"", CLUB+"" };
        String[] card = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K" };

        // insert 52 cards into the deck list
        for (String s : suite) {
            for (String c : card) {
                cards.add(s + c);
            }
        }

        // shuffle the cards
        Collections.shuffle(cards);

        if(check_for_duplicates()){
            System.out.println("there are duplicate cards in the deck");
            cards.clear();
        }
    }


    /**
     loads the deck from a new line separated file
     @param filepath :-> the path of the file containing the deck
     @return true if successful in loading the file, false otherwise
     */
    private boolean load_deck_from_file(String filepath){
        try {
            FileReader in = new FileReader(filepath);
            BufferedReader reader = new BufferedReader(in);

            String oneLine;
            while((oneLine = reader.readLine()) != null) {
                cards.add(oneLine.trim().split(" +")[0]);
            }
            reader.close();

            if(check_for_duplicates())
                throw new Exception("there are duplicate cards in the deck");

            return true;
        }
        catch(Exception ex){
            System.out.println(ex.toString());
        }

        return false;
    }


    /**
     deck class constructor
     @param deck_cards :-> a string containing the cards in the deck separated by space
     */
    public Deck(String deck_cards){
        cards = new LinkedList<>(Arrays.asList(deck_cards.trim().split(" +")));

        if(check_for_duplicates()){
            System.out.println("there are duplicate cards in the deck");
            cards.clear();
        }
    }


    /**
     check if deck has any duplicate items
     @return true if deck has duplicates, false otherwise
     */
    private boolean check_for_duplicates(){
        Set inputSet = new HashSet(cards);
        // returns true if deck has duplicate cards
        return (inputSet.size() < cards.size());
    }


    /**
     remove and get the next card from the top of the deck
     @return a string representing the card that is taken
     */
    public String get_one(){
        return cards.pop();
    }


    /**
     check if deck has any more cards
     @return true if deck is empty, false otherwise
     */
    public boolean empty(){
        if(cards.isEmpty() || cards.pop().isEmpty() || cards.size() <= 0)
            return true;
        return false;
    }


    /**
     get the deck as a list
     @return a linked list containing the cards in order in the deck
     */
    public LinkedList<String> get_deck(){
        return ((LinkedList) cards.clone());
    }

}
