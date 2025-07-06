package ru.auskov.gpstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setContent {
            val navController = rememberNavController()

            val selectedItemState = remember {
                mutableStateOf(BottomMenuItem.Home.title)
            }

            GpsTrackerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomMenu(selectedItemState.value) { itemTitle ->
                            selectedItemState.value = itemTitle
                            when(itemTitle) {
                                BottomMenuItem.Home.title -> navController.navigate(HomeNavData)
                                BottomMenuItem.Track.title -> navController.navigate(TrackNavData)
                                BottomMenuItem.Settings.title -> navController.navigate(SettingsNavData)
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
                            TrackScreen()
                        }
                        composable<TrackViewerNavData> {
                            TrackViewerScreen()
                        }
                    }
                }
            }
        }
    }
}