package ru.auskov.gpstracker.main.home.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.auskov.gpstracker.location.LocationDataSharer
import ru.auskov.gpstracker.utils.TimeUtils
import ru.auskov.gpstracker.utils.TimerManager
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val timerManager: TimerManager,
    private val locationDataSharer: LocationDataSharer
) : ViewModel() {
    val timerState = mutableStateOf("00:00:00")

    init {
        viewModelScope.launch {
            locationDataSharer.locationFlow.collect { locationData ->
                //startTimer(locationData.startServiceTime)
            }
        }
    }

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
}