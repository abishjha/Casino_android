package edu.ramapo.ajha.casino;

/*
 ************************************************************
 * Name:     Abish Jha                                      *
 * Project:  Casino                                         *
 * Date:     November 20, 2018                              *
 ************************************************************
 */

import java.util.Arrays;
import java.util.LinkedList;

class Player {
   private LinkedList<String> card_hand;
   private LinkedList<String> card_captured;
   private int tournament_score;
   private boolean is_human_flag; // true for human

   static final String HUMAN = "Human";
   static final String COMPUTER = "Computer";


   /**
    constructor for player class
    */
   public Player() {
      is_human_flag = true;
      set_tournament_score(0);
      card_hand = new LinkedList<>();
      card_captured = new LinkedList<>();
   }


   /**
    constructor for player class
    @param is_human :-> a boolean representing if this player is human. true for human, false otherwise
    */
   public Player(boolean is_human) {
      is_human_flag = is_human;
      set_tournament_score(0);
      card_hand = new LinkedList<>();
      card_captured = new LinkedList<>();
   }


   /**
    constructor for player class
    @param is_human :-> a boolean representing if this player is human. true for human, false otherwise
    @param tour_score :-> the tournament score for the player
    @param card_hand_str :-> the hand cards of the player as a string
    @param card_captured_str :-> the captured cards of the player as a string
    */
   public Player(boolean is_human, int tour_score, String card_hand_str, String card_captured_str) {
      is_human_flag = is_human;
      set_tournament_score(tour_score);

      card_hand = new LinkedList<>(Arrays.asList(card_hand_str.trim().split(" +")));
      card_captured = new LinkedList<>(Arrays.asList(card_captured_str.trim().split(" +")));
   }


   /**
    setter for the tournament score
    @param score :-> the new tournament score for the player
    @return true if successful, false otherwise
    */
   public boolean set_tournament_score(int score) {
      if (score < 0 || score > 99)
         return false;

      tournament_score = score;
      return true;
   }


   /**
    add to the tournament score
    @param score :-> the score to add to the tournament score
    */
   public void add_tournament_score(int score) {
      tournament_score += score;
   }


   /**
    getter for the tournament score
    @return the player's tournament score as an integer
    */
   public int get_tournament_score() {
      return tournament_score;
   }


   /**
    take a card into the player's hand
    @param card :-> add a card to the player's hand
    */
   public void take_card(String card) {
      card_hand.add(card);
   }


   /**
    take a card into the player's captured pile
    @param card :-> add a card to the player's captured pile
    */
   public void capture_card(String card) {
      card_captured.add(card);
   }


   /**
    remove the card from the player's hand
    @param card :-> the card to be removed from the player's hand
    */
   public void remove_from_hand(String card) {
      card_hand.remove(card);
   }


   /**
    getter for the hand cards
    @return a linked list containing the player's hand cards
    */
   public LinkedList<String> get_hand_cards() {
      return ((LinkedList) card_hand.clone());
   }


   /**
    getter for the captured cards
    @return a linked list containing the player's captured cards
    */
   public LinkedList<String> get_captured_cards() {
      return ((LinkedList) card_captured.clone());
   }


   /**
    reset players pile and hand for a new round
    */
   public void reset_for_new_round() {
      card_hand.clear();
      card_captured.clear();
   }

}
