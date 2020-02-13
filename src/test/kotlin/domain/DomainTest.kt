package domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DomainTest {
    @Test
    fun `should remove the card from available card for boards after giving to the player`() {

        val board = Board(mutableListOf(Card(Suit.CLUBS, Value._10), Card(Suit.HEARTS, Value._9), Card(Suit.HEARTS, Value.ACE)))
        val retrievedCard = board.popCard()

        assertThat(board.cardsInGame)
                .hasSize(2)
                .doesNotContain(retrievedCard)
    }
    @Test
    fun `should return appropriate sum for a players hand` (){
        val playerHand = PlayerHand(Player.SAM, setOf(Card(Suit.HEARTS, Value.ACE),
                Card(Suit.CLUBS, Value._10),
                Card(Suit.SPADES, Value._9)))
        assertThat(playerHand.score)
                .isEqualTo(30)
    }
}