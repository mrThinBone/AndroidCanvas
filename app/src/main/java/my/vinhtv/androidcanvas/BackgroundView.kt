package my.vinhtv.androidcanvas

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import kotlin.math.roundToInt


class BackgroundView: FrameLayout, ViewTreeObserver.OnScrollChangedListener {

    private val paint: Paint
    private val rect: Rect
    private val path: Path
    private val angle = 30f
    private var mY = 0
    private val COLORS = intArrayOf(
//        Color.parseColor("#044fab"),
//        Color.parseColor("#21d6d3"),
//        Color.parseColor("#933c94"),
        Color.parseColor("#43b4ef"),
        Color.parseColor("#d40845")
    )

    // for debugging
    private val debugPaint: Paint

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, -1)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(paint) {
            style = Paint.Style.FILL
        }
        debugPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(debugPaint) {
            strokeWidth = dpToPx(1)
            style = android.graphics.Paint.Style.STROKE
            color = android.graphics.Color.LTGRAY
        }
        path = Path()
        rect = Rect(0,0,0,0)
        setWillNotDraw(false)

        viewTreeObserver.addOnScrollChangedListener(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mY = (h*angle/100).roundToInt()
        /** solid color background */
//        paint.color =

        /** image background */
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.bg)
        paint.shader = BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        /** gradient background */
        //https://stackoverflow.com/questions/25934678/how-to-rotate-the-lineargradient-in-a-given-shape
//        paint.shader = LinearGradient(0f,0f,0f, h.toFloat(),
//            COLORS, floatArrayOf(0.01f, 0.99f), Shader.TileMode.MIRROR)
        calculatePath(mY)
    }

    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawPath(path, paint)
        }
    }

    override fun onScrollChanged() {
        getLocalVisibleRect(rect)
        val top = rect.top
        val bottom = rect.bottom
        if(top > 0 && bottom > 0) {
            val diff = bottom - top
            if(diff in 0..mY) {
                calculatePath(diff)
                invalidate()
            }
        }
    }

    private fun calculatePath(diff: Int) {
        val w = width.toFloat()
        val h = height.toFloat()
        path.reset()
        path.moveTo(0f, h - diff)
        path.lineTo(w, h)
        path.lineTo(w, 0f)
        path.lineTo(0f, 0f)
        path.close()
    }

}