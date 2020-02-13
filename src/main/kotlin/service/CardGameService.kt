package service

import arrow.core.Option
import arrow.core.orElse
import domain.Board
import domain.Card
import domain.Player
import domain.Player.DEALER
import domain.Player.SAM
import domain.PlayerHand
import domain.Rule
import domain.Rule.BLACKJACK_ON_FIRST_SHUFFLE
import domain.Rule.BOTH_WITH_22
import domain.applyGameRuleAndGetWinner

fun play(cards: List<Card>): Option<PlayerHand> {
    val board = Board(cards.toMutableList())
    val handsAfterFirstRoundOfDraw =  distributeFirstRound(board, listOf(SAM, DEALER))
    return applyGameRuleAndGetWinner(BLACKJACK_ON_FIRST_SHUFFLE, handsAfterFirstRoundOfDraw)
            .orElse { applyGameRuleAndGetWinner(BOTH_WITH_22, handsAfterFirstRoundOfDraw) }
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
