package ru.auskov.gpstracker.location

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import ru.auskov.gpstracker.MainActivity
import ru.auskov.gpstracker.R

class LocationService : Service() {
    private val updateLocationTime = 3000L
    private val updateLocationPriority = Priority.PRIORITY_HIGH_ACCURACY
    private lateinit var locationProviderClient: FusedLocationProviderClient

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            Log.d("MyLog", "Location: ${location?.latitude}, ${location?.longitude}")
            super.onLocationResult(locationResult)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d("MyLog", "Service onCreate")
        initLocationProviderClient()
        super.onCreate()
    }

    override fun onDestroy() {
        Log.d("MyLog", "Service onDestroy")
        locationProviderClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyLog", "Service onStartCommand ${intent?.getStringExtra("test")}")
        showNotification()
        startLocationUpdates()
        return START_STICKY
    }

    private fun initLocationProviderClient() {
        locationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            updateLocationPriority, updateLocationTime,
        ).build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
            )
        }
    }

    private fun showNotification() {
        val channel = NotificationChannel(
            "channel_1",
            "Location Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = Notification.Builder(this, "channel_1")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Gps Tracker Running...")
            .setContentIntent(contentIntent)
            .build()

        startForeground(666, notification)
    }
}