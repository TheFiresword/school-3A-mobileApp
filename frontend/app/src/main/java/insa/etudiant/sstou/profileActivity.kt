package insa.etudiant.sstou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class profileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userTitle = intent.getStringExtra("userEmail")
        val Title = findViewById<TextView>(R.id.SSTProfile_Title)

        Title.setText(userTitle)

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