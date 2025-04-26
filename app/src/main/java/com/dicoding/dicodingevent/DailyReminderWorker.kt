package com.dicoding.dicodingevent

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.dicodingevent.data.retrofit.ApiConfig

class DailyReminderWorker(
    context: Context ,
    workerParams: WorkerParameters
) : Worker(context , workerParams) {

    override fun doWork(): Result {
        val apiService = ApiConfig.getApiService()

        return try {
            val response = apiService.getNotificationEvents(active = 1).execute() // hanya upcoming
            if (response.isSuccessful) {
                val result = response.body()
                if (result != null && !result.error && result.listEvents.isNotEmpty()) {
                    val event = result.listEvents[0]
                    showNotification("Upcoming: ${event.name}" , event.summary)
                }
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }


    @SuppressLint("MissingPermission")
    private fun showNotification(title: String , message: String) {
        val channelId = "upcoming_event_channel"
        val channelName = "Upcoming Event"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val channel = NotificationChannel(
                channelId ,
                channelName ,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext , MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("destination" , "upcoming")

        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext ,
            0 ,
            intent ,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext , channelId)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1001 , builder.build())
        }
    }
}
