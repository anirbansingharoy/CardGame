package domain

import arrow.core.Option
import arrow.core.getOrElse
import domain.Player.DEALER
import domain.Player.SAM
import domain.Rule.BLACKJACK_ON_FIRST_SHUFFLE
import domain.Rule.BOTH_WITH_22
import domain.Rule.DEALER_GREATER_THAN_21
import domain.Rule.HIGHEST_SCORE
import domain.Rule.SAM_GREATER_THAN_21
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GameRuleTest {

    @Test
    fun `should return null if no players satisfies blackjack rule`() {
        val playerSamHand = PlayerHand(SAM, setOf(Card(Suit.HEARTS, Value.KING),
                Card(Suit.CLUBS, Value._10)))
        val playerDealerHand = PlayerHand(DEALER, setOf(Card(Suit.HEARTS, Value.QUEEN),
                Card(Suit.CLUBS, Value._10)))

        assertThat(applyGameRuleAndGetWinner(BLACKJACK_ON_FIRST_SHUFFLE, listOf(playerSamHand, playerDealerHand)))
                .isEqualTo(Option.empty<Pair<PlayerHand, List<PlayerHand>>>())
    }

    @Test
    fun `should return winner player hand if one of player satisfies with blackjack rule`() {
        val playerSamHand = PlayerHand(SAM, setOf(Card(Suit.HEARTS, Value.KING),
                Card(Suit.CLUBS, Value._10)))
        val playerDealerHand = PlayerHand(DEALER, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._10)))

        assertThat(applyGameRuleAndGetWinner(BLACKJACK_ON_FIRST_SHUFFLE, listOf(playerSamHand, playerDealerHand)))
                .isNotNull
                .isEqualTo(Option.just(Pair(playerDealerHand, listOf(playerSamHand, playerDealerHand))))
    }

    @Test
    fun `should return SAM's hand if both satisfies with blackjack rule`() {
        val playerSamHand = PlayerHand(SAM, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value.QUEEN)))
        val playerDealerHand = PlayerHand(DEALER, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._10)))

        assertThat(applyGameRuleAndGetWinner(BLACKJACK_ON_FIRST_SHUFFLE, listOf(playerSamHand, playerDealerHand)))
                .isNotNull
                .isEqualTo(Option.just(Pair(playerSamHand, listOf(playerSamHand, playerDealerHand))))
    }
    @Test
    fun `should return DEALER's hand if both having 22 as their hand score`() {
        val playerSamHand = PlayerHand(SAM, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value.ACE)))
        val playerDealerHand = PlayerHand(DEALER, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value.ACE)))

        assertThat(applyGameRuleAndGetWinner(BOTH_WITH_22, listOf(playerSamHand, playerDealerHand)))
                .isNotNull
                .isEqualTo(Option.just(Pair(playerDealerHand, listOf(playerSamHand, playerDealerHand))))
    }

    @Test
    fun `should return DEALER's hand if sam is having greater than 21 score in hand`() {
        val playerSamHand = PlayerHand(SAM, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._9), Card(Suit.CLUBS, Value._8) ))
        val playerDealerHand = PlayerHand(DEALER, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._8), Card(Suit.CLUBS, Value._5)))

        assertThat(applyGameRuleAndGetWinner(SAM_GREATER_THAN_21, listOf(playerSamHand, playerDealerHand)))
                .isNotNull
                .isEqualTo(Option.just(Pair(playerDealerHand, listOf(playerSamHand, playerDealerHand))))
    }

    @Test
    fun `should return SAM's hand if Dealer is having greater than 21 score in hand`() {
        val playerDealerHand = PlayerHand(DEALER, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._9), Card(Suit.CLUBS, Value._8) ))
        val playerSamHand = PlayerHand(SAM, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._8), Card(Suit.CLUBS, Value._5)))

        assertThat(applyGameRuleAndGetWinner(DEALER_GREATER_THAN_21, listOf(playerSamHand, playerDealerHand)))
                .isNotNull
                .isEqualTo(Option.just(Pair(playerSamHand, listOf(playerSamHand, playerDealerHand))))
    }

    @Test
    fun `should return highest hand between both hands`() {
        val playerDealerHand = PlayerHand(DEALER, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._9), Card(Suit.CLUBS, Value._8) ))
        val playerSamHand = PlayerHand(SAM, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._8), Card(Suit.CLUBS, Value._5)))

        assertThat(applyGameRuleAndGetWinner(HIGHEST_SCORE, listOf(playerSamHand, playerDealerHand)))
                .isNotNull
                .isEqualTo(Option.just(Pair(playerDealerHand, listOf(playerSamHand, playerDealerHand))))
    }
}