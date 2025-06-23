package ru.auskov.gpstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            GpsTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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