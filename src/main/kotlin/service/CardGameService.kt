package service

import arrow.core.Option
import arrow.core.orElse
import domain.Board
import domain.Card
import domain.Player
import domain.Player.DEALER
import domain.Player.SAM
import domain.PlayerHand
import domain.Rule.BLACKJACK_ON_FIRST_SHUFFLE
import domain.Rule.BOTH_WITH_22
import domain.applyGameRuleAndGetWinner

fun play(cards: List<Card>): Option<PlayerHand> {
    val board = Board(cards.toMutableList())
    val playersHands =  distributeFirstRound(board, listOf(SAM, DEALER))
    return applyGameRuleAndGetWinner(BLACKJACK_ON_FIRST_SHUFFLE, playersHands)
            .orElse { applyGameRuleAndGetWinner(BOTH_WITH_22, playersHands) }
            .orElse {
                drawNewCardsFromBoard(board, playersHands.first { it.player == SAM }.score, 17)
                        .let { drawnCards ->
                            addCardsToPlayersHand(SAM, playersHands, drawnCards)
                        }.let { applyGameRuleAndGetWinner(BOTH_WITH_22, playersHands) }

            }
}

private fun addCardsToPlayersHand(player: Player, playersHands: List<PlayerHand>, drawnCards: List<Card>): List<PlayerHand> {
    return listOf(playersHands.first { it.player == player }
            .copy(cardsInHand = playersHands.first { it.player == SAM }.cardsInHand.plus(drawnCards)))
            .plus(playersHands.first { it.player == DEALER })
}

private fun drawNewCardsFromBoard(board: Board, currentScore: Int, upperScore: Int): List<Card> {
    var scoreOfTheHand = currentScore
    val drawnCards = mutableListOf<Card>()
    while (scoreOfTheHand < upperScore) {
        board.popCard()
                .also { scoreOfTheHand += it.value.weight }
                .also { drawnCards.add(it) }
    }
    return drawnCards
}

fun distributeFirstRound(board: Board, players: List<Player>): List<PlayerHand> {
    return (1..2).flatMap {
        players.map { Pair(it, board.popCard()) }
    }.let { pair ->
        players.map { player ->
            PlayerHand(player, pair.filter { it.first == player }
                    .map { it.second }.toSet())
        }
    }
}
