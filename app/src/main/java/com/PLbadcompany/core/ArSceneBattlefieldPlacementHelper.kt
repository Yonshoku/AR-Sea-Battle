package com.PLbadcompany.core

import android.content.Context
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class ArSceneBattlefieldPlacementHelper (val arFragment: ArFragment, val context: Context){
    private val TAG = this::class.java.simpleName
    var anchorNode: AnchorNode? = null

    private var bCubeRenderable: ModelRenderable? = null
    private var bCubeSize : Float = 0.1f

    private var bShotCubeRenderable: ModelRenderable? = null
    private var bShotCubeSize: Float = bCubeSize

    private var shipCubeRenderable : ModelRenderable? = null
    private var shipCubeSize : Float = 0.08f

    private var shotShipCubeRenderable: ModelRenderable? = null
    private var shotShipCubeSize: Float = shipCubeSize

    private var delimiterCubeRenderable: ModelRenderable? = null
    private var delimiterCubeSize : Float = 0.02f

    private var activeCubeRenderable: ModelRenderable? = null
    private var activeCubeSize : Float = bCubeSize

    private val untouchedColor : Int = android.graphics.Color.parseColor("#4D7EF2")
    private val shipColor : Int = android.graphics.Color.parseColor("#020304")
    private val shotShipColor : Int = android.graphics.Color.parseColor("#D50000")
    private val shotColor : Int = android.graphics.Color.parseColor("#C3D5FF")
    private val delimiterCubeColor : Int = android.graphics.Color.parseColor("#FFFFFF")
    private val activeColor: Int = android.graphics.Color.parseColor("#FF0009")

    private var shipMaterial: Material? = null
    private var shotMaterial: Material? = null
    private var shotShipMaterial: Material? = null
    private var untouchedMaterial: Material? = null
    private var delimiterCubeMaterial: Material? = null
    private var activeMaterial: Material? = null

    private var bigDelimiterWidth : Float = 0.06f
    private val shift : Float = bCubeSize * 10 + delimiterCubeSize * 9 + bigDelimiterWidth

    private var enemyFieldCubes: Array<Array<TransformableNode?>> = Array(10) {Array(10) {null} }
    private var ownFieldCubes: Array<Array<TransformableNode?>> = Array(10) {Array(10) {null} }
    private var enemyShipCubes: Array<Array<TransformableNode?>> = Array(10) {Array(10) {null} }
    private var ownShipCubes: Array<Array<TransformableNode?>> = Array(10) {Array(10) {null} }

    private var ownField: OwnField? = null
    private var enemyField: EnemyField? = null
    private var activeFieldX: Int = 4
    private var activeFieldY: Int = 4

    init {
        initMaterialAndShape()
    }

    private fun initMaterialAndShape() {
        // UNTOUCHED
        MaterialFactory.makeOpaqueWithColor(context, Color(untouchedColor))
            .thenAccept { material ->
                val vector3 = Vector3(bCubeSize, bCubeSize, bCubeSize)
                bCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                bCubeRenderable!!.isShadowCaster = false
                bCubeRenderable!!.isShadowReceiver = false

                untouchedMaterial = material
            }

        // SHOT
        MaterialFactory.makeOpaqueWithColor(context, Color(shotColor))
            .thenAccept { material ->
                val vector3 = Vector3(bShotCubeSize, bShotCubeSize, bShotCubeSize)
                bShotCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                bShotCubeRenderable!!.isShadowCaster = false
                bShotCubeRenderable!!.isShadowReceiver = false

                shotMaterial = material
            }

        // ACTIVE
        MaterialFactory.makeOpaqueWithColor(context, Color(activeColor))
            .thenAccept { material ->
                val vector3 = Vector3(activeCubeSize, activeCubeSize, activeCubeSize)
                activeCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                activeCubeRenderable!!.isShadowCaster = false
                activeCubeRenderable!!.isShadowReceiver = false

                activeMaterial = material
            }

        // SHIP
        MaterialFactory.makeOpaqueWithColor(context, Color(shipColor))
            .thenAccept { material ->
                val vector3 = Vector3(shipCubeSize, shipCubeSize, shipCubeSize)
                shipCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                shipCubeRenderable!!.isShadowCaster = false
                shipCubeRenderable!!.isShadowReceiver = false

                shipMaterial = material
            }

        // SHOT SHIP
        MaterialFactory.makeOpaqueWithColor(context, Color(shotShipColor))
            .thenAccept { material ->
                val vector3 = Vector3(shotShipCubeSize, shotShipCubeSize, shotShipCubeSize)
                shotShipCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                shotShipCubeRenderable!!.isShadowCaster = false
                shotShipCubeRenderable!!.isShadowReceiver = false

                shotShipMaterial = material
            }

        // DELIMITER
        MaterialFactory.makeOpaqueWithColor(context, Color(delimiterCubeColor))
            .thenAccept { material ->
                val vector3 = Vector3(delimiterCubeSize, delimiterCubeSize, delimiterCubeSize)
                delimiterCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                delimiterCubeRenderable!!.isShadowCaster = false
                delimiterCubeRenderable!!.isShadowReceiver = false

                delimiterCubeMaterial = material
            }

    }

    fun placeBattlefield(ownField: OwnField, enemyField: EnemyField) {
        this.ownField = ownField
        this.enemyField = enemyField

        val y = 0.01f

        placeField(0f, ownField)

        // Place delimiter between fields
        placeParallelepiped(
            delimiterCubeRenderable, delimiterCubeSize,
            3, 5, 59,
            shift - 5 * delimiterCubeSize, y, -2 * delimiterCubeSize
        )

        placeField(shift, enemyField)
    }

    fun setFieldActive(isOwn: Boolean, x: Int, y: Int) {
        val aCube: TransformableNode?
        val bCube: TransformableNode?

        if (isOwn) {
            bCube = ownFieldCubes[x][y]

        } else {
            bCube = enemyFieldCubes[x][y]
        }

        aCube = placeFieldCube(
            activeCubeRenderable,
            activeMaterial,
            bCube!!.localPosition.x,
            bCube.localPosition.y,
            bCube.localPosition.z
        )

        if (isOwn) {
            ownFieldCubes[x][y] = aCube

            // Return previous state to previous active field
            if (!(activeFieldX == x && activeFieldY == y))
                    updateCellState(isOwn, activeFieldX, activeFieldY, ownField!!.cellStatesMap[x][y])
        } else {
            enemyFieldCubes[x][y] = aCube

            // Return previous state to previous active field
            if (!(activeFieldX == x && activeFieldY == y))
                updateCellState(isOwn, activeFieldX, activeFieldY, enemyField!!.cellStatesMap[x][y])
        }

        activeFieldX = x
        activeFieldY = y

        // Remove previous bCube
        anchorNode!!.removeChild(bCube)
    }

    fun updateCellState(isOwn: Boolean, x : Int, y : Int, state : CellState?) {
        val bCube: TransformableNode?
        val shipCube: TransformableNode?

        val newBCube: TransformableNode?
        val newShipCube: TransformableNode?

        if (isOwn) {
            bCube = ownFieldCubes[x][y]
            shipCube = ownShipCubes[x][y]
            ownField!!.cellStatesMap[x][y] = state
        } else {
            bCube = enemyFieldCubes[x][y]
            shipCube = enemyShipCubes[x][y]
            enemyField!!.cellStatesMap[x][y] = state
        }

        when (state) {
            CellState.UNTOUCHED -> {
                newBCube = placeFieldCube(
                    bCubeRenderable,
                    untouchedMaterial,
                    bCube!!.localPosition.x,
                    bCube.localPosition.y,
                    bCube.localPosition.z
                )

                // Remove previous bCube
                anchorNode!!.removeChild(bCube)

                if (isOwn) ownFieldCubes[x][y] = newBCube
                else enemyFieldCubes[x][y] = newBCube
            }

            CellState.SHOT -> {
                newBCube = placeFieldCube(
                    bShotCubeRenderable,
                    shotMaterial,
                    bCube!!.localPosition.x,
                    bCube.localPosition.y,
                    bCube.localPosition.z
                )

                // Remove previous bCube
                anchorNode!!.removeChild(bCube)

                if (isOwn) ownFieldCubes[x][y] = newBCube
                else enemyFieldCubes[x][y] = newBCube
            }

            CellState.SHIP -> {
                newBCube = placeFieldCube(
                    bCubeRenderable,
                    untouchedMaterial,
                    bCube!!.localPosition.x,
                    bCube.localPosition.y,
                    bCube.localPosition.z
                )

                // Remove previous bCube
                anchorNode!!.removeChild(bCube)

                if (isOwn) ownFieldCubes[x][y] = newBCube
                else enemyFieldCubes[x][y] = newBCube


                newShipCube = placeShipCube(
                    shipCubeRenderable,
                    shipMaterial,
                    shipCube!!.localPosition.x,
                    shipCube.localPosition.y,
                    shipCube.localPosition.z
                )

                if (isOwn) ownShipCubes[x][y] = newShipCube
                else enemyShipCubes[x][y] = newShipCube

                // Remove previous shipCube
                anchorNode!!.removeChild(shipCube)
            }

            CellState.SHOT_SHIP -> {
                newBCube = placeFieldCube(
                    bShotCubeRenderable,
                    shotMaterial,
                    bCube!!.localPosition.x,
                    bCube.localPosition.y,
                    bCube.localPosition.z
                )

                // Remove previous bCube
                anchorNode!!.removeChild(bCube)

                if (isOwn) ownFieldCubes[x][y] = newBCube
                else enemyFieldCubes[x][y] = newBCube


                newShipCube = placeShipCube(
                    shotShipCubeRenderable,
                    shotShipMaterial,
                    shipCube!!.localPosition.x,
                    shipCube.localPosition.y,
                    shipCube.localPosition.z
                )

                if (isOwn) ownShipCubes[x][y] = newShipCube
                else enemyShipCubes[x][y] = newShipCube

                // Remove previous shipCube
                anchorNode!!.removeChild(shipCube)
            }
        }

    }

    fun placeFieldCube(renderable: ModelRenderable?, material: Material?, x: Float, y: Float, z: Float): TransformableNode{
        val bCube: TransformableNode?
        bCube = TransformableNode(arFragment.transformationSystem)
        bCube.renderable = renderable
        bCube.renderable!!.material = material

        bCube.localPosition = Vector3(x, y, z)

        bCube.isSelectable = false
        bCube.parent = anchorNode

        return bCube
    }

    fun placeShipCube(renderable: ModelRenderable?, material: Material?, x: Float, y: Float, z: Float): TransformableNode {
        val shipCube: TransformableNode?
        shipCube = TransformableNode(arFragment.transformationSystem)
        shipCube.renderable = renderable
        shipCube.renderable!!.material = material

        shipCube.localPosition = Vector3(x, y, z)

        shipCube.isSelectable = false
        shipCube.parent = anchorNode

        return shipCube
    }

    fun placeField(shift: Float, field: Field) {
        var bCube: TransformableNode?
        var shipCube: TransformableNode?
        val y : Float = bCubeSize / 2 // Height above the ground

        for (i in 0 until 10) {
            for (j in 0 until 10) {
                // Place cube and ship
                when (field.cellStatesMap[j][i]) {
                    CellState.UNTOUCHED -> {
                        bCube = placeFieldCube(
                            bCubeRenderable,
                            untouchedMaterial,
                            bCubeSize * j + delimiterCubeSize * j + shift,
                            y,
                            bCubeSize * i + delimiterCubeSize * i
                        )

                        if (shift == 0f) ownFieldCubes[i][j] = bCube
                        else enemyFieldCubes[i][j] = bCube
                    }


                    CellState.SHOT -> {
                        bCube = placeFieldCube(
                            bShotCubeRenderable,
                            shotMaterial,
                            bCubeSize * j + delimiterCubeSize * j + shift,
                            y,
                            bCubeSize * i + delimiterCubeSize * i
                        )

                        if (shift == 0f) ownFieldCubes[i][j] = bCube
                        else enemyFieldCubes[i][j] = bCube
                    }

                    CellState.SHIP -> {
                        bCube = placeFieldCube(
                            bCubeRenderable,
                            untouchedMaterial,
                            bCubeSize * j + delimiterCubeSize * j + shift,
                            y,
                            bCubeSize * i + delimiterCubeSize * i
                        )

                        if (shift == 0f) ownFieldCubes[i][j] = bCube
                        else enemyFieldCubes[i][j] = bCube

                        shipCube = placeShipCube(
                            shipCubeRenderable,
                            shipMaterial,
                            bCubeSize * j + delimiterCubeSize * j + shift,
                            y / 2 + bCubeSize,
                            bCubeSize * i + delimiterCubeSize * i
                        )

                        if (shift == 0f) ownShipCubes[i][j] = shipCube
                        else enemyShipCubes[i][j] = shipCube
                    }

                    CellState.SHOT_SHIP -> {
                        bCube = placeFieldCube(
                            bShotCubeRenderable,
                            shotMaterial,
                            bCubeSize * j + delimiterCubeSize * j + shift,
                            y,
                            bCubeSize * i + delimiterCubeSize * i
                        )

                        if (shift == 0f) ownFieldCubes[i][j] = bCube
                        else enemyFieldCubes[i][j] = bCube

                        shipCube = placeShipCube(
                            shotShipCubeRenderable,
                            shotShipMaterial,
                            bCubeSize * j + delimiterCubeSize * j + shift,
                            y / 2 + bCubeSize,
                            bCubeSize * i + delimiterCubeSize * i
                        )

                        if (shift == 0f) ownShipCubes[i][j] = shipCube
                        else enemyShipCubes[i][j] = shipCube
                    }
                }

                // Delimiters between field cubes
                if (j < 9) {
                    // 1. Place delimiter at right of the (x, z) cube
                    placeParallelepiped(
                        delimiterCubeRenderable, delimiterCubeSize,
                        1, 5, 5,
                        bCubeSize * (j + 1) + delimiterCubeSize * (j - 2) + shift,
                        0.01f,
                        bCubeSize * i + delimiterCubeSize * (i - 2)
                    )
                }

                if (i < 9) {
                    // 2. Place delimiter at bottom of the (x, z) cube
                    placeParallelepiped(
                        delimiterCubeRenderable, delimiterCubeSize,
                        5, 5, 1,
                        bCubeSize * j + delimiterCubeSize * (j - 2) + shift,
                        0.01f,
                        bCubeSize * (i + 1) + delimiterCubeSize * (i - 2)
                    )
                }

                if (i < 9 && j < 9) {
                    // 3. Place delimiter at right-bottom of the (x, z) cube
                    placeParallelepiped(
                        delimiterCubeRenderable, delimiterCubeSize,
                        1, 5, 1,
                        bCubeSize * (j + 1) + delimiterCubeSize * (j - 2) + shift,
                        0.01f,
                        bCubeSize * (i + 1) + delimiterCubeSize * (i - 2)
                    )
                }

            }
        }
    }

    private fun placeParallelepiped (cubeRenderable: ModelRenderable?, cubeSize: Float,
                                     widthX: Int, heightY: Int, lengthZ: Int,
                                     x: Float, y: Float, z: Float) {
        var cube : TransformableNode?

        for (i in 0 until lengthZ) {
            for (j in 0 until heightY) {
                for (k in 0 until widthX) {
                    // Place cube at (x, z) on y plane
                    cube = TransformableNode(arFragment.transformationSystem)
                    cube.renderable = cubeRenderable
                    cube.parent = anchorNode
                    cube.localPosition = Vector3(
                        cubeSize * k + x,
                        cubeSize * j + y,
                        cubeSize * i + z
                    )
                    cube.isSelectable = false
                }
            }
        }
    }


}