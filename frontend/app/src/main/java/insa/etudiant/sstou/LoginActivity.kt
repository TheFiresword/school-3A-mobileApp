package insa.etudiant.sstou

import android.content.ContentValues.TAG
import android.content.ContentValues.TAG
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

import org.json.JSONObject


fun authentification(requestQueue : RequestQueue, email : String, password : String, success_callback: (String, Int, String) -> Unit, error_callback: () -> Unit) {
    val url = "https://backend-service-3kjf.onrender.com/authentification/login"
    val requestBody = JSONObject().apply {
        put("email", email)
        put("password", password)
    }
    val jsonOR = JsonObjectRequest(
        Request.Method.POST,
        url,
        requestBody,
        { response ->
            val detailsObject = response.getJSONObject("details")
            val myToken = detailsObject.getString("token")
            val userDataObject = detailsObject.getJSONObject("userData")

            val myEmail = userDataObject.getString("email")
            val myId = userDataObject.getInt("id")

            success_callback(myEmail, myId, myToken)
        },
        { error ->
            error_callback()
        }
    )
    requestQueue.add(jsonOR)
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
            val reqQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()

            authentification(reqQueue, usermail, password,
                success_callback = { myEmail, myId, myToken ->
                    Toast.makeText(applicationContext, "Login Réussi.", Toast.LENGTH_SHORT).show()

                    val intent: Intent
                    if (myEmail == "sstou.insa.projet@gmail.com") {
                        intent = Intent(this, RescuerListActivity::class.java)
                    } else {
                        intent = Intent(this, ProfileActivity::class.java)
                    }
                    intent.putExtra("usermail", myEmail)
                    intent.putExtra("Id", myId)
                    intent.putExtra("Token", myToken)
                    val id = myId
                    val token = myToken
                    val requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
                    var siteJunior = "https://backend-service-3kjf.onrender.com/" + id
                    var patchurl = siteJunior + "rescuers/" + id

                    // Send firebase token to backend
                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                            return@OnCompleteListener
                        }

                        // Get new FCM registration token
                        val firetoken = task.result
                        val jsonOR = JsonObjectRequest(
                            Request.Method.PATCH,
                            patchurl,
                            JSONObject().apply {
                                put("tokenFirebase", firetoken)
                                put("id", id)
                                put("token", token)
                            }, // envoi token firebase
                            { response ->
                                Toast.makeText(
                                    applicationContext,
                                    "Réussite chargement BDD",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                //val jsonArray = response.getJSONArray("details")
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
                    startActivity(intent)
                },
                error_callback = {
                    Toast.makeText(applicationContext, "Login Echoué", Toast.LENGTH_SHORT).show()
                })
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














