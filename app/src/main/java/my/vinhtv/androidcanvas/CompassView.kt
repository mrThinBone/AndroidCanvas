package my.vinhtv.androidcanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by vinh.trinh on 3/16/2018.
 */
class CompassView: View {

    var bearing: Float = 0f
    private val markerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var north: String? = null
    var east: String? = null
    var south: String? = null
    var west: String? = null
    var textHeight = 0

    constructor(context: Context): super(context) {
        initCompassView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initCompassView()
    }

    init {
        north = resources.getString(R.string.cardinal_north)
        west = resources.getString(R.string.cardinal_west)
        east = resources.getString(R.string.cardinal_east)
        south = resources.getString(R.string.cardinal_south)

        with(circlePaint) {
            color = resources.getColor(R.color.background_color)
            strokeWidth = 1f
            style = Paint.Style.FILL_AND_STROKE
        }

        with(textPaint) {
            color = resources.getColor(R.color.text_color)
            textSize = 30f
        }

        with(markerPaint) {
            color = resources.getColor(R.color.marker_color)
            strokeWidth = 5f
        }

        textHeight = textPaint.measureText("yY").toInt()
    }

    private fun initCompassView() {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // The compass is a circle that fills as much space as possible
        // set the measured dimensions by figuring out the shortest boundary,
        // height or width
        val measureWidth = measure(widthMeasureSpec)
        val measureHeight = measure(heightMeasureSpec)

        val d = Math.min(measureWidth, measureHeight)
        setMeasuredDimension(d, d)
    }

    private fun measure(measureSpec: Int): Int {

        // decode measurement specifications
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        return if(specMode == MeasureSpec.UNSPECIFIED) 200 else specSize
    }

    override fun onDraw(canvas: Canvas?) {
        val w = measuredWidth
        val h = measuredHeight
        val px = (w/2).toFloat()
        val py = (h/2).toFloat()
        val radius = Math.min(px, py)
        canvas?.drawCircle(px, py, radius, circlePaint)

        canvas?.save()
        canvas?.rotate(-bearing, px, py)

        val textWidth = textPaint.measureText("W")
        val cardinalX = px - textWidth/2
        val cardinalY = py - radius + textHeight
        // draw the marker every 15 degrees and text every 45
        for (i in 0..23) {
//            Log.d("vinhtv", "$px - $px, ${py-radius} - ${py-radius+10}")
            canvas?.drawLine(px, py-radius, px, py-radius+10, markerPaint)
            canvas?.save()
            canvas?.translate(0f, textHeight.toFloat())
            if(i%6 == 0) {
                val dirString = when(i) {
                    0 -> north
                    6 -> east
                    12 -> south
                    else -> west
                }
                canvas?.drawText(dirString, cardinalX, cardinalY, textPaint)
                if(i == 0) {
                    val arrowY = (2*textHeight).toFloat()
                    canvas?.drawLine(px, arrowY, px-10, 3*textHeight.toFloat(), markerPaint)
                    canvas?.drawLine(px, arrowY, px+10, 3*textHeight.toFloat(), markerPaint)
                }
            } else if(i%3 == 0) {
                // draw text every alternate 45deg
                val angle = (i*15).toString()
                val angleTextWidth = textPaint.measureText(angle)

                val angleTextX = px-angleTextWidth/2
                val angleTextY = py-radius+textHeight
                canvas?.drawText(angle, angleTextX, angleTextY, textPaint)
            }
            canvas?.restore()
            canvas?.rotate(15f, px, py)
        }
        canvas?.restore()
    }

}