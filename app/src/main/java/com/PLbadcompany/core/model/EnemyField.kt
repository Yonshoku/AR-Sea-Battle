package com.PLbadcompany.core.model

import com.PLbadcompany.core.Field
import com.PLbadcompany.core.controller.ArActivityController

class EnemyField() : Field() {
    init {
        for (i in 0..9) {
            for (j in 0..9) {
                cellStatesMap[i][j] = CellState.UNTOUCHED
            }
        }
    }

    fun handleShotResponse(arActivityController: ArActivityController, shotResponse: ShotResponse, x: Int, y: Int, update: Boolean) {
        when (shotResponse) {
            ShotResponse.PAST -> {
                cellStatesMap[y][x] = CellState.SHOT

                if (update)
                    arActivityController.updateCellState(false, x, y, CellState.SHOT)
            }
            ShotResponse.WOUND -> {
                cellStatesMap[y][x] = CellState.SHOT_SHIP

                if (update)
                    arActivityController.updateCellState(false, x, y, CellState.SHOT_SHIP)
            }
            ShotResponse.KILL -> {
                cellStatesMap[y][x] = CellState.SHOT_SHIP

                if (update)
                    surroundKilledShipWithShotStates(arActivityController, false, x, y)
                else
                    surroundKilledCellWithShotStatesNoUpdate(x, y)
            }
        }
    }


}