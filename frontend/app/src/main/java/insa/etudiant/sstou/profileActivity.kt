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
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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
        val userId = intent.getStringExtra("userId")
        val Title = findViewById<TextView>(R.id.SSTProfile_Title)


        val response = sendGetRequest("http://localhost:3000/rescuers/$userId")
        //Test si message est Succés et non échec
        val profile = extractProfileData(response)

        val Telinput = findViewById<EditText>(R.id.SSTProfile_Tel)
        val Passwordinput = findViewById<EditText>(R.id.SSTProfile_Password)
        val PasswordConfirminput = findViewById<EditText>(R.id.SSTProfile_Password_confirm)
        val firstNameinput = findViewById<EditText>(R.id.SSTProfile_firstname)
        val lastNameinput = findViewById<EditText>(R.id.SSTProfile_lastname)
        val Emailinput = findViewById<EditText>(R.id.SSTProfile_email)

        firstNameinput.setText(profile.firstName)
        lastNameinput.setText(profile.lastName)
        Emailinput.setText(profile.email)
        Title.setText(profile.firstName)

        //Remplir les valeurs par défault avec celle de la base de données.

        val Confirmation_button = findViewById<Button>(R.id.button_confirmation)

        Confirmation_button.setOnClickListener {

            var PasswordChange = false
            var FirstNameChange = false
            var LastNameChange = false
            var TelephoneChange = false
            var EmailChange = false

            if(Passwordinput.text == PasswordConfirminput.text)
            {
                PasswordChange = true
            }

            if(firstNameinput.text.toString() != profile.firstName)
            {
                FirstNameChange = true
            }

            if(lastNameinput.text.toString() != profile.lastName)
            {
                LastNameChange = true
            }

            if(Emailinput.text.toString() != profile.email)
            {
                EmailChange = true
            }

            //Pareil mais avec le numéro de téléphone

            //Pareil avec les préférences

            //Pareil avec la disponibilité

            //Gestion mdp spéciaux

            if(((Passwordinput.text.toString() == "") and (PasswordConfirminput.text.toString() != ""))
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
                patchRequest("http://localhost:3000/rescuers/"+profile.id,
                    "{\"firstname\": \"$firstname\" , \"lastname\": \"$lastname\" , " +
                            "\"password\": \"$password\" , \"email\" : \"$email\"}"
                )

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }



        }

        val bouton_suppression = findViewById<Button>(R.id.button_supprimer)

        bouton_suppression.setOnClickListener {
            deleteRequest("http://localhost:3000/rescuers/"+profile.id)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Récupérer les données
    }
}