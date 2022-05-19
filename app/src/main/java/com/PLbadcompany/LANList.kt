package com.PLbadcompany

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class LANList : AppCompatActivity() {
    private val SENSITIVE_PERMISSIONS_CODE = 1
    private var permissions = java.util.ArrayList<String>(
        listOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lanlist)
    }

    override fun onStart() {
        super.onStart()

        requestPermissions()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            SENSITIVE_PERMISSIONS_CODE -> {
                if (!grantResults.all {it == PackageManager.PERMISSION_GRANTED}) {
                    Toast.makeText(this, "Not every permission is granted", Toast.LENGTH_LONG)
                    if (permissions.any { shouldShowRequestPermissionRationale(it) }) {
                        AlertDialog.Builder(this)
                            .setTitle("Permissions")
                            .setMessage("This permissions are neccesary for app working. Do you want to request permissions again?")
                            .setPositiveButton("Yes") { dialog, which ->
                                requestPermissions()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { dialog, which ->
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("Permissions")
                            .setMessage("This app needs permissions to work. You can grant them with yourself from settings. Do you want to open settings?")
                            .setPositiveButton("Yes") { dialog, which ->
                                openSettings()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { dialog, which ->
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show()
                    }
                } else {
                    Toast.makeText(this,"All permissions granted!", Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun requestPermissions() {
        if (!permissions.all {ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED}) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), SENSITIVE_PERMISSIONS_CODE)
        }
    }

    private fun openSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )

        if (packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(this, "Something went wrong. Please, try to grant permissions manually or contact support", Toast.LENGTH_LONG)
        } else {
            startActivity(appSettingsIntent)
        }
    }

}