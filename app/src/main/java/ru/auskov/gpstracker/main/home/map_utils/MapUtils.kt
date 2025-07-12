package ru.auskov.gpstracker.main.home.map_utils

import androidx.core.graphics.drawable.toBitmap
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.auskov.gpstracker.R

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