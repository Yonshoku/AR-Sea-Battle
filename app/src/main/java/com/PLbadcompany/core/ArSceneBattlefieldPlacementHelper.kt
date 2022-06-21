package com.PLbadcompany.core

import android.content.Context
import android.util.Log
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

// Todo
// 1. Clear anchors
// 2. Fill array

class ArSceneBattlefieldPlacementHelper (val arFragment: ArFragment, val context: Context){
    private val TAG = this::class.java.simpleName

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

    private val untouchedColor : Int = android.graphics.Color.parseColor("#4D7EF2")
    private val shipColor : Int = android.graphics.Color.parseColor("#020304")
    private val shotShipColor : Int = android.graphics.Color.parseColor("#D50000")
    private val shotColor : Int = android.graphics.Color.parseColor("#C3D5FF")
    private val delimiterCubeColor : Int = android.graphics.Color.parseColor("#FFFFFF")

    private var shipMaterial: Material? = null
    private var shotMaterial: Material? = null
    private var shotShipMaterial: Material? = null
    private var untouchedMaterial: Material? = null
    private var delimiterCubeMaterial: Material? = null

    private var bigDelimiterWidth : Float = 0.06f
    private val shift : Float = bCubeSize * 10 + delimiterCubeSize * 9 + bigDelimiterWidth

    private var enemyFieldCubes: Array<Array<TransformableNode?>> = Array(10) {Array(10) {null} }
    private var ownFieldCubes: Array<Array<TransformableNode?>> = Array(10) {Array(10) {null} }
    private var enemyShipCubes: Array<Array<TransformableNode?>> = Array(10) {Array(10) {null} }
    private var ownShipCubes: Array<Array<TransformableNode?>> = Array(10) {Array(10) {null} }

    init {
        initMaterialAndShape()
    }

    fun initMaterialAndShape() {
        MaterialFactory.makeOpaqueWithColor(context, Color(untouchedColor))
            .thenAccept { material ->
                val vector3 = Vector3(bCubeSize, bCubeSize, bCubeSize)
                bCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                bCubeRenderable!!.isShadowCaster = false
                bCubeRenderable!!.isShadowReceiver = false

                untouchedMaterial = material
            }

        MaterialFactory.makeOpaqueWithColor(context, Color(shotColor))
            .thenAccept { material ->
                val vector3 = Vector3(bShotCubeSize, bShotCubeSize, bShotCubeSize)
                bShotCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                bShotCubeRenderable!!.isShadowCaster = false
                bShotCubeRenderable!!.isShadowReceiver = false

                shotMaterial = material
            }

        MaterialFactory.makeOpaqueWithColor(context, Color(shipColor))
            .thenAccept { material ->
                val vector3 = Vector3(shipCubeSize, shipCubeSize, shipCubeSize)
                shipCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                shipCubeRenderable!!.isShadowCaster = false
                shipCubeRenderable!!.isShadowReceiver = false

                shipMaterial = material
            }

        MaterialFactory.makeOpaqueWithColor(context, Color(shotShipColor))
            .thenAccept { material ->
                val vector3 = Vector3(shotShipCubeSize, shotShipCubeSize, shotShipCubeSize)
                shotShipCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                shotShipCubeRenderable!!.isShadowCaster = false
                shotShipCubeRenderable!!.isShadowReceiver = false

                shotShipMaterial = material
            }

        MaterialFactory.makeOpaqueWithColor(context, Color(delimiterCubeColor))
            .thenAccept { material ->
                val vector3 = Vector3(delimiterCubeSize, delimiterCubeSize, delimiterCubeSize)
                delimiterCubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                delimiterCubeRenderable!!.isShadowCaster = false
                delimiterCubeRenderable!!.isShadowReceiver = false

                delimiterCubeMaterial = material
            }

    }

    fun placeBattlefield(anchorNode: AnchorNode, ownField: OwnField, enemyField: EnemyField) {
        val y : Float = bCubeSize / 2

        placeField(anchorNode, 0f, ownField)

        // Place delimiter between fields
        placeParallelepiped(
            anchorNode, delimiterCubeRenderable, delimiterCubeSize,
            3, 5, 59,
            shift - 5 * delimiterCubeSize, 0.01f, -2 * delimiterCubeSize
        )

        placeField(anchorNode, shift, enemyField)
    }

    fun updateBattlefield(anchorNode: AnchorNode, x : Int, y : Int, state : CellState) {
        
    }

    fun placeField(anchorNode: AnchorNode, shift: Float, field: Field) {
        var bCube: TransformableNode?
        var shipCube: TransformableNode?
        val y : Float = bCubeSize / 2 // Height above the ground

        for (i in 0 until 10) {
            for (j in 0 until 10) {
                // Place cube at (x, z) on y plane'

                if (field.cellStatesMap[j][i] == CellState.UNTOUCHED) {
                    bCube = TransformableNode(arFragment.transformationSystem)
                    bCube.renderable = bCubeRenderable
                    bCube.renderable!!.material = untouchedMaterial

                     bCube.localPosition = Vector3(
                        bCubeSize * j + delimiterCubeSize * j + shift,
                        y,
                        bCubeSize * i + delimiterCubeSize * i
                    )

                    bCube.isSelectable = false
                    bCube.parent = anchorNode

                } else {
                    // SHOT, SHOT_SHIP, SHIP
                    bCube = TransformableNode(arFragment.transformationSystem)
                    bCube.renderable = bShotCubeRenderable
                    bCube.renderable!!.material = shotMaterial

                    bCube.localPosition = Vector3(
                        bCubeSize * j + delimiterCubeSize * j + shift,
                        y,
                        bCubeSize * i + delimiterCubeSize * i
                    )

                    bCube.isSelectable = false
                    bCube.parent = anchorNode

                    if (field.cellStatesMap[j][i] == CellState.SHIP) {
                        // SHIP
                        shipCube = TransformableNode(arFragment.transformationSystem)
                        shipCube.renderable = shipCubeRenderable
                        shipCube.renderable!!.material = shipMaterial

                        shipCube.localPosition = Vector3(
                            bCubeSize * j + delimiterCubeSize * j + shift,
                            y / 2 + bCubeSize,
                            bCubeSize * i + delimiterCubeSize * i
                        )

                        shipCube.isSelectable = false
                        shipCube.parent = anchorNode
                        Log.v(TAG, "$j, $i is SHIP")

                    } else if (field.cellStatesMap[j][i] == CellState.SHOT_SHIP) {
                        // SHOT_SHIP
                        shipCube = TransformableNode(arFragment.transformationSystem)
                        shipCube.renderable = shotShipCubeRenderable
                        shipCube.renderable!!.material = shotShipMaterial

                        shipCube.localPosition = Vector3(
                            bCubeSize * j + delimiterCubeSize * j + shift,
                            y / 2 + bCubeSize,
                            bCubeSize * i + delimiterCubeSize * i
                        )

                        shipCube.isSelectable = false
                        shipCube.parent = anchorNode
                        Log.v(TAG, "$j, $i is SHOT_SHIP")
                    }
                }

                if (shift == 0f) ownFieldCubes[i][j] = bCube
                else enemyFieldCubes[i][j] = bCube

                if (j < 9) {
                    // 1. Place delimiter at right of the (x, z) cube
                    placeParallelepiped(
                        anchorNode, delimiterCubeRenderable, delimiterCubeSize,
                        1, 5, 5,
                        bCubeSize * (j + 1) + delimiterCubeSize * (j - 2) + shift,
                        0.01f,
                        bCubeSize * i + delimiterCubeSize * (i - 2)
                    )
                }

                if (i < 9) {
                    // 2. Place delimiter at bottom of the (x, z) cube
                    placeParallelepiped(
                        anchorNode, delimiterCubeRenderable, delimiterCubeSize,
                        5, 5, 1,
                        bCubeSize * j + delimiterCubeSize * (j - 2) + shift,
                        0.01f,
                        bCubeSize * (i + 1) + delimiterCubeSize * (i - 2)
                    )
                }

                if (i < 9 && j < 9) {
                    // 3. Place delimiter at right-bottom of the (x, z) cube
                    placeParallelepiped(
                        anchorNode, delimiterCubeRenderable, delimiterCubeSize,
                        1, 5, 1,
                        bCubeSize * (j + 1) + delimiterCubeSize * (j - 2) + shift,
                        0.01f,
                        bCubeSize * (i + 1) + delimiterCubeSize * (i - 2)
                    )
                }

            }
        }
    }

    private fun placeParallelepiped (anchorNode: AnchorNode, cubeRenderable: ModelRenderable?, cubeSize: Float,
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

    fun eraseBattlefield() {

    }
}