package com.PLbadcompany.core

class OwnField() : Field() {
    init {
        for (i in 0..9) {
            for (j in 0..9) {
                cellStatesMap[i][j] = CellState.UNTOUCHED
            }
        }

        generateShipsPlacement()
    }

    fun generateShipsPlacement() {
        cellStatesMap[0][0] = CellState.SHIP
        cellStatesMap[4][5] = CellState.SHIP
        cellStatesMap[5][5] = CellState.SHIP
        cellStatesMap[6][0] = CellState.SHIP
        cellStatesMap[8][8] = CellState.SHIP
        cellStatesMap[8][7] = CellState.SHIP
        cellStatesMap[8][6] = CellState.SHIP

        cellStatesMap[1][1] = CellState.SHOT
        cellStatesMap[1][3] = CellState.SHOT
        cellStatesMap[8][9] = CellState.SHOT_SHIP
        cellStatesMap[6][5] = CellState.SHOT_SHIP
    }

}