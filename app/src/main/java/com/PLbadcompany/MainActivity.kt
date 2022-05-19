package com.PLbadcompany

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.ArrayList


class MainActivity : AppCompatActivity() , View.OnClickListener {

    private var LANBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LANBtn = findViewById(R.id.LANButton)
        LANBtn?.setOnClickListener(this)
    }

    override fun onClick(v : View) {
        var deniedPermissionsList = ArrayList<String>(listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN))

        /* deniedPermissionsList.indices.forEach { i ->
            if (ContextCompat.checkSelfPermission(this, deniedPermissionsList[i]) == PackageManager.PERMISSION_GRANTED) {
                deniedPermissionsList.removeAt(i)
            }
        } */

        when (v.id) {
            R.id.LANButton ->  {
                if (deniedPermissionsList.isNotEmpty()) {
                    startActivity(Intent(this, PermissionsRequest::class.java).putStringArrayListExtra("deniedPermissionsList", deniedPermissionsList))
                } else {
                    startActivity(Intent(this, LANList::class.java))
                }

            }

        }
    }
}

