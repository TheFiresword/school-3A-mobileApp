package insa.etudiant.sstou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

//ChatGPT's POST
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader

fun sendPostRequest(url: String, requestBody: String): String {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "POST"
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





class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val Confirmation = findViewById<Button>(R.id.Connection_button)
        val MDP_forget = findViewById<Button>(R.id.MDP_forget) // Bouton Mot de passe oublié
        val ret_button = findViewById<Button>(R.id.ret_button_2) // Bouton Retour

        Confirmation.setOnClickListener {
            val Username_entry = findViewById<EditText>(R.id.Username_entry)
            val Password_entry = findViewById<EditText>(R.id.Password_entry)

            val username = Username_entry.text.toString()
            val password = Password_entry.text.toString()

            if((username == "Admin") or (username == "admin"))
            {
                //Admin -> redirige vers la liste des comptes (RescuerListActivity)
            }
            else
            {
                var response = sendPostRequest("http://localhost:3000/authentification/login", "{ \"email\": \"$username\", \"password\": \"$password\" }")
                val intent = Intent(this, profileActivity::class.java)
                intent.putExtra("userTitle", username)
                startActivity(intent) //Redirige vers profil utilisateur (profileActivity

            }
        }

        MDP_forget.setOnClickListener {
            val JustTrolling = findViewById<TextView>(R.id.Troll)
            JustTrolling.setVisibility(View.VISIBLE)
        }

        ret_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}