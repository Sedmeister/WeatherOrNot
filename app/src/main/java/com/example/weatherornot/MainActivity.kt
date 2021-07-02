package com.example.weatherornot

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.CityEditText)
        val city = editText.text
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=8e6d3f022e686a9f72efbdc889b4b25b"
        run(url)

    }

    fun search(vi: View) {
        val editText = findViewById<EditText>(R.id.CityEditText)
        val city = editText.text
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=8e6d3f022e686a9f72efbdc889b4b25b"
        run(url)
    }

    private fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    val jsonString = response.body()?.string()
                    val jsonObject = JSONObject(jsonString.toString())

                    // Processing main json with trycatch feature for incorrect city names
                    try {
                        val mainString = jsonObject.get("main").toString()
                        val mainJson = JSONObject(mainString)
                        val temp: Double = Math.round(
                            (mainJson.get("temp").toString().toDouble() - 273.15) * 10.0
                        ) / 10.0
                        val temp_min: Double = Math.round(
                            (mainJson.get("temp_min").toString().toDouble() - 273.15) * 10.0
                        ) / 10.0
                        val temp_max: Double = Math.round(
                            (mainJson.get("temp_max").toString().toDouble() - 273.15) * 10.0
                        ) / 10.0
                        val feels_like: Double = Math.round(
                            (mainJson.get("feels_like").toString().toDouble() - 273.15) * 10.0
                        ) / 10.0

                        val LowHigh = findViewById<TextView>(R.id.LowHigh)
                        LowHigh.text = temp_max.toString() + "°/" + temp_min.toString() + "°"
                        val CurrentTemp = findViewById<TextView>(R.id.CurrentTemp)
                        CurrentTemp.text = temp.toString() + "°"
                        val FeelsLike = findViewById<TextView>(R.id.FeelsLike)
                        FeelsLike.text = "Feels like " + feels_like.toString() + "°"


                        // processing weather
                        val weatherString = jsonObject.getJSONArray("weather")[0].toString()
                        val weatherJson = JSONObject(weatherString)
                        val description = weatherJson.get("description").toString()
                        //var icon = weatherJson.get("icon").toString()
                        val Description = findViewById<TextView>(R.id.Description)
                        Description.text = description

                        //processing country, there might be the same cities in different countries
                        val sysString = jsonObject.get("sys").toString()
                        val sysJSON = JSONObject(sysString)
                        val country = sysJSON.get("country").toString()
                        val Country = findViewById<TextView>(R.id.Country)
                        Country.text = country
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@MainActivity,
                            "This city does not exist, Kindly please type in a valid city name",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    //For future use: Changing the icon based on the weather condition
                    //var imageView = findViewById<ImageView>(R.id.iconImage)
                    //val url = URL("http://openweathermap.org/img/wn/"+icon +"@2x.png")
                    //val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    //imageView.setImageBitmap(bmp)


                }
            }
        })
    }
}

