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
        val myparametersbutton = findViewById<Button>(R.id.Params_button)
        val requestQueue = Volley.newRequestQueue(this)


        myhelpbutton.setOnClickListener {                                           //Quand on clique sur le bonton rouge
            val intent = Intent(this, EnterLocationActivity::class.java)
            startActivity(intent)                                                          //se diriger vers l'activité EnterLocation
        }

        myparametersbutton.setOnClickListener {
            val intent = Intent(this, parameters::class.java)
            startActivity(intent)
        }

        val myparametersbutton2 = findViewById<Button>(R.id.buttontestnotif)

        myparametersbutton2.setOnClickListener {
            WaitingForHelpActivity.sendNotif("Aurore","dZAVyEPBRuOJQoNoLblJHQ:APA91bF4Sk0qKy_gkODgpLUxX15Mh_cl3Mtx8O-9r1Rm2-DWSubj-AR9y37x6c9heSfNLaLxxs3EyWFI_AsWK_n5wh_C9Vze39LDSvmh9Y7rf66yEixweqQGTayAWfTn9Js8WmfJ8nO_")
        }

    }
}