package com.PLbadcompany.lan

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.PLbadcompany.MainActivity
import com.PLbadcompany.R
import java.net.InetAddress
import java.net.ServerSocket

class LANList : AppCompatActivity() {
    private val TAG : String = "LAN_Activity"
    private val SENSITIVE_PERMISSIONS_CODE = 1
    private var permissions = java.util.ArrayList<String>(
        listOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lanlist)
    }

    override fun onStart() {
        super.onStart()

        requestPermissions()
        initializeServerSocket()
        registerService(localPort)
    }


    override fun onResume() {
        Log.v(TAG, "onResume called")

        super.onResume()
    }

    override fun onPause() {
        Log.v(TAG, "onPause called")
        tearDownService()

        super.onPause()
    }

    override fun onStop() {
        tearDown()

        super.onStop()
    }

    override fun onDestroy() {
        tearDown()
        serverSocket.close()

        super.onDestroy()
    }

    // Request permissions code

    private fun requestPermissions() {
        // Request permissions if any of them denied
        if (!permissions.all {ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED}) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), SENSITIVE_PERMISSIONS_CODE)
        } else {
            Log.v(TAG, "all permissions are granted")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            // Permissions for LAN working
            SENSITIVE_PERMISSIONS_CODE -> {
                if (!grantResults.all {it == PackageManager.PERMISSION_GRANTED}) {
                    // Not all or none of permissions granted
                    if (permissions.any { shouldShowRequestPermissionRationale(it) }) {
                        // Some or all permissions can be requested again
                        // Show dialog to ask if app may request them again
                        AlertDialog.Builder(this)
                            .setTitle("Permissions")
                            .setMessage("This permissions are necessary for app working. Do you want to request permissions again?")
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
                        // All permissions denied forever
                        // Ask user if it wants to go to the settings and open them if it wants
                        AlertDialog.Builder(this)
                            .setTitle("Permissions")
                            .setMessage("This app needs permissions to work. You can grant them with yourself from settings. Do you want to open settings?")
                            .setPositiveButton("Yes") { dialog, which ->
                                openSettings()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { dialog, which ->
                                Log.v(TAG, "User denied all permissions and denied to go to settings")
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show()
                    }
                } else {
                    Log.v(TAG, "All permissions granted")
                }
            }
        }
    }

    private fun openSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )

        if (packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) !== null)
            startActivity(appSettingsIntent)

    }

    // network service discovery (NSD) code

    // Socket
    private lateinit var serverSocket : ServerSocket
    private var localPort : Int = -1

    // Nsd info of this app
    private lateinit var nsdServiceInfo: NsdServiceInfo
    private lateinit var nsdManager: NsdManager

    // Nsd info of resolved app
    private lateinit var resolvedNsdServiceInfo: NsdServiceInfo
    private var resolvedPort : Int = -1
    private lateinit var resolvedHost: InetAddress

    // Nsd listeners
    private var mDiscoveryListener: NsdManager.DiscoveryListener? = null
    private var mRegistrationListener : NsdManager.RegistrationListener? = null
    private var mResolveListener: NsdManager.ResolveListener? = null

    // Init socket and get free port
    private fun initializeServerSocket() {
        // Random unused port will be selected automatically
        serverSocket = ServerSocket(0).also { socket ->
            localPort = socket.localPort
        }

        Log.v(TAG, "Socket has been initialized with port: $localPort")
    }

    // Register service to everyone could find us
    // Every device create service so everyone can find each others

    private fun registerService(port: Int) {
        // Create NsdServiceInfo
        nsdServiceInfo = NsdServiceInfo().apply {
            serviceName = "AR_SeaBattle"
            serviceType = "_arseabattle._tcp"
            setPort(port)
        }

        // Create registration listener
        // It's saved for further unregistration use
        mRegistrationListener = object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
                nsdServiceInfo.serviceName = NsdServiceInfo.serviceName
                Log.i(TAG,"Service ${NsdServiceInfo.serviceName} registered successfully")
                // Discover services when our service was registered
                discoverServices()
            }


            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.println(Log.ERROR, TAG, "Registration service has failed")
                AlertDialog.Builder(this@LANList)
                    .setTitle("Error")
                    .setMessage("Sorry, network discovery service couldn't be registered. Please, contact technical support")
                    .setPositiveButton("Yes") { dialog, which ->
                        startActivity(Intent(this@LANList, MainActivity::class.java))
                        dialog.dismiss()
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }

            override fun onServiceUnregistered(arg0: NsdServiceInfo) {
                Log.i(TAG, "Service ${nsdServiceInfo.serviceName} unregistered successfully")
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.println(Log.ERROR, TAG, "Unregistration service has failed")
            }
        }

        nsdManager = (getSystemService(Context.NSD_SERVICE) as NsdManager)
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener)
    }

    fun unregisterService() {
        if (mRegistrationListener != null) {
            try {
                nsdManager.unregisterService(mRegistrationListener)
            } finally {
            }
            mRegistrationListener = null
        }
    }


    // Discovery other services
    private fun discoverServices() {
        // Create discoveryListener
        mDiscoveryListener = object : NsdManager.DiscoveryListener {

            override fun onDiscoveryStarted(regType: String) {
                Log.i(TAG, "Service ${nsdServiceInfo.serviceName} started discovery successfully")
            }

            override fun onServiceFound(service: NsdServiceInfo) {
                Log.i(TAG, "Service discovered $service")
                when {
                    service.serviceType != nsdServiceInfo.serviceType -> // Different service type
                        Log.d(TAG, "Unknown Service Type: ${service.serviceType}")
                    service.serviceName == nsdServiceInfo.serviceName -> // Service discovered itself
                        Log.d(TAG, "Same machine: ${nsdServiceInfo.serviceName}")
                    service.serviceName.contains("NsdChat") -> { // Service discovered other one
                        add(service.serviceName)
                        // Try to connect
                        resolveService(service)
                    }
                }
            }

            override fun onServiceLost(service: NsdServiceInfo) {
                Log.v(TAG, "service lost: $service")
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.i(TAG, "Discovery stopped: $serviceType")
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.println(Log.ERROR, TAG, "Discovery failed: Error code:$errorCode")
                nsdManager.stopServiceDiscovery(mDiscoveryListener)
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e(TAG, "Discovery failed: Error code:$errorCode")
                nsdManager.stopServiceDiscovery(mDiscoveryListener)
            }
        }

        // Start services discovering
        nsdManager.discoverServices(nsdServiceInfo.serviceType, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener)
    }

    fun stopDiscovery() {
        if (mDiscoveryListener != null) {
            try {
                nsdManager.stopServiceDiscovery(mDiscoveryListener)
            } finally {
            }
            mDiscoveryListener = null
        }
    }


    // Connect to discovered service
    private fun resolveService(service : NsdServiceInfo) {
        nsdManager.resolveService(service, mResolveListener)

        mResolveListener = object : NsdManager.ResolveListener {

            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                // Called when the resolve fails. Use the error code to debug.
                Log.e(TAG, "Resolve failed: $errorCode")
            }

            override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                Log.e(TAG, "Resolve Succeeded. $serviceInfo")

                if (serviceInfo.serviceName == nsdServiceInfo.serviceName) {
                    Log.d(TAG, "Same IP.")
                    return
                }
                resolvedNsdServiceInfo = serviceInfo
                resolvedPort = serviceInfo.port
                resolvedHost = serviceInfo.host
            }
        }
    }

    // Closing or pausing LANList
    private fun tearDownService() {
        stopDiscovery()
        unregisterService()
    }


    private fun tearDown() {
        tearDownService()
        serverSocket.close()
    }

    // Test code
    private fun add(str : String) {
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