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
import java.util.Objects;

public class Logic {

    /**
     get all possible unique subsets of the given set
     @param arr :-> the list to get all the subsets of
     @return a linked list of linked list containing all the possible subsets
     */
    private static LinkedList<LinkedList<String>> get_all_subsets(LinkedList<String> arr) {
        LinkedList<String> list = new LinkedList<>();
        int n = arr.size();

        /* Run counter i from 000..0 to 111..1*/
        for (int i = 0; i < (int) Math.pow(2, n); i++) {
            String subset = "";

            // consider each element in the set
            for (int j = 0; j < n; j++) {
                // Check if jth bit in the i is set. If the bit
                // is set, we consider jth element from set
                if ((i & (1 << j)) != 0)
                    subset += arr.get(j) + "|";
            }

            // if subset is encountered for the first time
            // If we use set<String>, we can directly insert
            if (!list.contains(subset))
                list.add(subset);
        }

        LinkedList<LinkedList<String>> ret_list = new LinkedList<>();

        // consider every subset
        for (String subset : list)
            // split the subset and store its elements in the ret_list
            ret_list.add(new LinkedList<>(Arrays.asList(subset.split("\\|"))));

        return ret_list;
    }


    /**
     get the best possible move for the current game state
     @param hand :-> the hand cards of the current player
     @param table :-> the table cards of the current game
     @param current_build :-> the current game's build object
     @param is_human :-> true if the current player is human, false otherwise
     @return a Move object containing the best move
     */
    public static Move get_move(LinkedList<String> hand, LinkedList<String> table, Build current_build, boolean is_human) {
        // try build... extend build, or multi build, or create new build
        {
            int max_card_count = 0;
            LinkedList<String> max_capture_cards = new LinkedList<>();
            String hand_card = "";

            // check if opponent has builds you can extend
            if (current_build.has_a_build(!is_human)) {
                LinkedList<String> opponent_builds = current_build.get_player_builds(!is_human);

                // see if it is possible to extend the build
                for (String build : opponent_builds) {
                    for (String s : hand) {
                        if (can_make_build(s, current_build.get_cards_in_build(build), hand)) {
                            if (max_card_count < current_build.get_cards_in_build(build).size()) {
                                hand_card = s;
                                max_card_count = current_build.get_cards_in_build(build).size();
                                max_capture_cards.add(build);
                            }
                        }
                    }
                }

                Move curr_move = new Move(Move.ACTION_EXTEND, hand_card, max_capture_cards);
                if (max_card_count > 0 && check_move(curr_move, hand, table, current_build, is_human))
                    return curr_move;
            }

            // check if there are any build you can make a multi build out of
            // only make your own builds into multiple builds as making the opponents build a multi build with only give them more cards to capture
            if (current_build.has_a_build(is_human)) {
                LinkedList<String> builds = current_build.get_player_builds(is_human);
                Move curr_move;

                for (String build : builds) {
                    int build_sum = current_build.get_build_sum(build);

                    for (String h : hand) {
                        if (build_sum < Logic.get_card_value(h))
                            continue;
                        else if (build_sum == Logic.get_card_value(h)) {
                            LinkedList<String> temp = new LinkedList<>();
                            temp.add(build);
                            curr_move = new Move(Move.ACTION_MULTI, h, temp);
                            if (check_move(curr_move, hand, table, current_build, is_human))
                                return curr_move;
                        }

                        LinkedList<String> loose_cards = (LinkedList) table.clone();

                        // find subset where the total val is build_sum - hand card value
                        LinkedList<LinkedList<String>> subsets = get_all_subsets(loose_cards);
                        for (LinkedList<String> subset : subsets) {
                            int loose_cards_sum = 0;
                            for (String s : subset)
                                loose_cards_sum += Logic.get_card_value(s);

                            if (loose_cards_sum == (build_sum - Logic.get_card_value(h))) {
                                subset.add(build);
                                curr_move = new Move(Move.ACTION_MULTI, h, subset);
                                if (check_move(curr_move, hand, table, current_build, is_human))
                                    return curr_move;
                            }
                        }
                    }
                }
            }

            // check if you can create a build
            max_card_count = -1;

            LinkedList<LinkedList<String>> subsets = get_all_subsets((LinkedList) table.clone());

            for (String h : hand) {
                for (LinkedList<String> loose_card_set : subsets) {
                    int loose_cards_size = loose_card_set.size();

                    if (can_make_build(h, loose_card_set, hand)) {
                        if (loose_cards_size > max_card_count) {
                            max_capture_cards = loose_card_set;
                            hand_card = h;
                            max_card_count = loose_card_set.size();
                        }
                    }
                }
            }

            Move curr_move = new Move(Move.ACTION_BUILD, hand_card, max_capture_cards);
            if (max_card_count != -1 && check_move(curr_move, hand, table, current_build, is_human)) {
                //System.out.println("biuld: " + curr_move.get_hand_card() + " :: " + curr_move.get_loose_cards() );
                return curr_move;
            }
        }

        // try capture
        {
            int max_card_count = 0;
            LinkedList<String> max_capture_cards = new LinkedList<>();
            String hand_card = "";

            // check to see which card lets you capture the most number of cards
            for (String play_card : hand) {
                int play_card_val = Logic.get_card_value(play_card);
                LinkedList<String> capture_cards = new LinkedList<>();
                LinkedList<String> other_loose_cards = new LinkedList<>();
                int card_count = 0;

                for (String t : table) {
                    if (play_card_val == Logic.get_card_value(t)) {
                        capture_cards.add(t);
                        card_count++;
                    } else
                        other_loose_cards.add(t);
                }

                for (String t : current_build.get_builds()) {
                    if (play_card_val == current_build.get_build_sum(t)) {
                        capture_cards.add(t);
                        card_count += current_build.get_cards_in_build(t).size();
                    }
                }

                while (true) {
                    LinkedList<LinkedList<String>> subsets = get_all_subsets(other_loose_cards);

                    if (subsets.isEmpty()) break;

                    boolean any_subset = false;
                    for (LinkedList<String> subset : subsets) {
                        int tot_subset_sum = 0;

                        for (String item : subset)
                            tot_subset_sum += Logic.get_card_value(item);

                        if (tot_subset_sum == play_card_val) {
                            any_subset = true;

                            for (String item : subset) {
                                capture_cards.add(item);
                                other_loose_cards.remove(item);

                                card_count++;
                            }
                            // break out to the outer loop to recalculate subsets
                            break;
                        }
                    }

                    if (!any_subset) break;
                }

                if (card_count > max_card_count) {
                    max_card_count = card_count;
                    max_capture_cards = capture_cards;
                    hand_card = play_card;
                }
            }

            if (max_card_count > 0)
                return new Move(Move.ACTION_CAPTURE, hand_card, max_capture_cards);
        }


        // then try trail with least significant card
        {
            String card_to_trail = "";

            for (String h : hand) {
                if (!Objects.equals(h, "DX") && !Objects.equals(h, "S2")) {
                    card_to_trail = h;
                    break;
                }
            }

            if (Objects.equals(card_to_trail, ""))
                card_to_trail = hand.pop();

            Move curr_move = new Move(Move.ACTION_TRAIL, card_to_trail, new LinkedList<String>());
            if (check_move(curr_move, hand, table, current_build, is_human))
                return curr_move;
        }

        // return an empty move object if none of the above find a solution
        // not finding a move is not possible as the user can always trail, however this statement is here so all paths return a value
        return new Move("", "", new LinkedList<String>());
    }


    /**
     checks if the given set of cards can make a build
     @param hand :-> the hand card to be used in the build
     @param loose :-> the loose cards on the table
     @param hand_cards :-> the hand cards of the current player for whom we are checking
     @return true if the given set of cards can make a build, false otherwise
     */
    public static boolean can_make_build(String hand, LinkedList<String> loose, LinkedList<String> hand_cards) {
        int hand_card_val = Logic.get_card_value(hand);
        int loose_card_val = 0;

        for (String c : loose)
            loose_card_val += Logic.get_card_value(c);

        // sum of build
        int sum = hand_card_val + loose_card_val;

        for (String c : hand_cards) {
            // dont look at the card that is being played
            if (Objects.equals(c, hand))
                continue;

            // check if the user has the card that is equal to the sum of the build in his hand
            if (Logic.get_card_value(c) == sum)
                return true;
        }

        return false;
    }


    /**
     get the best possible move for the current game state
     @param execute :-> the move to be checked for validity
     @param hand :-> the hand cards of the current player
     @param table :-> the table cards of the current game
     @param current_build :-> the current game's build object
     @param is_human :-> true if the current player is human, false otherwise
     @return true if the given move is valid, false otherwise
     */
    public static boolean check_move(Move execute, LinkedList<String> hand, LinkedList<String> table, Build current_build, boolean is_human) {
        /* also get table build cards from build object */

        if (Objects.equals(execute.get_action(), Move.ACTION_BUILD)) {
            if (execute.get_loose_cards().isEmpty() || execute.get_loose_cards().pop().isEmpty() || execute.get_hand_card().isEmpty())
                return false;

            return can_make_build(execute.get_hand_card(), execute.get_loose_cards(), hand);
        } else if (Objects.equals(execute.get_action(), Move.ACTION_EXTEND)) {
            if (execute.get_loose_cards().isEmpty() || execute.get_loose_cards().pop().isEmpty() || execute.get_hand_card().isEmpty())
                return false;

            String ex_build = execute.get_loose_cards().peek();

            // cannot extend multi builds
            if (!current_build.is_build(ex_build) || current_build.is_multi_build(ex_build))
                return false;

            // player cannot extend his own build
            if (is_human && Objects.equals(current_build.get_owner(ex_build), Player.HUMAN))
                return false;
            else if (!is_human && Objects.equals(current_build.get_owner(ex_build), Player.COMPUTER))
                return false;

            return can_make_build(execute.get_hand_card(), current_build.get_cards_in_build(ex_build), hand);
        } else if (Objects.equals(execute.get_action(), Move.ACTION_MULTI)) {
            if (execute.get_hand_card().isEmpty())
                return false;

            boolean one_build = false;
            String ex_build = "";

            // find the first build in the input and store it in the ex_build variable
            // then check if the input has other builds, if yes, move is not valid as there is ambiguity
            for (String s : execute.get_loose_cards()) {
                if (current_build.is_build(s) && !one_build) {
                    ex_build = s;
                    one_build = true;
                } else if (current_build.is_build(s) && one_build)
                    return false;
            }

            // if there is no build in the input, exit
            if (!one_build) return false;

            int build_sum = current_build.get_build_sum(ex_build);

            int loose_sum = 0;
            LinkedList<String> tot_cards = execute.get_all_cards();
            for (String c : tot_cards) {
                // skip the build object
                if (current_build.is_build(c))
                    continue;

                loose_sum += Logic.get_card_value(c);
            }

            // check if the loose cards sum adds up to the build sum so it can be made into a multi build, and,
            // check if the user has the card that is equal to the sum of the build in his hand
            if (loose_sum == build_sum) {
                for (String s : hand) {
                    // dont look at the card that is being played
                    if (Objects.equals(s, execute.get_hand_card()))
                        continue;

                    // check if the user has the card that is equal to the sum of the build in his hand
                    if (Logic.get_card_value(s) == build_sum)
                        return true;
                }
            }
        } else if (Objects.equals(execute.get_action(), Move.ACTION_CAPTURE)) {
            if (execute.get_loose_cards().isEmpty() || execute.get_loose_cards().pop().isEmpty() || execute.get_hand_card().isEmpty())
                return false;

            int card_val = Logic.get_card_value(execute.get_hand_card());
            LinkedList<String> selected_capture_cards = execute.get_loose_cards();

            // check if all cards that have the same value as the hand card are captured
            for (String s : table) {
                // if table card has different value, continue to next
                if (card_val != Logic.get_card_value(s))
                    continue;

                // every loose card that matches the card value must be captured
                if (!selected_capture_cards.contains(s))
                    return false;

            }

            // this LinkedList will store all the values for the loose cards that have been selected to capture
            LinkedList<Integer> loose_vals = new LinkedList<>();

            // check if the sums match up to the capture card value
            for (String s : execute.get_loose_cards()) {
                // if is a build but the build sum is different than capture card, return
                if (current_build.is_build(s)) {
                    if (current_build.get_build_sum(s) != card_val)
                        return false;
                    continue;
                }

                loose_vals.add(Logic.get_card_value(s));
            }

            // loop to check if the given loose cards add up to the hand card value
            // note: the sets of loose cards that add up to the hand card value have to be in order
            for (int i = 0; i < loose_vals.size(); i++) {
                int temp = loose_vals.get(i);

                if (temp > card_val)
                    return false;
                else if (temp == card_val)
                    continue;

                int t_sum = 0, j = i;
                while (true) {
                    t_sum += loose_vals.get(j);

                    if (t_sum == card_val) {
                        i = j;
                        break;
                    } else if (t_sum > card_val)
                        return false;

                    j++;
                    if (j == loose_vals.size())
                        return false;
                }
            }

            return true;
        } else if (Objects.equals(execute.get_action(), Move.ACTION_TRAIL)) {
            // card not specified
            if (execute.get_hand_card().isEmpty())
                return false;

            // don't let players who have a build trail
            if (current_build.has_a_build(is_human))
                return false;

            int card_val = Logic.get_card_value(execute.get_hand_card());

            // check if card matches any loose cards.  if yes, dont let them trail
            for (String c : table) {
                if (card_val == Logic.get_card_value(c))
                    return false;
            }

            return true;
        }
        return false;
    }


    /**
     gets the value of the passed in card
     @param card :-> the card to get the value of
     @return an integer representing the value of the passed in card
     */
    public static int get_card_value(String card) {
        // make card represent only the value of the card
        if (card.length() == 2)
            card = card.charAt(1) + "";
        else if (card.length() != 1)
            return -1;  // invalid card

        switch (card.charAt(0)) {
            case 'A':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'X':
                return 10;
            case 'J':
                return 11;
            case 'Q':
                return 12;
            case 'K':
                return 13;
            default:
                return -1;  // invalid card
        }
    }
}
