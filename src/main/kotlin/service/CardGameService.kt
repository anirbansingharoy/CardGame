package service

import arrow.core.orElse
import domain.Board
import domain.Card
import domain.Player
import domain.Player.DEALER
import domain.Player.SAM
import domain.PlayerHand
import domain.Rule.BLACKJACK_ON_FIRST_SHUFFLE
import domain.Rule.BOTH_WITH_22
import domain.Rule.DEALER_GREATER_THAN_21
import domain.Rule.HIGHEST_SCORE
import domain.Rule.SAM_GREATER_THAN_21
import domain.applyGameRuleAndGetWinner

fun play(cards: List<Card>): Pair<PlayerHand, List<PlayerHand>> {
    val board = Board(cards.toMutableList())
    val playersHands = distributeFirstRound(board, listOf(SAM, DEALER))

    return applyGameRuleAndGetWinner(BLACKJACK_ON_FIRST_SHUFFLE, playersHands)
            .orElse { applyGameRuleAndGetWinner(BOTH_WITH_22, playersHands) }
            .orElse {
                addCardsToPlayersHand(SAM, playersHands, drawNewCardsFromBoard(board, playersHands.first { it.player == SAM }.score, 17))
                        .let { newPlayersHands ->
                            applyGameRuleAndGetWinner(SAM_GREATER_THAN_21, newPlayersHands)
                                    .orElse {
                                        addCardsToPlayersHand(DEALER, newPlayersHands, drawNewCardsFromBoard(board, newPlayersHands.first { it.player == DEALER }.score, newPlayersHands.first { it.player == SAM }.score))
                                                .let {
                                                    applyGameRuleAndGetWinner(DEALER_GREATER_THAN_21, it)
                                                            .orElse { applyGameRuleAndGetWinner(HIGHEST_SCORE, it) }
                                                }
                                    }
                        }

            }
            .toList()[0]
}



private fun addCardsToPlayersHand(player: Player, playersHands: List<PlayerHand>, drawnCards: List<Card>): List<PlayerHand> {
    return listOf(playersHands.first { it.player == player }
            .copy(cardsInHand = playersHands.first { it.player == player }.cardsInHand.plus(drawnCards)))
            .plus(playersHands.first { it.player != player })
}

private fun drawNewCardsFromBoard(board: Board, currentScore: Int, upperScore: Int): List<Card> {
    var scoreOfTheHand = currentScore
    val drawnCards = mutableListOf<Card>()
    while (scoreOfTheHand < upperScore+1) {
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
