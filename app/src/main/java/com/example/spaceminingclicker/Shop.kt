package com.example.spaceminingclicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import com.example.spaceminingclicker.MainActivity.Companion.getStringIdentifier
import com.example.spaceminingclicker.MainActivity.Companion.makeNthClick
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
    private lateinit var upgradesLvl: MutableList<Int>
    private lateinit var money: BigInteger
    private lateinit var clickCost: BigInteger
    private var clickLvl: Int = -1

    private lateinit var clickAnimation: Animation

    fun readUpgrades() {
        val scanner = Scanner(assets.open("Upgrades"))
        for (i in 0..9) {
            buttons.add(ShopButton(scanner.nextInt(), scanner.nextInt(), BigInteger(scanner.next()), BigInteger(scanner.next()), upgradesLvl[i]))
        }
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
        val arguments = intent.extras
        upgradesLvl = arguments?.getIntArray("upgradesLvl")?.toMutableList() ?: throw NullPointerException("Bad Bundle")
        money = BigInteger(arguments.getString("money", "0"))
        clickLvl = arguments.getInt("clickLvl")
        moneyTextView = findViewById(R.id.money)
        moneyTextView.text = money.toMyString()
        clickAnimation = AnimationUtils.loadAnimation(this, R.anim.animation)
        clickCost = makeNthClick(clickLvl)

        readUpgrades()
        fillButtonsArrayAndText()
        evaluateAfk()

        var tmp = 1
        repeat(clickLvl + 1) {
            tmp *= 2
        }
    }

    fun OnClickBackArrow(view: View) {
        val data2 = Intent(this, MainActivity::class.java)
        data2.putExtra("upgradesLvl", upgradesLvl.toIntArray())
        data2.putExtra("money", money.toString())
        data2.putExtra("afkCost", afkCost.toString())
        data2.putExtra("clickLvl", clickLvl)
        startActivity(data2)
    }

    override fun onDestroy() {
        super.onDestroy()
        OnClickBackArrow(moneyTextView)
    }
    override fun onPause() {
        super.onPause()
        OnClickBackArrow(moneyTextView)
    }

    fun fillButtonsArrayAndText() {
        for (i in 0..9) {
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
}