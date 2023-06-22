package insa.etudiant.sstou

//ChatGPT's GET request

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.Notification
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import org.json.JSONArray
import com.android.volley.RequestQueue
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//ChatGPT's sendGetRequest to get one user by id
/*fun sendGetRequest(url: String): String {
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
}*/

/*fun deleteRequest(url: String): String {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "DELETE"

    val response = StringBuilder()
    BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
    }
    connection.disconnect()

    return response.toString()
}*/

/*fun deleteVolleyRequest(id : Int, successCallback: (response:JSONObject) -> Unit, errorCallback: (error:VolleyError) -> Unit) {
    val url = "https://backend-service-3kjf.onrender.com/rescuers/"+id
    requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
    val jsonRequest = JsonObjectRequest(
        Request.Method.DELETE,
        url,
        null,
        { response ->
            successCallback(response)
        },
        { error ->
            errorCallback(error)
        }
    )
    requestQueue?.add(jsonRequest)
}*/

/*fun patchVolleyRequest(id: Int, firstName: String, lastName: String, email: String, Tele: String, Dispo: Boolean, description: String, password: String, token: String, successCallback: (response:JSONObject) -> Unit, errorCallback: (error:VolleyError) -> Unit)
{
    val url = "https://backend-service-3kjf.onrender.com/rescuers/$id"
    requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
    val requestBody = JSONObject().apply {
        put("firstname", firstName)
        put("lastname", lastName)
        put("email", email)
        put("telephone", Tele)
        put("disponibility", Dispo)
        put("description", description)
        put("password", password)
        put("tokenFirebase", token)
    }

    val jsonRequest = JsonObjectRequest(
        Request.Method.PATCH,
        url,
        requestBody,
        { response ->
            successCallback(response)
        },
        { error ->
            errorCallback(error)
        }
    )

    requestQueue?.add(jsonRequest)
}*/

/*fun patchRequest(url: String, requestBody: String): String {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "PATCH"
    connection.doOutput = true

    val postData = requestBody.toByteArray(Charsets.UTF_8)
    connection.setRequestProperty("Content-Length", postData.size.toString())
    DataOutputStream(connection.outputStream).use { outputStream ->
        outputStream.write(postData)
    }

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
}*/

/*fun getAllUsers(): String {
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
}*/



//ChatGPT's getUserByEmail
/*fun getUserByEmail(usersJson: String, email: String): Profile? {
    val usersArray = JSONArray(usersJson)

    for (i in 0 until usersArray.length()) {
        val userObj = usersArray.getJSONObject(i)
        val userEmail = userObj.getString("email")

        if (userEmail == email) {
            val id = userObj.getInt("id")
            val firstName = userObj.getString("firstName")
            val lastName = userObj.getString("lastName")

            return Profile(id, email, firstName, lastName /* and other fields */)
        }
    }

    return null
}*/


//ChatGPT's json extractProfile
data class Profile(val id: Int, val firstName: String, val lastName: String, val email: String, val telephone: String, val disponibility : Boolean, val token : String)
/*fun extractProfileData(response: String): Profile {
    val jsonResponse = JSONObject(response)

    val id = jsonResponse.getString("id")
    val firstName = jsonResponse.getString("firstname")
    val lastName = jsonResponse.getString("lastname")
    val email = jsonResponse.getString("email")

    return Profile(id, firstName, lastName, email)
}*/



class ProfileActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        fun patchVolleyRequest(id: Int, firstName: String, lastName: String, email: String, Tele: String, Dispo: Boolean, description: String, password: String, token: String, successCallback: (response:JSONObject) -> Unit, errorCallback: (error:VolleyError) -> Unit)
        {
            val url = "https://backend-service-3kjf.onrender.com/rescuers/$id"
            requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
            val requestBody = JSONObject().apply {
                put("firstname", firstName)
                put("lastname", lastName)
                put("email", email)
                put("telephone", Tele)
                put("disponibility", Dispo)
                put("description", description)
                put("password", password)
                put("tokenFirebase", token)
            }

            val jsonRequest = object : JsonObjectRequest(
                Request.Method.PATCH,
                url,
                requestBody,
                { response ->
                    successCallback(response)
                },
                { error ->
                    errorCallback(error)
                }

            )
            {
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

        fun patchVolleyRequestWithoutmail(id: Int, firstName: String, lastName: String, Tele: String, Dispo: Boolean, description: String, password: String, token: String, successCallback: (response:JSONObject) -> Unit, errorCallback: (error:VolleyError) -> Unit)
        {
            val url = "https://backend-service-3kjf.onrender.com/rescuers/$id"
            requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
            val requestBody = JSONObject().apply {
                put("firstname", firstName)
                put("lastname", lastName)
                put("telephone", Tele)
                put("disponibility", Dispo)
                put("description", description)
                put("password", password)
                put("tokenFirebase", token)
            }

            val jsonRequest = object : JsonObjectRequest(
                Request.Method.PATCH,
                url,
                requestBody,
                { response ->
                    successCallback(response)
                },
                { error ->
                    errorCallback(error)
                }

            )
            {
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

        fun deleteVolleyRequest(id : Int, token : String, successCallback: (response:JSONObject) -> Unit, errorCallback: (error:VolleyError) -> Unit) {
            val url = "https://backend-service-3kjf.onrender.com/rescuers/"+id
            requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
            val jsonRequest = object : JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                { response ->
                    successCallback(response)
                },
                { error ->
                    errorCallback(error)
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

        fun getRescuerByMailList(email: String, callback : (Profile) -> Unit) {
            val url = "https://backend-service-3kjf.onrender.com/rescuers"
            requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
            var id = -1
            var firstname = "John"
            var lastname = "Doe"
            var email_returned = "JohnDoe@mail.fr"
            var telephone = "060708"
            var disponibility = false
            var token = "-1"

            val jsonOR = JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // Rien besoin d'envoyer
                { response ->
                    val jsonArray = response.getJSONArray("details")
                    for (i in 0 until jsonArray.length()) {
                        val rescuer = jsonArray.getJSONObject(i)
                        if(rescuer.getString("email") == email)
                        {
                            println("Sauveteur trouvé")
                            id = rescuer.getInt("id")
                            firstname = rescuer.getString("firstname")
                            lastname = rescuer.getString("lastname")
                            email_returned = rescuer.getString("email")
                            telephone = rescuer.getString("telephone")
                            disponibility = rescuer.getBoolean("disponibility")
                            token = ""//rescuer.getString("tokenFirebase")

                            println(id)
                            println(lastname)
                            println(email_returned)

                            callback(Profile(id,firstname,lastname,email_returned,telephone,disponibility,token))
                        }
                    }
                },
                { error ->
                    //Toast.makeText(applicationContext, "Erreur de la chargement BDD", Toast.LENGTH_LONG).show()
                    println("error BDD not reached or something")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            )
            requestQueue.add(jsonOR)
        }


        val usermail = intent.getStringExtra("usermail")
        val userpassword = intent.getStringExtra("userpassword")

        /*var profile = Profile(-1,"John","Doe","JohnDoe@mail.com","0769997558",true, "")*/
        if (usermail != null) {
            getRescuerByMailList(usermail) { profile ->
                val title = findViewById<TextView>(R.id.SSTProfile_Title)
                val Telinput = findViewById<EditText>(R.id.SSTProfile_Tel)
                val Passwordinput = findViewById<EditText>(R.id.SSTProfile_Password)
                val PasswordConfirminput = findViewById<EditText>(R.id.SSTProfile_Password_confirm)
                val firstNameinput = findViewById<EditText>(R.id.SSTProfile_firstname)
                val lastNameinput = findViewById<EditText>(R.id.SSTProfile_lastname)
                val Emailinput = findViewById<EditText>(R.id.SSTProfile_email)
                val DisponibilitySwitch = findViewById<Switch>(R.id.switch_dispo)
                val SMSswitch = findViewById<Switch>(R.id.Switch_SMS)
                val NotificationSwitch = findViewById<Switch>(R.id.Switch_Notif)

                title.setText(profile.firstName + " " + profile.lastName) //Firstname
                firstNameinput.setText(profile.firstName)
                lastNameinput.setText(profile.lastName)
                Emailinput.setText(profile.email)
                Telinput.setText(profile.telephone)

                if(userpassword != null)
                {
                    Passwordinput.setText(userpassword)
                    PasswordConfirminput.setText(userpassword)
                }

                if(profile.disponibility)
                {
                    DisponibilitySwitch.setChecked(true)
                }
                else
                {
                    DisponibilitySwitch.setChecked(false)
                }

                //Remplir les valeurs par défault avec celle de la base de données.

                val Confirmation_button = findViewById<Button>(R.id.button_confirmation)

                /*FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TOKEN", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                })*/

                Confirmation_button.setOnClickListener {

                    var PasswordChange = false
                    var FirstNameChange = false
                    var LastNameChange = false
                    var TelephoneChange = false
                    var EmailChange = false
                    var DispoChange = false
                    var SMSChange = false
                    var NotifChange = false

                    if (Passwordinput.text == PasswordConfirminput.text) {
                        PasswordChange = true
                    }

                    if (firstNameinput.text.toString() != profile.firstName) {
                        FirstNameChange = true
                    }

                    if (lastNameinput.text.toString() != profile.lastName) {
                        LastNameChange = true
                    }

                    if (Emailinput.text.toString() != profile.email) {
                        EmailChange = true
                    }

                    //Pareil mais avec le numéro de téléphone
                    if (Telinput.text.toString() != profile.telephone)
                    {
                        TelephoneChange = true
                    }

                    //Pareil avec les préférences
                    if (((SMSswitch.isChecked()) and false) or ((!SMSswitch.isChecked()) and true))
                    {
                        SMSChange = true
                    }
                    if (((NotificationSwitch.isChecked()) and false) or ((!NotificationSwitch.isChecked()) and true))
                    {
                        NotifChange = true
                    }

                    //Pareil avec la disponibilité
                    if (((DisponibilitySwitch.isChecked()) and !profile.disponibility) or ((!DisponibilitySwitch.isChecked()) and profile.disponibility))
                    {
                        DispoChange = true
                    }

                    //Gestion mdp spéciaux

                    if (((Passwordinput.text.toString() == "") and (PasswordConfirminput.text.toString() != ""))
                        or ((Passwordinput.text.toString() != "") and (PasswordConfirminput.text.toString() == "")))
                    {
                        //Champs des mots de passe incomplet == Non envoie du nouveau mot de passe
                        val message_alerte = findViewById<TextView>(R.id.Alerte)
                        message_alerte.text = "Veuillez saisir le même mot de passe"
                    }
                    else if ((firstNameinput.text.toString() == "") or (lastNameinput.text.toString() == "") or (Emailinput.text.toString() == "") or (Telinput.text.toString() == ""))
                    {
                        val message_alerte = findViewById<TextView>(R.id.Alerte)
                        message_alerte.text = "Champs non complétés"
                    }
                    else if ((Passwordinput.text.toString() == "") or (PasswordConfirminput.text.toString() == ""))
                    {
                        val message_alerte = findViewById<TextView>(R.id.Alerte)
                        message_alerte.text = "Champs non complétés"
                    }
                    else
                    {
                        val firstname = firstNameinput.text.toString()
                        val lastname = lastNameinput.text.toString()
                        val password = Passwordinput.text.toString()
                        val email = Emailinput.text.toString()
                        val telephone = Telinput.text.toString()
                        val description = "Patch"
                        val token : String? = intent.getStringExtra("Token")

                        //Si vous lisez ceci, sachez que j'ai plusieurs Teraoctets de ************ sur le

                        if (token != null) {
                            if(EmailChange)
                            patchVolleyRequest(profile.id,firstname,lastname,email,telephone,DisponibilitySwitch.isChecked(), description, password, token,
                                successCallback = { response ->
                                    println("Test Patch")
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                },
                                errorCallback = { error ->
                                    val errorMessage = error.message ?: "Erreur dans la modification du compte"
                                    println("Probleme Patch")
                                    Toast.makeText(applicationContext, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                                })
                            else
                            {
                                patchVolleyRequestWithoutmail(profile.id,firstname,lastname,telephone,DisponibilitySwitch.isChecked(), description, password, token,
                                    successCallback = { response ->
                                        println("Test Patch")
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                    },
                                    errorCallback = { error ->
                                        val errorMessage = error.message ?: "Erreur dans la modification du compte"
                                        println("Probleme Patch")
                                        Toast.makeText(applicationContext, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                                    })
                            }
                        }

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }


                }

                val bouton_suppression = findViewById<Button>(R.id.button_supprimer)

                bouton_suppression.setOnClickListener {
                    val token = intent.getStringExtra("Token")
                    if (token != null) {
                        deleteVolleyRequest(profile.id, token,
                            successCallback = { response ->
                                val message = response.getString("Compte supprimé")
                                Toast.makeText(applicationContext, "Success: $message", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            },
                            errorCallback = { error ->
                                val errorMessage = error.message ?: "Erreur dans la suppression du compte"
                                Toast.makeText(applicationContext, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                            })
                    }
                }
            }
        }

        // Send firebase token to backend
        //val intent = Intent(this, profileActivity::class.java)
        //intent.putExtra("userId", profile.id)

            /*// Get new FCM registration token
            var token = task.result // le token a envoyer
            // A envoyer
            var siteJunior = ""
            val urltokenFirebase = siteJunior+"/rescuers/"+profile.id.toString()
            sendPatchRequest(urltokenFirebase,"{  \"tokenFirebase\": \"$token\" }")*/

        //Récupérer les données
    }
}