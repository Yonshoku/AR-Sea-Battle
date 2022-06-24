package com.PLbadcompany.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

class PermissionHelper {
    private val TAG = this::class.java.simpleName

    fun hasPermissions(context: Context, permissions: Array<String>) : Boolean {
        return permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED}
    }

    fun requestPermissions(context: Context, permissions: Array<String>, requestCode: Int = 0) {
        ActivityCompat.requestPermissions(context as Activity, permissions, requestCode)
    }

    fun openSettings(context: Context) {
        startActivity(context,
            Intent(Settings.ACTION_SETTINGS),
            (context as ActivityOptionsCompat).toBundle())
    }

}