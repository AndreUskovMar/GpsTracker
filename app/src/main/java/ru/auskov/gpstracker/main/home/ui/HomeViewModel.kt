package ru.auskov.gpstracker.main.home.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.auskov.gpstracker.utils.TimeUtils
import ru.auskov.gpstracker.utils.TimerManager
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val timerManager: TimerManager
) : ViewModel() {
    val timerState = mutableStateOf("00:00:00")

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