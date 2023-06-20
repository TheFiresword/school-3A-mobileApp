package insa.etudiant.sstou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val myButton = findViewById<Button>(R.id.helpButton)    //Bouton "demander de l'aide"
        val myhelpbutton = findViewById<ImageView>(R.id.HelpButton)
        val myparametersbutton = findViewById<Button>(R.id.Params_button)

        /*myButton.setOnClickListener {                                               //Quand on clique sur le bouton AIDE
            val intent = Intent(this, EnterLocationActivity::class.java)       //Se diriger vers l'activité "Entrer salle"
            startActivity(intent)
        }*/

        myhelpbutton.setOnClickListener {                                           //Quand on clique sur le bonton rouge
            val intent = Intent(this, EnterLocationActivity::class.java)
            startActivity(intent)                                                          //se diriger vers l'activité EnterLocation
        }

        myparametersbutton.setOnClickListener {
            val intent = Intent(this, parameters::class.java)
            startActivity(intent)
        }

    }
}