package com.example.foodkatta

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class AddNewMember : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_member)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.title="Adding new member"
        val checkButton: Button = findViewById(R.id.create_button)

        checkButton.setOnClickListener {
            if (checkForInternet(this)) {

                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
                sendcall()
            }
            else {
                Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun checkForInternet(context: Context): Boolean {


        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            val network = connectivityManager.activeNetwork ?: return false


            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }



    fun backToHome(view: View){
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun sendcall() {
        val name = findViewById<EditText>(R.id.fname).text.toString()
        val contact = findViewById<EditText>(R.id.mono).text.toString()
        val paid = findViewById<EditText>(R.id.pb).text.toString()
        val balance = findViewById<EditText>(R.id.b).text.toString()
        val group = findViewById<EditText>(R.id.grp).text.toString()

        val url = "https://food-member-list-nodejs-api.herokuapp.com/api/v1"

        //RequestQueue initialized
        val  mRequestQueue = Volley.newRequestQueue(this)

        //String Request initialized
        val   mStringRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
        Toast.makeText(this, "The Member has added", Toast.LENGTH_SHORT).show()


        }, Response.ErrorListener { error ->
            Log.i("This is the error", "Error :" + error.toString())
        Toast.makeText( this,"Something went wrong", Toast.LENGTH_SHORT).show()
        }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val params2 = HashMap<String, String>()
                params2.put("name", name.toString())
                params2.put("contact", contact.toString())
                params2.put("paid", paid.toString())
                params2.put("balance", balance.toString())
                params2.put("group", group.toString())
                return JSONObject(params2 as Map<*, *>).toString().toByteArray()
            }

        }
        mRequestQueue!!.add(mStringRequest!!)
    }
}
