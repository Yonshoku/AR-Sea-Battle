package com.PLbadcompany.core

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.PLbadcompany.R
import com.google.ar.core.Config
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.gorisse.thomas.sceneform.light.LightEstimationConfig
import com.gorisse.thomas.sceneform.lightEstimationConfig

// Todo
// 1. Clear anchors
// 2. Log

class ArActivity : AppCompatActivity() {
    private var TAG = this::class.java.simpleName

    private lateinit var arFragment : ArFragment
    private lateinit var placementHelper : ArSceneBattlefieldPlacementHelper

    private lateinit var seaBattle: SeaBattle

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "Single player activity has created")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.ar_activity)

        arFragment = supportFragmentManager.findFragmentById(R.id.single_player_ar_fragment) as ArFragment
        placementHelper = ArSceneBattlefieldPlacementHelper(arFragment, this)
        configArFragment()

        seaBattle = SeaBattle(SeaBattle.SINGLE_PLAYER_MODE)

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.parent = arFragment.arSceneView.scene

                placementHelper.placeBattlefield(anchorNode, seaBattle.player1!!.ownField, seaBattle.player1!!.enemyField)

                arFragment.arSceneView.scene.addChild(anchorNode)
            }


    }

    private fun configArFragment() {
        arFragment.apply {
            setOnSessionConfigurationListener { session, config ->
                // Depth
                if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    config.depthMode = Config.DepthMode.AUTOMATIC
                }

                // Lights
                arSceneView.lightEstimationConfig = LightEstimationConfig.DISABLED
            }
            setOnViewCreatedListener { arSceneView ->
                // Available modes: DEPTH_OCCLUSION_DISABLED, DEPTH_OCCLUSION_ENABLED
                arSceneView.cameraStream.depthOcclusionMode =
                    CameraStream.DepthOcclusionMode.DEPTH_OCCLUSION_ENABLED
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