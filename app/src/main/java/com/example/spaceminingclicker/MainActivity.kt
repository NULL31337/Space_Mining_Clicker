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
import android.util.DisplayMetrics
import android.util.Log
import java.math.BigInteger
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        fun makeNthClick(n: Int): BigInteger {
            var ans = BigInteger("10000")
            repeat(n) {
                ans = (ans * 250.toBigInteger()) / 100.toBigInteger()
            }
            return ans
        }

        fun getStringIdentifier(context: Context, type: String, name: String?): Int {
            return context.resources.getIdentifier(name, type, context.packageName)
        }
    }

    //Data
    private lateinit var data: SharedPreferences
    private lateinit var money: BigInteger
    private var clickLvl: Int = -1
    private var clickCost = BigInteger("10")
    private var afkCost = BigInteger("0")
    private var upgradesLvl = mutableListOf<Int>()
    private var clicksCount = 0

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
        data = getSharedPreferences("info3", Context.MODE_PRIVATE)
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
                    "${(clicksCount.toBigInteger() * clickCost + afkCost).toMyString()} cps"
                money += afkCost
                moneyTextView.text = money.toMyString()
                clicksCount = 0
            }
        }, 0, 1000)
    }

    fun onClickPlanetView(view: View) {
        clicksCount++
        money += clickCost
        moneyTextView.text = money.toMyString()
        planetSpriteView.startAnimation(clickAnimation)
    }

    fun onClickShopView(view: View) {
        val shopIntent = Intent(this, Shop::class.java)
        shopIntent.putExtra("upgradesLvl", upgradesLvl.toIntArray())
        shopIntent.putExtra("money", money.toString())
        shopIntent.putExtra("afkCost", afkCost.toString())
        shopIntent.putExtra("clickLvl", clickLvl)
        startActivity(shopIntent)
    }


    fun saveData(edit: SharedPreferences.Editor) {
        for (i in 0..9) {
            edit.putInt("010$i", upgradesLvl[i]).apply()
        }
        edit.putString("money", money.toString()).apply()
        edit.putInt("clickLvl", clickLvl)
    }

    fun restoreData(data: SharedPreferences) {
        if (intent.getStringExtra("money") == null) {
            for (i in 0..9) {
                upgradesLvl.add(data.getInt("010$i", 0))
            }
            money = BigInteger(data.getString("money", "0"))
            clickLvl = data.getInt("clickLvl", 0)
            clickCost = makeNthClick(clickLvl)
            repeat(clickLvl) {
                clickCost *= 2.toBigInteger()
            }
            planetSpriteView.setImageResource(
                getStringIdentifier(
                    baseContext,
                    "drawable",
                    "planet10$clickLvl"
                )
            )
            afkCost = BigInteger(data.getString("afkCost", "0"))
        } else {
            upgradesLvl = intent.getIntArrayExtra("upgradesLvl")!!.toMutableList()
            afkCost = BigInteger(intent.getStringExtra("afkCost"))
            money = BigInteger(intent.getStringExtra("money"))
            clickLvl = intent.getIntExtra("clickLvl", 0)

            planetSpriteView.setImageResource(
                getStringIdentifier(
                    baseContext,
                    "drawable",
                    "planet10$clickLvl"
                )
            )
        }
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
            "kk"
        ) ?: "KEKW"
    }
}
