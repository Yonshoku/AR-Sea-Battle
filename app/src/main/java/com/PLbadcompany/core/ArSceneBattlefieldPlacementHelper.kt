package com.PLbadcompany.core

import android.content.Context
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

// Todo
// 1. Clear anchors

class ArSceneBattlefieldPlacementHelper (val arFragment: ArFragment, val context: Context){
    private var cubeRenderable: ModelRenderable? = null
    private var cubeSize : Float = 0.1f
    private var cubeMaterial: Material? = null

    private var delimiterCubeSize : Float = 0.15f
    private var delimiterCubeRenderable: ModelRenderable? = null
    private var delimiterCubeMaterial: Material? = null

    private val untouchedCellColor : Int = android.graphics.Color.parseColor("#4D7EF2")
    private val delimiterCubeColor : Int = android.graphics.Color.parseColor("#333B46")

    private var homeCubes : Array<Array<TransformableNode?>> = Array(10) {Array(10) {null} }
    private var scale : Int = 1

    init {
        initMaterialAndShape(cubeSize)
    }

    fun initMaterialAndShape(size : Float) {
        MaterialFactory.makeOpaqueWithColor(context, Color(untouchedCellColor))
            .thenAccept { material ->
                val vector3 = Vector3(size, size, size)
                cubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                cubeRenderable!!.isShadowCaster = false
                cubeRenderable!!.isShadowReceiver = false

                cubeMaterial = material
            }

        MaterialFactory.makeOpaqueWithColor(context, Color(untouchedCellColor))
            .thenAccept { material ->
                val vector3 = Vector3(size, size, size)
                cubeRenderable = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                cubeRenderable!!.isShadowCaster = false
                cubeRenderable!!.isShadowReceiver = false

                cubeMaterial = material
            }
    }

    fun placeBattlefield(anchorNode: AnchorNode) {
        placeField(anchorNode)
    }

    fun placeField(anchorNode: AnchorNode) {
        var cube : TransformableNode?
        var cube2 : TransformableNode?
        val y : Float = 0.05f * scale // Height above the ground

        for (i in 0..9) {
            for (j in 0..9) {
                // Place cube at (x, z) on y plane
                cube = TransformableNode(arFragment.transformationSystem)
                cube.renderable = cubeRenderable
                cube.parent = anchorNode
                cube.localPosition = Vector3(
                    cubeSize * scale * j + delimiterCubeSize * scale * j,
                    y * scale,
                    cubeSize * scale * i + delimiterCubeSize * scale * i
                )
                cube.isSelectable = false

                cube2 = TransformableNode(arFragment.transformationSystem)
                cube2.renderable = delimiterCubeRenderable
                cube2.parent = anchorNode
                cube2.localPosition = Vector3(
                    cubeSize * scale * (j + 1) + delimiterCubeSize * scale * j,
                    y * scale,
                    cubeSize * scale * (i + 1) + delimiterCubeSize * scale * i
                )
                cube2.isSelectable = false

                // 1. Place delimiter at right of the (x, z) cube
                placeParallelepiped(anchorNode, delimiterCubeRenderable, delimiterCubeSize,
                    delimiterCubeSize.toInt(), (cubeSize / delimiterCubeSize).toInt(), (cubeSize / delimiterCubeSize).toInt(),
                    cubeSize * scale * (j + 1) + delimiterCubeSize * scale * j,
                    y * scale,
                    cubeSize * scale * (i + 1) + delimiterCubeSize * scale * i)
            }
        }
    }

    private fun placeParallelepiped (anchorNode: AnchorNode, cubeRenderable: ModelRenderable?, cubeSize: Float,
                                     widthX: Int, heightY: Int, lengthZ: Int,
                                     x: Float, y: Float, z: Float) {
        var cube : TransformableNode?
        val y : Float = 0.05f * scale // Height above the ground

        for (i in 0..heightY) {
            for (j in 0..lengthZ) {
                for (k in 0..widthX) {
                    cube = TransformableNode(arFragment.transformationSystem)
                    cube.renderable = cubeRenderable
                    cube.parent = anchorNode
                    cube.localPosition = Vector3(
                        cubeSize * scale * k + x,
                        cubeSize * scale * i + y,
                        cubeSize * scale * j + z
                    )
                    cube.isSelectable = false
                }
            }
        }
    }

    fun eraseBattlefield() {

    }
}