package com.PLbadcompany.core.model

class ShipsPlacementGenerator {
    var placements: Array<Array<Array<CellState?>>> = Array(5) {Array(10) {Array(10) {null} } }

    init {
        for (i in (0..4)) {
            for (j in (0..9)) {
                for (k in 0..9) {
                    placements[i][j][k] = CellState.UNTOUCHED
                }
            }
        }
    }

    init {
        // Placement 0
        placements[0][0][1] = CellState.SHIP
        placements[0][1][1] = CellState.SHIP
        placements[0][2][1] = CellState.SHIP
        placements[0][3][1] = CellState.SHIP

        placements[0][0][8] = CellState.SHIP

        placements[0][1][4] = CellState.SHIP
        placements[0][1][5] = CellState.SHIP
        placements[0][1][6] = CellState.SHIP

        placements[0][3][4] = CellState.SHIP

        placements[0][3][8] = CellState.SHIP
        placements[0][4][8] = CellState.SHIP

        placements[0][6][0] = CellState.SHIP
        placements[0][7][0] = CellState.SHIP

        placements[0][6][2] = CellState.SHIP
        placements[0][7][2] = CellState.SHIP
        placements[0][8][2] = CellState.SHIP

        placements[0][6][6] = CellState.SHIP
        placements[0][6][7] = CellState.SHIP

        placements[0][8][6] = CellState.SHIP

        placements[0][9][9] = CellState.SHIP

        // Placement 1
        placements[1][0][1] = CellState.SHIP
        placements[1][0][2] = CellState.SHIP
        placements[1][0][3] = CellState.SHIP
        placements[1][0][4] = CellState.SHIP

        placements[1][0][7] = CellState.SHIP
        placements[1][0][8] = CellState.SHIP

        placements[1][2][5] = CellState.SHIP

        placements[1][2][9] = CellState.SHIP
        placements[1][3][9] = CellState.SHIP

        placements[1][4][1] = CellState.SHIP
        placements[1][4][2] = CellState.SHIP
        placements[1][4][3] = CellState.SHIP

        placements[1][4][7] = CellState.SHIP
        placements[1][5][7] = CellState.SHIP
        placements[1][6][7] = CellState.SHIP

        placements[1][6][0] = CellState.SHIP

        placements[1][6][5] = CellState.SHIP

        placements[1][8][3] = CellState.SHIP
        placements[1][9][3] = CellState.SHIP

        placements[1][9][7] = CellState.SHIP

        // Placement 2
        placements[2][1][1] = CellState.SHIP
        placements[2][1][2] = CellState.SHIP

        placements[2][1][5] = CellState.SHIP

        placements[2][1][8] = CellState.SHIP

        placements[2][4][0] = CellState.SHIP
        placements[2][4][1] = CellState.SHIP
        placements[2][4][2] = CellState.SHIP
        placements[2][4][3] = CellState.SHIP

        placements[2][4][7] = CellState.SHIP
        placements[2][4][8] = CellState.SHIP
        placements[2][4][9] = CellState.SHIP

        placements[2][8][0] = CellState.SHIP
        placements[2][9][0] = CellState.SHIP

        placements[2][7][3] = CellState.SHIP
        placements[2][8][3] = CellState.SHIP
        placements[2][9][3] = CellState.SHIP

        placements[2][7][5] = CellState.SHIP
        placements[2][7][6] = CellState.SHIP

        placements[2][9][6] = CellState.SHIP

        placements[2][8][8] = CellState.SHIP

        // Placement 3
        placements[3][1][1] = CellState.SHIP
        placements[3][2][1] = CellState.SHIP
        placements[3][3][1] = CellState.SHIP

        placements[3][5][0] = CellState.SHIP
        placements[3][5][1] = CellState.SHIP

        placements[3][8][1] = CellState.SHIP
        placements[3][9][1] = CellState.SHIP

        placements[3][0][4] = CellState.SHIP

        placements[3][4][3] = CellState.SHIP
        placements[3][4][4] = CellState.SHIP
        placements[3][4][5] = CellState.SHIP
        placements[3][4][6] = CellState.SHIP

        placements[3][7][3] = CellState.SHIP

        placements[3][9][5] = CellState.SHIP

        placements[3][0][7] = CellState.SHIP
        placements[3][1][7] = CellState.SHIP

        placements[3][4][9] = CellState.SHIP

        placements[3][7][7] = CellState.SHIP
        placements[3][7][8] = CellState.SHIP
        placements[3][7][9] = CellState.SHIP

        // Placement 4
        placements[4][3][0] = CellState.SHIP
        placements[4][3][1] = CellState.SHIP
        placements[4][3][2] = CellState.SHIP

        placements[4][7][1] = CellState.SHIP

        placements[4][9][2] = CellState.SHIP
        placements[4][9][3] = CellState.SHIP

        placements[4][0][4] = CellState.SHIP
        placements[4][1][4] = CellState.SHIP
        placements[4][2][4] = CellState.SHIP

        placements[4][4][4] = CellState.SHIP
        placements[4][5][4] = CellState.SHIP

        placements[4][1][7] = CellState.SHIP

        placements[4][1][9] = CellState.SHIP

        placements[4][4][7] = CellState.SHIP
        placements[4][4][8] = CellState.SHIP

        placements[4][6][6] = CellState.SHIP
        placements[4][6][7] = CellState.SHIP
        placements[4][6][8] = CellState.SHIP
        placements[4][6][9] = CellState.SHIP

        placements[4][9][8] = CellState.SHIP


    }

    fun generate(): Array<Array<CellState?>> {
        var index = (2..4).random()
        return placements[index]
    }
}