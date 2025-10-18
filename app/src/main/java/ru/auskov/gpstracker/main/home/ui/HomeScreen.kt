package ru.auskov.gpstracker.main.home.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.osmdroid.views.overlay.Polyline
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.components.OsmMap
import ru.auskov.gpstracker.components.TrackDialog
import ru.auskov.gpstracker.location.data.MapData
import ru.auskov.gpstracker.main.home.map_utils.geoPointsToString
import ru.auskov.gpstracker.main.home.map_utils.getAverageSpeed
import ru.auskov.gpstracker.main.home.map_utils.isLocationServiceRunning
import ru.auskov.gpstracker.main.home.map_utils.startLocationService
import ru.auskov.gpstracker.main.home.map_utils.stopLocationService
import ru.auskov.gpstracker.main.track.data.TrackData
import ru.auskov.gpstracker.utils.TimeUtils

@SuppressLint("DefaultLocale")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var mapDataState by remember {
        mutableStateOf<MapData?>(null)
    }

    var isServiceRunning by remember {
        mutableStateOf(false)
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

    var isTrackDialogVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        isServiceRunning = isLocationServiceRunning(context)
        viewModel.locationFlow.collect { locationData ->
            val distance = String.format("%.1f", locationData.distance / 1000f)
            val speed = String.format("%.1f", 3.6 * locationData.speed)
            val averageSpeed = getAverageSpeed(locationData.distance, locationData.startServiceTime)

            mapDataState = MapData(averageSpeed, speed, distance)

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

    OsmMap(
        lineColor = Color(viewModel.getColor().toULong()).toArgb(),
        lineWidth = viewModel.getTrackLineWidth().toFloat(),
        timerText = viewModel.timerState.value,
        mapData = mapDataState,
        topButtonIconId = R.drawable.ic_follow_location,
        middleButtonIconId = R.drawable.ic_my_location,
        bottomButtonIconId = if (isServiceRunning) {
            R.drawable.ic_stop
        } else {
            R.drawable.ic_play
        },
        onTopButtonClick = { _, myLocationNewOverlay ->
            myLocationNewOverlay.enableFollowLocation()
        },
        onMiddleButtonClick = { mapView, myLocationNewOverlay ->
            mapView.controller.animateTo(myLocationNewOverlay.myLocation)
        },
        onBottomButtonClick = { _, _ ->
            if (isServiceRunning) {
                stopLocationService(context)
                viewModel.stopTimer()
                isServiceRunning = false
                isTrackDialogVisible = true
            } else {
                isServiceRunning = true
                val priority = viewModel.getPriority()
                val updateTime = viewModel.getLocationUpdateInterval().toLong()
                val startTimeInMillis = System.currentTimeMillis()
                viewModel.startTimer(startTimeInMillis)
                startLocationService(context, startTimeInMillis, updateTime, priority)
            }
        },
        onPolylineInit = { polyline ->
            myPolyline = polyline
        }
    )

    TrackDialog(
        title = stringResource(R.string.sure_save_track),
        isVisible = isTrackDialogVisible,
        onDismiss = {
            isTrackDialogVisible = false
        },
        onSubmit = { trackName ->
            isTrackDialogVisible = false
            Log.d("MyLog", trackName)
            if (mapDataState == null) return@TrackDialog
            val trackData = TrackData(
                name = trackName,
                date = TimeUtils.getTrackTime(),
                distance = mapDataState!!.distance,
                averageSpeed = mapDataState!!.averageSpeed,
                geoPoints = geoPointsToString(myPolyline?.actualPoints!!)
            )

            viewModel.insertTrack(trackData)
        }
    )
}