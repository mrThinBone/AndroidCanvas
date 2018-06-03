package my.vinhtv.androidcanvas

import android.content.Context
import android.graphics.*
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

class MessageView: AppCompatTextView {

    private val rect = RectF()
    private val path = Path()
    private val paint = Paint()
    private val strokePaint = Paint()

    private val topLeft = RectF()
    private val bottomLeft = RectF()
    private val topRight = RectF()

    private var radius = 0f
    private var xSize = 0f

    constructor(context: Context): this(context, null)

    constructor(context: Context, attributes: AttributeSet?): super(context, attributes) {
        val density = resources.displayMetrics.density
        with(paint) {
            style = Paint.Style.FILL
//            strokeWidth = 7f
            color = resources.getColor(R.color.horizon_ground_from)
        }
        with(strokePaint) {
            style = Paint.Style.STROKE
            strokeWidth = 1 * density // 1dp
            color = resources.getColor(R.color.horizon_ground_from)
        }
        val dashLength = 2 * density
        val dashGap = 2 * density
        val pathEffect = DashPathEffect(floatArrayOf(dashLength, dashGap), 0f)
        strokePaint.pathEffect = pathEffect
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()
        rect.set(0f, 0f, width, height)
        radius = height/5f
        xSize = height/4f
        // left, top, right, bottom
        topLeft.set(0f, 0f, radius*2, radius*2)
        bottomLeft.set(0f, rect.bottom - radius*2, radius*2, rect.bottom)
        topRight.set(rect.right-xSize-radius*2, 0f, rect.right-xSize, radius*2)
    }

    override fun onDraw(canvas: Canvas?) {
//        canvas?.(rect, 0f, 90f, true, paint)
        /*path.reset()
        path.arcTo(rect, 90f,90f)
        path.close()
        canvas?.drawPath(path, paint)*/
        /*canvas?.drawRect(topLeft, paint)
        canvas?.drawRect(bottomLeft, paint)
        canvas?.drawRect(topRight, paint)*/
        path.reset()
        path.moveTo(topLeft.left + radius, topLeft.top)
        path.lineTo(topRight.right - radius, topRight.top)
        path.arcTo(topRight, 270f, 90f)
        path.lineTo(rect.right - xSize, rect.bottom - xSize)
        path.lineTo(rect.right, rect.bottom)
        path.lineTo(bottomLeft.left + radius, bottomLeft.bottom)
        path.arcTo(bottomLeft, 90f, 90f)
        path.lineTo(topLeft.left, topLeft.top + radius)
        path.arcTo(topLeft, 180f, 90f)
        path.close()
        // ordinary version
        canvas?.drawPath(path, paint)

        // dash path version
//        canvas?.drawPath(path, strokePaint)
        super.onDraw(canvas)
    }
}