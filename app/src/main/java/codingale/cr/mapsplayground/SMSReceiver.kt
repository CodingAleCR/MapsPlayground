package codingale.cr.mapsplayground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            context.applicationContext.startService(Intent(context.applicationContext, MusicService::class.java))
        }
    }
}
