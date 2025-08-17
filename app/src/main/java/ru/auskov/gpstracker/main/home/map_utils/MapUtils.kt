package ru.auskov.gpstracker.main.home.map_utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.core.graphics.drawable.toBitmap
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.location.LocationService

const val START_TIME = "start_time"

fun initMyLocationOverlay(mapView: MapView): MyLocationNewOverlay {
    val gpsProvider = GpsMyLocationProvider(mapView.context)
    val myLocationNewOverlay = MyLocationNewOverlay(gpsProvider, mapView)
    val personMarker = mapView.context.getDrawable(R.drawable.ic_my_location)?.toBitmap()
    val directionMarker = mapView.context.getDrawable(R.drawable.ic_my_location)?.toBitmap()
    myLocationNewOverlay.setPersonIcon(personMarker)
    myLocationNewOverlay.setDirectionIcon(directionMarker)
    myLocationNewOverlay.enableMyLocation()
    myLocationNewOverlay.enableFollowLocation()
    return myLocationNewOverlay
}

fun startLocationService(context: Context, startTime: Long) {
    val intent = Intent(context, LocationService::class.java).apply {
        putExtra(START_TIME, startTime)
    }
    context.startForegroundService(intent)
}

fun stopLocationService(context: Context) {
    val intent = Intent(context, LocationService::class.java)
    context.stopService(intent)
}

fun isLocationServiceRunning(context: Context): Boolean {
    val acManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    for (service in acManager.getRunningServices(Int.MAX_VALUE)) {
        return LocationService::class.java.name == service.service.className
    }

    return false
}