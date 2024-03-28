package model

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent



class DismissNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_DISMISS_NOTIFICATION) {
            // Delete or cancel the notification from your app
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1) // Replace `notificationId` with the ID of your notification
        }
    }
}