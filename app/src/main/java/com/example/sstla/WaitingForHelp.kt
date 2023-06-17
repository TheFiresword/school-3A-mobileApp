package com.example.sstla

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class WaitingForHelp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_for_help)

        val location = intent.getStringExtra("location")
        val waitingSentence1 = getString(R.string.waitingSentence1)
        val waitingSentence2 = getString(R.string.waitingSentence2)

        val waitingText = findViewById<TextView>(R.id.waitingText)
        waitingText.text = waitingSentence1 + location + waitingSentence2


    }
}