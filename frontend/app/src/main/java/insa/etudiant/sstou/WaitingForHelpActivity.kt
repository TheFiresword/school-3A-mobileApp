package insa.etudiant.sstou

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Objects
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL



data class Helpers(val id: String, val firstName: String, val lastName: String, val email: String, val tokenFirebase: String, val telephone: String)



class WaitingForHelpActivity : AppCompatActivity() {

    fun sendNotif(room: String, theToken: String) {
        val notif = HashMap<String, String>()
        notif.put("title", "Aide demandée !")
        notif.put("body", "Urgence en salle " + room + " !")
        notif.put("to", theToken)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result.toString()
            println("token = " + token)
            notif.put("exp", token)
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("Notifications").document().set(notif)
            println("done")
        })
    }


    private val PERMISSION_REQUEST_SEND_SMS = 1


    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescuer_list)
        var helperList = mutableListOf<Helpers>()
        fun getHelperList(url: String): List<Helpers> {
            val url = "https://backend-service-3kjf.onrender.com/rescuers/available"
            requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
            var names = arrayListOf(
                "Secouriste 1",
                "Secouriste 2",
                "Secouriste 3",
                "Secouriste 4"
            ) //Default List

            val jsonOR = JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // Rien besoin d'envoyer
                { response ->
                    Toast.makeText(applicationContext, "Réussite chargement BDD", Toast.LENGTH_LONG)
                        .show()
                    val jsonArray = response.getJSONArray("details")
                    println(names)
                    names.clear()
                    for (i in 0 until jsonArray.length()) {
                        val rescuerName = jsonArray.getJSONObject(i)
                        val rescuer = rescuerName.getString("lastname")
                        names.add(rescuer)
                        val id = rescuerName.getString("id")
                        val firstName = rescuerName.getString("firstName")
                        val lastName = rescuerName.getString("lastName")
                        val email = rescuerName.getString("email")
                        val tokenFirebase = rescuerName.getString("tokenFirebase")
                        val telephone = rescuerName.getString("telephone")
                        val helper =
                            Helpers(id, firstName, lastName, email, tokenFirebase, telephone)
                        helperList.add(helper)
                    }
                },
                { error ->
                    Toast.makeText(
                        applicationContext,
                        "Erreur du chargement BDD",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
            requestQueue?.add(jsonOR)
            return helperList
        }

        val siteJunior = "https://backend-service-3kjf.onrender.com/"
        val helpUrl = "$siteJunior/rescuers/available"
        val response = getHelperList(helpUrl)
        val salle = EnterLocationActivity().defLocation

        for (secourist in response) {
            sendNotif(salle, secourist.tokenFirebase)
            if (secourist.telephone != "") {
                sendMessage(secourist.telephone)
            }

        }
    }
    /*
    private fun initiateMessage() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.SEND_SMS),
                PERMISSION_REQUEST_SEND_SMS
            )
        } else {
            sendMessage()
        }
    }
    */

    private fun sendMessage(phoneNumber: String) {
        val smsManager = SmsManager.getDefault()
        val location = intent.getStringExtra("location")
        val message = "[SST][URGENT] Votre aide a été demandée en $location !"

        try {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "Message sent.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send message.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    companion object {
        fun sendNotif(room: String, theToken: String) {
            val notif = HashMap<String, String>()
            notif.put("title", "Aide demandée !")
            notif.put("body", "Urgence en salle " + room + " !")
            notif.put("to", theToken)
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result.toString()
                println("token = " + token)
                notif.put("exp", token)
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("Notifications").document().set(notif)
                println("done")
            })
        }
    }
}


/*

    private fun getAvailableSST(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.doOutput = true

        /*val postData = requestBody.toByteArray(Charsets.UTF_8)
        connection.setRequestProperty("Content-Length", postData.size.toString())
        DataOutputStream(connection.outputStream).use { outputStream ->
            outputStream.write(postData)
        }*/

        val responseCode = connection.responseCode
        val response = StringBuilder()
        BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
        }
        connection.disconnect()

        return response.toString()
    }

    /*private fun extractPhoneNumbers(response: String)
    {
        var repertoire = null
        var deplacement = 0
        for (i<=)
            val pos = response.indexOf("telephone:",deplacement)
            deplacement = pos + 1
    }*/



    private fun initiateMessage() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.SEND_SMS),
                PERMISSION_REQUEST_SEND_SMS
            )
        } else {
            sendMessage()
        }
    }
    private fun sendMessage() {
        val smsManager = SmsManager.getDefault()
        val location = intent.getStringExtra("location")
        val message = "[SST][URGENT] Votre aide a été demandée en $location !"

        try {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "Message sent.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send message.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }



/*
        override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_SEND_SMS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage()
                }
            }
        }
    }


    companion object {
        fun sendNotif(room: String, theToken: String) {
            val notif = HashMap<String, String>()
            notif.put("title", "Aide demandée !")
            notif.put("body", "Urgence en salle " + room + " !")
            notif.put("to", theToken)
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result.toString()
                println("token = " + token)
                notif.put("exp", token)
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("Notifications").document().set(notif)
                println("done")
            })

            // Get new FCM registration token
//            val firestore = FirebaseFirestore.getInstance()
//            firestore.collection("Notifications").document().set(notif)
//            println("done")
        }
    }
}
*/
 */