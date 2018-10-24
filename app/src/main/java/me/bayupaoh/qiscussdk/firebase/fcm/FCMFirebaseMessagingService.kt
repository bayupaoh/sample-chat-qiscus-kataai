package me.bayupaoh.qiscussdk.firebase.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import me.bayupaoh.qiscussdk.R

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import me.bayupaoh.qiscussdk.feature.main.MainActivity


class FCMFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val title = if (remoteMessage!!.data["title"] != null) remoteMessage.data["title"] else "SuitCore Notification"
        val body = if (remoteMessage.data["body"] != null) remoteMessage.data["body"] else "Test Notification"
        sendNotification(body.toString(), remoteMessage.data["id"].toString(), title.toString())
    }

    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String, id: String, title: String) {
        val intent: Intent

        try {
            intent = MainActivity.createIntent(this)
            val pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT)

            Observable.just(messageBody)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                        val notificationBuilder = NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(messageBody)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent)

                        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        notificationManager.notify(0  /*ID of notification*/, notificationBuilder.build())
                        //  PreferenceManager.instance().incrementNotificationId();
                        //  Log.d("NOTIF_ID", PreferenceManager.instance().getNotificationId()+ " --");
                    }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {

        private val TAG = "MyFirebaseMsgService"
    }

}
