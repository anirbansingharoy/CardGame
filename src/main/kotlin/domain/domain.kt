package domain

import arrow.core.Option
import arrow.core.Option.Companion.empty
import arrow.core.Option.Companion.just
import arrow.core.toOption
import domain.Player.DEALER
import domain.Player.SAM
import domain.Rule.BLACKJACK_ON_FIRST_SHUFFLE
import domain.Rule.BOTH_WITH_22
import domain.Rule.DEALER_GREATER_THAN_21
import domain.Rule.HIGHEST_SCORE
import domain.Rule.SAM_GREATER_THAN_21

data class Card(
        val suit: Suit,
        val value: Value
) {
    override fun toString(): String {
        return this.suit.referenceCode.plus(this.value.referenceCode)
    }
}

data class Board(
        val cardsInGame: MutableList<Card>
) {
    fun popCard(): Card {
        return this.cardsInGame[0].also { this.cardsInGame.removeAt(0) }
    }
}

enum class Suit(val referenceCode: String) {
    CLUBS("C"),
    DIAMONDS("D"),
    HEARTS("H"),
    SPADES("S");
    companion object {
        fun getSuit(suit: Char): Option<Suit> {
            return values().find { it.referenceCode == suit.toString() }.toOption()
        }
    }
}

enum class Value(val referenceCode: String, val weight: Int) {
    _2("2", 2),
    _3("3", 3),
    _4("4", 4),
    _5("5", 5),
    _6("6", 6),
    _7("7", 7),
    _8("8", 8),
    _9("9", 9),
    _10("10", 10),
    JACK("J", 10),
    QUEEN("Q", 10),
    KING("K", 10),
    ACE("A", 11);

    companion object {
        fun getValue(value: String): Option<Value> {
            return values().find { it.referenceCode == value.toString() }.toOption()
        }
    }
}

data class PlayerHand(
        val player: Player,
        val cardsInHand: Set<Card>
) {
    val score: Int =
            cardsInHand.sumBy { it.value.weight }

    fun contains(value: Value): Boolean {
        return this.cardsInHand.any { it.value == value }
    }

    private fun containsAnyOf(values: List<Value>): Boolean {
        return values.any { contains(it) }
    }

    val hasBlackJack: Boolean = (this.cardsInHand.size == 2)
            .and(this.score == 21)
            .and(this.contains(Value.ACE)
                    .and(this.containsAnyOf(listOf(Value._10, Value.JACK, Value.QUEEN, Value.KING))))

}

enum class Rule {
    BLACKJACK_ON_FIRST_SHUFFLE,
    BOTH_WITH_22,
    SAM_GREATER_THAN_21,
    DEALER_GREATER_THAN_21,
    HIGHEST_SCORE
}

enum class Player {
    SAM,
    DEALER
}