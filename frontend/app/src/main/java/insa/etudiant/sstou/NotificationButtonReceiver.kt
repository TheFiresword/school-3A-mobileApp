package insa.etudiant.sstou

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore

class NotificationButtonReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "MY_NOTIFICATION_BUTTON_CLICK") {
            println("là")
            val notif = HashMap<String, String>()
            notif.put("title", "Demande acceptée !")
            notif.put("body", "Un secouriste est en route")
            notif.put("to", intent.getStringExtra("exp")!!)
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("Notifications").document().set(notif)
            println("done")
        }
    }
}