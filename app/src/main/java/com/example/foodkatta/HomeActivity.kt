package com.example.foodkatta
import android.accounts.NetworkErrorException
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.foodkatta.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class HomeActivity : AppCompatActivity() {
    lateinit var pDialog: ProgressDialog
private lateinit var binding:ActivityMainBinding
    lateinit var valueI:String
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home1)
    supportActionBar?.hide()


    val checkButton: Button = findViewById(R.id.search_button)

    checkButton.setOnClickListener {
        if (checkForInternet(this)) {
            val editText= findViewById<EditText>(R.id.editTextPersonName)
            valueI=editText.text.toString()
            val isNullOrEmpty = valueI.isNullOrEmpty()
            if(!isNullOrEmpty){
            val url="https://food-member-list-nodejs-api.herokuapp.com/api/v1/members/${valueI}"
                AsyncTaskHandler().execute(url)

            }
            else{
                val url="https://food-member-list-nodejs-api.herokuapp.com/api/v1/members/fdfddad"
            AsyncTaskHandler().execute(url)
            }


        }
        else {
            Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }



//        val connectonManager:ConnectivityManager=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork:NetworkInfo?= connectonManager.activeNetworkInfo
//        val isConnected:Boolean = activeNetwork?.isConnectedOrConnecting==true
//
//        if (isConnected){
//            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
//        }
//            else{
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
//            }




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




    inner class AsyncTaskHandler:AsyncTask<String,String,String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            pDialog=ProgressDialog(this@HomeActivity)

                pDialog.setMessage("please Wait")
//                    pDialog.setCancelMessage(false)
                    pDialog.show()

        }

        override fun doInBackground(vararg url: String?): String {
            //TODO("Not yet implemented")



            val res:String
            val connection= URL(url[0]).openConnection() as HttpURLConnection





                connection.setConnectTimeout(30000)
            try {
                res = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
                connection.connect()
            }
            finally {
                connection.disconnect()
            }

            return res
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(pDialog.isShowing){
                pDialog.dismiss()

                jsonResult(result)

            }
        }
        private  fun jsonResult(jsonString:String?){
        val jsonArray= JSONArray(jsonString)
        val list = ArrayList<MyData>()
        var i=0
        while(i<jsonArray.length()){
            val jsonObject= jsonArray.getJSONObject(i)
            list.add(
                MyData(
                    jsonObject.getString("_id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("nameId"),
                    jsonObject.getString("contact"),
                    jsonObject.getString("balance"),
                    jsonObject.getString("paid"),
                    jsonObject.getString("group")
            )

            )
            i++;
        }


            var  listView = findViewById<ListView>(R.id.listView)

            val adapter= ListAdapter(this@HomeActivity,list)

//            Log.d("here it is",list.toString())
            listView.adapter=adapter

            listView.setOnItemClickListener { parent, view, position, id ->
                val element = adapter.getItem(position) // The item that was clicked
                val jsonObject= jsonArray.getJSONObject(element as Int)

                viewOrUpdate(jsonObject)
            }

    }
         }



    fun createMember(view: View) {
        val intent = Intent(this,AddNewMember::class.java)
        startActivity(intent)
        finish()
    }

    fun viewOrUpdate(jsonObject: JSONObject) {




        val data = ArrayList<Memberdata>()
        data.add(Memberdata(
            jsonObject.getString("_id"),
            jsonObject.getString("name"),
            jsonObject.getString("nameId"),
            jsonObject.getString("contact"),
            jsonObject.getString("balance"),
            jsonObject.getString("paid"),
            jsonObject.getString("group")
        ))
      var id=  jsonObject.getString("_id").toString()
      var name=  jsonObject.getString("name").toString()
      var contact=  jsonObject.getString("contact").toString()
      var balance=  jsonObject.getString("balance").toString()
        var paid=  jsonObject.getString("paid").toString()
        var group=  jsonObject.getString("group").toString()
//        Log.d("this is pos Listview",data[0].toString())
        val intent = Intent(this,ViewUpdate::class.java)
        intent.putExtra("id",id)
        intent.putExtra("NAME",name)
        intent.putExtra("contact",contact)
        intent.putExtra("balance",balance)
        intent.putExtra("paid",paid)
        intent.putExtra("group",group)
        startActivity(intent)
//        finish()

    }



//    private fun fetchMember() {











//
//        val queue = Volley.newRequestQueue(this)
//        val url = "http://10.0.2.2:5000/api/v1/members/sauravjadhav"
////        val url = "https://www.boredapi.com/api/activity"
//
//        val jsonObjectRequest =JsonArrayRequest(
//            Request.Method.GET, url,null,
//            { response ->
//
//                for (i in 0 until response.length()) {
//                    val item = response.getJSONObject(i)
//                    val editLinearLayout = findViewById<LinearLayout>(R.id.editTextLinearLayout)
//
//
//                    val editText = TextView(this)
//                    val params = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                    )
//                    params.setMargins(0, 25, 0, 0)
//                    editText.text = "${item}"
//                    editText.layoutParams = LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT)
//                    editText.setPadding(20, 5, 20, 20)
//                    editText.layoutParams = params
//                    editText.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_view_border))
//                    editLinearLayout?.addView(editText)
////                    Log.d("this is object in array", item.toString())
//                }
//
//
//
//
//
//
//
//
//
//
//
//
//                Log.d("it is working", response.toString())
//            },
//            {
//                Log.d("it is not working",url)
//
//            })
//
//// Add the request to the RequestQueue.
//        queue.add(jsonObjectRequest)
// }



 }








