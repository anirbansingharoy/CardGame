package service

import domain.Card
import domain.Suit
import domain.Value

class CardGameService {
    companion object {
        val totalCardsAvailableForPlay: Set<Card>
            get() = Suit.values()
                    .flatMap { suit ->
                        Value.values()
                                .map { Card(suit, it) }
                    }.toSet()

    }
}