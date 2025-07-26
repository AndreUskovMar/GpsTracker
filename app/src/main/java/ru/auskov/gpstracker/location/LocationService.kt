package ru.auskov.gpstracker.location

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class LocationService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d("MyLog", "Service onCreate")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.d("MyLog", "Service onDestroy")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyLog", "Service onStartCommand ${intent?.getStringExtra("test")}")
        return START_STICKY
    }
}