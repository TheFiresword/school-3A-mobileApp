package com.example.sstla

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myhelpbutton = findViewById<ImageView>(R.id.HelpButton)
        val myparametersbutton = findViewById<Button>(R.id.Params_button)

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