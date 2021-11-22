package com.example.spaceminingclicker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Bitmap
import android.util.DisplayMetrics

class MainActivity : AppCompatActivity() {
    companion object {
        private lateinit var data: SharedPreferences
        var money = 0
    }
    lateinit var moneyView: TextView
    lateinit var mainView: SpriteView
    lateinit var animation: Animation
    lateinit var backgroundAnimation: Animation
    lateinit var background: ImageView
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        data = getSharedPreferences("moneys", Context.MODE_PRIVATE)
        money = data.getInt("money", 0)
        moneyView = findViewById(R.id.money)
        mainView = findViewById(R.id.mainPicture)
        animation = AnimationUtils.loadAnimation(this, R.anim.animation)
        background = findViewById(R.id.backgroundView)

        var display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        background.layoutParams.width = ((display.widthPixels - 1) / 400 + 1) * 400
        background.layoutParams.height = background.layoutParams.width * 4
        backgroundAnimation = AnimationUtils.loadAnimation(this, R.anim.up)
        background.startAnimation(backgroundAnimation)
        moneyView.text=money.toString()
    }



    fun buttonClicked(view: View) {
        money++
        moneyView.text=money.toString()
        mainView.startAnimation(animation)
    }

    fun buttonShop(view: View) {
        val shopIntent = Intent(this, Shop::class.java)
        shopIntent.putExtra("money", money)
        startActivity(shopIntent)
    }


    override fun onPause() {
        super.onPause()
        val edit = data.edit()
        edit.putInt("money", money).apply()

    }


    override fun onResume() {
        super.onResume()
        money = data.getInt("money", 0)
    }
    override fun onDestroy() {
        super.onDestroy()
        val edit = data.edit()
        edit.putInt("money", money).apply()
    }
}