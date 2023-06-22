package insa.etudiant.sstou

import android.content.ContentValues.TAG
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
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

    private lateinit var requestQueue: RequestQueue
    fun patchVolleyRequest(
        id:String,
        token: String,
    ) {
        val url = "https://backend-service-3kjf.onrender.com/rescuers/$id"
        val requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
        val requestBody = JSONObject().apply {
            put("tokenFirebase", token)
        }

        val jsonRequest = object : JsonObjectRequest(
            Request.Method.PATCH,
            url,
            requestBody,
            { response ->
                println("everything is fine")
            },
            { error ->
                println("everything is burning to the grounds")

            }

        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                println(token)
                println("TEST")
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(jsonRequest)

    }

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
                    if (myId == 1) {
                        intent = Intent(this, RescuerListActivity::class.java)
                    } else {
                        intent = Intent(this, ProfileActivity::class.java)
                    }
                    intent.putExtra("usermail", myEmail)
                    intent.putExtra("Id", myId)
                    intent.putExtra("Token", myToken)
                    intent.putExtra("userpassword", password)
                    val id = myId
                    val token = myToken
                    patchVolleyRequest(id.toString(),token)
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










/*
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
        }

*/

