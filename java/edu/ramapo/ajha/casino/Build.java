package edu.ramapo.ajha.casino;

/*
 ************************************************************
 * Name:     Abish Jha                                      *
 * Project:  Casino                                         *
 * Class:    CMPS 366 Organization of Programming Languages *
 * Date:     November 20, 2018                              *
 ************************************************************
 */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;


public class Build {
    private LinkedList<String> builds;

    // map == build --> owner
    private Map<String, String> owner;

    // map == build --> sum
    private Map<String, Integer> sum;


    /**
     constuctor for Build class
     */
    public Build() {
        builds = new LinkedList<>();
        owner = new HashMap<>();
        sum = new HashMap<>();
    }


    /**
     creates a build out of the given string and owner
     @param b_build :-> the build to be stored as a string in the format : [DX A9]
     @param b_is_human :-> a boolean value to indicate true if the owner of the build is human, false otherwise
     */
    // create a new build with given build string.  used for serialization
    public void create_build(String b_build, boolean b_is_human) {
        b_build = b_build.trim();

        // add the build to the list of builds, exit if not a build
        if (is_build(b_build))
            builds.add(b_build);
        else
            return;

        // set the owner of the build
        if (b_is_human)
            owner.put(b_build, Player.HUMAN);
        else
            owner.put(b_build, Player.COMPUTER);

        // calculate sum of build
        sum.put(b_build, get_build_sum(b_build));
    }


    /**
     create a new build with given cards
     @param cards :-> a linked list of string containing the cards to be put into the build
     @param is_human :-> a boolean value to indicate true if the owner of the build is human, false otherwise
     */
    public void create_build(LinkedList<String> cards, boolean is_human) {
        boolean first = true;

        String b_builder = "[";
        for (String card : cards) {
            if (first) first = false;
            else b_builder += " ";

            b_builder += card;
        }
        b_builder += "]";

        create_build(b_builder, is_human);
    }


    /**
     used to extend a build
     @param build :-> the build to be extended
     @param new_card :-> the card to be added to the build
     */
    public void add_to_build(String build, String new_card) {
        build = build.trim();

        // extending a build can only be done to the opponent's build and the ownership is changed therewith
        String b_owner = owner.get(build);
        boolean is_human = false;
        if (Objects.equals(b_owner, Player.HUMAN))
            is_human = true;

        // remove old build
        delete_build(build);

        // create new build
        LinkedList<String> tot_cards = get_cards_in_build(build);
        tot_cards.add(new_card);

        // as the ownership is transferred for the build when extended
        create_build(tot_cards, !is_human);
    }


    /**
     used to create a multi build either out of a build or a multi build
     @param build :-> the build to be created multi build out of
     @param other_cards :-> the card to be added to the build
     @param is_human :-> a boolean value to indicate true if the owner of the build is human, false otherwise
     */
    public void add_build_multi(String build, LinkedList<String> other_cards, boolean is_human) {
        build = build.trim();
        int sum_local = sum.get(build);

        delete_build(build);

        String b_builder = "";
        boolean first;

        if (is_build(build) && !is_multi_build(build)) {
            first = true;

            // build multi build string
            b_builder = "[ " + build + " [";
            for (String card : other_cards) {
                if (first) first = false;
                else b_builder += " ";

                b_builder += card;
            }
            b_builder += "] ]";
        } else if (is_multi_build(build)) {
            first = true;

            int strt = build.indexOf("[");
            b_builder = build.substring(0, strt + 1);
            b_builder += " [";
            for (String card : other_cards) {
                if (first) first = false;
                else b_builder += " ";

                b_builder += card;
            }
            b_builder += "] " + build.substring(strt + 2);
        }

        if (!b_builder.equals("")) {
            builds.add(b_builder);
            sum.put(b_builder, sum_local);
            owner.put(b_builder, ((is_human) ? Player.HUMAN : Player.COMPUTER));
        }
    }


    /**
     check if a given string is a build
     @param build :-> the string to be checked for
     */
    public boolean is_build(String build) {
        build = build.trim();
        return (build.contains("[") && build.contains("]"));
    }


    /**
     check if a given string is a multi build
     @param build :-> the string to be checked for
     */
    public boolean is_multi_build(String build) {
        int first = build.indexOf('[');
        if (first == -1) return false;

        int second = build.indexOf('[', first + 1);
        if (second == -1) return false;

        return true;
    }


    /**
     check if a given string is a multi build
     @return a linked list containing all the builds that exist in the current game
     */
    public LinkedList<String> get_builds() {
        return ((LinkedList) builds.clone());
    }


    /**
     get all current build as a string.  mainly used by the serialization process to get the builds
     @return a string containing all the builds that exist in the current game, space separated
     */
    public String get_builds_str() {
        String ret = "";
        for (String build : builds)
            ret += build + " ";
        return ret;
    }


    /**
     get all current build with their owner as a string.  mainly used by the serialization process to get the builds with owner
     @return a string containing all the builds that exist in the current game, space separated, with their owner
     */
    public String get_builds_with_owner_str() {
        String ret = "";
        for (String build : builds)
            ret += build + " " + owner.get(build) + " ";
        return ret;
    }


    /**
     get all the start and end index of the first complete build in the provided string
     @param build :-> string containing the build to be analyzed
     @return a string containing all the builds that exist in the current game, space separated, with their owner
     */
    int[] get_first_build_start_end_pos(String build) {
        int[] pos = {-1, -1};

        if (!is_build(build))
            return pos;

        int open_b = 0, close_b = 0;
        boolean first_b = false; // first bracket found flag

        for (int i = 0; i < build.length(); i++) {
            if (build.charAt(i) == '[') {
                open_b++;

                if (!first_b) {
                    pos[0] = i;
                    first_b = true;
                }
            } else if (build.charAt(i) == ']')
                close_b++;

            if (first_b && (open_b == close_b)) {
                pos[1] = i;
                return pos;
            }
        }

        // inconsistent number of open and close bracket
        pos[0] = -1;
        pos[1] = -1;
        return pos;
    }


    /**
     to be used for the loading serialization.  parses and stores builds and return a list of the loose cards
     @param build :-> string containing the builds to be parsed
     @return a linked list containing all cards as separated entries in the string that were not a build
     */
    //
    LinkedList<String> parse_builds_str(String build) {
        while (true) {
            // get start and end position of the first build in the input string
            int[] pos = get_first_build_start_end_pos(build);
            if (pos[0] == -1 && pos[1] == -1) break;

            // store the parsed build into the builds list
            String parsed_build = build.substring(pos[0], pos[1] + 1).trim();
            builds.add(parsed_build);

            System.out.println("build : " + parsed_build);

            // remove the parsed build from the input string for further searches
            build = build.replace(parsed_build, "");

            // store the sum of the parsed build.  owner will be parsed later
            sum.put(parsed_build, get_build_sum(parsed_build));
        }

        // parse and return loose cards in a list for the table
        return new LinkedList<>(Arrays.asList(build.trim().split(" +")));
    }


    /**
     to be used for the loading serialization.  parses and stores builds with their owner
     @param build_w_owner :-> string containing the builds to be parsed
     @return a linked list containing all cards as separated entries in the string that were not a build
     */
    // to be used for the loading serialization
    public void parse_builds_with_owner_str(String build_w_owner) {
        for (String build : builds) {
            // if the current build has a owner, no need to pass the incoming string to look for it again
            if (owner.containsKey(build))
                continue;

            int idx = build_w_owner.indexOf(build);
            if (idx == -1) {
                System.out.println("[error] build owner not found for " + build);
                continue;
            }

            String word = build_w_owner.substring(idx + build.length());
            word = word.trim().split(" +")[0];

            if (word.equals("Human") || word.equals("human"))
                owner.put(build, Player.HUMAN);
            else
                owner.put(build, Player.COMPUTER);
        }
    }


    /**
     delete a build from the current database
     @param build :-> the build to delete
     @return true if the build was deleted, false otherwise
     */
    public boolean delete_build(String build) {
        build = build.trim();

        if (builds.contains(build)) {
            builds.remove(build);
            owner.remove(build);
            sum.remove(build);
            return true;
        }

        // build does not exist
        return false;
    }


    /**
     delete a build from the current database and return all the cards in the build as a list to be added to the capturing player's pile
     @param build :-> the build to capture
     @return a linked list of cards that has every card in the passed in build
     */
    // remove the build because somebody captured it and got all the cards
    LinkedList<String> capture_build(String build) {
        if (delete_build(build))
            return get_cards_in_build(build);
        else
            return new LinkedList<>();
    }


    /**
     get the owner of a build between the computer and the human player
     @param build :-> the build to get the owner of
     @return a string saying Human, if the owner is Human and Computer otherwise
     */
    public String get_owner(String build) {
        build = build.trim();
        return owner.get(build);
    }


    /**
     get all the cards present in a build
     @param build :-> the build to get cards from
     @return a linked list containing all the cards as individual elements
     */
    public LinkedList<String> get_cards_in_build(String build) {
        // divide build into individual cards so it can be added to a player's captured pile
        build = build.trim();

        // remove all '[' and ']' brackets
        build = build.replaceAll("\\[", "").replaceAll("\\]", "");

        // breaking the string using the delimiting character " " and returning the result list
        return new LinkedList<>(Arrays.asList(build.trim().split(" +")));
    }


    /**
     get the sum of the passed in build
     @param build :-> the build to get the sum of
     @return an int representing the sum of the build
     */
    public int get_build_sum(String build) {
        build = build.trim();

        // get sum from map for pre-calculated builds
        if (sum.containsKey(build))
            return sum.get(build);

        // calculate sum for new builds
        int end = build.indexOf(']');
        if (end == -1)
            return -1; // not a build

        int begin = build.indexOf('[');
        while (true) {
            int next = build.indexOf('[', begin + 1);

            if (next == -1 || next > end)
                break;

            begin = next;
        }

        LinkedList<String> cards = get_cards_in_build(build.substring(begin, end));
        int _sum = 0, val;
        for (String card : cards) {
            val = Logic.get_card_value(card);
            if (val == -1)
                return -1; // invalid card in build

            _sum += val;
        }

        return _sum;
    }


    /**
     check if player has any builds
     @param is_human :-> true if you wanna check for human, false otherwise
     @return true if player has any builds, false otherwise
     */
    public boolean has_a_build(boolean is_human) {
        for (String build : builds) {
            if (is_human && owner.get(build) == Player.HUMAN)
                return true;
            else if (!is_human && owner.get(build) == Player.COMPUTER)
                return true;
        }
        return false;
    }


    /**
     get all the builds for the current player
     @param is_human :-> true if you wanna get for human, false otherwise
     @return a linked list containing all the builds
     */
    public LinkedList<String> get_player_builds(boolean is_human) {
        LinkedList<String> ret = new LinkedList<>();

        for (String build : builds) {
            if (is_human && (Objects.equals(owner.get(build), Player.HUMAN)))
                ret.add(build);
            else if (!is_human && (Objects.equals(owner.get(build), Player.COMPUTER)))
                ret.add(build);
        }

        return ret;
    }

}
