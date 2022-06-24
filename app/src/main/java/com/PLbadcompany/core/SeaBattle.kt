package com.PLbadcompany.core

// TODO
// 1. Create queue of players
// 2. Win and lose logic

class SeaBattle (private val gameMode: Int){
    var player1: Player? = null
    var player2: Player? = null
    private var currentPlayer : Player? = player1
    private var id : Int = 0

    init {
        when(gameMode) {
            SINGLE_PLAYER_MODE -> {
                player1 = Player(Player.HUMAN)
                player2 = Player(Player.AI)
            }
        }
    }

    fun shoot(id: Int, x: Int, y: Int) {
        player2!!.ownField.acceptShot(x, y)
        player1!!.enemyField.acceptShot(x, y)
    }

    companion object {
        val SINGLE_PLAYER_MODE = 0
        val LAN_MODE = 1
        val MULTIPLAYER_MODE = 2
    }
}