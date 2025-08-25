package ru.auskov.gpstracker.main.settings.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.auskov.gpstracker.utils.LOCATION_UPDATE_INTERVAL
import ru.auskov.gpstracker.utils.PRIORITY
import ru.auskov.gpstracker.utils.SettingsPreferencesManager
import ru.auskov.gpstracker.utils.TRACK_COLOR
import ru.auskov.gpstracker.utils.TRACK_LINE_WIDTH
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferencesManager
) : ViewModel() {
    fun saveLocationUpdateInterval(value: String) {
        settingsPreferences.setString(LOCATION_UPDATE_INTERVAL, value)
    }

    fun getLocationUpdateInterval(): String {
        return settingsPreferences.getString(LOCATION_UPDATE_INTERVAL, "5000")
    }

    fun saveTrackLineWidth(value: String) {
        settingsPreferences.setString(TRACK_LINE_WIDTH, value)
    }

    fun getTrackLineWidth(): String {
        return settingsPreferences.getString(TRACK_LINE_WIDTH, "10")
    }

    fun savePriority(value: String) {
        settingsPreferences.setString(PRIORITY, value)
    }

    fun getPriority(): String {
        return settingsPreferences.getString(PRIORITY, "PRIORITY_HIGH_ACCURACY")
    }

    fun saveColor(color: Color) {
        settingsPreferences.setString(TRACK_COLOR, color.value.toString())
    }

    fun getColor(): String {
        return settingsPreferences.getString(TRACK_COLOR, Color.Red.value.toString())
    }
}