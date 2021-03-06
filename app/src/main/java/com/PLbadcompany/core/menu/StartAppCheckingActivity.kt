package com.PLbadcompany.core.menu

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.PLbadcompany.R
import com.PLbadcompany.common.DialogHelper
import com.PLbadcompany.common.PermissionHelper
import com.google.ar.core.ArCoreApk
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException


// TODO
// 1. Make app landscape

class StartAppCheckingActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName

    private var mUserRequestedInstall = true
    private val requestCode = 1
    private val permissions = arrayOf(
        Manifest.permission.CAMERA
    )

    private val DEVICE_DOESNT_SUPPORT_AR_MESSAGE = "Sorry, it seems that your device doesn't support Augmented reality (AR)."
    private val ASK_USER_REQUEST_PERMISSION_AGAIN_MESSAGE = "This permissions are necessary for app working. Do you want to request permissions again?"
    private val OPEN_SETTINGS_MESSAGE = "This app needs permissions to work. You can grant them with yourself from settings. Do you want to open settings?"
    private val USER_DENIED_INSTALLATION_MESSAGE = "Sorry, this app needs updated Google Services to work."
    private val ARCORE_INSTALLATION_FAILED_MESSAGE = "Sorry, Google Service installation failed."

    private var dialogHelper : DialogHelper =  DialogHelper()
    private var permissionHelper : PermissionHelper = PermissionHelper()
    val MIN_OPENGL_VERSION = 3.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_app_activity)

        // Call checkArAvailability to get it's return from cache in the future
        // It will request permissions if device supports AR
        // checkArAvailability()
    }

    override fun onStart() {
        super.onStart()

        checkArAvailability()

        if (checkIsOpenGLSupported())
            startActivity(Intent(this, MainMenuActivity::class.java))
    }

    override fun onResume() {
        super.onResume()

        // Request installation or updating of google services if it's needed
       try {
            when (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                    mUserRequestedInstall = false
                    Log.v(TAG, "Google Play service installation or updating were requested")
                    return
                } ArCoreApk.InstallStatus.INSTALLED -> {
                    return
                }
            }
        } catch (e: UnavailableUserDeclinedInstallationException) {
            Log.v(TAG, "User denied to update google services")

            dialogHelper.showOkDialog(this,
                USER_DENIED_INSTALLATION_MESSAGE,
                { android.os.Process.killProcess(android.os.Process.myPid()) }
            )
        } catch (e: Exception) {
            Log.e(TAG, "ARCore not installed", e)

            dialogHelper.showOkDialog(this,
                ARCORE_INSTALLATION_FAILED_MESSAGE,
                { android.os.Process.killProcess(android.os.Process.myPid()) }
            )
        }
    }

    // ARCore and google services availability
   private fun checkArAvailability() {
        val availability = ArCoreApk.getInstance().checkAvailability(this)

        if (availability.isTransient) {
            // Continue to query availability at 5Hz while compatibility is checked in the background.
            Handler(Looper.getMainLooper()).postDelayed({
                checkArAvailability()
            }, 200)
        }

        // Close app if arcore isn't supported
        if (!availability.isSupported) {
            dialogHelper.showOkDialog(this,
                DEVICE_DOESNT_SUPPORT_AR_MESSAGE,
                {android.os.Process.killProcess(android.os.Process.myPid())})
        }
    }

    fun checkIsOpenGLSupported(): Boolean {
        val openGlVersionString =
            (this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion

        if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later")
            return false
        }

        return true
    }

}