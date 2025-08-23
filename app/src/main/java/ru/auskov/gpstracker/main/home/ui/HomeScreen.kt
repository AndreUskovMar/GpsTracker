package ru.auskov.gpstracker.main.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.components.RoundedCornerText
import ru.auskov.gpstracker.main.home.map_utils.getAverageSpeed
import ru.auskov.gpstracker.main.home.map_utils.initMyLocationOverlay
import ru.auskov.gpstracker.main.home.map_utils.isLocationServiceRunning
import ru.auskov.gpstracker.main.home.map_utils.startLocationService
import ru.auskov.gpstracker.main.home.map_utils.stopLocationService

@SuppressLint("DefaultLocale")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(20.0)
        }
    }

    val myLocationNewOverlay = remember {
        mutableStateOf<MyLocationNewOverlay?>(null)
    }

    var isServiceRunning by remember {
        mutableStateOf(false)
    }

    var distance by remember {
        mutableStateOf("0,0")
    }

    var speed by remember {
        mutableStateOf("0,0")
    }

    var averageSpeed by remember {
        mutableStateOf("0,0")
    }

    var isStartTracking by remember {
        mutableStateOf(false)
    }

    var isPolylineResume by remember {
        mutableStateOf(false)
    }

    var myPolyline by remember {
        mutableStateOf<Polyline?>(null)
    }

    LaunchedEffect(Unit) {
        myPolyline = Polyline()
        myLocationNewOverlay.value = initMyLocationOverlay(mapView)
        mapView.overlays.add(myPolyline)
        mapView.overlays.add(myLocationNewOverlay.value)
        isServiceRunning = isLocationServiceRunning(context)
        viewModel.locationFlow.collect { locationData ->
            distance = String.format("%.1f", locationData.distance / 1000f)
            speed = String.format("%.1f", 3.6 * locationData.speed)
            averageSpeed = getAverageSpeed(locationData.distance, locationData.startServiceTime)

            if (isServiceRunning && !isStartTracking) {
                isStartTracking = true
                viewModel.startTimer(locationData.startServiceTime)
            }
            if (isServiceRunning) {
                if (isPolylineResume) {
                    myPolyline?.addPoint(locationData.geoPoints.last())
                } else {
                    isPolylineResume = true
                    locationData.geoPoints.forEach { geoPoint ->
                        myPolyline?.addPoint(geoPoint)
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            RoundedCornerText(text = "Time: ${viewModel.timerState.value}")
            Spacer(modifier = Modifier.height(3.dp))
            RoundedCornerText(text = "Average Speed: ${averageSpeed}km/h")
            Spacer(modifier = Modifier.height(3.dp))
            RoundedCornerText(text = "Speed: ${speed}km/h")
            Spacer(modifier = Modifier.height(3.dp))
            RoundedCornerText(text = "Distance: ${distance}km", fontSize = 20, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                containerColor = Color.White,
                contentColor = Color.Black,
                onClick = {
                    myLocationNewOverlay.value?.enableFollowLocation()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_follow_location),
                    contentDescription = "follow_location"
                )
            }
            Spacer(Modifier.height(5.dp))
            FloatingActionButton(
                containerColor = Color.White,
                contentColor = Color.Black,
                onClick = {
                    mapView.controller.animateTo(myLocationNewOverlay.value?.myLocation)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_my_location),
                    contentDescription = "my_location"
                )
            }
            Spacer(Modifier.height(5.dp))
            FloatingActionButton(
                containerColor = Color.White,
                contentColor = Color.Black,
                onClick = {
                    if (isServiceRunning) {
                        stopLocationService(context)
                        viewModel.stopTimer()
                        isServiceRunning = false
                    } else {
                        isServiceRunning = true
                        val startTimeInMillis = System.currentTimeMillis()
                        viewModel.startTimer(startTimeInMillis)
                        startLocationService(context, startTimeInMillis)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(if (isServiceRunning) {
                        R.drawable.ic_stop
                    } else {
                        R.drawable.ic_play
                    }),
                    contentDescription = "play_and_stop"
                )
            }
        }
    }
}