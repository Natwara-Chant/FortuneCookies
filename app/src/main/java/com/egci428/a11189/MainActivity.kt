package com.egci428.a11189

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import java.util.*

class MainActivity : AppCompatActivity() {
    var showWishes: ArrayList<Wish>? = null
    var showDates: ArrayList<Date>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//Customize Action Bar
        var actionBar = supportActionBar

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar_layout)

        //set color of the bar
        var colorDrawable = ColorDrawable(Color.parseColor("#D4E9FF"))
        actionBar!!.setBackgroundDrawable(colorDrawable)

//Click to next page
        val plusButton = findViewById<ImageButton>(R.id.plusButton)

        plusButton.setOnClickListener {

            var intent = Intent(this, NewFortuneCookies::class.java)
            startActivity(intent)

        }

//Prepare ListView
        showWishes = mainWishes.getWish()
        showDates = mainDates.getDate()

        val mainListView = findViewById<ListView>(R.id.mainListView)
        val mainWishesAdapter = myCustomAdapter(this, showWishes!!, showDates!!)
        mainListView.adapter = mainWishesAdapter
    }

//save to the list
    override fun onResume() {
        val mainListView = findViewById<ListView>(R.id.mainListView)
        super.onResume()
        showWishes = mainWishes.getWish()
        val fortuneArrayAdapter = myCustomAdapter(this,showWishes!!,showDates!!)
        mainListView.adapter = fortuneArrayAdapter
    }
}

    private class myCustomAdapter(var context: Context, var mainWish: ArrayList<Wish>, var mainDate:ArrayList<Date>): BaseAdapter() {

        override fun getCount(): Int {
            return mainWish.size
        }

        override fun getItem(position: Int): Any {
            return mainWish[position]
        }

        override fun getItemId(position: Int): Long {
            return (position+1).toLong()
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val row: View
            val wish = mainWish[position]
            val date = mainDate[position]

            if(convertView == null){
                val layoutInflater = LayoutInflater.from(viewGroup!!.context)
                row = layoutInflater.inflate(R.layout.row, viewGroup,false)
                val wishText = row.findViewById<TextView>(R.id.wishTextView)
                val dateText = row.findViewById<TextView>(R.id.dateTextView)
                val openedCookieView = row.findViewById<ImageView>(R.id.openedCookieView)
                val viewHolder = ViewHolder(wishText, dateText, openedCookieView)

                row.tag = viewHolder

            } else {

                row = convertView

            }

            val viewHolder = row.tag as ViewHolder
            viewHolder.wishText.text = wish.message

//set text color
            val blueColor = Color.parseColor("#149CFC")
            val orangeColor = Color.parseColor("#FF9800")

            if (wish.status == "negative"){
                viewHolder.wishText.setTextColor(orangeColor)
            }
            else if (wish.status == "positive"){
                viewHolder.wishText.setTextColor(blueColor)
            }

            viewHolder.dateText.text = "Date: "+ date.date

//set image
            var imgPos: String = wish.message
            imgPos = imgPos.replace(" ","_").toLowerCase()
            imgPos = imgPos.replace("'","")

            val imgRes = context.resources.getIdentifier("$imgPos", "drawable", context.packageName)
            viewHolder.imageView.setImageResource(imgRes)
            //viewHolder.imageView.setImageResource(R.drawable.opened_cookie)

//remove from the list
            row.setOnClickListener {
                row.animate().setDuration(1500).alpha(0F).withEndAction(Runnable {
                    mainWish.removeAt(position)
                    mainDate.removeAt(position)
                    notifyDataSetChanged()
                    row.alpha = 1F
                })

            }

            return row
        }

        private class ViewHolder(val wishText: TextView, val dateText: TextView, val imageView: ImageView)
    }