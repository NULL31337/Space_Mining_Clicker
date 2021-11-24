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
import android.util.Log
import java.math.BigInteger
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        private lateinit var data: SharedPreferences
        var money = 0
        private lateinit var moneyView: TextView
        lateinit var cpsView: TextView
        lateinit var mainView: SpriteView
        lateinit var animation: Animation
        lateinit var backgroundAnimation: Animation
        lateinit var background: ImageView
    }


    var upgradesCostStartLevel = hashMapOf<Pair<Int, Int>, Pair<Int, Int>>()
    var upgrades = hashMapOf<String, Int>()

    lateinit var afk: BigInteger
    private val timer = Timer()
    var clicks = 0


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
        cpsView = findViewById(R.id.cps)
        createUpgrades()
        var tmp = 1
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                cpsView.text = "$clicks cps"
                clicks = 0
                mainView.setImageResource(getStringIdentifier("planet10$tmp"))
                tmp = (++tmp) % 10
            }
        }, 0, 1000)

        var display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        background.layoutParams.width = ((display.widthPixels - 1) / 400 + 1) * 400
        background.layoutParams.height = background.layoutParams.width * 4
        backgroundAnimation = AnimationUtils.loadAnimation(this, R.anim.up)
        background.startAnimation(backgroundAnimation)
        moneyView.text = (money / 10).toString()
    }

    fun getStringIdentifier(name: String?): Int {
        return resources.getIdentifier(name, "drawable", packageName)
    }


    private fun createUpgrades() {
        val scanner = Scanner(assets.open("Upgrades"))
        while (scanner.hasNextLine()) {
            val a = scanner.nextInt()
            val b = scanner.nextInt()
            val c = scanner.nextInt()
            upgradesCostStartLevel[(a / 10) to (a % 10)] = b to c
        }
    }

    fun buttonClicked(view: View) {
        clicks++
        money += 10
        moneyView.text = (money / 10).toString()
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