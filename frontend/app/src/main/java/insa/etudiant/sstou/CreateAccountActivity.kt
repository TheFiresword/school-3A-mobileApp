package insa.etudiant.sstou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


//data class Profile(val id: String, val firstName: String, val lastName: String, val email: String)
fun createRescuer(requestQueue : RequestQueue, name: String, phoneNumber: String, mdp:String, successCallback: (response:JSONObject) -> Unit, errorCallback: (error:VolleyError) -> Unit) {
    val url = "https://backend-service-3kjf.onrender.com/rescuers"
    val requestBody = JSONObject().apply {
        put("name", name)
        put("phone_number", phoneNumber)
        put("password", mdp)
    }

    val jsonRequest = JsonObjectRequest(
        Request.Method.POST,
        url,
        requestBody,
        { response ->
            successCallback(response)
        },
        { error ->
            errorCallback(error)
        }
    )
    requestQueue.add(jsonRequest)
}

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val title = findViewById<TextView>(R.id.Title)
        val username = findViewById<EditText>(R.id.SSTProfile_username)
        val telnum = findViewById<EditText>(R.id.SSTProfile_Tel)
        val mdp = findViewById<EditText>(R.id.SSTProfile_Password)
        val mdpconf = findViewById<EditText>(R.id.SSTProfile_Password_confirm)
        val confirmation_button = findViewById<Button>(R.id.button_confirmation)
        val warningText = findViewById<TextView>(R.id.warningText)

        title.setText("Création d'un nouveau compte SST")

        confirmation_button.setOnClickListener {
            val confUsername = username.text.toString()
            val confTelnum = telnum.text.toString()
            val confMdp = mdp.text.toString()
            val confConfMdp = mdpconf.text.toString()
            //Erreurs dans le remplissage des champs :
            if (confUsername.isEmpty() or confTelnum.isEmpty() or confMdp.isEmpty() or confConfMdp.isEmpty()) {
                warningText.setVisibility(View.VISIBLE)
                warningText.setText("Veuillez remplir tous les champs !")
            }
            else if (confMdp != confConfMdp) {
                warningText.setVisibility(View.VISIBLE)
                warningText.setText("Les mots de passe ne sont pas les mêmes !")
            }
            else {
                requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
                createRescuer(requestQueue, confUsername,confTelnum,confMdp,
                    successCallback = { response ->
                        val message = response.getString("Utilisateur ajouté avec succès")
                        Toast.makeText(applicationContext, "Success: $message", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, RescuerListActivity::class.java)
                        startActivity(intent)
                },
                    errorCallback = { error ->
                        val errorMessage = error.message ?: "Erreur dans la création de l'utilisateur"
                        Toast.makeText(applicationContext, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    })
            }

        }
    }
}