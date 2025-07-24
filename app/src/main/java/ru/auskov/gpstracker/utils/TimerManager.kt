package ru.auskov.gpstracker.utils

import java.util.Timer
import java.util.TimerTask
import javax.inject.Singleton

@Singleton
class TimerManager() {
    private var timer: Timer? = null

    fun startTimer(task: TimerTask) {
        timer?.cancel()
        timer = Timer()
        timer?.schedule(task, 1000, 1000)
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }
}