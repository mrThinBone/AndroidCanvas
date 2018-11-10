package my.vinhtv.androidcanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.*

class AnalogClockView: View {

    private var w = 0
    private var h = 0
    private var padding = 0
    private var fontSize = 0f
    private var numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val numbers = intArrayOf(1,2,3,4,5,6,7,8,9,10,11,12)
    private val rect = Rect()
    private var init = false

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0): super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        w = measuredWidth
        h = measuredHeight
        padding = numeralSpacing + 50
        fontSize = 36f
        val min = Math.min(w, h)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        init = true
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(!init) return
        canvas?.let {
            drawCircle(it)
            drawCenter(it)
            drawNumeral(it)
            drawHands(it)
        }
    }

    private fun drawCircle(canvas: Canvas) {
        with(paint) {
            reset()
            color = resources.getColor(android.R.color.white)
            strokeWidth = 5f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        canvas.drawCircle(w/2f, h/2f, radius + padding - 10f, paint)
    }

    private fun drawCenter(canvas: Canvas) {
        with(paint) {
            style = Paint.Style.FILL
        }
        canvas.drawCircle(w/2f, h/2f, 12f, paint)
    }

    private fun drawNumeral(canvas: Canvas) {
        with(paint) {
            textSize = fontSize
        }
        for (number in numbers) {
            val tmp = "$number"
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = w/2 + Math.cos(angle) * radius - rect.width() /2
            val y = h/2 + Math.sin(angle) * radius + rect.height() /2
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint)
        }
    }

    private fun drawHand(canvas: Canvas, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc/30 - Math.PI/2
        val handRadius = if(isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation
        canvas.drawLine(w/2f, h/2f,
                w/2 + Math.cos(angle).toFloat() * handRadius,
                h/2 + Math.sin(angle).toFloat() * handRadius,
                paint)
    }

    private fun drawHands(canvas: Canvas) {
        val c = Calendar.getInstance()
        var hour = c[Calendar.HOUR_OF_DAY]
        hour = if(hour>12) hour-12 else hour
        drawHand(canvas, hour + c[Calendar.MINUTE]/60 * 5.toDouble(), true)
        drawHand(canvas, c[Calendar.MINUTE].toDouble(), false)
        drawHand(canvas, c[Calendar.SECOND].toDouble(), false)
    }
}