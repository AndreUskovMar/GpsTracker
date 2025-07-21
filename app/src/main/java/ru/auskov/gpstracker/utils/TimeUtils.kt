package ru.auskov.gpstracker.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

@SuppressLint("ConstantLocale", "SimpleDateFormat")
object TimeUtils {
    private val timeFormatter = SimpleDateFormat("HH:mm:ss")

    fun getTimerTime(startTimeInMillis: Long): String {
        val elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMillis
        val cv = Calendar.getInstance()
        cv.timeInMillis = elapsedTimeInMillis
        return timeFormatter.format(cv.time)
    }
}