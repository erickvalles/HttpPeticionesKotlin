package com.example.erick.httppeticiones

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url = "https://randomuser.me/api/?results=5"

        val cache = DiskBasedCache(cacheDir,1024*1024)
        val network = BasicNetwork(HurlStack())

        val requestQueue = RequestQueue(cache,network).apply {
            start()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,null,
            Response.Listener { response ->
                val personas = ArrayList<Persona>();
                val respuesta = JSONObject(response.toString())
                val personasJSON = respuesta.getJSONArray("results")

                for (i in 0..personasJSON.length()-1){
                    val objetoJSON = personasJSON.getJSONObject(i)
                    val nombre = objetoJSON.getJSONObject("name")
                    val foto = objetoJSON.getJSONObject("picture")
                    personas.add(Persona(nombre.getString("first"),nombre.getString("last"),foto.getString("large")))
                }

                Log.d("tamaño","${personas.size}");

            },
            Response.ErrorListener { error ->
                Log.wtf("error",error.localizedMessage)
            }
        )

        requestQueue.add(jsonObjectRequest)

    }
}
