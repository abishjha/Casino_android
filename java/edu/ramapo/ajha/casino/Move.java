package edu.ramapo.ajha.casino;

/*
 ************************************************************
 * Name:     Abish Jha                                      *
 * Project:  Casino                                         *
 * Class:    CMPS 366 Organization of Programming Languages *
 * Date:     November 20, 2018                              *
 ************************************************************
 */

import java.util.LinkedList;
import java.util.Objects;

class Move {
    static final String ACTION_BUILD = "build";
    static final String ACTION_CAPTURE = "capture";
    static final String ACTION_TRAIL = "trail";
    static final String ACTION_EXTEND = "extend_build";
    static final String ACTION_MULTI = "multi_build";


    private String action;
    private String hand_card;
    private LinkedList<String> loose_cards;


    /**
     constructor for the move class
     */
    public Move(){
        action = "";
        hand_card = "";
        loose_cards = new LinkedList<>();
    }


    /**
     constructor for the move class
     @param action :-> the action for the move (one of five options)
     @param hand_card :-> the hand card involved in the move
     @param loose_cards :-> the loose cards involved in the move
     */
    public Move(String action, String hand_card, LinkedList<String> loose_cards) {
        set_action(action);
        set_hand_card(hand_card);
        set_loose_cards(loose_cards);
    }


    /**
     setter for action
     @param act :-> the action to be set
     */
    public void set_action(String act){
        action = act;
    }


    /**
     getter for action
     @return a string containing the action
     */
    public String get_action(){
        return action;
    }


    /**
     setter for hand card
     @param card :-> the hand card to be set
     */
    public void set_hand_card(String card){
        hand_card = card;
    }


    /**
     getter for hand card
     @return a string containing the hand card
     */
    public String get_hand_card(){
        return hand_card;
    }


    /**
     setter for loose cards
     @param cards :-> the loose cards to be set
     */
    public void set_loose_cards(LinkedList<String> cards){
        loose_cards = (LinkedList) cards.clone();
    }


    /**
     getter for loose cards
     @return a linked list containing the loose cards
     */
    public LinkedList<String> get_loose_cards(){
        return ((LinkedList) loose_cards.clone());
    }


    /**
     get all the loose cards as a string
     @return a string containing the loose cards as space separated
     */
    public String get_loose_cards_str(){
        String ret = "";
        for(String item : loose_cards)
            ret += item + " ";
        return ret;
    }


    /**
     get all the card involved in the move
     @return a linked list containing all the cards i.e. hand card and loose cards
     */
    public LinkedList<String> get_all_cards(){
        LinkedList<String> all_cards = get_loose_cards();
        all_cards.add(get_hand_card());
        return all_cards;
    }


    /**
     stringify the current move with reasoning for the chosen move
     @return a string containg the move details and the reason in printable form
     */
    public String pretty_str(){
        String reason = "";

        if (Objects.equals(get_action(), ACTION_EXTEND))
            reason = "as a defensive move to potentially stop the other user from capturing his/her build";
        else if (Objects.equals(get_action(), ACTION_MULTI))
            reason = "extend one of our own builds to make a multi build so we can maximize capture card number in the next turn";
        else if (Objects.equals(get_action(), ACTION_BUILD))
            reason = "so we can maximize capture card number in the next turn";
        else if (Objects.equals(get_action(), ACTION_CAPTURE))
            reason = "no suitable build related option so we capture the maximum amount of card, set, and/or build possible";
        else if (Objects.equals(get_action(), ACTION_TRAIL))
            reason = "no better move available";

        return " +--------------------------------------------\n"
                + " | action      : " + get_action() + "\n"
                + " | hand card   : " + get_hand_card() + "\n"
                + " | loose cards : " + get_loose_cards_str() + "\n"
                + " | reason      : " + reason + "\n"
                + " +---------------------------------------------\n";
    }
}
