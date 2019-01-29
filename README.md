<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<h1>Casino - Android</h1>

Casino is a card game played by 2 players.

<h2>The Objective</h2>

The objective of this game is to score the most points by capturing cards.

<h2>The Players</h2>

Two players play this game - 
one player will be the human user of your program,
and the other player will be your program/computer.
<br>
The two players will play a "tournament", consisting of one or more rounds.<br>
Each round will consist of the two players playing till all the cards have been captured.
The tournament ends when one of the players scores 21 or more points.

<h2>The Setup</h2>

A standard 52-card deck is used.
<p>
  Initially, the first four cards of the deck are dealt to the human player and
  the next four cards are dealt to the computer.
Thereafter, the next four cards in the deck are placed face up on the table.
</p><p>
Each time all the players have played all their cards, four more cards are
dealt first to the human player and then to the computer from the remaining deck and the round continues.
When the deck is exhausted and all the cards have been played,
the round ends.
</p><p>
  In this game, Kings are valued at 13, Queens at 12, Jacks at 11, and Aces at 1.
  Other cards are valued at their face value.

</p><h2>A Round</h2>

The two players take alternate turns till the round ends.

<h3>A Turn</h3>

During her turn, a player plays exactly one card from her hand, in one
of three ways:
<ul>
    
  <li> <b>Build:</b> A build is a group of cards that must be played
    together.
    <ul>
      <li> <b>Building a build:</b>
    The player can combine the played card with a <b>loose</b> card or cards on the
    table to form a build. (A loose card is a card that is not part of any build.)
    In order to do so:
    <ul>
      <li> The player must also have the card in her hand
      whose value is the sum of the cards in the build, e.g.,
      2 and 3 loose cards are on the table, and the player plays a 5 to create
      a build of the three cards 2, 3 and 5 because the player also has
      a 10 card in her hand that she can play in the future to capture
      the entire build (2 + 3 + 5 = 10). In this scenario, the 2 and 3
      cards were loose cards, i.e., they were not part of any other
      build.
      </li>
      <li>
      The player is required to hold the 10 card in her hand until the build is
      captured or another player takes over the build.
      </li>
      <li> As the player who most recently added a card to the build, the
      player becomes its <b>owner</b>.
      </li>
    </ul>

   </li>
   <li> 
        <b>Multiple build:</b>	  
    A player can create <b>multiple builds</b>, all with the same capture
    value, e.g., a build of 6 and 3, another of 5 and 4, and yet
    another of 8 and Ace.

   </li>
   <li> 
        <b>Increasing a build:</b> A player can add a card to a single build owned by an opponent to
      increase the value of the build if the player also has the card
      in her hand with the value equal to the increased sum of the
      build, e.g., the opponent has a build of 6+3; the player adds 2
      to the build since the player also has Jack in her hand (6 + 3 +
      2 = 11).
      The player who added the card now becomes the owner of
      the build. But adding a card to a build cannot be done if the
      build is part of multiple builds. It cannot be done by a player
      to his own build.
    </li>
  </ul>

 </li>
 <li> <b>Capture:</b> The player may play a card to capture one or
    more cards on the table:
    <ul>
      <li> <b>Individual card:</b>
	If the played card matches an individual card on the table,
	that card must be captured, e.g., if the player plays a 6, the
	player will capture any and all 6 cards on the table.
      </li>
      <li> <b>Set of cards:</b>
	If the played card matches the sum of a set of cards on
	the table, the player may choose to capture the set of cards
	or not, e.g., if the player plays an 8, and a 5 and 3 are on
	the table, the player may choose to capture both the 5 and 3
	  from the table with her 8 card.
      </li>
      <li> <b>Combination of individual and set of cards:</b>
	The player can capture one or more individual cards as well as
	one or more sets of cards all with the same played card, e.g.,
	if the player plays a 9 and the cards on the table are 9, 5,
	4, 6 and 3, the player can capture all the cards with the same
	9 card since 5 + 4 = 9 and 6 + 3 = 9. Whereas the player must
	capture the 9 card, capturing the 5 and 4 set or the 6 and 3
	set is left to the player's discretion.
	</li>
	<li> The above rules apply only as long as the captured cards
	are not part of a <b>build</b>. <br>
	  The player has the option to (but is not required to) capture one or more complete
	  builds (single or multiple) whose value is equal to that of the played card.
    </li>
    </ul>
    The played card and the captured card(s) are added to the player's
    pile.
 </li>
 <li>
	<b>Trail:</b>
	The player plays a card that does not match any individual
	loose card,
	and therefore, cannot capture any individual loose card.
	The played card is left on the table, to be captured or
	incorporated into a build later.
	<br>
	Note that if the played card matches a set of cards or a
	build, since capturing them is not mandatory, the player can
	choose to trail rather than capture with the card. 
	<br>
	Trailing option is not available to the owner of a build - since that
	player can play the card matching the build to capture it or
	work on a multiple build.
 </li>
</ul>

<h3>Round Ending</h3>

The round ends when the players have played all the cards and the deck
is empty. 
Any cards that remain on the table are taken by the last player that
made a capture.
<br>
The piles of both the players are printed at the end of the round.


<h3>Score</h3>

When a round ends, the points earned by each player are calculated
based on the cards in each player's pile:
<ul>
  <li> The player with the most cards in the pile gets 3 points. In
  the event of a tie, neither player gets points.
  </li><li> The player with the most spades gets 1 point.  In
  the event of a tie, neither player gets points.
  </li><li> The player with 10 of Diamonds gets 2 points.
  </li><li> The player with 2 of Spades gets 1 point.
  </li><li> Each player gets one point per Ace.
</li></ul>
The scores earned by the two players must be declared at
the end of each round. <br> The tournament scores of both the players
must be updated and printed at the end of each round.

<h3>Winning the tournament</h3>

If either player's score reaches or crosses 21 points by the end of a round,
the tournament ends. Otherwise, a new round is started.
<br>
When the tournament ends, the player with the greater score wins the tournament. 
If both players go over 21 points in the same round and
end up with the same tournament score, the tournament results in a tie.

<h3>First Player</h3>

On the first round of the tournament, a coin is flipped and the human
player is asked to call the toss to determine the first player.
On subsequent rounds, the player who captured last on the previous
round plays first.


<h2> Implementation</h2>

<ul>
<li><b>User Interface:</b> You must provide a user-friendly interface
  for the game.
<ul>

  <li> Use syntax to group cards in a build, as well as multiple builds.

  </li><li> All human inputs must be validated.

  </li><li> Human and computer moves must be described in terms of the actions:
    capture, build or trail. When capturing, the cards/sets/builds
    captured must be clearly stated. When building, whether single
    build, multiple build or extension of an opponent's build should
    be clarified. 
    

  </li><li> <b>Before</b> each player plays, the following menu must be displayed and
    processed:
    <ol>
      <li> Save the game
      </li><li> Make a move
      </li><li> Ask for help (only before human player plays)
      </li><li> Quit the game
      </li></ol>
    
  </li><li> The turn played by the computer as well as the strategy 
    it uses must be displayed on the screen, e.g.,
    <pre>      The computer chose to play a 9 of Clubs to capture the 9 of
      Diamonds and the set of 6 of Hearts and 3 of Spades.
      It wanted to maximize the number of captured cards.
    </pre>
</li></ul>

</li><li><b>Deck:</b> 
  In the code that handles creating the deck to be dealt to the players,
build in a configurable option.
If the option is set to true, the code will load the deck of cards 
from a text file instead of randomly generating it.
The text file will contain one card per line (suit character S/C/D/H followed
  by face 1-9/X/J/Q/K/A - note X for 10), as follows:
<pre>  C8
  H9
  D4
  SK
  CQ
  DJ
  SA
</pre>
Note that the first card in the file is at the top of the deck, and
will be the first card dealt.

</li><li>
<b>Help Mode:</b>
When the human player is playing, the computer must provide a help mode:
<ul>
 <li> If the human player asks for a recommendation,
   the computer must suggest:
   <ul>
     <li> Which card to play
     </li><li> What action to take - capture, build or trail;
     </li><li> Why - e.g., to capture as many cards as possible, to prevent
       opponent from capturing his own build, etc.
   </li></ul>
The computer must use its own playing strategy to come up with this recommendation.
</li></ul>

</li><li> 
<b>Serialization:</b> The user should be able
to suspend the game before either player's turn, and resume at a later time from
where the game was left off. In order to do this:
  <ul>
   <li> Provide the option to serialize before each player's turn
   </li><li> When the serialization option is exercised, your program should
save the current state of the game into a file and quit.
We will use text format for the file.
<br>

<hr>

The text format for Java/Android will be as follows:
<pre>Round: 3
  
Computer:
   Score: 17
   Hand: H5 H6 D4 D7 
   Pile: SX SQ SK D6 H8
 
Human:
   Score: 14
   Hand: SA S4 CA C9
   Pile: DJ DA C3 C5

Table:  [ [C6 S3] [S9] ] C8 CJ HA 

Build Owner: [ [C6 S3] [S9] ] Human

Last Capturer: Human

Deck: S7 D3 D5 H2 H3 S5 D8 C2 H9 CX CQ CK HJ S2 S6 D9 DX DQ DK D2 HX HQ HK C4 C7 S8 SJ H4 H7 

Next Player: Human
</pre>
The above snapshot is for the 3rd round in the tournament.
The computer has scored 17 points in the first two rounds. On this
round, it has four cards in its hand: 5 of Hearts, 6 of Hearts, 4 of
Diamonds and 7 of Diamonds. In its pile of captured cards are 10 of
Spades, Queen of Spades, King of Spades, 6 of Diamonds, and 8 of Hearts.
On the table is a multiple build adding up to 9. It contains one build
of 6 of Clubs and 3 of Spades and another build of 9 of Spades.
Also on the table are the loose cards 8 of Clubs, Jack of Clubs and Ace
of Hearts. The owner of the build on the table is the Human.
(If there are several (not multiple) builds on the table, the file will contain a separate
entry for the ownership of each build. If there is no build, the file will not containm Build Owner tag.)
The player who was the last to capture before the game was suspended was the human player.
The first card that will be dealt from the deck is 7 of
Spades. The next player to play on this round is the human player. 
<p>
<hr>

   </li><li> When your program is started, it should provide the option to 
resume a game from a previously saved state. If yes, it should ask for
the name of the text file from which to read the current state of the
game, and resume playing from that state.
  </li></ul>
 
</li>


<h2>Acknowledgments</h2>

This game was adapted and modified from the description at
<a href="https://www.pagat.com/fishing/royal_casino.html">pagat.com</a>

   


</body></html>
