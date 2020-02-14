import domain.Card
import domain.Suit
import domain.Value
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

internal class FileParserTest(){
    @Test
    fun `should read comma separated cards from text file`() {
        assertThat(parseFileInput("H9,SA"))
                .hasSize(2)
                .satisfies {
                    assertThat(it.any { card -> card == null })
                            .isFalse()
                }
    }

    @Test
    fun `should not map invalid suit code and value code combination`() {

        val result = parseFileInput("K9,SA,H7,M,M12222,,C9")
                .partition { it == null }

        assertThat(result.first)
                .hasSize(4)

        assertThat(result.second)
                .hasSize(3)
                .containsAll(listOf(Card(Suit.SPADES, Value.ACE),Card(Suit.HEARTS, Value._7), Card(Suit.CLUBS, Value._9)))
    }

    @Test
    fun `should read contains from file`() {
        assertThat(readFileInput("src/test/resources/testInputFile.txt"))
                .hasSize(4)
    }

}
