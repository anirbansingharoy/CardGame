package domain

import arrow.core.Option
import arrow.core.getOrElse
import domain.Rule.BLACKJACK_ON_FIRST_SHUFFLE
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GameRuleTest {

    @Test
    fun `should return null if no players satisfies blackjack rule`() {
        val playerSamHand = PlayerHand(Player.SAM, setOf(Card(Suit.HEARTS, Value.KING),
                Card(Suit.CLUBS, Value._10)))
        val playerDealerHand = PlayerHand(Player.DEALER, setOf(Card(Suit.HEARTS, Value.QUEEN),
                Card(Suit.CLUBS, Value._10)))

        assertThat(applyGameRuleAndGetWinner(BLACKJACK_ON_FIRST_SHUFFLE, listOf(playerSamHand, playerDealerHand)))
                .isEqualTo(Option.empty<PlayerHand>())
    }

    @Test
    fun `should return winner player hand if one of player satisfies with blackjack rule`() {
        val playerSamHand = PlayerHand(Player.SAM, setOf(Card(Suit.HEARTS, Value.KING),
                Card(Suit.CLUBS, Value._10)))
        val playerDealerHand = PlayerHand(Player.DEALER, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._10)))

        assertThat(applyGameRuleAndGetWinner(BLACKJACK_ON_FIRST_SHUFFLE, listOf(playerSamHand, playerDealerHand)).getOrElse { null })
                .isNotNull
                .hasFieldOrPropertyWithValue("player", Player.DEALER)
    }

    @Test
    fun `should return SAM's hand if both satisfies with blackjack rule`() {
        val playerSamHand = PlayerHand(Player.SAM, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value.QUEEN)))
        val playerDealerHand = PlayerHand(Player.DEALER, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._10)))

        assertThat(applyGameRuleAndGetWinner(BLACKJACK_ON_FIRST_SHUFFLE, listOf(playerSamHand, playerDealerHand)).getOrElse { null })
                .isNotNull
                .hasFieldOrPropertyWithValue("player", Player.SAM)
    }
    @Test
    fun `should return DEALER's hand if both having 22 as their hand score`() {
        val playerSamHand = PlayerHand(Player.SAM, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value.ACE)))
        val playerDealerHand = PlayerHand(Player.DEALER, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value.ACE)))

        assertThat(applyGameRuleAndGetWinner(Rule.BOTH_WITH_22, listOf(playerSamHand, playerDealerHand)).getOrElse { null })
                .isNotNull
                .hasFieldOrPropertyWithValue("player", Player.DEALER)
    }

}