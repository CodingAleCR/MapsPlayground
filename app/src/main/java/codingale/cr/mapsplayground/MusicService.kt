package codingale.cr.mapsplayground

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class MusicService : Service() {

    private lateinit var player: MediaPlayer

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        player = MediaPlayer.create(this, R.raw.audio)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        player.start()
        return START_STICKY
    }

    override fun onDestroy() {
        player.stop()
        player.release()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    fun createNotification(): Notification {
        notificationManager =
                baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CANAL_ID, "Mis Notificaciones", NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = "Música en Servicio"
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val intent = PendingIntent.getActivity(baseContext, 0, intentFor<MusicActivity>(), 0)
        val notification = NotificationCompat.Builder(baseContext, CANAL_ID)
            .setContentIntent(intent)
            .setSmallIcon(android.R.drawable.presence_audio_online).setContentTitle("Servicio de Música")
            .setContentText("Suena el OST de Star Wars.")
        notificationManager.notify(NOTIFICATION_ID, notification.build())

        return notification.build()
    }

    companion object {
        const val CANAL_ID = "mi_canal"
        const val NOTIFICATION_ID = 1
    }
}
