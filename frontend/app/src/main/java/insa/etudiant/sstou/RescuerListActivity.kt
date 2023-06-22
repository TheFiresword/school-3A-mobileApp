package insa.etudiant.sstou

//import android.app.DownloadManager.Request
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.VolleyError


import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import org.json.JSONObject

class RescuerListActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescuer_list)

        fun getRescuerList(callback: (ArrayList<String>, ArrayList<String>) -> Unit) {
            val url = "https://backend-service-3kjf.onrender.com/rescuers"
            requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
            var names = arrayListOf("Secouriste 1", "Secouriste 2", "Secouriste 3", "Secouriste 4") //Default List
            var emails = arrayListOf<String>()

            val jsonOR = JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // Rien besoin d'envoyer
                { response ->
                    Toast.makeText(applicationContext, "Réussite chargement BDD", Toast.LENGTH_LONG).show()
                    val jsonArray = response.getJSONArray("details")
                    names.clear()
                    for (i in 0 until jsonArray.length()) {
                        val rescuer = jsonArray.getJSONObject(i)
                        val rescuerName = rescuer.getString("lastname") + "  " + rescuer.getString("firstname")
                        val rescuerEmail = rescuer.getString("email")
                        names.add(rescuerName)
                        emails.add(rescuerEmail)
                    }
                    callback(names, emails)
                },
                { error ->
                    Toast.makeText(applicationContext, "Erreur du chargement BDD", Toast.LENGTH_LONG).show()
                }
            )
            requestQueue.add(jsonOR)
        }

        class OperationAdapter(private val operations: ArrayList<String>, private val emails: ArrayList<String>) : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {
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
                    val context = itemView.context
                    itemView.setOnClickListener {
                        val clickedPosition = adapterPosition
                        val clickedRescuerEmail = emails[clickedPosition]
                        //val clickedRescuer = operations[adapterPosition]
                        val intent = Intent(context, ProfileActivity::class.java)
                        intent.putExtra("usermail", clickedRescuerEmail)
                        intent.putExtra("Token", intent.getStringExtra("Token"))
                        startActivity(intent)
                    }
                }
            }
        }

        getRescuerList { operations, emails ->
            val recyclerView = findViewById<RecyclerView>(R.id.recycler)
            val adapter = OperationAdapter(operations, emails)
            recyclerView.adapter = adapter

            val addRescuerButton = findViewById<ImageButton>(R.id.imageButton)
            addRescuerButton.setOnClickListener {
                val intent = Intent(this, CreateAccountActivity::class.java)
                startActivity(intent)

            }
        }

    }
}

/*
fun getRescuerList(): ArrayList<String> {
            val url = "https://backend-service-3kjf.onrender.com/rescuers"
            requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
            var names = arrayListOf("Secouriste 1", "Secouriste 2", "Secouriste 3", "Secouriste 4") //Default List

            val jsonOR = JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // Rien besoin d'envoyer
                { response ->
                    Toast.makeText(applicationContext, "Réussite chargement BDD", Toast.LENGTH_LONG).show()
                    val jsonArray = response.getJSONArray("rescuers")
                    println(names)
                    names.clear()
                    for (i in 0 until jsonArray.length()) {
                        val rescuerName = jsonArray.getJSONObject(i)
                        val rescuer = rescuerName.getString("lastname")
                        names.add(rescuer)

                    }
                },
                { error ->
                    Toast.makeText(applicationContext, "Erreur du chargement BDD", Toast.LENGTH_LONG).show()
                }
            )
            requestQueue?.add(jsonOR)
            return names
        }
 */
/*
package insa.etudiant.sstou

//import android.app.DownloadManager.Request
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.VolleyError


import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import org.json.JSONObject

class RescuerListActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescuer_list)

        fun getRescuerList(callback: (ArrayList<String>) -> Unit) {
            val url = "https://backend-service-3kjf.onrender.com/rescuers"
            requestQueue = VolleyRequestQueue.getInstance(this).getOurRequestQueue()
            var names = arrayListOf("Secouriste 1", "Secouriste 2", "Secouriste 3", "Secouriste 4") //Default List

            val jsonOR = JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // Rien besoin d'envoyer
                { response ->
                    Toast.makeText(applicationContext, "Réussite chargement BDD", Toast.LENGTH_LONG).show()
                    val jsonArray = response.getJSONArray("rescuers")
                    println(names)
                    names.clear()
                    for (i in 0 until jsonArray.length()) {
                        val rescuerName = jsonArray.getJSONObject(i)
                        val rescuer = rescuerName.getString("lastname")
                        names.add(rescuer)
                    }
                    callback(names)
                },
                { error ->
                    Toast.makeText(applicationContext, "Erreur du chargement BDD", Toast.LENGTH_LONG).show()
                }
            )
            requestQueue?.add(jsonOR)
        }


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
                    val context = itemView.context
                    itemView.setOnClickListener {
                        val clickedRescuer = operations[adapterPosition]
                        val intent = Intent(context, ProfileActivity::class.java)
                        intent.putExtra("userTitle", clickedRescuer)
                        startActivity(intent)
                    }
                }
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        val adapter = OperationAdapter()
        recyclerView.adapter = adapter

        val addRescuerButton = findViewById<ImageButton>(R.id.imageButton)
        addRescuerButton.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }
}
 */