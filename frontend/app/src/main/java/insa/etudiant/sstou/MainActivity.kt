package insa.etudiant.sstou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.ImageView
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myhelpbutton = findViewById<ImageView>(R.id.HelpButton)
        val myloginbutton = findViewById<Button>(R.id.Params_button)
        val requestQueue = Volley.newRequestQueue(this)


        myhelpbutton.setOnClickListener {                                           //Quand on clique sur le bonton rouge
            val intent = Intent(this, EnterLocationActivity::class.java)
            startActivity(intent)                                                          //se diriger vers l'activité EnterLocation
        }

        myloginbutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java) //Par défault : Page de login
            startActivity(intent)
        }
    }
}