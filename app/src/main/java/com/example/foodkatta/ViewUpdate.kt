package com.example.foodkatta

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap


class ViewUpdate : AppCompatActivity() {
    companion object{
        const val NAME="NAME"
        const val MOBILE="contact"
        const val PAID="paid"
        const val BALANCE="balance"
        const val GROUP="group"
        const val ID="id"
    }
    lateinit var IdNo:String

    lateinit var name:EditText
    lateinit var mobile:EditText
    lateinit var group:EditText
    lateinit var paid:EditText
    lateinit var balance:EditText

    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_update)
         IdNo= intent.getStringExtra(ID).toString()
        val nameV= intent.getStringExtra(NAME)
        val mobileV= intent.getStringExtra(MOBILE)
        val paidV= intent.getStringExtra(PAID)
        val balanceV= intent.getStringExtra(BALANCE)
        val groupV= intent.getStringExtra(GROUP)



         name = findViewById<EditText>(R.id.fullname)
         mobile = findViewById<EditText>(R.id.mobileno)
         balance = findViewById<EditText>(R.id.balanceamount)
         group = findViewById<EditText>(R.id.groupname)
         paid = findViewById<EditText>(R.id.paidamount)

        name.setText(nameV)
        mobile.setText(mobileV)
        balance.setText(balanceV)
        group.setText(groupV)
        paid.setText(paidV)
        val updateButton: Button = findViewById(R.id.updatebutton)

        updateButton.setOnClickListener {
            if (checkForInternet(this)) {

                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
                memUdate()
            }
            else {
                Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
            }
        }

        val deleteButton: Button = findViewById(R.id.deletemember)

        deleteButton.setOnClickListener {
            if (checkForInternet(this)) {

                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
                memDel()
            }
            else {
                Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun backToHome(view:View){
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
        finish()
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

    fun memUdate() {


        val url = "https://food-member-list-nodejs-api.herokuapp.com/api/v1/members/$IdNo"
        //RequestQueue initialized
        val  mRequestQueue = Volley.newRequestQueue(this)

        //String Request initialized
        val   mStringRequest = object : StringRequest(Request.Method.PATCH, url, Response.Listener { response ->
            Toast.makeText(this, "The Member has been updated", Toast.LENGTH_SHORT).show()


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
                params2.put("name", name.text.toString())
                params2.put("contact", mobile.text.toString())
                params2.put("paid", paid.text.toString())
                params2.put("balance", balance.text.toString())
                params2.put("group", group.text.toString())
                return JSONObject(params2 as Map<*, *>).toString().toByteArray()
            }

        }
        mRequestQueue!!.add(mStringRequest!!)
    }

    fun memDel() {


        val url = "https://food-member-list-nodejs-api.herokuapp.com/api/v1/members/$IdNo"
        //RequestQueue initialized
        val  mRequestQueue = Volley.newRequestQueue(this)

        //String Request initialized
        val   mStringRequest = object : StringRequest(Request.Method.DELETE, url, Response.Listener { response ->
            Toast.makeText(this, "The Member has been deleted", Toast.LENGTH_SHORT).show()


        }, Response.ErrorListener { error ->
            Log.i("This is the error", "Error :" + error.toString())
            Toast.makeText( this,"Something went wrong", Toast.LENGTH_SHORT).show()
        }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }




        }
        mRequestQueue!!.add(mStringRequest!!)
    }
}











