package com.example.spaceminingclicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.spaceminingclicker.MainActivity.Companion.afkCps
import com.example.spaceminingclicker.MainActivity.Companion.calculateCps
import com.example.spaceminingclicker.MainActivity.Companion.makeNthUpgrade
import com.example.spaceminingclicker.MainActivity.Companion.money
import com.example.spaceminingclicker.MainActivity.Companion.moneyToString
import com.example.spaceminingclicker.MainActivity.Companion.upgrades
import com.example.spaceminingclicker.MainActivity.Companion.upgradesCostStartLevel
import java.math.BigInteger
import java.util.*

class Shop : AppCompatActivity() {
    lateinit var moneyView: TextView
    var buttonsCost = mutableListOf<TextView>()
    var buttonsLVL = mutableListOf<TextView>()
    var buttonsGive = mutableListOf<TextView>()
    var buttons = hashMapOf<View, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        MainActivity.restoreData(MainActivity.data)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        createUpgrades()
        moneyView = findViewById(R.id.money)
        moneyView.text = moneyToString(money)
    }

    override fun onResume() {
        super.onResume()
        MainActivity.restoreData(MainActivity.data)
    }

    override fun onPause() {
        super.onPause()
        MainActivity.saveData(MainActivity.data.edit())
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.saveData(MainActivity.data.edit())
    }

    fun createUpgrades() {
        for (i in 0..9) {
            buttonsGive.add(
                findViewById(
                    resources.getIdentifier(
                        "shopButton${i}Give",
                        "id",
                        packageName
                    )
                )
            )
            buttonsCost.add(
                findViewById(
                    resources.getIdentifier(
                        "shopButton${i}Cost",
                        "id",
                        packageName
                    )
                )
            )
            buttonsLVL.add(
                findViewById(
                    resources.getIdentifier(
                        "shopButton${i}LVL",
                        "id",
                        packageName
                    )
                )
            )
            Log.d("SUKAA", "createUpgrades: $i")
            buttons[findViewById(
                resources.getIdentifier(
                    "upgradeButton${i}",
                    "id",
                    packageName
                )
            )] = i
            Log.d("SUKAA", "createUpgrades: $i")
            buttonsCost[i].text =
                moneyToString(makeNthUpgrade(upgrades[i], upgradesCostStartLevel[1 to i]!!.second))
            buttonsGive[i].text = moneyToString(upgradesCostStartLevel[1 to i]!!.first)
            buttonsLVL[i].text = upgrades[i].toBigInteger().toString()
        }
    }

    fun shopButtons(view: View) {
        view.startAnimation(MainActivity.animation)
        val i = buttons[view] ?: 0
        val tmp = makeNthUpgrade(upgrades[i], upgradesCostStartLevel[1 to i]!!.second)
        if (money > tmp) {
            money -= tmp
            upgrades[i]++
            calculateCps()
            moneyView.text = moneyToString(money)
            buttonsCost[i].text = moneyToString(makeNthUpgrade(1, tmp))
            buttonsLVL[i].text = upgrades[i].toBigInteger().toString()
        }
    }
}