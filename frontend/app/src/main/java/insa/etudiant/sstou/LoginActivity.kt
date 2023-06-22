package insa.etudiant.sstou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

//ChatGPT's POST
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader

import org.json.JSONObject
import java.io.File


fun sendPostRequest(url: String, requestBody: String): String {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "POST"
    connection.doOutput = true

    val postData = requestBody.toByteArray(Charsets.UTF_8)
    connection.setRequestProperty("Content-Length", postData.size.toString())
    DataOutputStream(connection.outputStream).use { outputStream ->
        outputStream.write(postData)
    }

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


//ChatGPT's ServerResponse

data class ServerResponse(val message: String, val details: String)

fun extractServerResponse(response: String): ServerResponse {
    val jsonResponse = JSONObject(response)

    val message = jsonResponse.getString("message")
    val details = jsonResponse.getString("details")

    return ServerResponse(message, details)
}






class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val confirmation = findViewById<Button>(R.id.Connection_button)
        val mdpForget = findViewById<Button>(R.id.MDP_forget) // Bouton Mot de passe oublié
        val retButton = findViewById<Button>(R.id.ret_button_2) // Bouton Retour
        val superButton = findViewById<Button>(R.id.superB)

        confirmation.setOnClickListener {
            val usermailEntry = findViewById<EditText>(R.id.Usermail_entry)
            val passwordEntry = findViewById<EditText>(R.id.Password_entry)

            val usermail = usermailEntry.text.toString()
            val password = passwordEntry.text.toString()

            //Attention : ne pas confondre la requête pour se connecter (ici dans loginActivity) et la requête pour obtenir les infos sur le profil (dans profileActivity)


            val response = sendPostRequest("https://backend-service-3kjf.onrender.com/login",
                "{ \"email\": \"$usermail\", \"password\": \"$password\" }")
            //println(response)
            //La réponse est sous la forme : { "message": "Succès", "details": "..." }
            val serverResponse = extractServerResponse(response)

            val message = serverResponse.message
            val details = serverResponse.details

            val id = "someIdIcantgetbecausefrançoishasnotfinishedhisjob"

            var requestQueue: RequestQueue


            fun sendPatchRequest(id:String) {
                requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()

                // Send firebase token to backend
                //val intent = Intent(this, profileActivity::class.java)
                //intent.putExtra("userId", profile.id)
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result

                    // Log and toast


                    var siteJunior = "https://backend-service-3kjf.onrender.com/"+id
                    var patchurl = siteJunior + "rescuers/" + id
                    val jsonOR = JsonObjectRequest(
                    Request.Method.PATCH,
                    patchurl,
                    JSONObject().apply {
                        put("tokenFirebase", token)
                    }, // envoi token firebase
                    { response ->
                        Toast.makeText(applicationContext, "Réussite chargement BDD", Toast.LENGTH_LONG)
                            .show()
                        val jsonArray = response.getJSONArray("details")
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
            })


            }
            sendPatchRequest(id)
            intent.putExtra("usermail", usermail)
            startActivity(intent) //Redirige vers profil utilisateur (profileActivity
        }


        mdpForget.setOnClickListener {
            val justTrolling = findViewById<TextView>(R.id.Troll)
            justTrolling.text = "Dommage !"
            justTrolling.visibility = View.VISIBLE
        }


        superButton.setOnClickListener {
            val intent = Intent(this, RescuerListActivity::class.java)
            startActivity(intent)
        }

        retButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}