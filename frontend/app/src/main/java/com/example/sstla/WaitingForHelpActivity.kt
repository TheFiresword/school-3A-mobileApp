package com.example.sstla

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.Manifest
import android.content.pm.PackageManager
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast

class WaitingForHelpActivity : AppCompatActivity() {
    val phoneNumber = getString(R.string.baseNumber)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_for_help)

        val location = intent.getStringExtra("location")
        val waitingSentence1 = getString(R.string.waitingSentence1)
        val waitingSentence2 = getString(R.string.waitingSentence2)

        val waitingText = findViewById<TextView>(R.id.waitingText)
        waitingText.text = waitingSentence1 + location + waitingSentence2

        val retButton = findViewById<Button>(R.id.button2)     //Bouton de retour vers la page d'accueil.
        retButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    /*

    private fun initiateMessage() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                PERMISSION_REQUEST_SEND_SMS
            )
        } else {
            sendMessage()
            }
    }

    private fun sendMessage() {
        val smsManager = SmsManager.getDefault()
        val location = intent.getStringExtra("location")
        val message = "[SST][URGENT] Votre aide a été demandée en $location !"

        try {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "Message sent.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send message.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_SEND_SMS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage()
                }
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_SEND_SMS = 1
    }

     */
}
