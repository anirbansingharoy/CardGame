import arrow.core.Option
import arrow.core.orElse
import domain.Card
import domain.Suit
import domain.Value.Companion.getValue
import domain.totalCardsAvailableForPlay
import service.play
import java.io.File

fun main(args: Array<String>) {
    println("####### Welcome to Card Game ########")
    val cardsInTheGame = when (args.isEmpty()) {
        true -> totalCardsAvailableForPlay
        false -> readFileInput(args[0])
    }
    if (cardsInTheGame.any { it == null }.or(cardsInTheGame.isEmpty())) {
        return
    }
    print("\n******** Cards in Game ******** \n")
    cardsInTheGame.filterNotNull().forEach { print(it).also { print(", ") } }.also { print("\n\n") }

    play(cardsInTheGame.filterNotNull().toSet().toList().shuffled())
            .also { print("******** Printing Results ********* \n") }
            .also { print(it.first.name) }
            .also { result -> result.second.forEach { print("\n".plus(it)) } }
            .also { print("\n".plus("******** Game Ends ********* ")) }
}

fun readFileInput(filePath: String): List<Card?> {
    return parseFileInput(File(filePath).useLines { it.toList() }[0])
}

fun parseFileInput(it: String): List<Card?> {
    return it.split(",")
            .map { cards ->
                cards.isEmpty().let {
                    if (it) {
                        null
                    } else {
                        Pair(cards[0], cards.substring(1, cards.length))
                                .let { suitAndValueCombination ->
                                    Suit.getSuit(suitAndValueCombination.first)
                                            .flatMap { suit ->
                                                getValue(suitAndValueCombination.second)
                                                        .map { value -> Card(suit, value) }
                                                        .orElse { printMessage("Value Code ${suitAndValueCombination.second} not recognized. Enter valid Value code") }
                                            }
                                            .orElse { printMessage("Suit code ${suitAndValueCombination.first} not recognized. Enter valid suit code") }
                                            .orNull()
                                }
                    }
                }
            }
}

private fun printMessage(message: String): Option<Card> {
    print(message)
    return Option.empty()
}
