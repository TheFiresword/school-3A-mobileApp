package insa.etudiant.sstou

//ChatGPT's GET request

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import org.json.JSONObject
import org.json.JSONArray


import java.io.BufferedReader
import java.io.DataOutputStream
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

fun deleteRequest(url: String): String {
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
}

fun patchRequest(url: String, requestBody: String): String {
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

        //problème : il faut ici utiliser une requête permettant d'obtenir un rescuer par son mail,
        //ce qui nécessite une requête du côté du backend (laquelle n'existe pas encore)
        val response = sendGetRequest("http://localhost:3000/rescuers/$usermail")  //Rajouter un try catch ?
        val jsonResponse = JSONObject(response)
        val status = jsonResponse.getString("message")

        if(status == "Succès") {
            //Test si message est Succés et non échec
            val profile = extractProfileData(response)

            val Telinput = findViewById<EditText>(R.id.SSTProfile_Tel)
            val Passwordinput = findViewById<EditText>(R.id.SSTProfile_Password)
            val PasswordConfirminput = findViewById<EditText>(R.id.SSTProfile_Password_confirm)
            val firstNameinput = findViewById<EditText>(R.id.SSTProfile_firstname)
            val lastNameinput = findViewById<EditText>(R.id.SSTProfile_lastname)
            val Emailinput = findViewById<EditText>(R.id.SSTProfile_email)


            title.setText(profile?.firstName)
            firstNameinput.setText(profile.firstName)
            lastNameinput.setText(profile.lastName)
            Emailinput.setText(profile.email)

            //Remplir les valeurs par défault avec celle de la base de données.

            val Confirmation_button = findViewById<Button>(R.id.button_confirmation)

            Confirmation_button.setOnClickListener {

                var PasswordChange = false
                var FirstNameChange = false
                var LastNameChange = false
                var TelephoneChange = false
                var EmailChange = false

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

                //Pareil avec les préférences

                //Pareil avec la disponibilité

                //Gestion mdp spéciaux

                if (((Passwordinput.text.toString() == "") and (PasswordConfirminput.text.toString() != ""))
                    or ((Passwordinput.text.toString() != "") and (PasswordConfirminput.text.toString() == "")))
                {
                    //Champs des mots de passe incomplet == Non envoie du nouveau mot de passe
                }
                else
                {
                    val firstname = firstNameinput.text.toString()
                    val lastname = lastNameinput.text.toString()
                    val password = Passwordinput.text.toString()
                    val email = Emailinput.text.toString()

                    //Insérer requête SQL ici
                    patchRequest(
                        "http://localhost:3000/rescuers/" + profile.id,
                        "{\"firstname\": \"$firstname\" , \"lastname\": \"$lastname\" , " +
                                "\"password\": \"$password\" , \"email\" : \"$email\"}"
                    )

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }


            }

            val bouton_suppression = findViewById<Button>(R.id.button_supprimer)

            bouton_suppression.setOnClickListener {
                deleteRequest("http://localhost:3000/rescuers/" + profile.id)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        else
        {
            //Erreur : La requête GET à echouer
            val message_erreur = jsonResponse.getString("details")
            println(message_erreur)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}