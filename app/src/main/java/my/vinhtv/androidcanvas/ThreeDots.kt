package my.vinhtv.androidcanvas


import android.animation.*
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ThreeDots : View {

    private val COLORS = intArrayOf(
        Color.parseColor("#039BE5"),
        Color.parseColor("#D32F2F"),
        Color.parseColor("#FFB300"),
        Color.parseColor("#6D4C41")
    )

    private val paint: Paint
    private val rectPaint: Paint
    private var radius = 0f
    private val dotsPosition = arrayOf(
        floatArrayOf(0f, 0f),
        floatArrayOf(0f, 0f),
        floatArrayOf(0f, 0f)
    )
    private var arrangement = intArrayOf(0, 1, 2)

    private val firstLine = Path()
    private val secondLine = Path()
    private val firstCurve = Path()
    private val secondCurve = Path()

    // for debugging
    private var pathPaint: Paint

    constructor(context: Context?, attrs: AttributeSet?): this(context, attrs, -1)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(paint) {
            style = Paint.Style.FILL
        }
        rectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(rectPaint) {
            strokeWidth = dpToPx(1)
            style = Paint.Style.STROKE
        }
        pathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(pathPaint) {
            strokeWidth = dpToPx(1)
            style = Paint.Style.STROKE
            color = Color.LTGRAY
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = measuredWidth
        val h = measuredHeight
        val gapX = (w / 4f)
        val gapY = h / 2f
        radius = h / 6f
//        centerY = (radius + gapY)
        val centerXs = floatArrayOf(0f, 0f, 0f)
        val middleXs = floatArrayOf(0f, 0f)
        val centerY = radius+gapY
        for (i in 0..2) {
            centerXs[i] = gapX * (i+1) - radius * (1-i)
            if(i > 0) middleXs[i-1] = (centerXs[i] + centerXs[i-1])/2
        }
        val firstDot = dotsPosition[0]
        firstDot[0] = centerXs[0]
        firstDot[1] = centerY
        val secondDot = dotsPosition[1]
        secondDot[0] = centerXs[1]
        secondDot[1] = centerY
        val thirdDot = dotsPosition[2]
        thirdDot[0] = centerXs[2]
        thirdDot[1] = centerY

        firstLine.moveTo(centerXs[1], centerY)
        firstLine.lineTo(centerXs[0], centerY)

        secondLine.moveTo(centerXs[2], centerY)
        secondLine.lineTo(centerXs[1], centerY)

        firstCurve.moveTo(centerXs[0], centerY)
        firstCurve.quadTo(middleXs[0], -radius, centerXs[1], centerY)

        secondCurve.moveTo(centerXs[1], centerY)
        secondCurve.quadTo(middleXs[1], -radius, centerXs[2], centerY)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val firstDot = dotsPosition[0]
        paint.color = COLORS[0]
        canvas?.drawCircle(firstDot[0], firstDot[1], radius, paint)

        val secondDot = dotsPosition[1]
        paint.color = COLORS[1]
        canvas?.drawCircle(secondDot[0], secondDot[1], radius, paint)

        val thirdDot = dotsPosition[2]
        paint.color = COLORS[2]
        canvas?.drawCircle(thirdDot[0], thirdDot[1], radius, paint)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), rectPaint)
        //canvas?.drawPath(secondCurve, pathPaint)
    }

    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    fun play() {

        val jumper = dotsPosition[arrangement[0]]
        val firstMover = dotsPosition[arrangement[1]]
        val secondMover = dotsPosition[arrangement[2]]


        val firstLinePM = PathMeasure(firstLine, false)
        val firstCurvePM = PathMeasure(firstCurve, false)
        val p1 = PropertyValuesHolder.ofFloat("line", 0f, firstLinePM.length)
        val c1 = PropertyValuesHolder.ofFloat("curve", 0f, firstCurvePM.length)
        val firstJump = ValueAnimator()
        firstJump.setValues(p1, c1)
        firstJump.duration = 400
        firstJump.addUpdateListener {
            val lineDistance = it.getAnimatedValue("line") as Float
            val curveDistance = it.getAnimatedValue("curve") as Float
            firstCurvePM.getPosTan(curveDistance, jumper, null)
            firstLinePM.getPosTan(lineDistance, firstMover, null)
            invalidate()
        }

        val secondLinePM = PathMeasure(secondLine, false)
        val secondCurvePM = PathMeasure(secondCurve, false)
        val p2 = PropertyValuesHolder.ofFloat("line", 0f, firstLinePM.length)
        val c2 = PropertyValuesHolder.ofFloat("curve", 0f, firstCurvePM.length)
        val secondJump = ValueAnimator()
        secondJump.setValues(p2, c2)
        secondJump.duration = 400
        secondJump.addUpdateListener {
            val lineDistance = it.getAnimatedValue("line") as Float
            val curveDistance = it.getAnimatedValue("curve") as Float
            secondCurvePM.getPosTan(curveDistance, jumper, null)
            secondLinePM.getPosTan(lineDistance, secondMover, null)
            invalidate()
        }
        secondJump.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                arrangement = when {
                    arrangement[0] == 0 -> intArrayOf(1,2,0)
                    arrangement[0] == 1 -> intArrayOf(2, 0 , 1)
                    else -> intArrayOf(0, 1, 2)
                }
                play()
            }
        })

        val animationSet = AnimatorSet()
        animationSet.playSequentially(firstJump, secondJump)
        animationSet.start()
    }
}