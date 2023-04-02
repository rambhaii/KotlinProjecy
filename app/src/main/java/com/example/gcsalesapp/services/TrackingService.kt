package com.example.gcsalesapp.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.location.LocationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.gcsalesapp.R
import com.example.gcsalesapp.data.Constants.Companion.FASTEST_LOCATION_INTERVAL
import com.example.gcsalesapp.data.Constants.Companion.LOCATION_UPDATE_INTERVAL
import com.example.gcsalesapp.data.Constants.Companion.NOTIFICATION_CHANNEL_ID
import com.example.gcsalesapp.data.Constants.Companion.NOTIFICATION_CHANNEL_NAME
import com.example.gcsalesapp.data.Constants.Companion.NOTIFICATION_ID
import com.example.gcsalesapp.data.Constants.Companion.SMALLEST_DISPLACEMENT
import com.example.gcsalesapp.repository.PrefrenceRepository
import com.example.gcsalesapp.repository.Utill
import com.example.gcsalesapp.ui.view.HomeActivity
import com.example.gcsalesapp.ui.viewmodal.HomeViewModel
import com.example.gcsalesapp.ui.viewmodal.TrackingViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TrackingService : LifecycleService() {


    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder



    companion object {
        val pathPoints = MutableLiveData<LatLng>()
    }



    override fun onCreate() {
        super.onCreate()
        updateLocationTracking()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Utill.print("on start interval ==${TrackingViewModel.interval}")
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
        Utill.print("on stop interval ==${TrackingViewModel.interval}")
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        Utill.print("on onDestroy interval ==${TrackingViewModel.interval}")
    }


    @SuppressLint("MissingPermission")
    private fun updateLocationTracking() {

        if (Utill.hasLocationPermissions(this)) {
            val request = LocationRequest().apply {
                interval = TrackingViewModel.interval
                fastestInterval = TrackingViewModel.interval
                priority = PRIORITY_HIGH_ACCURACY
                smallestDisplacement = SMALLEST_DISPLACEMENT
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            result?.locations?.let { locations ->
                pathPoints.value = LatLng(locations.last().latitude, locations.last().longitude)

                Utill.print("on onLocationResult interval ==${TrackingViewModel.interval}")
            }
        }
    }


    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        pathPoints.observe(this, { latlong ->
            val notification =
                notificationBuilder.setContentText("${latlong.latitude},${latlong.longitude}")
            notificationManager.notify(NOTIFICATION_ID, notification.build())
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }


}