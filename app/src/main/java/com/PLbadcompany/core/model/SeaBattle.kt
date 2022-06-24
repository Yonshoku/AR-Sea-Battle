package com.PLbadcompany.core.model

import android.util.Log
import com.PLbadcompany.core.controller.ArActivityController

// TODO
// 1. Create queue of players
// 2. Win and lose logic

class SeaBattle (private val gameMode: Int, player: Player){
    private val TAG = this::class.java.simpleName

    var player1: Player? = null
    var player2: Player? = null

    init {
        when(gameMode) {
            SINGLE_PLAYER_MODE -> {
                player1 = player
                player2 = PlayerAI()
            }
        }
    }

    fun handleShot(arActivityController: ArActivityController, x: Int, y: Int) {
        Log.v(TAG, (0..4).random().toString())
        when(gameMode) {
            SINGLE_PLAYER_MODE -> {
                // Player1 shoot player2
                // Update player1.enemyField and player2.ownField
                val shotResponsePlayer2: ShotResponse =
                    player2!!.ownField.handleShot(arActivityController, x, y, false)
                player1!!.enemyField.handleShotResponse(arActivityController, shotResponsePlayer2, x, y, true)

                // AI shoot back
                val shot: Shot = (player2 as PlayerAI).shoot()
                val shotResponsePlayer1: ShotResponse =
                    player1!!.ownField.handleShot(arActivityController, shot.x, shot.y, true)
                player2!!.enemyField.handleShotResponse(arActivityController, shotResponsePlayer1, x, y, false)

            }
        }
    }

    companion object {
        val SINGLE_PLAYER_MODE = 0
        val LAN_MODE = 1
        val MULTIPLAYER_MODE = 2
    }
}