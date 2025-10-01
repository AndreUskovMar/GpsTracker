package ru.auskov.gpstracker.main.track_viewer.ui

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.components.RoundedCornerText
import ru.auskov.gpstracker.main.home.map_utils.getPolylinesFromString
import ru.auskov.gpstracker.main.home.map_utils.initMyLocationOverlay
import ru.auskov.gpstracker.main.track_viewer.data.TrackViewerNavData

@Composable
fun TrackViewerScreen(
    navData: TrackViewerNavData,
    viewModel: TrackViewerViewModel = hiltViewModel(),
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

    var myPolyline by remember {
        mutableStateOf<Polyline?>(null)
    }

    LaunchedEffect(Unit) {
        myPolyline = getPolylinesFromString(
            viewModel.getColor().toULong(),
            viewModel.getTrackLineWidth().toFloat(),
            navData.geoPoints
        )
        myLocationNewOverlay.value = initMyLocationOverlay(mapView)
        mapView.overlays.add(myPolyline)
        mapView.overlays.add(myLocationNewOverlay.value)
        val startMarker = Marker(mapView).apply {
            position = myPolyline?.actualPoints?.first()
            icon = context.getDrawable(R.drawable.ic_follow_location)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        val finishMarker = Marker(mapView).apply {
            position = myPolyline?.actualPoints?.last()
            icon = context.getDrawable(R.drawable.ic_follow_location)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        mapView.overlays.add(startMarker)
        mapView.overlays.add(finishMarker)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                RoundedCornerText(
                    text = "${stringResource(R.string.track)}: ${navData.name}",
                    fontSize = 20,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                RoundedCornerText(text = "${stringResource(R.string.time)}: ${navData.time}")
                Spacer(modifier = Modifier.height(3.dp))
                RoundedCornerText(text = "${stringResource(R.string.average_speed)}: ${navData.averageSpeed}km/h")
                Spacer(modifier = Modifier.height(3.dp))
                RoundedCornerText(text = "${stringResource(R.string.speed)}: 0.0km/h")
                Spacer(modifier = Modifier.height(3.dp))
                RoundedCornerText(
                    text = "${stringResource(R.string.distance)}: ${navData.distance}km",
                    fontSize = 20,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    onClick = {
                        myLocationNewOverlay.value?.disableFollowLocation()
                        mapView.controller.animateTo(myPolyline?.actualPoints?.first())
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

                    }
                ) {
                    Icon(
                        painter = painterResource(
                            R.drawable.ic_stop
                        ),
                        contentDescription = "stop"
                    )
                }
            }
        }
    }
}