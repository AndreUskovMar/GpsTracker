package ru.auskov.gpstracker.main.track_viewer.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.auskov.gpstracker.utils.SettingsPreferencesManager
import ru.auskov.gpstracker.utils.TRACK_COLOR
import ru.auskov.gpstracker.utils.TRACK_LINE_WIDTH
import javax.inject.Inject

@HiltViewModel
class TrackViewerViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferencesManager,
) : ViewModel() {
    fun getColor(): String {
        return settingsPreferences.getString(TRACK_COLOR, Color.Red.value.toString())
    }

    fun getTrackLineWidth(): String {
        return settingsPreferences.getString(TRACK_LINE_WIDTH, "10")
    }
}