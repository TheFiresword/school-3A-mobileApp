package com.example.sstla
import android.content.Intent
import android.widget.EditText


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class EnterLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_location)

        val locationEdit = findViewById<EditText>(R.id.inputLocation)
        val confirmLoc = findViewById<Button>(R.id.confirmLocationButton)    //Bouton "confirmer"

        confirmLoc.setOnClickListener {              //Quand on clique sur le bouton confirmer
            val confirmedLoc = locationEdit.text.toString()               //On enregistre le texte
            val intent = Intent(this, WaitingForHelp::class.java)       //Se diriger vers l'activité "Suivante"
            intent.putExtra("location", confirmedLoc)
            startActivity(intent)
        }
    }
}