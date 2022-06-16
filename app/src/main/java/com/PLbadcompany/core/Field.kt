package com.PLbadcompany.core

import java.lang.Exception

open class Field() {
    var cellStatesMap : Array<Array<CellState?>> = Array(10) {Array(10) {null} }

    fun acceptShot(x: Int, y: Int) {
        if (x < 0 || x >= 10)
            throw InvalidFieldCoordinatesException("Row of a cell should be between 0 and 9, but $x was given")

        if (y < 0 || y >= 10)
            throw InvalidFieldCoordinatesException("Column of a cell should be between 0 and 9, but $y was given")

        when (cellStatesMap[x][y]) {
            CellState.UNTOUCHED -> cellStatesMap[x][y] = CellState.SHOT
            CellState.SHIP -> cellStatesMap[x][y] = CellState.SHOT_SHIP
            CellState.SHOT -> throw SameFieldShotAgainExpection("Field $x, $y has ${cellStatesMap[x][y]} state already")
            CellState.SHOT_SHIP -> throw SameFieldShotAgainExpection("Field $x, $y has ${cellStatesMap[x][y]} state already")
        }
    }
}

class InvalidFieldCoordinatesException (message : String) : Exception()
class SameFieldShotAgainExpection (message: String) : Exception()