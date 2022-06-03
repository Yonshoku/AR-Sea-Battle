package com.PLbadcompany.core

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.PLbadcompany.common.DialogHelper
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException

// TODO
// 1. Test installation failures

class ARCoreSessionActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName
    private lateinit var mSession : Session

    private var dialogHelper: DialogHelper = DialogHelper()
    private val seaBattle: SeaBattle = SeaBattle()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        createSession()
    }

    override fun onDestroy() {
        super.onDestroy()

        mSession.close()
    }

    fun createSession() {
        // Create a new ARCore session.
        mSession = Session(this)

        // Create a session config.
        val config = Config(mSession)

        // Do feature-specific operations here, such as enabling depth or turning on
        // support for Augmented Faces.

        // Configure the session.
        mSession.configure(config)
    }

}