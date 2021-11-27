package com.example.spaceminingclicker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.util.DisplayMetrics
import android.util.Log
import com.example.spaceminingclicker.ShopButton.Companion.makeNthUpgrade
import java.math.BigInteger
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        fun getStringIdentifier(context: Context, type: String, name: String?): Int {
            return context.resources.getIdentifier(name, type, context.packageName)
        }
    }

    //Data
    private lateinit var data: SharedPreferences
    private lateinit var money: BigInteger
    private var clickCost = BigInteger("10")
    private var afkCost = BigInteger("0")
    private var upgradesLvl = mutableListOf<Int>()
    private var clicksCount = 0
    private var multiply = 1

    private val timer = Timer()

    //Views
    private lateinit var moneyTextView: TextView
    private lateinit var cpsTextView: TextView
    private lateinit var planetSpriteView: SpriteView
    private lateinit var clickAnimation: Animation
    private lateinit var backgroundAnimation: Animation
    private lateinit var backgroundImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("build", "settings")
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        Log.d("build", "readInfo")
        data = getSharedPreferences("infoKekw", Context.MODE_PRIVATE)
        planetSpriteView = findViewById(R.id.mainPicture)
        restoreData(data)
        Log.d("build", "setViews")
        setViews()
        everySecond()
    }

    private fun setViews() {
        moneyTextView = findViewById(R.id.money)
        planetSpriteView = findViewById(R.id.mainPicture)
        clickAnimation = AnimationUtils.loadAnimation(this, R.anim.animation)
        backgroundImageView = findViewById(R.id.backgroundView)
        cpsTextView = findViewById(R.id.cps)

        moneyTextView.text = money.toMyString()
        Log.d("build", "set background")
        var display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        backgroundImageView.layoutParams.width = ((display.widthPixels - 1) / 400 + 1) * 400
        backgroundImageView.layoutParams.height = backgroundImageView.layoutParams.width * 4
        backgroundAnimation = AnimationUtils.loadAnimation(this, R.anim.up)
        backgroundImageView.startAnimation(backgroundAnimation)
    }

    fun everySecond() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                cpsTextView.text =
                    "${(clicksCount.toBigInteger() * clickCost * multiply.toBigInteger() + afkCost).toMyString()} cps"
                money += afkCost

                moneyTextView.text = money.toMyString()
                if (clicksCount / 5 + 1 > multiply) {
                    multiply++
                } else {
                    if (multiply != 1) {
                        multiply--
                    }
                }
                clicksCount = 0
                when (multiply) {
                    1 -> moneyTextView.setTextColor(Color.parseColor("#FF00FF"))
                    2 -> moneyTextView.setTextColor(Color.parseColor("#FFFFFF"))
                    3 -> moneyTextView.setTextColor(Color.parseColor("#000000"))
                    4 -> moneyTextView.setTextColor(Color.parseColor("#123456"))
                    5 -> moneyTextView.setTextColor(Color.parseColor("#FF22FF"))
                }
            }
        }, 0, 1000)
    }

    fun onClickPlanetView(view: View) {
        clicksCount++
        money += clickCost * multiply.toBigInteger()
        moneyTextView.text = money.toMyString()
        planetSpriteView.startAnimation(clickAnimation)
    }

    fun onClickShopView(view: View) {
        saveData(data.edit())
        val shopIntent = Intent(this, Shop::class.java)
        startActivity(shopIntent)
    }


    fun saveData(edit: SharedPreferences.Editor) {
        for (i in 0..10) {
            edit.putInt("010$i", upgradesLvl[i]).apply()
        }
        edit.putString("money", money.toString()).apply()
        edit.putString("afkGIve", afkCost.toString())
        edit.commit()
    }

    fun restoreData(data: SharedPreferences) {
        upgradesLvl = mutableListOf()
        for (i in 0..10) {
            upgradesLvl.add(data.getInt("010$i", 0))
        }
        money = BigInteger(data.getString("money", "0"))
        clickCost = BigInteger("10")
        repeat(upgradesLvl[10]) {
            clickCost *= 2.toBigInteger()
        }
        planetSpriteView.setImageResource(
            getStringIdentifier(
                baseContext,
                "drawable",
                "planet10${upgradesLvl[10]}"
            )
        )
        afkCost = BigInteger(data.getString("afkCost", "0"))
    }

    override fun onPause() {
        super.onPause()
        val edit = data.edit()
        saveData(edit)
    }

    override fun onResume() {
        super.onResume()
        restoreData(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        val edit = data.edit()
        saveData(edit)
    }
}


fun BigInteger.condition(min: String, max: String, string: String): String? {
    val minI = BigInteger(min)
    val maxI = BigInteger(max)
    val it = this
    if (it >= minI && maxI > it) {
        return buildString {
            append(it / minI)
            var afterDot = (it % minI) / (minI / BigInteger("1000"))
            if (afterDot != BigInteger("0")) {
                append(".")
                if (it / minI >= BigInteger("100")) {
                    afterDot /= BigInteger("100")
                    append(
                        String.format(
                            "%01d",
                            (afterDot)
                        )
                    )
                } else if (it / minI >= BigInteger("10")) {
                    afterDot /= BigInteger("10")
                    append(
                        String.format(
                            "%02d",
                            (afterDot)
                        )
                    )
                } else {
                    append(
                        String.format(
                            "%03d",
                            (afterDot)
                        )
                    )
                }
            }
            append(string)
        }
    } else {
        return null
    }
}

fun BigInteger.toMyString() = when {
    this == 0.toBigInteger() -> "0"
    this < 10.toBigInteger() -> "0.${this % 10.toBigInteger()}"
    this < 10000.toBigInteger() -> "${this / BigInteger("10")}" +
            if (this % BigInteger("10") != BigInteger("0")) {
                ".${
                    this % BigInteger(
                        "10"
                    )
                }"
            } else ""
    else -> {
        this.condition("10000", "10000000", "k") ?: this.condition(
            "10000000",
            "10000000000",
            "KK"
        ) ?: "KEKW"
    }
}
