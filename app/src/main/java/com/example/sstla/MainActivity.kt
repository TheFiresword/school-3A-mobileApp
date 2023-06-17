package com.example.sstla

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myButton = findViewById<Button>(R.id.helpButton)    //Bouton "demander de l'aide"

        myButton.setOnClickListener {                                               //Quand on clique sur le bouton AIDE
            val intent = Intent(this, EnterLocationActivity::class.java)       //Se diriger vers l'activité "Entrer salle"
            startActivity(intent)
        }

    }
}