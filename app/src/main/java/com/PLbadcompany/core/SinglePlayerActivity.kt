package com.PLbadcompany.core

import android.graphics.Color.RED
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.PLbadcompany.R
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.gorisse.thomas.sceneform.light.LightEstimationConfig
import com.gorisse.thomas.sceneform.lightEstimationConfig

// Todo
// 1. Clear anchors

class SinglePlayerActivity : AppCompatActivity() {
    private var TAG = this::class.java.simpleName

    private lateinit var arFragment : ArFragment

    private var battlefieldCube: ModelRenderable? = null
    private var battlefieldCubeSize : Float = 0.1f
    private var battlefieldDelimiterSize : Float = 0.02f
    private var battlefieldMaterial: Material? = null
    private val battlefieldMaterialColor : Int = android.graphics.Color.parseColor("#4D7EF2") // Color(133f, 209f, 232f, 1f)
    private var battlefieldCubes : Array<Array<TransformableNode?>> = Array(10) {Array(10) {null} }
    private var battlefieldScale : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "Single player activity has created")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_player_activity)

        arFragment = supportFragmentManager.findFragmentById(R.id.single_player_ar_fragment) as ArFragment

        initBattlefieldCubeAndMaterial(battlefieldCubeSize)

        arFragment
            .setOnTapArPlaneListener { hitResult, plane, motionEvent ->
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.parent = arFragment.arSceneView.scene
                setBattlefield(anchorNode, battlefieldScale)
                arFragment.arSceneView.lightEstimationConfig = LightEstimationConfig.DISABLED

                arFragment.arSceneView.scene.addChild(anchorNode)
            }
    }

    private fun initBattlefieldCubeAndMaterial(size : Float) {
        MaterialFactory.makeOpaqueWithColor(this, Color(battlefieldMaterialColor))
            .thenAccept { material ->
                val vector3 = Vector3(size, size, size)
                battlefieldCube = ShapeFactory.makeCube(vector3, Vector3.zero(), material)
                battlefieldCube!!.isShadowCaster = false
                battlefieldCube!!.isShadowReceiver = false

                battlefieldMaterial = material
            }
    }

    private fun setBattlefield(anchorNode: AnchorNode, scale : Int) {
        var node : TransformableNode?
        val y : Float = 0.05f * scale // Height above the ground

        for (i in 0..9) {
            for (j in 0..9) {
                node = TransformableNode(arFragment.transformationSystem)
                node.renderable = battlefieldCube
                node.parent = anchorNode
                node.localPosition = Vector3(
                    battlefieldCubeSize * scale * j + battlefieldDelimiterSize * scale * j,
                    y * scale,
                    battlefieldCubeSize * scale * i + battlefieldDelimiterSize * scale * i
                )
                node.isSelectable = false
            }
        }
    }

    /* private fun initBattlefieldControlScrolls() {
        val seekBar1 = findViewById<SeekBar>(R.id.seekBar1)
        val textView1 = findViewById<TextView>(R.id.seekBarValue1)
        seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textView1.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar1: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar1: SeekBar?) {}
        })

        val seekBar2 = findViewById<SeekBar>(R.id.seekBar2)
        val textView2 = findViewById<TextView>(R.id.seekBarValue2)
        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textView2.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar2: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar2: SeekBar?) {}
        })
    } */


}