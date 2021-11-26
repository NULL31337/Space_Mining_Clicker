package com.example.spaceminingclicker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import com.example.spaceminingclicker.MainActivity.Companion.getStringIdentifier
import java.lang.NullPointerException
import java.math.BigInteger
import java.util.*
import kotlin.collections.HashMap

class Shop : AppCompatActivity() {
    private lateinit var moneyTextView: TextView
    private var getIdByView = HashMap<View, Int>()
    private var buttons = mutableListOf<ShopButton>()
    private var buttonsView = mutableListOf<ShopButtonView>()
    private lateinit var afkCost: BigInteger
    private var upgradesLvl = mutableListOf<Int>()
    private lateinit var money: BigInteger
    private var clickLvl: Int = -1

    private lateinit var data: SharedPreferences
    private lateinit var clickAnimation: Animation

    fun readUpgrades() {
        val scanner = Scanner(assets.open("Upgrades"))
        for (i in 0..9) {
            buttons.add(ShopButton(scanner.nextInt(), scanner.nextInt(), BigInteger(scanner.next()), BigInteger(scanner.next()), upgradesLvl[i], typeOfButton.AFK))
        }
        buttons.add(ShopButton(scanner.nextInt(), scanner.nextInt(), BigInteger(scanner.next()), BigInteger(scanner.next()), upgradesLvl[10], typeOfButton.CLICK))

    }

    fun evaluateAfk() {
        afkCost = BigInteger("0")
        for (i in 0..9) {
            afkCost += upgradesLvl[i].toBigInteger() * buttons[i].give
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        supportActionBar?.hide()
        data = getSharedPreferences("infoKekw", Context.MODE_PRIVATE)
        restoreData(data)
        moneyTextView = findViewById(R.id.money)
        moneyTextView.text = money.toMyString()
        clickAnimation = AnimationUtils.loadAnimation(this, R.anim.animation)

        readUpgrades()
        fillButtonsArrayAndText()
        evaluateAfk()

    }

    fun OnClickBackArrow(view: View) = onBackPressed()

    override fun onBackPressed() {
        saveData(data.edit())
        super.onBackPressed()
    }
    override fun onDestroy() {
        saveData(data.edit())
        super.onDestroy()
    }
    override fun onPause() {
        saveData(data.edit())
        super.onPause()
    }

    fun fillButtonsArrayAndText() {
        for (i in 0..10) {
            buttonsView.add(findViewById( resources.getIdentifier("button$i", "id", packageName)))
            getIdByView[buttonsView[i].clickableLayout] = i
            buttonsView[i].setButton(buttons[i])
            buttons[i].update()
        }
    }

    fun shopButtons(view: View) {
        view.startAnimation(clickAnimation)
        val i = getIdByView[view] ?: 0
        if (money > buttons[i].cost) {
            money -= buttons[i].cost
            upgradesLvl[i]++
            buttons[i].currentLvl++
            evaluateAfk()
            buttons[i].update()
            moneyTextView.text = money.toMyString()
            buttonsView[i].setButton(buttons[i])
        }
    }


    fun saveData(edit: SharedPreferences.Editor) {
        for (i in 0..10) {
            edit.putInt("010$i", upgradesLvl[i]).apply()
        }
        edit.putString("money", money.toString()).apply()
        edit.putString("afkCost", afkCost.toString())
        edit.commit()
    }

    fun restoreData(data: SharedPreferences) {
        for (i in 0..10) {
            upgradesLvl.add(data.getInt("010$i", 0))
        }
        money = BigInteger(data.getString("money", "0"))
    }
}