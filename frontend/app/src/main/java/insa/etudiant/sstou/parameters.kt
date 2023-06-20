package insa.etudiant.sstou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class parameters : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameters)

        val Data_button = findViewById<Button>(R.id.BDD_button)
        val ret_button = findViewById<Button>(R.id.button_ret_1)

        Data_button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java) //Par défault : Page de login
            startActivity(intent)
        }

        ret_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}