package com.PLbadcompany.core

class Player (val type: Int) {
    val ownField : OwnField = OwnField()
    val enemyField : EnemyField = EnemyField()

    init {
        ownField.generateShipsPlacement()
    }

    companion object {
        val HUMAN = 0
        val AI = 1
    }

}