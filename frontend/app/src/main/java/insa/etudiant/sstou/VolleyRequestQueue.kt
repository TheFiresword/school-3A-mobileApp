package insa.etudiant.sstou

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyRequestQueue private constructor(context: Context) {
    private val requestQueue: RequestQueue by lazy {
        // Create the RequestQueue using the application context
        Volley.newRequestQueue(context.applicationContext)
    }

    companion object {
        @Volatile
        private var INSTANCE: VolleyRequestQueue? = null

        fun getInstance(context: Context): VolleyRequestQueue =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleyRequestQueue(context).also { INSTANCE = it }
            }
    }

    fun getOurRequestQueue(): RequestQueue {
        return requestQueue
    }

}
