package com.example.spaceminingclicker
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class Shop : AppCompatActivity() {
    lateinit var moneyView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        moneyView = findViewById(R.id.money)
        moneyView.text = MainActivity.money.toString()
    }
    fun shopButtons(view: View){

    }
}