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
        lateinit var data: SharedPreferences
        var money = 0.toBigInteger()
        private lateinit var moneyView: TextView
        lateinit var cpsView: TextView
        lateinit var mainView: SpriteView
        lateinit var animation: Animation
        lateinit var backgroundAnimation: Animation
        lateinit var background: ImageView
        var upgradesCostStartLevel = hashMapOf<Pair<Int, Int>, Pair<BigInteger, BigInteger>>()
        var upgrades = mutableListOf<Int>()
        var afkCps = BigInteger("0")
        var clickCost = BigInteger("10")
        var cpsLvl = 1
        fun moneyToString(i: BigInteger) =
            when {
                i == 0.toBigInteger() -> "0"
                i < 10.toBigInteger() -> "0.${i % 10.toBigInteger()}"
                i < 10000.toBigInteger() -> (i / 10.toBigInteger()).toString() + ".${i % 10.toBigInteger()}"
                i < 10000000.toBigInteger() -> buildString {
                    append(i / (10000.toBigInteger()))
                    if (((i % 10000.toBigInteger()) / 10.toBigInteger()) != 0.toBigInteger()) {
                        append(".")
                        append(
                            String.format(
                                "%03d",
                                ((i % 10000.toBigInteger()) / 10.toBigInteger())
                            )
                        )
                    }
                    append("k")
                }
                i < 10000000000.toBigInteger() -> buildString {
                    append(i / (10000000.toBigInteger()))
                    if ((i % 10000000.toBigInteger()) / 10000.toBigInteger() != 0.toBigInteger()) {
                        append(".")
                        append(
                            String.format(
                                "%03d",
                                (i % 10000000.toBigInteger()) / 10000.toBigInteger()
                            )
                        )
                    }
                    append("kk")
                }
                else -> {
                    "Kekw"
                }
            }

        fun makeNthUpgrade(n: Int, bigInteger: BigInteger): BigInteger {
            var ans = bigInteger
            repeat(n) {
                ans = (ans * 115.toBigInteger()) / 100.toBigInteger()
            }
            return ans
        }

        fun saveData(edit: SharedPreferences.Editor) {
            for (i in 0..10) {
                edit.putInt("010$i", upgrades[i]).apply()
            }
            edit.putString("money", money.toString()).apply()
            edit.putInt("clickLvl", cpsLvl)
        }

        fun restoreData (data: SharedPreferences) {
            for (i in 0..10) {
                upgrades.add(data.getInt("010$i", 0))
            }
            money = BigInteger(data.getString("money", "0"))
            cpsLvl = data.getInt("clickLvl", 0)
            clickCost = BigInteger("10")
            repeat(cpsLvl) {
                clickCost *= 2.toBigInteger()
            }
            calculateCps()
        }
        fun calculateCps() {
            afkCps = BigInteger("0")
            for (i in 0..9) {
                afkCps += upgrades[i].toBigInteger() * upgradesCostStartLevel[1 to i]!!.first
            }
        }
    }

    lateinit var afk: BigInteger
    private val timer = Timer()
    var clicks = 0

    fun readUpgrades() {
        val scanner = Scanner(assets.open("Upgrades"))
        for (i in 0..9) {
            val planet = scanner.nextInt()
            val lvl = scanner.nextInt()
            val give = BigInteger(scanner.next())
            val cost = BigInteger(scanner.next())
            upgradesCostStartLevel[planet to lvl] = give to cost
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        readUpgrades()
        data = getSharedPreferences("info2", Context.MODE_PRIVATE)
        restoreData(data)
        moneyView = findViewById(R.id.money)
        mainView = findViewById(R.id.mainPicture)
        animation = AnimationUtils.loadAnimation(this, R.anim.animation)
        background = findViewById(R.id.backgroundView)
        cpsView = findViewById(R.id.cps)
        var tmp = 1
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                cpsView.text = "${moneyToString(clicks.toBigInteger() * clickCost + afkCps)} cps"
                money += afkCps
                moneyView.text = moneyToString(money)
                clicks = 0
                mainView.setImageResource(getStringIdentifier("drawable", "planet10$tmp"))
                tmp = (++tmp) % 10
            }
        }, 0, 1000)

        var display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        background.layoutParams.width = ((display.widthPixels - 1) / 400 + 1) * 400
        background.layoutParams.height = background.layoutParams.width * 4
        backgroundAnimation = AnimationUtils.loadAnimation(this, R.anim.up)
        background.startAnimation(backgroundAnimation)
        moneyView.text = moneyToString(money)
    }

    fun getStringIdentifier(type: String, name: String?): Int {
        return resources.getIdentifier(name, type, packageName)
    }



    fun buttonClicked(view: View) {
        clicks++
        money += 10.toBigInteger()
        moneyView.text = moneyToString(money)
        mainView.startAnimation(animation)
    }


    fun buttonShop(view: View) {
        val shopIntent = Intent(this, Shop::class.java)
        saveData(data.edit())
        startActivity(shopIntent)
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