package insa.etudiant.sstou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject

//ChatGPT's GET request
import java.io.BufferedReader
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
        val profile = extractProfileData(response)


        Title.setText(profile.firstName)

        //Remplir les valeurs par défault avec celle de la base de données.

        val Confirmation_button = findViewById<Button>(R.id.button_confirmation)

        Confirmation_button.setOnClickListener {

            //Insérer Comparaison ici
            //Insérer requête SQL ici

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Récupérer les données
    }
}