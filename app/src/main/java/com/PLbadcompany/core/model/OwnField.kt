package com.PLbadcompany.core.model

import android.util.Log
import com.PLbadcompany.core.Field
import com.PLbadcompany.core.InvalidFieldCoordinatesException
import com.PLbadcompany.core.SameFieldShotAgainExpection
import com.PLbadcompany.core.controller.ArActivityController

class OwnField() : Field() {
    private val TAG = this::class.java.simpleName

    init {
        val shipsPlacementGenerator = ShipsPlacementGenerator()
        cellStatesMap = shipsPlacementGenerator.generate()
    }

    fun handleShot(arActivityController: ArActivityController, x: Int, y: Int, update: Boolean): ShotResponse {
        Log.v(TAG, "BEFORE")
        for (i in (0..9)) {
            for (j in (0..9)) {
                Log.v(TAG, "$i, $j, ${cellStatesMap[i][j]}")
            }
        }

        if (x < 0 || x >= 10)
            throw InvalidFieldCoordinatesException("Row of a cell should be between 0 and 9, but $x was given")

        if (y < 0 || y >= 10)
            throw InvalidFieldCoordinatesException("Column of a cell should be between 0 and 9, but $y was given")

        when (cellStatesMap[y][x]) {
            CellState.UNTOUCHED -> {
                cellStatesMap[y][x] = CellState.SHOT
                if (update)
                    arActivityController.updateCellState(true, x, y, CellState.SHOT)

                Log.v(TAG, "AFTER")
                for (i in (0..9)) {
                    for (j in (0..9)) {
                        Log.v(TAG, "$i, $j, ${cellStatesMap[i][j]}")
                    }
                }

                return ShotResponse.PAST
            }

            CellState.SHIP -> {
                cellStatesMap[y][x] = CellState.SHOT_SHIP
                // Check if there are parts of ship in 4 directions
                if (x - 1 >= 0 && cellStatesMap[y][x - 1] == CellState.SHIP) {
                    if (update)
                        arActivityController.updateCellState(true, x - 1, y, CellState.SHOT_SHIP)
                    return ShotResponse.WOUND
                }
                else if (x + 1 <= 9 && cellStatesMap[y][x + 1] == CellState.SHIP) {
                    if (update)
                        arActivityController.updateCellState(true, x + 1, y, CellState.SHOT_SHIP)
                    return ShotResponse.WOUND
                }
                else if (y - 1 >= 0 && cellStatesMap[y - 1][x] == CellState.SHIP) {
                    if (update)
                        arActivityController.updateCellState(true, x, y - 1, CellState.SHOT_SHIP)
                    return ShotResponse.WOUND
                }
                else if (y + 1 <= 9 && cellStatesMap[y + 1][x] == CellState.SHIP) {
                    if (update)
                        arActivityController.updateCellState(true, x, y + 1, CellState.SHOT_SHIP)
                    return ShotResponse.WOUND
                }
                else {
                    if (update)
                        surroundKilledShipWithShotStates(arActivityController, true, x, y)
                    else
                        surroundKilledCellWithShotStatesNoUpdate(x, y)
                    return ShotResponse.KILL
                }

            }

            CellState.SHOT -> throw SameFieldShotAgainExpection("Field $x, $y has ${cellStatesMap[y][x]} state already")
            CellState.SHOT_SHIP -> throw SameFieldShotAgainExpection("Field $x, $y has ${cellStatesMap[y][x]} state already")
        }

        Log.v(TAG, "AFTER2")
        for (i in (0..9)) {
            for (j in (0..9)) {
                Log.v(TAG, "$i, $j, ${cellStatesMap[i][j]}")
            }
        }

        return ShotResponse.PAST
    }

}