package com.example.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.os.Looper
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task

class LocationProvider(
    private val context: Context
) {

    init {

        if (!context.isLocationEnabled()) {
            (context as Activity)
            createLocationRequest(context)
        }

    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private fun locationRequest(): LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000
    ).setMinUpdateIntervalMillis(2000).build()

    //this fun is responsible for asking to the user to activate the gps
    private fun createLocationRequest(activity: Activity) {

        val locationRequest = locationRequest()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            //here the gps is now enabled
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {

                    exception.startResolutionForResult(
                        activity,
                        REQUEST_CHECK_LOCATION_SETTINGS
                    )

                } catch (sendException: IntentSender.SendIntentException) {
                    //Ignore
                }
            }

        }

    }

    private fun locationCallback(): LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {

                    //here is where you get the reference of the locations that will be updated with the interval you setted before.

                }
            }
        }

    //here is your request to the location updates
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest(),
            locationCallback(),
            Looper.getMainLooper()
        )
    }

    companion object {
        const val REQUEST_CHECK_LOCATION_SETTINGS = 1001
    }
}