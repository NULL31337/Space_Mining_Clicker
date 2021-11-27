package com.example.spaceminingclicker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log

class CircularView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {
    private var size = -1
    private var paint = Paint()
    private var matrix_ = Matrix()
    private var path = Path()
    private var rectSize = -1
    private var widthSpec: Int = -1
    private var heightSpec: Int = -1
    private var angle = 0F
    private var speed = 1F
    private var list = listOf<Int>()

    fun setSpeed(i: Int) {
        speed = i.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthSpec = MeasureSpec.getSize(widthMeasureSpec)
        heightSpec = MeasureSpec.getSize(heightMeasureSpec)
        size = (if (widthSpec > heightSpec) heightSpec else widthSpec)
        rectSize = size / 20
        setMeasuredDimension(size, size)
    }

    fun setSpaceShips(list_: List<Int>) {
        list = list_
    }

    fun drawSpaceShips(canvas: Canvas) {
        if (widthSpec < heightSpec) {
            path.addRect(
                size.toFloat() / 2 - rectSize.toFloat() / 2,
                ((heightSpec - size) / 2).toFloat(),
                size.toFloat() / 2 + rectSize.toFloat() / 2,
                ((heightSpec - size) / 2).toFloat() + rectSize.toFloat(),
                Path.Direction.CW
            )
        } else {
            path.addRect(
                ((widthSpec - size) / 2).toFloat(),
                size.toFloat() / 2 - rectSize.toFloat() / 2,
                ((widthSpec - size) / 2).toFloat() + rectSize.toFloat(),
                size.toFloat() / 2 + rectSize.toFloat() / 2,
                Path.Direction.CW
            )
        }
        matrix_.setRotate(angle, widthSpec.toFloat() / 2, heightSpec.toFloat() / 2)
        path.transform(matrix_)

        for (i in list.size - 1 downTo 0) {
            if (i == 9) {
                repeat(list[i]) {
                    addRect(i, canvas)
                    Log.d("kekw", "drawSpaceShips: ЫЫЫЫ")
                }
            } else if (i != 10) {
                repeat(list[i] - list[i + 1]) {
                    Log.d("kekw", "drawSpaceShips: ЫЫЫЫ")
                    addRect(i, canvas)
                }
            }
        }
    }

    init {
        path.reset()
    }

    fun addRect(i: Int, canvas: Canvas) {
        matrix_.setRotate(9F, widthSpec.toFloat() / 2, heightSpec.toFloat() / 2)
        path.transform(matrix_)
        paint.color = when (i) {
            0 -> Color.LTGRAY
            1 -> Color.RED
            2 -> Color.GREEN
            3 -> Color.BLUE
            4 -> Color.YELLOW
            5 -> Color.WHITE
            6 -> Color.CYAN
            7 -> Color.DKGRAY
            8 -> Color.MAGENTA
            9 -> Color.TRANSPARENT
            else -> Color.BLACK
        }
        canvas.drawPath(path, paint)
    }

    override fun onDraw(canvas: Canvas) {
        path.reset()
        drawSpaceShips(canvas)
        angle += 0.5F * speed
        invalidate()

    }

}