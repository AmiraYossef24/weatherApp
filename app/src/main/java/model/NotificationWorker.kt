package model

import AppDB.WeatherLocalDataSource
import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.R
import countrydetails.view.CountryDetailsFragment
import home.viewModel.HomeViewModel
import home.viewModel.HomeViewModelFactory
import network.weatherRemoteDataSource
import android.app.NotificationManager as NotificationManager

val ACTION_DISMISS_NOTIFICATION = "com.example.weatherApp.ACTION_DISMISS_NOTIFICATION"

class NotificationWorker(context: Context, parameters: WorkerParameters) :
    Worker(context, parameters) {

    private val API_KEY="af0b74520668db5033dea0b93e9a70c3"
    private  val TAG = "NotificationWorker"
    lateinit var des : String
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    @SuppressLint("WrongThread")
    override fun doWork(): Result {
        Log.i(TAG, "I am inside doWork()")

        val countryName = inputData.getString("countryName")
        val des=inputData.getString("des")
        Log.i(TAG, "country name from notification worker : ${countryName} and desc = ${des} ")


        countryName?.let { showNotification(it, des.toString()) }
        if (countryName != null) {

        }
        return Result.success()
    }


    private fun showNotification(countryName : String , des:String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel_id",
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val dismissIntent = Intent(applicationContext, DismissNotificationReceiver::class.java).apply {
            action = ACTION_DISMISS_NOTIFICATION
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(applicationContext, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(applicationContext, "default_channel_id")
            .setContentTitle(countryName)
            .setContentText("${des} Remember to show the weather there!")
            .setSmallIcon(R.drawable.sunny)
            .addAction(R.drawable.cancel, "Dismiss", dismissPendingIntent)
            .build()


        notificationManager.notify(1, notification)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channel_id", "Weather App Channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
    }


}

