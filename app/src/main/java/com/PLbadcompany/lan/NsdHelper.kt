package com.PLbadcompany.lan

import android.content.Context
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.PLbadcompany.common.DialogHelper
import com.PLbadcompany.core.menu.MainMenuActivity
import java.net.InetAddress

class NsdHelper (val connectionsListActivity: ConnectionsListActivity) {
    private val TAG = this::class.java.simpleName
    private var dialogHelper: DialogHelper = DialogHelper()

    private val SERVICE_REGISTRATION_FAILED_MESSAGE = "Sorry, network discovery service couldn't be registered. Please, contact technical support"

    private lateinit var nsdServiceInfo: NsdServiceInfo
    private lateinit var nsdManager: NsdManager
    private lateinit var resolvedNsdServiceInfo: NsdServiceInfo
    private var resolvedPort : Int = 0
    private lateinit var resolvedHost: InetAddress

    // Nsd listeners
    private var mDiscoveryListener: NsdManager.DiscoveryListener? = null
    private var mRegistrationListener : NsdManager.RegistrationListener? = null
    private var mResolveListener: NsdManager.ResolveListener? = null

    // Register service to everyone could find us
    // Every device create service so everyone can find each others

    fun registerService(port: Int) {
        // Exit if service already exist
        if (mRegistrationListener != null)
            return

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

                dialogHelper.showOkDialog(connectionsListActivity,
                    SERVICE_REGISTRATION_FAILED_MESSAGE
                ) {
                    ActivityCompat.startActivity(
                        connectionsListActivity,
                        Intent(connectionsListActivity, MainMenuActivity::class.java),
                        (connectionsListActivity as ActivityOptionsCompat).toBundle()
                    )
                }

            }

            override fun onServiceUnregistered(arg0: NsdServiceInfo) {
                Log.i(TAG, "Service ${nsdServiceInfo.serviceName} unregistered successfully")
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.println(Log.ERROR, TAG, "Unregistration service has failed")
            }
        }

        // Create NsdServiceInfo
        nsdServiceInfo = NsdServiceInfo().apply {
            serviceName = "AR_SeaBattle"
            serviceType = "_arseabattle._tcp"
            setPort(port)
        }

        // Create nsdManager and register service
        nsdManager = connectionsListActivity.getSystemService(Context.NSD_SERVICE) as NsdManager
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener)
    }

    // Copied from https://github.com/aosp-mirror/platform_development/blob/73f1ba806fe5fa0dad2334b168deefaf6d8ca5c3/samples/training/NsdChat/src/com/example/android/nsdchat/NsdHelper.java
    // It works and no one doesn't know how
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
    fun discoverServices() {
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
                        connectionsListActivity.add(service.serviceName)
                        // Try to connect
                        Log.i(TAG, " Service discovered $service")
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

    // Copied from https://github.com/aosp-mirror/platform_development/blob/73f1ba806fe5fa0dad2334b168deefaf6d8ca5c3/samples/training/NsdChat/src/com/example/android/nsdchat/NsdHelper.java
    // It works and no one don't know how
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
    fun resolveService(service : NsdServiceInfo) {
        Log.v(TAG, "resolve started")
        // Create resolve listener
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

                connectionsListActivity.socketHelper.acceptSocket()
            }

        }

        // Start resolving
        nsdManager.resolveService(service, mResolveListener)
    }

    // Stop service
    fun tearDown() {
        stopDiscovery()
        unregisterService()
    }
}