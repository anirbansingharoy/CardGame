package domain

data class Card(
        val suit: Suit,
        val value: Value
)

enum class Suit(val code: String) {
    CLUBS("C"),
    DIAMONDS("D"),
    HEARTS("H"),
    SPADES("S")
}

enum class Value(val referenceCode: String, val weight: Int) {
    `2`("2", 2),
    `3`("3", 3),
    `4`("4", 4),
    `5`("5", 5),
    `6`("6", 6),
    `7`("7", 7),
    `8`("8", 8),
    `9`("9", 9),
    `10`("10", 10),
    JACK("J", 10),
    QUEEN("Q", 10),
    KING("K", 10),
    ACE("A", 11)
}

data class PlayerHand(
        val player: Player,
        val cardsInHand: Set<Card>
){
    val score: Int =
            cardsInHand.sumBy { it.value.weight }
}

enum class Player {
    SAM,
    DEALER
}