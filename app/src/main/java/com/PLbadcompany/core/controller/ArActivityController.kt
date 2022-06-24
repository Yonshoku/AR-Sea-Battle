package com.PLbadcompany.core.controller

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.PLbadcompany.R
import com.PLbadcompany.core.model.CellState
import com.PLbadcompany.core.model.Player
import com.PLbadcompany.core.model.PlayerHuman
import com.PLbadcompany.core.model.SeaBattle
import com.PLbadcompany.core.view.ArSceneBattlefieldPlacementHelper
import com.google.ar.core.Config
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.gorisse.thomas.sceneform.light.LightEstimationConfig
import com.gorisse.thomas.sceneform.lightEstimationConfig

// Todo
// 1. Create scrolls and make them work
// 2. Create shoot button and make it work

class ArActivityController : AppCompatActivity() {
    private var TAG = this::class.java.simpleName

    private var wasArPlaneTapped: Boolean = false
    private lateinit var arFragment : ArFragment
    private lateinit var placementHelper : ArSceneBattlefieldPlacementHelper
    private lateinit var seaBattle: SeaBattle
    private lateinit var player: Player

    private var horizontalSeekBarValue = 5
    private var verticalSeekBarValue = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "Single player activity has created")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.ar_activity)

        arFragment = supportFragmentManager.findFragmentById(R.id.single_player_ar_fragment) as ArFragment
        player = PlayerHuman()
        seaBattle = SeaBattle(SeaBattle.SINGLE_PLAYER_MODE, player)
        placementHelper = ArSceneBattlefieldPlacementHelper(arFragment, player, this)

        configArFragment()
        initControl()

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            // Prevent multiple battlefields placement
            if (wasArPlaneTapped)
                return@setOnTapArPlaneListener

            wasArPlaneTapped = true

            // Get anchor from a tapped plane
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.parent = arFragment.arSceneView.scene

            // Place battlefield
            placementHelper.anchorNode = anchorNode
            placementHelper.placeBattlefield()
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

    private fun initControl() {
        val horizontal = findViewById<SeekBar>(R.id.horizontalSeekbar)
        val horizontalValue = findViewById<TextView>(R.id.horizontalSeekbarValue)

        val vertical = findViewById<SeekBar>(R.id.verticalSeekbar)
        val verticalValue = findViewById<TextView>(R.id.verticalSeekbarValue)

        val shootButton = findViewById<Button>(R.id.shoot_button)

        var alphabet = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")

        horizontal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                horizontalValue.text = alphabet[progress - 1]
                horizontalSeekBarValue = progress
                placementHelper.setFieldActive(false, progress - 1, verticalSeekBarValue - 1)
            }

            override fun onStartTrackingTouch(seekBar1: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar1: SeekBar?) {}
        })


        vertical.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                verticalValue.text = progress.toString()
                verticalSeekBarValue = progress
                placementHelper.setFieldActive(false, horizontalSeekBarValue - 1, progress - 1)
            }

            override fun onStartTrackingTouch(seekBar2: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar2: SeekBar?) {}
        })

        shootButton.setOnClickListener {
            Log.v(TAG, "FIRE")
            seaBattle.handleShot(this, horizontalSeekBarValue - 1, verticalSeekBarValue - 1)
        }
    }

    fun updateCellState(isOwn: Boolean, x: Int, y: Int, state: CellState) {
        placementHelper.updateCellState(isOwn, x, y, state)
    }

    fun setFieldActive(isOwn: Boolean, x: Int, y: Int) {
        placementHelper.setFieldActive(isOwn, x, y)
    }

}