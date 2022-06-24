package com.PLbadcompany.core.model

class PlayerAI : Player() {
    fun shoot(): Shot {
        return Shot((0..9).random(), (0..9).random())
    }
}