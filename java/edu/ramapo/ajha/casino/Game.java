package edu.ramapo.ajha.casino;

/*
 ************************************************************
 * Name:     Abish Jha                                      *
 * Project:  Casino                                         *
 * Class:    CMPS 366 Organization of Programming Languages *
 * Date:     November 20, 2018                              *
 ************************************************************
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.LinkedList;

class Game {
   private LinkedList<String> loose_cards; // the cards on the table
   private Deck game_deck;             // the active game's deck
   private Player human;               // the human player object
   private Player computer;            // the computer player object
   private String current_player;      // use Player.HUMAN or Player.COMPUTER
   private String last_capture;        // use Player.HUMAN or Player.COMPUTER
   private int round_number;
   private Build current_builds;



   /**
    use to set up a new game. if this is used, also use the set current player function to add the first player from the coin toss result
    */
   public Game() {
      // start a new tournament
      loose_cards = new LinkedList<>();
      human = new Player(true);
      computer = new Player(false);
      round_number = 1;

      setup_new_round();
   }


   // do all automatic stuff here i.e. setting the game, menu is provided in parent
   public void setup_new_round() {
      System.out.println("starting new round...");

      human.reset_for_new_round();
      computer.reset_for_new_round();

      current_builds = new Build();

      // initialize the game deck
      game_deck = new Deck();

      // deal four cards to human and then computer
      deal_to_human_and_computer();

      // put four cards on the table
      for (int i = 0; i < 4; i++) {
         add_to_table(game_deck.get_one());
      }
   }


   /**
    load game from file
    @param file_path :-> the path for the load game file from which to load the game
    */
   public Game(String file_path) {
      current_builds = new Build();

      try {
         FileReader in = new FileReader(file_path);
         BufferedReader reader = new BufferedReader(in);

         String oneLine;
         while ((oneLine = reader.readLine()) != null) {
            // skip if empty line
            if (oneLine.trim().isEmpty())
               continue;

            String[] content = oneLine.trim().split(" +");

            if (content[0].equals("Round:")) {
               round_number = Integer.parseInt(content[1]);

            } else if (content[0].equals("Computer:") || content[0].equals("Human:")) {
               String hand = "", pile = "", score = "0";

               for (int i = 0; i < 3; i++) {
                  String sub_line = reader.readLine();

                  if (sub_line.contains("Score:"))
                     score = sub_line.trim().split(" +")[1];
                  else if (sub_line.contains("Hand:"))
                     hand = sub_line.substring(sub_line.indexOf(":") + 1).trim();
                  else if (sub_line.contains("Pile:"))
                     pile = sub_line.substring(sub_line.indexOf(":") + 1).trim();
               }

               if (content[0].equals("Human:"))
                  human = new Player(true, Integer.parseInt(score), hand, pile);
               else if (content[0].equals("Computer:"))
                  computer = new Player(false, Integer.parseInt(score), hand, pile);

            } else if (content[0].equals("Table:")) {
               String cards = oneLine.substring(oneLine.indexOf(":") + 1);
               loose_cards = current_builds.parse_builds_str(cards);

            } else if (content[0].equals("Build") && content[1].equals("Owner:")) {
               current_builds.parse_builds_with_owner_str(oneLine.substring(oneLine.indexOf(":") + 1));

            } else if (content[0].equals("Last") && content[1].equals("Capturer:")) {
               if (content[2].equals("Human"))
                  last_capture = Player.HUMAN;
               else
                  last_capture = Player.COMPUTER;

            } else if (content[0].equals("Deck:")) {
               game_deck = new Deck(oneLine.substring(oneLine.indexOf(":") + 1));

            } else if (content[0].equals("Next") && content[1].equals("Player:")) {
               if (content[2].equals("Human"))
                  current_player = Player.HUMAN;
               else
                  current_player = Player.COMPUTER;

            }
         }

         reader.close();
         in.close();

         System.out.println("load was a success");
      } catch (Exception ex) {
         System.out.println("load was not a success");
         System.out.println(ex.toString());
      }
   }


   /** start -- setters and getters **/

   public void set_current_player(String player) {
      if (player.equals(Player.HUMAN))
         current_player = Player.HUMAN;
      else
         current_player = Player.COMPUTER;
   }

   public String get_current_player() {
      return current_player;
   }

   public int get_round_number() {
      return round_number;
   }

   public LinkedList<String> get_game_deck(){
      return game_deck.get_deck();
   }

   public LinkedList<String> get_human_hand(){
      return human.get_hand_cards();
   }

   public LinkedList<String> get_human_pile(){
      return human.get_captured_cards();
   }

   public int get_human_score(){
      return human.get_tournament_score();
   }

   public LinkedList<String> get_computer_hand(){
      return computer.get_hand_cards();
   }

   public LinkedList<String> get_computer_pile(){
      return computer.get_captured_cards();
   }

   public int get_computer_score(){
      return computer.get_tournament_score();
   }

   public LinkedList<String> get_loose_cards(){
      return ((LinkedList) loose_cards.clone());
   }

   public LinkedList<String> get_build_cards(){
      return current_builds.get_builds();
   }

   public LinkedList<String> get_table_cards(){
      LinkedList<String> total_cards = get_loose_cards();
      total_cards.addAll(get_build_cards());
      return total_cards;
   }

   public int get_build_sum(String build){ return current_builds.get_build_sum(build); }

   /** end -- setters and getters **/


   /**
    add a card to the table
    @param card :-> the card to be added to the table
    */
   public void add_to_table(String card) {
      loose_cards.add(card);
   }


   /**
    remove a card to the table
    @param card :-> the card to be removed from the table
    */
   public void remove_from_table(String card) {
      loose_cards.remove(card);
   }


   /**
    give four cards to human player and four to computer player
    */
   private void deal_to_human_and_computer() {
      // give four cards to human
      for (int i = 0; i < 4; i++) {
         human.take_card(game_deck.get_one());
      }

      // give four cards to computer
      for (int i = 0; i < 4; i++) {
         computer.take_card(game_deck.get_one());
      }
   }


   /**
    check to see if game has ended, if yes play another round or end tournament. call this after every move to check if round or game has ended
    @return  a string with a status code
            *  1. nothing i.e. continue
            *  2. round has ended
            *  3. tournament has ended
            * if 2, or 3 if returned, there is going to be additional round related stuff appended to the end of the return string
    */
   public String check_game_state() {
      /* returns */

      if (human.get_hand_cards().isEmpty() && computer.get_hand_cards().isEmpty() && game_deck.empty()) {
         // player who made last capture gets all remaining table cards
         // at this point, builds can not and should not exist
         if (last_capture.equals(Player.COMPUTER)) {
            for (String card : loose_cards)
               computer.capture_card(card);

            // starting player for next round is the one who made the last capture in previous round
            current_player = Player.COMPUTER;
         } else {
            for (String card : loose_cards)
               human.capture_card(card);

            // starting player for next round is the one who made the last capture in previous round
            current_player = Player.HUMAN;
         }
         loose_cards.clear();

         // process and store round and tournament score to be displayed in the UI
         String ret = process_round_score();
         ret += " | Human Pile: " + human.get_captured_cards() + " | Computer Pile: " + computer.get_captured_cards();

         round_number++;

         if (human.get_tournament_score() >= 21 || computer.get_tournament_score() >= 21) {
            // tournament has ended
            return "3" + " | " + ret;
         } else {
            // clear the table and setup the cards
            setup_new_round();
            // next round
            return "2" + " | " + ret;
         }
      }
      // if both players hands are empty, give them four cards each
      else if (human.get_hand_cards().isEmpty() && computer.get_hand_cards().isEmpty())
         deal_to_human_and_computer();

      return "1";
   }


   /**
    get a move from the logic class
    @return a Move object containing the calculated move from the logic
    */
   public Move get_move() {
      if (current_player.equals(Player.HUMAN))
         return Logic.get_move(human.get_hand_cards(), loose_cards, current_builds, true);
      else
         return Logic.get_move(computer.get_hand_cards(), loose_cards, current_builds, false);
   }


   /**
    check if the passed in move is valid
    @param in :-> a Move object containing the move will to be checked for validity
    @return true if move is possible, false otherwise
    */
   public boolean check_move(Move in) {
      if (current_player.equals(Player.HUMAN))
         return Logic.check_move(in, human.get_hand_cards(), loose_cards, current_builds, true);
      else
         return Logic.check_move(in, computer.get_hand_cards(), loose_cards, current_builds, false);
   }


   /**
    driver for the execute function which executes the passed in move
    @param execute :-> a Move object containing the move to be executed
    @return true if move executed successfully, false otherwise
    */
   public boolean make_move(Move execute) {
      if(execute(execute)){
         if(current_player.equals(Player.HUMAN))
            current_player = Player.COMPUTER;
         else
            current_player = Player.HUMAN;
         return true;
      }

      return false;
   }


   /**
    execute a move and change cards to appropriate location based on the nature of the move. called by the make_move function
    @param execute :-> a Move object containing the move to be executed
    @return true if move executed successfully, false otherwise
    */
   public boolean execute(Move execute){
      // return if move is not valid
      if (!check_move(execute)) return false;

      boolean flag = true;
      boolean is_human = false;
      if (current_player.equals(Player.HUMAN)) is_human = true;

      if (execute.get_action().equals(Move.ACTION_BUILD)) {
         current_builds.create_build(execute.get_all_cards(), is_human);
      } else if (execute.get_action().equals(Move.ACTION_EXTEND)) {
         if (current_builds.is_build(execute.get_loose_cards().pop()))
            current_builds.add_to_build(execute.get_loose_cards().pop(), execute.get_hand_card());
         else flag = false;
      } else if (execute.get_action().equals(Move.ACTION_MULTI)) {
         String build = "";
         for (String s : execute.get_loose_cards()) {
            if (current_builds.is_build(s))
               build = s;
         }

         LinkedList<String> other_cards = new LinkedList<>();
         for (String s : execute.get_all_cards()) {
            if (!s.equals(build))
               other_cards.add(s);
         }

         current_builds.add_build_multi(build, other_cards, is_human);
      } else if (execute.get_action().equals(Move.ACTION_CAPTURE)) {
         if (is_human) human.capture_card(execute.get_hand_card());
         else computer.capture_card(execute.get_hand_card());

         for (String s : execute.get_loose_cards()) {
            // will also remove the build object from the table and all its cards
            if (current_builds.is_build(s)) {
               LinkedList<String> t_cards = current_builds.capture_build(s);

               for (String c : t_cards) {
                  if (is_human) human.capture_card(c);
                  else computer.capture_card(c);
               }

               continue;
            }

            if (is_human) human.capture_card(s);
            else computer.capture_card(s);
         }

         // set the last capture
         if (is_human) last_capture = Player.HUMAN;
         else last_capture = Player.COMPUTER;
      } else if (execute.get_action().equals(Move.ACTION_TRAIL)) {
         // do nothing as the card is removed from hand below
         add_to_table(execute.get_hand_card());
      } else
         flag = false;

      if (flag) {
         // remove hand card from players hand
         if (is_human) human.remove_from_hand(execute.get_hand_card());
         else computer.remove_from_hand(execute.get_hand_card());

         // remove the used cards from the table
         for (String s : execute.get_loose_cards()) {
            // dont do anything to builds as this is handled by the build class
            if (current_builds.is_build(s)) continue;

            remove_from_table(s);
         }
      }

      return flag;
   }


   /**
    process the current rounds score
    @return a string containing stats from the current round ending like the player's piles and their total cards and so on
    */
   private String process_round_score() {
      int human_score = 0, computer_score = 0;

      LinkedList<String> human_cards = human.get_captured_cards();
      LinkedList<String> computer_cards = computer.get_captured_cards();

      // adding three points to player with most captured cards
      if (human_cards.size() > computer_cards.size())
         human_score += 3;
      else if (human_cards.size() < computer_cards.size())
         computer_score += 3;

      int c_spade = 0, h_spade = 0;

      // calculations for human player's captured cards
      for (String s : human_cards) {
         // counting the number of spades in human cards
         if (s.charAt(0) == 'S')
            h_spade++;

         // player with 10 of diamond gets two points
         if (s.equals("DX"))
            human_score += 2;

         // player with 2 of spade gets one points
         if (s.equals("S2"))
            human_score += 1;

         // each ace earns a point
         if (s.charAt(1) == 'A')
            human_score += 1;
      }

      // calculations for computer player's captured cards
      for (String s : computer_cards) {
         // counting the number of spades in computer cards
         if (s.charAt(0) == 'S')
            c_spade++;

         // player with 10 of diamond gets two points
         if (s.equals("DX"))
            computer_score += 2;

         // player with 2 of spade gets one points
         if (s.equals("S2"))
            computer_score += 1;

         // each ace earns a point
         if (s.charAt(1) == 'A')
            computer_score += 1;
      }

      if (h_spade > c_spade)
         human_score += 1;
      else if (h_spade < c_spade)
         computer_score += 1;

      // add the calculated round score to the tournament score in the individual player's object
      human.add_tournament_score(human_score);
      computer.add_tournament_score(computer_score);

      return ("Human Score: " + human_score + " | Computer Score: " + computer_score + " | "
                + "Human Spade: " + h_spade + " | Computer Spade: " + c_spade + " | "
                + "Human Total Cards: " + human_cards.size() + " | Computer Total Cards: " + computer_cards.size());
   }


   /**
    save the current state of the game
    @param file_path :-> a Move object containing the move to be executed
    @return true if game saved successfully, false otherwise
    */
   public boolean save_game(String file_path) {
      System.out.println("filepath : " + file_path);

      try {
         File writeFile = new File(file_path);
         if (!writeFile.exists())
            writeFile.createNewFile();

         FileOutputStream out = new FileOutputStream(writeFile);

         out.write(("Round: " + round_number + "\n\n").getBytes());

         out.write(("Computer:\n").getBytes());
         out.write(("\tScore: " + computer.get_tournament_score() + "\n").getBytes());
         out.write(("\tHand: " + list_to_string(computer.get_hand_cards()) + "\n").getBytes());
         out.write(("\tPile: " + list_to_string(computer.get_captured_cards()) + "\n\n").getBytes());

         out.write(("Human:\n").getBytes());
         out.write(("\tScore: " + human.get_tournament_score() + "\n").getBytes());
         out.write(("\tHand: " + list_to_string(human.get_hand_cards()) + "\n").getBytes());
         out.write(("\tPile: " + list_to_string(human.get_captured_cards()) + "\n\n").getBytes());

         out.write(("Table: " + current_builds.get_builds_str() + " " + list_to_string(loose_cards) + "\n\n").getBytes());

         out.write(("Build Owner: " + current_builds.get_builds_with_owner_str() + "\n\n").getBytes());

         out.write(("Last Capturer: " + last_capture + "\n\n").getBytes());

         out.write(("Deck: " + list_to_string(game_deck.get_deck()) + "\n\n").getBytes());

         out.write(("Next Player: " + current_player + "\n\n").getBytes());

         out.close();
         return true;
      } catch (Exception ex) {
         System.out.println(ex.toString());
         return false;
      }
   }


   /**
    convert the passed in list to a space separated string
    @param list :-> a list containing items
    @return all the items in the list joined by a space as a string
    */
   private String list_to_string(LinkedList<String> list) {
      String ret = "";
      for (String item : list)
         ret += item + " ";
      return ret;
   }


   // test driver program for game logic. uses the console to act as View
   public static void main(String[] args) {

   }
}
