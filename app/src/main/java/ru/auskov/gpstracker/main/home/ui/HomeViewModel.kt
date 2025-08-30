package ru.auskov.gpstracker.main.home.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.auskov.gpstracker.db.MainDb
import ru.auskov.gpstracker.location.LocationDataSharer
import ru.auskov.gpstracker.utils.LOCATION_UPDATE_INTERVAL
import ru.auskov.gpstracker.utils.PRIORITY
import ru.auskov.gpstracker.utils.SettingsPreferencesManager
import ru.auskov.gpstracker.utils.TRACK_COLOR
import ru.auskov.gpstracker.utils.TRACK_LINE_WIDTH
import ru.auskov.gpstracker.utils.TimeUtils
import ru.auskov.gpstracker.utils.TimerManager
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val timerManager: TimerManager,
    private val settingsPreferences: SettingsPreferencesManager,
    private val mainDb: MainDb,
    locationDataSharer: LocationDataSharer,
) : ViewModel() {
    val timerState = mutableStateOf("00:00:00")

    val locationFlow = locationDataSharer.locationFlow

    fun startTimer(startTimeInMillis: Long) {
        timerManager.startTimer(object : TimerTask() {
            override fun run() {
                timerState.value = TimeUtils.getTimerTime(startTimeInMillis)
            }
        })
    }

    fun stopTimer() {
        timerManager.stopTimer()
    }

    fun getLocationUpdateInterval(): String {
        return settingsPreferences.getString(LOCATION_UPDATE_INTERVAL, "5000")
    }

    fun getPriority(): String {
        return settingsPreferences.getString(PRIORITY, "PRIORITY_HIGH_ACCURACY")
    }

    fun getColor(): String {
        return settingsPreferences.getString(TRACK_COLOR, Color.Red.value.toString())
    }

    fun getTrackLineWidth(): String {
        return settingsPreferences.getString(TRACK_LINE_WIDTH, "10")
    }
}