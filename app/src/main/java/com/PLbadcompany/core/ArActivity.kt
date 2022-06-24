package com.PLbadcompany.core

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.PLbadcompany.R
import com.google.ar.core.Config
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.gorisse.thomas.sceneform.light.LightEstimationConfig
import com.gorisse.thomas.sceneform.lightEstimationConfig

// Todo
// 1. Create scrolls and make them work
// 2. Create shoot button and make it work

class ArActivity : AppCompatActivity() {
    private var TAG = this::class.java.simpleName

    private var wasArPlaneTapped: Boolean = false
    private lateinit var arFragment : ArFragment
    private lateinit var placementHelper : ArSceneBattlefieldPlacementHelper

    private lateinit var seaBattle: SeaBattle

    private var horizontalSeekBarValue = 5
    private var verticalSeekBarValue = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "Single player activity has created")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.ar_activity)

        initBattlefieldControlScrolls()

        arFragment = supportFragmentManager.findFragmentById(R.id.single_player_ar_fragment) as ArFragment
        placementHelper = ArSceneBattlefieldPlacementHelper(arFragment, this)
        configArFragment()

        seaBattle = SeaBattle(SeaBattle.SINGLE_PLAYER_MODE)

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (wasArPlaneTapped)
                return@setOnTapArPlaneListener

            wasArPlaneTapped = true

            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.parent = arFragment.arSceneView.scene

            placementHelper.anchorNode = anchorNode
            placementHelper.placeBattlefield(seaBattle.player1!!.ownField, seaBattle.player1!!.enemyField)
            placementHelper.setFieldActive(false, 4, 4)

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


    private fun initBattlefieldControlScrolls() {
        val horizontal = findViewById<SeekBar>(R.id.horizontalSeekbar)
        val horizontalValue = findViewById<TextView>(R.id.horizontalSeekbarValue)

        val vertical = findViewById<SeekBar>(R.id.verticalSeekbar)
        val verticalValue = findViewById<TextView>(R.id.verticalSeekbarValue)

        var alphabet = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")

        horizontal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                horizontalValue.text = alphabet[progress - 1]
                horizontalSeekBarValue = progress
                placementHelper.setFieldActive(false, verticalSeekBarValue - 1, progress - 1)
            }

            override fun onStartTrackingTouch(seekBar1: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar1: SeekBar?) {}
        })


        vertical.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                verticalValue.text = progress.toString()
                verticalSeekBarValue = progress
                placementHelper.setFieldActive(false, progress - 1, horizontalSeekBarValue - 1)
            }

            override fun onStartTrackingTouch(seekBar2: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar2: SeekBar?) {}
        })
    }


}