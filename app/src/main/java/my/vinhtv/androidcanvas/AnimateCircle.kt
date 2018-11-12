package my.vinhtv.androidcanvas

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class AnimateCircle : View {

    private val paint: Paint
    private val rect: RectF

    private var blueAngle = 0f
    private var redAngle = 0f
    private var orangeAngle = 0f

    private val COLORS = intArrayOf(
        Color.parseColor("#DEDEDE"),
        Color.parseColor("#636EBE"),
        Color.parseColor("#C94D52"),
        Color.parseColor("#DB6D03")
    )

    constructor(context: Context?, attrs: AttributeSet?): this(context, attrs, -1)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(paint) {
            strokeWidth = dpToPx(30)
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeMiter = 10f
        }
        rect = RectF(0f, 0f, 0f, 0f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = dpToPx(30)
        //val gap = dpToPx(30)
        val startX = if(w>h) (w-h).div(2f) else 0f
        val startY = if(h>w) (h-w).div(2f) else 0f
        val size = if(h > w) w else h
        rect.set(startX+padding,startY+ padding,size+startX-padding,size+startY-padding)
        //minorRect.set(rect.left+gap, rect.top+gap, rect.right-gap, rect.bottom-gap)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            paint.color = COLORS[0]
            it.drawArc(rect, 270f, 360f, false, paint)
            paint.color = COLORS[3]
            it.drawArc(rect, 270f, orangeAngle, false, paint)
            paint.color = COLORS[2]
            it.drawArc(rect, 270f, redAngle, false, paint)
            paint.color = COLORS[1]
            it.drawArc(rect, 270f, blueAngle, false, paint)
        }
    }

    fun animateCrazyProgress() {
        //if(majorAngle == 360f) return
        val vAnimator = ValueAnimator()
        vAnimator.setValues(
            PropertyValuesHolder.ofFloat("b", 0f, 60f),
            PropertyValuesHolder.ofFloat("r", 0f, 120f),
            PropertyValuesHolder.ofFloat("o", 0f, 270f)
        )
        vAnimator.duration = 1000
        vAnimator.addUpdateListener {
            blueAngle = it.getAnimatedValue("b") as Float
            redAngle = it.getAnimatedValue("r") as Float
            orangeAngle= it.getAnimatedValue("o") as Float
            invalidate()
        }
        vAnimator.start()
    }


    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
}