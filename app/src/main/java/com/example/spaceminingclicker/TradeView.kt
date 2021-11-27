package com.example.spaceminingclicker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.spaceminingclicker.MainActivity.Companion.getStringIdentifier
import java.util.*

class TradeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {
    private val lastTrades = mutableListOf<Int>()
    private var size: Int = -1
    private var paint = Paint()
    private val bitmap: Bitmap
    private val options = BitmapFactory.Options()

    fun addTrade(i: Int) {
        Log.d("kekw", "addTrade: add $i")
        lastTrades.add(i)
        if (lastTrades.size == 11) {
            lastTrades.removeAt(0)
        }
        Log.d("kekw", "addTrade: $lastTrades")
    }

    init {
        options.inScaled = false
        bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.oldscreen,
            options
        )
        addTrade(5)
        everySecond()
    }

    fun everySecond() {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val i = Random().nextInt(100)
                Log.d("kekw", "run: $i")
                when {
                    i < 20 -> {
                        if (lastTrades.last() <= 2) {
                            addTrade(1)
                        } else {
                            addTrade(lastTrades.last() - 2)
                        }
                    }
                    i < 50 -> {
                        if (lastTrades.last() == 1) {
                            addTrade(1)
                        } else {
                            addTrade(lastTrades.last() - 1)
                        }
                    }
                    i < 80 -> {
                        if (lastTrades.last() >= 9) {
                            addTrade(10)
                        } else {
                            addTrade(lastTrades.last() + 1)
                        }
                    }
                    else -> {
                        if (lastTrades.last() >= 8) {
                            addTrade(10)
                        } else {
                            addTrade(lastTrades.last() + 2)
                        }
                    }
                }
            }
        }, 0, 1000)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpec = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpec = MeasureSpec.getSize(heightMeasureSpec)
        size = (if (widthSpec > heightSpec) heightSpec else widthSpec)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("kekw", "onDraw: $lastTrades")
        paint.color = Color.BLACK
        canvas.drawRect(Rect(0, 0, size, size), paint)
        for (i in 0..9) {
            if (i >= 10 - lastTrades.size) {
                if (lastTrades[9 - i] <= 3) {
                    paint.color = Color.RED
                } else if (lastTrades[9 - i] <= 7) {
                    paint.color = Color.YELLOW
                } else {
                    paint.color = Color.GREEN
                }
                canvas.drawRect(
                    Rect(
                        (9 - i) * size / 9,
                        size - (lastTrades[9 - i] - 1) * size / 9,
                        (8 - i) * size / 9,
                        size / 2
                    ), paint
                )
            }
        }
        canvas.drawBitmap(bitmap, Rect(0, 0, size, size), Rect(0, 0, size, size), paint)
        invalidate()
    }
}