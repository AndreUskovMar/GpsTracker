package ru.auskov.gpstracker.location

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import ru.auskov.gpstracker.MainActivity
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.location.data.LocationData
import ru.auskov.gpstracker.main.home.map_utils.START_TIME
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {
    @Inject
    lateinit var locationDataSharer: LocationDataSharer

    private val updateLocationTime = 3000L
    private val updateLocationPriority = Priority.PRIORITY_HIGH_ACCURACY
    private lateinit var locationProviderClient: FusedLocationProviderClient

    private var distance = 0f

    private var lastLocation: Location? = null
    private var startTime = 0L

    private val geoPoints = mutableListOf<GeoPoint>()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation

            if (location != null) {
                Log.d("MyLog", "Location: ${location.latitude}, ${location.longitude}")
                val geoPoint = GeoPoint(
                    location.latitude,
                    location.longitude,
                    location.altitude
                )

                geoPoints.add(geoPoint)

                val newDistance = lastLocation?.distanceTo(location) ?: 0f

                if (newDistance < 5f) {
                    distance += newDistance
                }

                val locationData = LocationData(
                    speed = location.speed,
                    distance = distance,
                    startServiceTime = startTime,
                    geoPoints
                )

                CoroutineScope(Dispatchers.IO).launch {
                    locationDataSharer.updateLocation(locationData)
                }

                lastLocation = location
            }
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
        startTime = intent?.getLongExtra(START_TIME, -1L) ?: -1L
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