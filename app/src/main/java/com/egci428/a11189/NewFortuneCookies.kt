package com.egci428.a11189

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.appcompat.app.ActionBar
import com.google.gson.Gson
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import kotlin.random.Random

class NewFortuneCookies : AppCompatActivity(), SensorEventListener {

    var showWishes: ArrayList<Wish>? = null
    var showDates: ArrayList<Date>? = null

    private var sensorManager: SensorManager? = null
    private var lastUpdate: Long = 0

//Date and Time
    fun getTime (): String? {

        val current = LocalDateTime.now()

        val timeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")
        val timeFormatted = current.format(timeFormatter)

        return timeFormatted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_fortune_cookies)

        var actionBar = supportActionBar

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar_new)

        var colorDrawable = ColorDrawable(Color.parseColor("#D4E9FF"))
        actionBar!!.setBackgroundDrawable(colorDrawable)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()

        val backButton = findViewById<ImageButton>(R.id.plusButton)
        backButton.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val showResultTextView = findViewById<TextView>(R.id.showResultTextView)
        val newDateTextView = findViewById<TextView>(R.id.newDateTextView)
        val wishButton = findViewById<ImageButton>(R.id.wishButton)
        val saveButton = findViewById<ImageButton>(R.id.saveButton)
        val cookieImageView = findViewById<ImageView>(R.id.cookieImageView)
        val wishBtnTextView = findViewById<TextView>(R.id.wishBtnTextView)
        val saveBtnTextView = findViewById<TextView>(R.id.saveBtnTextView)
        val wishResultText = findViewById<TextView>(R.id.wishResultText)

//Click on Make a Wish Btn
        wishButton.setOnClickListener {

            val num = kotlin.random.Random.nextInt(0, 9).toString()
            val jsonURL: String = "https://egco428-json.firebaseio.com/fortunecookies/" +num+".json"

            val client = OkHttpClient()
            var asyncTask = object : AsyncTask<String, String, String>() {

                override fun onPreExecute() {
                    Toast.makeText(this@NewFortuneCookies, "Waiting", Toast.LENGTH_SHORT).show()
                }

                override fun doInBackground(vararg arg: String?): String {
                    val builder = Request.Builder()
                    builder.url(arg[0].toString())
                    val request = builder.build()
                    try {
                        val response = client.newCall(request).execute()
                        return response.body!!.string()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return ""
                }

                override fun onPostExecute(result: String?) {
                    val newWishText = Gson().fromJson(result, Wish::class.java)

                    val blueColor = Color.parseColor("#149CFC")
                    val orangeColor = Color.parseColor("#FF9800")

                    cookieImageView.setImageResource(R.drawable.opened_cookie)
                    wishResultText.text = newWishText.message
                    wishResultText.visibility = View.VISIBLE

                    showResultTextView.text = "  Result: " + newWishText.message

                    if (newWishText.status == "negative"){
                        showResultTextView.setTextColor(orangeColor)
                    }
                    else if (newWishText.status == "positive"){
                        showResultTextView.setTextColor(blueColor)
                    }

                    newDateTextView.text = "Date: " + getTime() +"  "

                    wishButton.visibility = View.GONE
                    wishBtnTextView.visibility = View.GONE
                    saveButton.visibility = View.VISIBLE
                    saveBtnTextView.visibility = View.VISIBLE

                    showWishes = mainWishes.getWish()
                    showWishes!!.add(Wish("${newWishText.message}","${newWishText.status}"))

                    showDates = mainDates.getDate()
                    showDates!!.add(Date("Date: ${getTime()}"))
                }
            }
            asyncTask.execute(jsonURL)
        }

//Click on Save Btn
        saveButton.setOnClickListener {
            Toast.makeText(this@NewFortuneCookies,"Save", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

//Shake the phone
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }
    }

    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values

        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accel = (x*x + y*y + z*z) / (SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH)
        val actualTime = System.currentTimeMillis()

        if (accel >= 2) {
            if (actualTime-lastUpdate < 200) {
                return
            }
            lastUpdate = actualTime

            val showResultTextView = findViewById<TextView>(R.id.showResultTextView)
            val newDateTextView = findViewById<TextView>(R.id.newDateTextView)
            val wishButton = findViewById<ImageButton>(R.id.wishButton)
            val saveButton = findViewById<ImageButton>(R.id.saveButton)
            val cookieImageView = findViewById<ImageView>(R.id.cookieImageView)
            val wishBtnTextView = findViewById<TextView>(R.id.wishBtnTextView)
            val saveBtnTextView = findViewById<TextView>(R.id.saveBtnTextView)
            val wishResultText = findViewById<TextView>(R.id.wishResultText)

//get data from JSON
            val num = kotlin.random.Random.nextInt(0, 9).toString()
            val jsonURL: String = "https://egco428-json.firebaseio.com/fortunecookies/" +num+".json"

            val client = OkHttpClient()
            var asyncTask = object : AsyncTask<String, String, String>() {

                override fun onPreExecute() {
                    Toast.makeText(this@NewFortuneCookies, "Waiting", Toast.LENGTH_SHORT).show()
                }

                override fun doInBackground(vararg arg: String?): String {
                    val builder = Request.Builder()
                    builder.url(arg[0].toString())
                    val request = builder.build()
                    try {
                        val response = client.newCall(request).execute()
                        return response.body!!.string()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return ""
                }

                override fun onPostExecute(result: String?) {

                    val newWishText = Gson().fromJson(result, Wish::class.java)

                    val blueColor = Color.parseColor("#149CFC")
                    val orangeColor = Color.parseColor("#FF9800")

 //New Wish Image Result
                    cookieImageView.setImageResource(R.drawable.opened_cookie)
                    wishResultText.text = newWishText.message
                    wishResultText.visibility = View.VISIBLE

//Show new wish and set text color
                    showResultTextView.text = "  Result: " + newWishText.message

                    if (newWishText.status == "negative"){
                        showResultTextView.setTextColor(orangeColor)
                    }
                    else if (newWishText.status == "positive"){
                        showResultTextView.setTextColor(blueColor)
                    }

                    newDateTextView.text = "Date: " + getTime() +"  "

                    wishButton.visibility = View.GONE
                    wishBtnTextView.visibility = View.GONE
                    saveButton.visibility = View.VISIBLE
                    saveBtnTextView.visibility = View.VISIBLE

                    showWishes = mainWishes.getWish()
                    showWishes!!.add(Wish("${newWishText.message}","${newWishText.status}"))

                    showDates = mainDates.getDate()
                    showDates!!.add(Date("Date: ${getTime()}"))
                }
            }
            asyncTask.execute(jsonURL)
            }
        }

    override fun onAccuracyChanged(event: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
}