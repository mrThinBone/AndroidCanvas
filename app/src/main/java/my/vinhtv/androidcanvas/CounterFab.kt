package my.vinhtv.androidcanvas

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Property
import android.view.animation.OvershootInterpolator

class CounterFab : FloatingActionButton {

    private val MAX_COUNT = 99
    private val MAX_COUNT_TEXT = "99+"
    private val TEXT_SIZE_DP = 11
    private val TEXT_PADDING_DP = 1
    private val animationInterpolator = OvershootInterpolator()

    private val mTextPaint = Paint()
    private val mCirclePaint = Paint()
    private val mContentBounds = Rect()
    private val mCircleBounds = Rect()

    private var mCount = 1
    private var mText = "1"
    private var mTextHeight = 0
    private var mTextSize = 0f

    private var mAnimator: ObjectAnimator? = null

    constructor(context: Context?): this(context, null)

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {
        val density = resources.displayMetrics.density
        mTextSize = TEXT_SIZE_DP * density
        val textPadding = TEXT_PADDING_DP * density

        with(mTextPaint) {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            textSize = mTextSize
            textAlign = Paint.Align.CENTER
            typeface = Typeface.MONOSPACE
        }

        with(mCirclePaint) {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = resources.getColor(R.color.horizon_sky_to)
        }

        val textBounds = Rect()
        mTextPaint.getTextBounds(MAX_COUNT_TEXT, 0, MAX_COUNT_TEXT.length, textBounds)
        mTextHeight = textBounds.height()

        val textWidth = mTextPaint.measureText(MAX_COUNT_TEXT)
        val circleRadius = Math.max(textWidth.toInt(), mTextHeight) / 2 + textPadding.toInt()

        mCircleBounds.set(0, 0, circleRadius * 2, circleRadius * 2)

        setOnClickListener({ increase() })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(mCount > 0) {
            if(getContentRect(mContentBounds)) {
                val right = mCircleBounds.right
                mCircleBounds.offset(mContentBounds.right - right, mContentBounds.top)
            }
            val cx = mCircleBounds.centerX().toFloat()
            val cy = mCircleBounds.centerY().toFloat()

            val radius = mCircleBounds.width()/2f * mAnimationFactor
            canvas?.drawCircle(cx, cy, radius, mCirclePaint)

            mTextPaint.textSize = mTextSize * mAnimationFactor
            canvas?.drawText(mText, cx, cy + mTextHeight/2f, mTextPaint)
        }
    }

    private fun isAnimating(): Boolean {
        return mAnimator != null && mAnimator!!.isRunning
    }

    private fun increase() {
        mCount++
        mText = "$mCount"
        if(ViewCompat.isLaidOut(this)) startAnimation()
    }

    private fun startAnimation() {
        var start = 0f
        var end = 1f
        if(mCount == 0) {
            start = 1f
            end = 0f
        }
        if(isAnimating()) mAnimator?.cancel()
        mAnimator = ObjectAnimator.ofObject(this, ANIMATION_PROPERTY, null, start, end)
        with(mAnimator) {
            this?.interpolator  = animationInterpolator
            this?.duration = 300
            this?.start()
        }
    }

    private var mAnimationFactor = 1f
    private val ANIMATION_PROPERTY = object: Property<CounterFab, Float>(Float::class.java, "animation") {
        override fun get(p0: CounterFab?): Float {
            return 0f
        }

        override fun set(`object`: CounterFab?, value: Float?) {
            value?.let {
                mAnimationFactor = value
                postInvalidateOnAnimation()
            }
        }
    }
}