package com.PLbadcompany.core

class EnemyField() : Field() {
    init {
        for (i in 0..9) {
            for (j in 0..9) {
                cellStatesMap[i][j] = CellState.UNTOUCHED
            }
        }
    }
}