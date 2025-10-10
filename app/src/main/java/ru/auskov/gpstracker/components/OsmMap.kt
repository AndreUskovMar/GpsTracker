package ru.auskov.gpstracker.components

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.location.data.MapData
import ru.auskov.gpstracker.main.home.map_utils.initMyLocationOverlay

@Composable
fun OsmMap(
    lineWidth: Float,
    lineColor: Int,
    timerText: String,
    mapData: MapData?,
    buttonsPanel: @Composable () -> Unit = {},
    topButtonIconId: Int,
    middleButtonIconId: Int,
    bottomButtonIconId: Int,
    onTopButtonClick: (MapView, MyLocationNewOverlay) -> Unit,
    onMiddleButtonClick: (MapView, MyLocationNewOverlay) -> Unit,
    onBottomButtonClick: (MapView, MyLocationNewOverlay) -> Unit,
    onPolylineInit: (Polyline) -> Unit,
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

    LaunchedEffect(Unit) {
        val polyline = Polyline().apply {
            outlinePaint.color = lineColor
            outlinePaint.strokeWidth = lineWidth
        }
        onPolylineInit(polyline)
        myLocationNewOverlay.value = initMyLocationOverlay(mapView)
        mapView.overlays.add(polyline)
        mapView.overlays.add(myLocationNewOverlay.value)
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
                RoundedCornerText(text = "${stringResource(R.string.time)}: $timerText")
                Spacer(modifier = Modifier.height(3.dp))
                RoundedCornerText(text = "${stringResource(R.string.average_speed)}: ${mapData?.averageSpeed ?: 0.0}km/h")
                Spacer(modifier = Modifier.height(3.dp))
                RoundedCornerText(text = "${stringResource(R.string.speed)}: ${mapData?.speed ?: 0.0}km/h")
                Spacer(modifier = Modifier.height(3.dp))
                RoundedCornerText(text = "${stringResource(R.string.distance)}: ${mapData?.distance ?: 0.0}km", fontSize = 20, fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                buttonsPanel()
                FloatingActionButton(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    onClick = {
                        onTopButtonClick(mapView, myLocationNewOverlay.value!!)
                    }
                ) {
                    Icon(
                        painter = painterResource(topButtonIconId),
                        contentDescription = "top button"
                    )
                }
                Spacer(Modifier.height(5.dp))
                FloatingActionButton(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    onClick = {
                        onMiddleButtonClick(mapView, myLocationNewOverlay.value!!)
                    }
                ) {
                    Icon(
                        painter = painterResource(middleButtonIconId),
                        contentDescription = "middle button"
                    )
                }
                Spacer(Modifier.height(5.dp))
                FloatingActionButton(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    onClick = {
                        onBottomButtonClick(mapView, myLocationNewOverlay.value!!)
                    }
                ) {
                    Icon(
                        painter = painterResource(bottomButtonIconId),
                        contentDescription = "bottom button"
                    )
                }
            }
        }
    }
}