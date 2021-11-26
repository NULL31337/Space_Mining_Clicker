package com.example.spaceminingclicker

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class ShopButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val costTextView: TextView
    private val giveTextView: TextView
    private val lvlTextView: TextView
    private val image: ImageView
    internal val clickableLayout: View
    init {
        clipChildren = false
        clipToPadding = false
        inflate(context, R.layout.shop_button, this)
        costTextView = findViewById(R.id.shopButtonCost)
        giveTextView = findViewById(R.id.shopButtonGive)
        lvlTextView = findViewById(R.id.shopButtonLVL)
        image = findViewById(R.id.shopButton)
        background = ContextCompat.getDrawable(context, R.drawable.button)
        clickableLayout = findViewById(R.id.upgradeButton)
    }

    fun setButton(button: ShopButton) {
        costTextView.text = button.cost.toMyString()
        giveTextView.text = button.give.toMyString()
        lvlTextView.text = button.currentLvl.toString()
    }
}
