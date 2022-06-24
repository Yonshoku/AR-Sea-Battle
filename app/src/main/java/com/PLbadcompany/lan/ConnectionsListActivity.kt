package com.PLbadcompany.lan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.PLbadcompany.core.menu.MainMenuActivity
import com.PLbadcompany.R
import com.PLbadcompany.common.DialogHelper
import com.PLbadcompany.common.PermissionHelper

// Todo
// 1. Add connection in onResolved
// 2. Test nsd for working on 2 devices

class ConnectionsListActivity : AppCompatActivity() {
    private val TAG : String = this::class.java.simpleName

    private val nsdHelper : NsdHelper = NsdHelper(this)
    val socketHelper : SocketHelper = SocketHelper()
    private val permissionHelper : PermissionHelper = PermissionHelper()
    private val dialogHelper : DialogHelper = DialogHelper()

    private val requestCode = 1
    private var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    private val ASK_USER_REQUEST_PERMISSION_AGAIN_MESSAGE = "This permissions are necessary for LAN working. Do you want to request permissions again?"
    private val OPEN_SETTINGS_MESSAGE = "This app needs permissions to work. You can grant them with yourself from settings. Do you want to open settings?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lan_connections_list_activity)

        if (!permissionHelper.hasPermissions(this, permissions))
            permissionHelper.requestPermissions(this, permissions, requestCode)
    }

    override fun onDestroy() {
        nsdHelper.tearDown()
        socketHelper.closeSocket()

        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()

        socketHelper.initializeSocket()
        nsdHelper.registerService(socketHelper.localPort)
    }

    override fun onStop() {
        nsdHelper.tearDown()
        socketHelper.closeSocket()

        super.onStop()
    }

    override fun onResume() {
        nsdHelper.registerService(socketHelper.localPort)

        super.onResume()
    }

    override fun onPause() {
        nsdHelper.tearDown()

        super.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (this.requestCode == requestCode) {
            if (!grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Not all or none of permissions granted
                if (permissions.any { shouldShowRequestPermissionRationale(it) }) {
                    // Some or all permissions can be requested again
                    // Show dialog to ask if app may request them again

                    dialogHelper.showDialog(this,
                        "Permission",
                        ASK_USER_REQUEST_PERMISSION_AGAIN_MESSAGE,
                        "Request permissions again",
                        "Back",
                        { permissionHelper.openSettings(this) },
                        { startActivity(Intent(this, MainMenuActivity::class.java))}
                    )
                } else {
                    // All permissions denied forever
                    // Ask user if it wants to go to the settings and open them if it wants
                    dialogHelper.showDialog(this,
                        "Permission",
                        OPEN_SETTINGS_MESSAGE,
                        "Open settings",
                        "Back",
                        { permissionHelper.openSettings(this) },
                        { startActivity(Intent(this, MainMenuActivity::class.java)) }
                    )
                }
            }
        }
    }

    // Test code
    fun add(str : String) {
        val lanWrapper : LinearLayout = findViewById(R.id.lanWrapper)
        val line = LinearLayout(this)
        val text = TextView(this)
        text.setText(str)
        text.isVisible = true
        text.setTextColor(Color.WHITE)
        line.isVisible = true
        line.addView(text)
        lanWrapper.addView(line)
    }
}