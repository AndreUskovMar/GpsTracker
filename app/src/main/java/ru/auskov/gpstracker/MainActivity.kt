package ru.auskov.gpstracker

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import ru.auskov.gpstracker.main.bottom_nav_bar.data.BottomMenuItem
import ru.auskov.gpstracker.main.bottom_nav_bar.ui.BottomMenu
import ru.auskov.gpstracker.main.home.data.HomeNavData
import ru.auskov.gpstracker.main.home.ui.HomeScreen
import ru.auskov.gpstracker.main.settings.data.SettingsNavData
import ru.auskov.gpstracker.main.settings.ui.SettingsScreen
import ru.auskov.gpstracker.main.track.data.TrackNavData
import ru.auskov.gpstracker.main.track.ui.TrackScreen
import ru.auskov.gpstracker.main.track_viewer.data.TrackViewerNavData
import ru.auskov.gpstracker.main.track_viewer.ui.TrackViewerScreen
import ru.auskov.gpstracker.ui.theme.GpsTrackerTheme
import ru.auskov.gpstracker.utils.PermissionManager

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()

    private lateinit var fineLocationPermission: ActivityResultLauncher<String>
    private lateinit var bgLocationPermission: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setUpOSM(this)

        fineLocationPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                PermissionManager.checkBackgroundLocation(
                    this,
                    onPermissionGranted = {

                    },
                    onPermissionDenied = {
                        bgLocationPermission.launch(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    }
                )
            } else {
                Toast.makeText(this, "Fine location permission has been denied", Toast.LENGTH_SHORT).show()
            }
        }

        bgLocationPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Background location permission has been granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Background location permission has been denied", Toast.LENGTH_SHORT).show()
            }
        }

        PermissionManager.checkFineLocation(
            this,
            onPermissionGranted = {
                PermissionManager.checkBackgroundLocation(
                    this,
                    onPermissionGranted = {
                        Toast.makeText(this, "Background location permission has been granted", Toast.LENGTH_SHORT).show()
                    },
                    onPermissionDenied = {
                        bgLocationPermission.launch(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    }
                )
            },
            onPermissionDenied = {
                fineLocationPermission.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        )

        setContent {
            val navController = rememberNavController()

            val selectedItemState = remember {
                mutableIntStateOf(BottomMenuItem.Home.titleId)
            }

            val isBottomTabbarVisible = remember {
                mutableStateOf(true)
            }

            GpsTrackerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (isBottomTabbarVisible.value) {
                            BottomMenu(selectedItemState.intValue) { itemTitle ->
                                selectedItemState.intValue = itemTitle
                                when(itemTitle) {
                                    BottomMenuItem.Home.titleId -> navController.navigate(HomeNavData)
                                    BottomMenuItem.Track.titleId -> navController.navigate(TrackNavData)
                                    BottomMenuItem.Settings.titleId -> navController.navigate(SettingsNavData)
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = HomeNavData,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<HomeNavData> {
                            HomeScreen()
                        }
                        composable<SettingsNavData> {
                            SettingsScreen()
                        }
                        composable<TrackNavData> {
                            isBottomTabbarVisible.value = true
                            TrackScreen { trackData ->
                                navController.navigate(TrackViewerNavData(
                                    trackData.name,
                                    trackData.date,
                                    trackData.distance,
                                    trackData.time,
                                    trackData.averageSpeed,
                                    trackData.geoPoints
                                ))
                            }
                        }
                        composable<TrackViewerNavData> { navBackStackEntry ->
                            val navData = navBackStackEntry.toRoute<TrackViewerNavData>()
                            TrackViewerScreen(navData)
                            isBottomTabbarVisible.value = false
                        }
                    }
                }
            }
        }
    }
}

private fun setUpOSM(context: Context) {
    val config = Configuration.getInstance()
    config.load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    config.userAgentValue = context.packageName
}