# Background

This is a CardGame application. There are two players in the game SAM and DEALER. The game will be played with the given set of cards provided in command line. If no cards provided, then the game will be played with all 52 cards.

The score of individual cards are as follows for all Suits (CLUBS, DIAMONDS, HEARTS , SPADES)


| Card | Score |
|----------|-------|
| 2        | 2     |
| 3        | 3     |
| 4        | 4     |
| 5        | 5     |
| 6        | 6     |
| 7        | 7     |
| 8        | 8     |
| 9        | 9     |
| 10       | 10    |
| JACK     | 10    |
| KING     | 10    |
| QUEEN    | 10    |
| ACE      | 11    |

The format for the command line input is as follows: H2(2 of Hearts), CJ(Jack of Clubs), DQ (Queen of Diamonds)

## Rules

- At first, two rounds will be distributed to the players. (1 card each time in one round)
- After first two round of distribution if there is a BlackJack, the player with BlackJack will win. If both of them have BlackJack, then SAM wins. (Rule for BlackJack:  The score of the hand should be 21 . The Hand should contain ACE and any one of 10, JACK,QUEEN,KING)
- After first two round of distribution if score of the Hand is 22 for both players, DEALER wins.
- SAM will start withdrawing cards from the board. SAM will stop withdrawing card if score of the Hand is greater than 17. After withdrawing the cards if the score of the Hand is greater than 21, DEALER wins.
- DEALER will start withdrawing cards from the board. DEALER will stop withdrawing card if score of the Hand is greater than the score of SAM’s Hand. After withdrawing the cards If the score of the DEALER’s hand is greater than 21 , SAM wins.
- Who-ever have the highest score in the hand wins the game.


Building & Running the Application  
---------------

Build
 
```
mvn clean install

```

Execution 

 
```
 
java -jar target/CardGame-1.0-SNAPSHOT-jar-with-dependencies.jar [Input File Path]

```


