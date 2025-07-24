package ru.auskov.gpstracker.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone


object TimeUtils {
    @SuppressLint("SimpleDateFormat")
    private val timeFormatter = SimpleDateFormat("HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun getTimerTime(startTimeInMillis: Long): String {
        val elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMillis
        val cv = Calendar.getInstance()
        cv.timeInMillis = elapsedTimeInMillis
        return timeFormatter.format(cv.time)
    }
}