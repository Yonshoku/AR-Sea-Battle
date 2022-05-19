package com.PLbadcompany

import android.Manifest
import android.Manifest.permission_group.SMS
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

class PermissionsRequest : AppCompatActivity() {
    private var deniedPermissionsList = ArrayList<String>();
    var REQUEST_RESULT = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions_request)
    }

    override fun onStart() {
        super.onStart()
        deniedPermissionsList = intent.getStringArrayListExtra("deniedPermissionsList") as ArrayList<String>

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CONTACTS), REQUEST_RESULT)
    }

    override fun onResume() {
        super.onResume()
        deniedPermissionsList = intent.getStringArrayListExtra("deniedPermissionsList") as ArrayList<String>

        ActivityCompat.requestPermissions(this, arrayOf( Manifest.permission.SEND_SMS), REQUEST_RESULT)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Toast.makeText(this, "XYI", Toast.LENGTH_SHORT).show();

        when (requestCode) {
            REQUEST_RESULT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    startActivity(Intent(this, LANList::class.java))
                }
            }
        }
    }

}