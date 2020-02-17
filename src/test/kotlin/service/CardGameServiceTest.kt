package service

import domain.Card
import domain.Player.DEALER
import domain.Player.SAM
import domain.PlayerHand
import domain.Suit.CLUBS
import domain.Suit.DIAMONDS
import domain.Suit.HEARTS
import domain.Suit.SPADES
import domain.Value.ACE
import domain.Value.KING
import domain.Value._2
import domain.Value._3
import domain.Value._4
import domain.Value._5
import domain.Value._9
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CardGameServiceTest {

    @Test
    fun `should return results based on blackjack rule`() {
        val cardsInGame = listOf(Card(HEARTS, KING),
                Card(CLUBS, _9), Card(CLUBS, ACE), Card(CLUBS, _5))
        assertThat(play(cardsInGame).first)
                .isEqualTo(PlayerHand(SAM, setOf(Card(HEARTS, KING), Card(CLUBS, ACE))))
    }

    @Test
    fun `should return results based on 22 rule`() {
        val cardsInGame = listOf(Card(HEARTS, ACE),
                Card(CLUBS, ACE), Card(SPADES, ACE), Card(DIAMONDS, ACE))
        assertThat(play(cardsInGame).first)
                .isEqualTo(PlayerHand(DEALER, setOf(Card(CLUBS, ACE), Card(DIAMONDS, ACE))))
    }

    @Test
    fun `should return results based on rule of 21 after sam took his set of cards from the deck`() {
        val cardsInGame = listOf(Card(HEARTS, _2),
                Card(CLUBS, _2), Card(SPADES, _3), Card(DIAMONDS, _5), Card(SPADES, ACE), Card(CLUBS, ACE))
        assertThat(play(cardsInGame).first)
                .isEqualTo(PlayerHand(DEALER, setOf(Card(CLUBS, _2), Card(DIAMONDS, _5))))
    }

    @Test
    fun `should return results based on rule of 21 after dealer took his set of cards from the deck`() {
        val cardsInGame = listOf(Card(HEARTS, _2),
                Card(CLUBS, _2), Card(SPADES, _3), Card(DIAMONDS, _5), Card(SPADES, ACE), Card(SPADES, _2), Card(HEARTS, ACE),Card(HEARTS, _4))
        assertThat(play(cardsInGame).first)
                .isEqualTo(PlayerHand(SAM, setOf(Card(HEARTS, _2), Card(SPADES, _3), Card(SPADES, ACE), Card(SPADES, _2))))
    }

    @Test
    fun `should return results based on highest score hand after dealer took his set of cards from the deck`() {
        val cardsInGame = listOf(Card(HEARTS, _2),
                Card(CLUBS, _2), Card(SPADES, _3), Card(DIAMONDS, _5), Card(SPADES, ACE), Card(SPADES, _2), Card(HEARTS, ACE),Card(SPADES, _2))
        assertThat(play(cardsInGame).first)
                .isEqualTo(PlayerHand(DEALER, setOf(Card(CLUBS, _2), Card(DIAMONDS, _5), Card(HEARTS, ACE),Card(SPADES, _2))))
    }
}