package com.example.weatherornot

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appTitle = findViewById<TextView>(R.id.appTitle)
        val editText = findViewById<EditText>(R.id.CityEditText)
        var city = editText.text
        var url = "https://api.openweathermap.org/data/2.5/weather?q=" + city +"&appid=8e6d3f022e686a9f72efbdc889b4b25b"
        run(url)


    }
    fun search(bt : View){
        val editText = findViewById<EditText>(R.id.CityEditText)
        var city = editText.text
        var url = "https://api.openweathermap.org/data/2.5/weather?q=" + city +"&appid=8e6d3f022e686a9f72efbdc889b4b25b"
        run(url)
    }

    fun run(url: String){
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread{
                    val jsonString = response.body()?.string()
                    var jsonObject = JSONObject(jsonString)

                    // Processing main json
                    var mainString = jsonObject.get("main").toString()
                    var mainJson = JSONObject(mainString)
                    var temp: Double = Math.round((mainJson.get("temp").toString().toDouble() - 273.15) * 10.0) / 10.0
                    var temp_min: Double = Math.round((mainJson.get("temp_min").toString().toDouble() - 273.15) * 10.0) / 10.0
                    var temp_max: Double = Math.round((mainJson.get("temp_max").toString().toDouble() - 273.15) * 10.0) / 10.0
                    var feels_like: Double = Math.round((mainJson.get("feels_like").toString().toDouble() - 273.15) * 10.0) / 10.0

                    var LowHigh = findViewById<TextView>(R.id.LowHigh)
                    LowHigh.text = temp_max.toString()+"°/"+temp_min.toString()+"°"
                    var CurrentTemp = findViewById<TextView>(R.id.CurrentTemp)
                    CurrentTemp.text = temp.toString()+"°"
                    var FeelsLike = findViewById<TextView>(R.id.FeelsLike)
                    FeelsLike.text = "Feels like "+feels_like.toString()+"°"


                    // processing weather
                    var weatherString = jsonObject.getJSONArray("weather")[0].toString()
                    var weatherJson = JSONObject(weatherString)
                    var main = weatherJson.get("main").toString()
                    var description = weatherJson.get("description").toString()
                    //var icon = weatherJson.get("icon").toString()
                    var Description = findViewById<TextView>(R.id.Description)
                    Description.text=description

                    //processing country, there might be the same cities in different countries
                    var sysString = jsonObject.get("sys").toString()
                    var sysJSON = JSONObject(sysString)
                    var country = sysJSON.get("country").toString()
                    var Country = findViewById<TextView>(R.id.Country)
                    Country.text=country

                    //var imageView = findViewById<ImageView>(R.id.iconImage)
                    //val url = URL("http://openweathermap.org/img/wn/"+icon +"@2x.png")
                    //val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    //imageView.setImageBitmap(bmp)



                }
            }
        })
    }
}

