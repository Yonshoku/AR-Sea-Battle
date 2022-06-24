package com.PLbadcompany.core

import com.PLbadcompany.core.controller.ArActivityController
import com.PLbadcompany.core.model.CellState
import com.PLbadcompany.core.model.ShotResponse
import java.lang.Exception

open class Field() {
    var cellStatesMap : Array<Array<CellState?>> = Array(10) {Array(10) {null} }
    var arActivityController : ArActivityController? = null
    var isOwn: Boolean = false

    fun surroundKilledShipWithShotStates(arActivityController: ArActivityController, isOwn: Boolean, x: Int, y: Int) {
        this.arActivityController = arActivityController
        this.isOwn = isOwn

        // Find whole killed ship and surround it with SHOT states
        surroundKilledCellWithShotStates(x, y)

        // Left
        if (x - 1 >= 0 && cellStatesMap[y][x - 1] == CellState.SHOT_SHIP) {
            surroundKilledCellWithShotStates(x - 1, y)
            if (x - 2 >= 0 && cellStatesMap[y][x - 2] == CellState.SHOT_SHIP)
                surroundKilledCellWithShotStates(x - 2, y)
            if (x - 3 >= 0 && cellStatesMap[y][x - 3] == CellState.SHOT_SHIP)
                surroundKilledCellWithShotStates(x - 3, y)
        }

        // Top
        if (y - 1 >= 0 && cellStatesMap[y - 1][x] == CellState.SHOT_SHIP) {
            surroundKilledCellWithShotStates(x , y - 1)
            if (y - 2 >= 0 && cellStatesMap[y - 2][x] == CellState.SHOT_SHIP)
                surroundKilledCellWithShotStates(x, y - 2)
            if (y - 3 >= 0 && cellStatesMap[y - 3][x] == CellState.SHOT_SHIP)
                surroundKilledCellWithShotStates(x, y - 3)
        }

        // Right
        if (x + 1 <= 9 && cellStatesMap[y][x + 1] == CellState.SHOT_SHIP) {
            surroundKilledCellWithShotStates(x + 1, y)
            if (x + 2 <= 9 && cellStatesMap[y][x + 2] == CellState.SHOT_SHIP)
                surroundKilledCellWithShotStates(x + 2, y)
            if (x + 3 <= 9 && cellStatesMap[y][x + 3] == CellState.SHOT_SHIP)
                surroundKilledCellWithShotStates(x + 3, y)
        }

        // Bottom
        if (y + 1 <= 9 && cellStatesMap[y + 1][x] == CellState.SHOT_SHIP) {
            surroundKilledCellWithShotStates(x , y + 1)
            if (y + 2 <= 9 && cellStatesMap[y + 2][x] == CellState.SHOT_SHIP)
                surroundKilledCellWithShotStates(x,  y + 2)
            if (y + 3 <= 9 && cellStatesMap[y + 3][x] == CellState.SHOT_SHIP)
                surroundKilledCellWithShotStates(x, y + 3)
        }
    }

    fun surroundKilledCellWithShotStates(x: Int, y: Int) {
        cellStatesMap[y][x] != CellState.SHOT_SHIP
        arActivityController!!.updateCellState(isOwn, x, y, CellState.SHOT_SHIP)

        if (y - 1 >= 0) {
            // Left top corner
            if (x - 1 >= 0) {
                cellStatesMap[y - 1][x - 1] = CellState.SHOT
                arActivityController!!.updateCellState(isOwn, x - 1, y - 1, CellState.SHOT)
            }

            // Above
            if (cellStatesMap[y - 1][x] != CellState.SHOT_SHIP) {
                cellStatesMap[y - 1][x] = CellState.SHOT
                arActivityController!!.updateCellState(isOwn, x, y - 1, CellState.SHOT)
            }

            // Right top corner
            if (x + 1 <= 9) {
                cellStatesMap[y - 1][x + 1] = CellState.SHOT
                arActivityController!!.updateCellState(isOwn, x + 1, y - 1, CellState.SHOT)
            }
        }

        // Left
        if (x - 1 >= 0 && cellStatesMap[y][x - 1] != CellState.SHOT_SHIP) {
            cellStatesMap[y][x - 1] = CellState.SHOT
            arActivityController!!.updateCellState(isOwn, x - 1, y, CellState.SHOT)
        }

        // Right
        if (x + 1 <= 9 && cellStatesMap[y][x + 1] != CellState.SHOT_SHIP) {
            cellStatesMap[y][x + 1] = CellState.SHOT
            arActivityController!!.updateCellState(isOwn, x + 1, y, CellState.SHOT)
        }

        if (y + 1 <= 9) {
            // Left bottom corner
            if (x - 1 >= 0) {
                cellStatesMap[y + 1][x - 1] = CellState.SHOT
                arActivityController!!.updateCellState(isOwn, x - 1, y + 1, CellState.SHOT)
            }

            // Bottom
            if (cellStatesMap[y + 1][x] != CellState.SHOT_SHIP) {
                cellStatesMap[y + 1][x] = CellState.SHOT
                arActivityController!!.updateCellState(isOwn, x, y + 1, CellState.SHOT)
            }

            // Right bottom corner
            if (x + 1 <= 9) {
                cellStatesMap[y + 1][x + 1] = CellState.SHOT
                arActivityController!!.updateCellState(isOwn, x + 1, y + 1, CellState.SHOT)
            }
        }
    }

    fun surroundKilledCellWithShotStatesNoUpdate(x: Int, y: Int) {
        cellStatesMap[y][x] != CellState.SHOT_SHIP

        if (y - 1 >= 0) {
            // Left top corner
            if (x - 1 >= 0) {
                cellStatesMap[y - 1][x - 1] = CellState.SHOT
            }

            // Above
            if (cellStatesMap[y - 1][x] != CellState.SHOT_SHIP) {
                cellStatesMap[y - 1][x] = CellState.SHOT
            }

            // Right top corner
            if (x + 1 <= 9) {
                cellStatesMap[y - 1][x + 1] = CellState.SHOT
            }
        }

        // Left
        if (x - 1 >= 0 && cellStatesMap[y][x - 1] != CellState.SHOT_SHIP) {
            cellStatesMap[y][x - 1] = CellState.SHOT
        }

        // Right
        if (x + 1 <= 9 && cellStatesMap[y][x + 1] != CellState.SHOT_SHIP) {
            cellStatesMap[y][x + 1] = CellState.SHOT
        }

        if (y + 1 <= 9) {
            // Left bottom corner
            if (x - 1 >= 0) {
                cellStatesMap[y + 1][x - 1] = CellState.SHOT
            }

            // Bottom
            if (cellStatesMap[y + 1][x] != CellState.SHOT_SHIP) {
                cellStatesMap[y + 1][x] = CellState.SHOT
            }

            // Right bottom corner
            if (x + 1 <= 9) {
                cellStatesMap[y + 1][x + 1] = CellState.SHOT
            }
        }
    }

}

class InvalidFieldCoordinatesException (message : String) : Exception()
class SameFieldShotAgainExpection (message: String) : Exception()