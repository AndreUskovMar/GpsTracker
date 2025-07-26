package ru.auskov.gpstracker.main.home.ui

import android.content.Intent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.auskov.gpstracker.R
import ru.auskov.gpstracker.components.RoundedCornerText
import ru.auskov.gpstracker.location.LocationService
import ru.auskov.gpstracker.main.home.map_utils.initMyLocationOverlay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(5.0)
        }
    }

    val myLocationNewOverlay = remember {
        mutableStateOf<MyLocationNewOverlay?>(null)
    }

    LaunchedEffect(Unit) {
        myLocationNewOverlay.value = initMyLocationOverlay(mapView)
        mapView.overlays.add(myLocationNewOverlay.value)
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
            RoundedCornerText(text = "Average Speed: 0.0km/h")
            Spacer(modifier = Modifier.height(3.dp))
            RoundedCornerText(text = "Speed: 0.0km/h")
            Spacer(modifier = Modifier.height(3.dp))
            RoundedCornerText(text = "Distance: 0.0km", fontSize = 20, fontWeight = FontWeight.Bold)
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
                    val intent = Intent(context, LocationService::class.java)
                    context.stopService(intent)

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
                    val intent = Intent(context, LocationService::class.java)
                    intent.putExtra("test", "test")
                    context.startService(intent)

                    viewModel.startTimer(System.currentTimeMillis())
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = "play"
                )
            }
        }
    }
}