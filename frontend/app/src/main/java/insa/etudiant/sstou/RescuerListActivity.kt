package insa.etudiant.sstou

//import android.app.DownloadManager.Request
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.VolleyError


import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import insa.etudiant.sstou.profileActivity
import org.json.JSONObject

class RescuerListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescuer_list)

        fun getRescuerList(): ArrayList<String> {
            val url = "http://localhost:3000/rescuers"
            var operations = arrayListOf("Secouriste 1", "Secouriste 2", "Secouriste 3", "Secouriste 4") //Default List

            val jsonOR = JsonObjectRequest(
                Request.Method.GET, // Request method (GET)
                url, // URL of the API endpoint
                null, // Request body
                { response ->
                    Toast.makeText(applicationContext, "Réussite chargement BDD", Toast.LENGTH_LONG).show()
                    val jsonArray = response.getJSONArray("rescuers")
                    operations.clear()
                    for (i in 0 until jsonArray.length()) {
                        val rescuer = jsonArray.getString(i)
                        operations.add(rescuer)
                    }
                },
                { error ->
                    Toast.makeText(applicationContext, "Erreur de la chargement BDD", Toast.LENGTH_LONG).show()
                }
            )
            return operations
        }

        var operations = getRescuerList()

        class OperationAdapter : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = inflater.inflate(R.layout.item_of_rescuer_list, parent, false)
                return OperationViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
                val operation = operations[position]
                holder.bind(operation)
            }

            override fun getItemCount() = operations.size

            inner class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private val itemTv = itemView.findViewById<TextView>(R.id.item_tv)

                fun bind(operation: String) {
                    itemTv.text = operation
                    itemView.setOnClickListener {
                        val clickedRescuer = operations[adapterPosition]
                        val intent = Intent(itemView.context, profileActivity::class.java)
                        intent.putExtra("userTitle", clickedRescuer)
                        startActivity(intent)
                    }
                }
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        val adapter = OperationAdapter()
        recyclerView.adapter = adapter
    }
}
