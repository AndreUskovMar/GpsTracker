package ru.auskov.gpstracker.main.home.map_utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.location.Priority
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.location.LocationService

const val START_TIME = "start_time"
const val UPDATE_TIME = "update_time"
const val PRIORITY = "priority"

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

fun startLocationService(context: Context, startTime: Long, updateTime: Long, priority: String) {
    val intent = Intent(context, LocationService::class.java).apply {
        putExtra(START_TIME, startTime)
        putExtra(UPDATE_TIME, updateTime)
        putExtra(PRIORITY, getPriority(priority))
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

@SuppressLint("DefaultLocale")
fun getAverageSpeed(distance: Float, startTime: Long): String {
    return String.format(
        "%.1f",
        3.6f * distance / ((System.currentTimeMillis() - startTime) / 1000f)
    )
}

fun getPriority(priority: String): Int {
    return when(priority) {
        "PRIORITY_HIGH_ACCURACY" -> Priority.PRIORITY_HIGH_ACCURACY
        "PRIORITY_PASSIVE" -> Priority.PRIORITY_PASSIVE
        "PRIORITY_LOW_POWER" -> Priority.PRIORITY_LOW_POWER
        "PRIORITY_BALANCED_POWER_ACCURACY" -> Priority.PRIORITY_BALANCED_POWER_ACCURACY
        else -> Priority.PRIORITY_HIGH_ACCURACY
    }
}

fun geoPointsToString(geoPoints: List<GeoPoint>?): String {
    if (geoPoints == null) return ""
    val stringBuilder = StringBuilder()
    geoPoints.forEach { geoPoint ->
        if (stringBuilder.isEmpty()) {
            stringBuilder.append("${geoPoint.latitude},${geoPoint.longitude}")
        } else {
            stringBuilder.append("/${geoPoint.latitude},${geoPoint.longitude}")
        }
    }
    return stringBuilder.toString()
}

fun getPolylinesFromString(color: ULong, lineWidth: Float, geoPointsString: String): Polyline {
    val myPolyline = Polyline().apply {
        outlinePaint.color = Color(color).toArgb()
        outlinePaint.strokeWidth = lineWidth
    }

    geoPointsString.split("/").forEach { geoData ->
        val coordinates = geoData.split(",")
        val geoPoint = GeoPoint(
            coordinates[0].toDouble(),
            coordinates[1].toDouble()
        )

        myPolyline.addPoint(geoPoint)
    }

    return myPolyline
}