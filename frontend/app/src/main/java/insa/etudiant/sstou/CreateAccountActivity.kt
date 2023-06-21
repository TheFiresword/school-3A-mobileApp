package insa.etudiant.sstou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject


//data class Profile(val id: String, val firstName: String, val lastName: String, val email: String)

class CreateAccountActivity : AppCompatActivity() {
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
            val warningText =
            //Erreurs dans le remplissage des champs :
            if (confUsername.isEmpty() or confTelnum.isEmpty() or confMdp.isEmpty() or confConfMdp.isEmpty()) {
                warningText.setVisibility(View.VISIBLE)
                warningText.setText("Veuillez remplir tous les champs !")
            }
            else if (confMdp != confConfMdp) {
                warningText.setVisibility(View.VISIBLE)
                warningText.setText("Les mots de passe ne sont pas les mêmes !")
            }
            //Confirmer la création de compte
            else {
                //Insérer requête SQL ici
                val intent = Intent(this, RescuerListActivity::class.java)
                startActivity(intent)
            }
        }
    }
}