package com.example.location

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices

class LocationService : Service() {

    private lateinit var locationProvider: LocationProvider

    override fun onCreate() {
        super.onCreate()
        locationProvider = LocationProvider(applicationContext)
        locationProvider.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
    }
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    //here we start the foreground service
    private fun start(){

        //creating the notification
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Location Service")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)

        //checking if the android version is 10 or +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification.build(), FOREGROUND_SERVICE_TYPE_LOCATION)
        }

        //getting location updates
        locationProvider.startLocationUpdates()
    }

    //here we stop the foreground service
    private fun stop(){
        stopForeground(true)
        stopSelf()
    }

    companion object{
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}