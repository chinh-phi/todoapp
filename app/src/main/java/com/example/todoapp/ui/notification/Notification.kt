package com.example.todoapp.ui.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.example.todoapp.IntentActivity
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.ui.taskdetail.TaskDetailFragment

class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingNotificationIntent = PendingIntent.getActivity(
            context,
            RC_TASK,
            IntentActivity.getIntent(
                context,
                TaskDetailFragment::class.java,
                args = bundleOf(
                    "taskId" to intent.getStringExtra(taskId).toString()
                )
            ),
            flags
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_todo)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setContentIntent(pendingNotificationIntent)
            .build()

        notification.flags = Notification.FLAG_AUTO_CANCEL

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }

    companion object {
        const val notificationId = 1
        const val channelId = "channel"
        const val titleExtra = "titleExtra"
        const val messageExtra = "messageExtra"
        const val taskId = "taskId"
        const val RC_TASK = 0x1111
    }
}