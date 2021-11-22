package com.example.spaceminingclicker

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet

class SpriteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private var x = 0
    private var y = 0
    private var spriteWidth: Int
    private var spriteHeight: Int
    private var countInLine: Int
    private var countLine: Int
    private var src_resource: Int
    private lateinit var bitmap: Bitmap
    private lateinit var paint: Paint
    private var fakeCounter = 0
    private var maxSize: Int = -1
    private var widthSpec: Int = -1
    private var heightSpec: Int = -1

    init {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.SpriteView
        )
        try {
            src_resource = attrs!!.getAttributeResourceValue(
                "http://schemas.android.com/apk/res/android",
                "src",
                0
            )
            spriteWidth = a.getDimensionPixelSize(R.styleable.SpriteView_SpriteWidth, 0)
            spriteHeight = a.getDimensionPixelSize(R.styleable.SpriteView_SpriteHeight, 0)
            countInLine = a.getInteger(R.styleable.SpriteView_CountInLine, 0)
            countLine = a.getInteger(R.styleable.SpriteView_CountLine, 0)

        } finally {
            a.recycle() // Не забывайте, это важно!
        }
        setup()
    }

    private fun setup() {
        paint = Paint()
        paint.isFilterBitmap = false
        val options = BitmapFactory.Options()
        options.inScaled = false
        bitmap = BitmapFactory.decodeResource(resources, src_resource, options)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthSpec = MeasureSpec.getSize(widthMeasureSpec)
        heightSpec = MeasureSpec.getSize(heightMeasureSpec)
        maxSize = (if (widthSpec > heightSpec ) heightSpec  else widthSpec)
        setMeasuredDimension(widthSpec, heightSpec)
    }
    private fun update() {

            if (x == countInLine - 1) {
                y = (++y) % countLine
            }
            x = (++x) % countInLine

    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(
            bitmap,
            Rect(x * spriteWidth, y * spriteHeight, (x + 1) * spriteWidth, (y + 1) * spriteHeight),
            Rect(
                widthSpec / 2 - maxSize * 95 / 200,
                heightSpec / 2 - maxSize * 95 / 200 ,
                widthSpec / 2 + maxSize * 95 / 200,
                heightSpec / 2 + maxSize * 95 / 200
            ),
            paint
        )
        update()
        invalidate()
    }

}