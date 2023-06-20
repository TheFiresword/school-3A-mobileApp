package insa.etudiant.sstou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

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
                //Admin -> redirige vers la liste des comptes (Database)
            }
            else
            {
                //insérer vérification mdp ici.
                val intent = Intent(this, profileActivity::class.java)
                intent.putExtra("userTitle", username)
                startActivity(intent) //Redirige vers profil utilisateur

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