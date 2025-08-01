package ru.auskov.gpstracker.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import ru.auskov.gpstracker.MainActivity
import ru.auskov.gpstracker.R

class LocationService : Service() {
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
        showNotification()
        return START_STICKY
    }

    private fun showNotification() {
        val channel = NotificationChannel(
            "channel_1",
            "Location Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = Notification.Builder(this, "channel_1")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Gps Tracker Running...")
            .setContentIntent(contentIntent)
            .build()

        startForeground(666, notification)
    }
}