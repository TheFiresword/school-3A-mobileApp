package insa.etudiant.sstou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import org.json.JSONArray


import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//ChatGPT's sendGetRequest to get one user by id
fun sendGetRequest(url: String): String {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

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


//ChatGPT's getAllUsers() to then filter by email
fun getAllUsers(): String {
    val urlString = "http://localhost:3000/rescuers"
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection

    try {
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                response.append(line)
            }

            bufferedReader.close()
            inputStream.close()

            return response.toString()
        } else {
            throw Exception("HTTP request failed with response code: $responseCode")
        }
    } finally {
        connection.disconnect()
    }
}


//ChatGPT's getUserByEmail
fun getUserByEmail(usersJson: String, email: String): Profile? {
    val usersArray = JSONArray(usersJson)

    for (i in 0 until usersArray.length()) {
        val userObj = usersArray.getJSONObject(i)
        val userEmail = userObj.getString("email")

        if (userEmail == email) {
            val id = userObj.getInt("id")
            val firstName = userObj.getString("firstName")
            val lastName = userObj.getString("lastName")

            return Profile(id.toString(), email, firstName, lastName /* and other fields */)
        }
    }

    return null
}


//ChatGPT's json extractProfile
data class Profile(val id: String, val firstName: String, val lastName: String, val email: String)
fun extractProfileData(response: String): Profile {
    val jsonResponse = JSONObject(response)

    val id = jsonResponse.getString("id")
    val firstName = jsonResponse.getString("firstname")
    val lastName = jsonResponse.getString("lastname")
    val email = jsonResponse.getString("email")

    return Profile(id, firstName, lastName, email)
}

class profileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //val userTitle = intent.getStringExtra("userTitle")
        val usermail = intent.getStringExtra("usermail")
        val title = findViewById<TextView>(R.id.SSTProfile_Title)

        // Ok, je vais simplement GET tous les users et filtrer en fonction du mail.


        val rescuers = getAllUsers()
        val profile = usermail?.let { getUserByEmail(rescuers, it) }




        title.setText(profile?.firstName)

        //Remplir les valeurs par défault avec celle de la base de données.

        val Confirmation_button = findViewById<Button>(R.id.button_confirmation)

        Confirmation_button.setOnClickListener {

            //Insérer Comparaison ici
            //Insérer requête SQL ici

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        // Send firebase token to backend
        //val intent = Intent(this, profileActivity::class.java)
        //intent.putExtra("userId", profile.id)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TOKEN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            var token = task.result // le token a envoyer
            // A envoyer
            var siteJunior = ""
            val urltokenFirebase = siteJunior+"/rescuers/"+profile?.id
                    sendPutRequest(urltokenFirebase,"{  \"tokenFirebase\": \"$token\" }")
        })
        //Récupérer les données
    }
}