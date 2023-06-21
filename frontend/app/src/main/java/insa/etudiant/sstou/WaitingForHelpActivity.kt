package insa.etudiant.sstou

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


fun sendHelpRequest(urlString: String): String {
    val url = URL(urlString)

    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "GET"

        val responseCode = responseCode

        val response = StringBuffer()

        inputStream.bufferedReader().use {
            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
        }

        return response.toString()
    }
}


data class Helpers(val id: String, val firstName: String, val lastName: String, val email: String, val tokenFirebase: String)
fun extractHelpersData(response: String): List<Helpers> {


    val helperList = mutableListOf<Helpers>()

    val jsonArray = JSONArray(response)
    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val id = jsonObject.getString("id")
        val firstName = jsonObject.getString("firstName")
        val lastName = jsonObject.getString("lastName")
        val email = jsonObject.getString("email")
        val tokenFirebase = jsonObject.getString("tokenFirebase")

        val helper = Helpers(id, firstName, lastName, email, tokenFirebase)
        helperList.add(helper)
    }
    return helperList

}
class WaitingForHelpActivity : AppCompatActivity() {
    private val phoneNumber = "+33781552056"

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

    val response = getAvailableSST("http://localhost:3000/rescuers/available")
    //  0123456789 10 11 121314151617 181920 2122232425 26 2728293031323334 -> DATA juste après au bout du 35e caractère

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_for_help)

        val location = intent.getStringExtra("location")
        val waitingSentence1 = getString(R.string.waitingSentence1)
        val waitingSentence2 = getString(R.string.waitingSentence2)
        val retButton = findViewById<Button>(R.id.button_ret_2) //Bouton "retour"

        val waitingText = findViewById<TextView>(R.id.waitingText)
        waitingText.text = waitingSentence1 + location + waitingSentence2
        retButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        /* Version un numéro Telephone
       initiateMessage()
       */
        // version notification
        val siteJunior = ""
        val helpUrl = "$siteJunior/rescuers/available"
        val response = sendHelpRequest(helpUrl)
        val helpers = extractHelpersData(response)
        for (secourist in helpers){
                Companion.sendNotif(location.toString(),secourist.tokenFirebase)
        }


    }

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
        fun sendNotif(room:String, theToken:String) {
            val notif = HashMap<String,String>()
            notif.put("title","Aide demandée !")
            notif.put("body", "Urgence en salle " + room + " !")
            notif.put("to", theToken)
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("Notifications").document().set(notif)
            println("done")
        }

        private const val PERMISSION_REQUEST_SEND_SMS = 1
    }

}


