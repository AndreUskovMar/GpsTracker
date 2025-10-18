package ru.auskov.gpstracker.main.track_viewer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.components.OsmMap
import ru.auskov.gpstracker.location.data.MapData
import ru.auskov.gpstracker.main.home.map_utils.getGeoPointsFromString
import ru.auskov.gpstracker.main.track_viewer.data.TrackViewerNavData

@Composable
fun TrackViewerScreen(
    navData: TrackViewerNavData,
    viewModel: TrackViewerViewModel = hiltViewModel(),
) {
    val geoPoints = remember {
        getGeoPointsFromString(navData.geoPoints)
    }

    OsmMap(
        lineColor = Color(viewModel.getColor().toULong()).toArgb(),
        lineWidth = viewModel.getTrackLineWidth().toFloat(),
        timerText = navData.time,
        mapData = MapData(navData.averageSpeed, navData.averageSpeed, navData.distance),
        geoPointsList = geoPoints,
        topButtonIconId = R.drawable.ic_follow_location,
        middleButtonIconId = R.drawable.ic_my_location,
        bottomButtonIconId = R.drawable.ic_stop,
        onTopButtonClick = { mapView, myLocationNewOverlay ->
            myLocationNewOverlay.disableFollowLocation()
            mapView.controller.animateTo(geoPoints.first())
        },
        onMiddleButtonClick = { mapView, myLocationNewOverlay ->
            myLocationNewOverlay.enableFollowLocation()
            mapView.controller.animateTo(myLocationNewOverlay.myLocation)
        },
        onBottomButtonClick = { _, _ ->

        }
    )
}