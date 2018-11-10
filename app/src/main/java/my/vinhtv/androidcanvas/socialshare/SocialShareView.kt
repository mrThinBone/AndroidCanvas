package my.vinhtv.androidcanvas.socialshare

import android.animation.ArgbEvaluator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import my.vinhtv.androidcanvas.R

class SocialShareView: FrameLayout, View.OnClickListener, View.OnAttachStateChangeListener {

    private val mPaint: Paint
    private val mTextPaint: Paint
    private val path = Path()
    private val mFloatingButton: SocialFloatingButton
    private var mActionContainer: SocialContextActions? = null
    private var leftTextX = 0.0f
    private var rightTextX = 0.0f
    private var textY = 0.0f
    private var shared = false

    private var debugPaint: Paint

    // default floating button and rect background color
    private val mColors = intArrayOf(
        Color.parseColor("#696969"), // button
        Color.parseColor("#A9A9A9") // background
    )

    private val mActionColors = hashMapOf(
        R.id.social_share_fb to Color.parseColor("#3b5998"),
        R.id.social_share_twitter to Color.parseColor("#55acee"),
        R.id.social_share_google_plus to Color.parseColor("#CC3333")
    )

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, -1)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        mFloatingButton = SocialFloatingButton(context, mColors[0])

        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        addView(mFloatingButton, params)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(mPaint) {
            style = Paint.Style.FILL
            color = mColors[1]
        }

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(mTextPaint) {
            color = Color.parseColor("#F8F8FF")
            textSize = dpToPx(15)
        }

        debugPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(debugPaint) {
            style = Paint.Style.STROKE
            strokeWidth = dpToPx(1)
            color = Color.LTGRAY
        }

        setBackgroundColor(Color.TRANSPARENT)
        mFloatingButton.setOnClickListener{
            if(!shared) showActions()
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val gap = dpToPx(7)
        val width = w.toFloat() - gap
        val height = h.toFloat() - gap
        val curve = width/2.2f
        val topLeft = RectF(gap, gap, curve, curve)
        val bottomLeft = RectF(gap, height-curve, curve, height)
        val topRight = RectF(width - curve, gap, width, curve)
        val bottomRight = RectF(width - curve, height - curve, width, height)
        path.reset()
        path.arcTo(topLeft, 180f, 90f, true)
        path.lineTo(width-curve, gap)
        path.arcTo(topRight, 270f, 90f, false)
        path.lineTo(width, height-curve)
        path.arcTo(bottomRight, 0f, 90f, false)
        path.lineTo(curve, height)
        path.arcTo(bottomLeft, 90f, 90f)
        path.lineTo(gap, curve)
        path.close()

        val xOffset = mTextPaint.measureText("Share")
        val yOffset = mTextPaint.fontMetrics.ascent * -0.4f
        leftTextX = gap * 3
        rightTextX = width - gap*2 - xOffset
        textY = h.toFloat()/2 + yOffset
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        canvas?.drawPath(path, mPaint)
        with(canvas!!) {
            drawRect(0f, 0f, width.toFloat(), height.toFloat(), debugPaint)
            drawPath(path, mPaint)
            drawText("1.2k", leftTextX, textY, mTextPaint)
            drawText("Share", rightTextX, textY, mTextPaint)
        }
    }

    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    private fun animateFloatingButton() {
        val rotation = if(shared) -360f else 360f
        val x = if(shared) 0f else width.toFloat()-mFloatingButton.width
        val animate = mFloatingButton.animate().rotation(rotation).translationX(x)
        animate.duration = 500
        animate.start()
        shared = !shared
    }

    private fun showActions() {
        if(mActionContainer != null) return

        mActionContainer = SocialContextActions(context, this)
        val parentView = rootView.findViewById<ViewGroup>(android.R.id.content)
        with(mActionContainer!!) {
            addOnAttachStateChangeListener(this@SocialShareView)
            parentView.addView(this)
            viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    val openLocation = intArrayOf(0,0)
                    mFloatingButton.getLocationOnScreen(openLocation)
                    with(mActionContainer!!) {
                        x = openLocation[0].toFloat() - mFloatingButton.width / 3
                        y = openLocation[1].toFloat() - mFloatingButton.height
                        pivotX = 0f
                        pivotY = 0f
                        scaleX = 0.1f
                        scaleY = 0.1f
                        animate().scaleX(1.0f).scaleY(1.0f)
                            .translationYBy(mFloatingButton.height.times(-0.8f))
                            .setDuration(500)
                            .start()
                    }
                    return false
                }
            })
        }
    }

    private fun hideActions() {
        if(mActionContainer == null) return
        with(mActionContainer!!) {
            alpha = 0.0f
            dismiss()
        }
    }

    override fun onViewAttachedToWindow(p0: View?) {}

    override fun onViewDetachedFromWindow(p0: View?) {
//        Log.d("vinhtv", "context action was detached")
        mActionContainer = null
    }

    override fun onClick(v: View) {
        shared = true
        hideActions()
        val selectedActonColor: Int = mActionColors[v.id] as Int
        val colorEvaluator = ArgbEvaluator()
        //animateFloatingButton()
        val rotation = PropertyValuesHolder.ofFloat("rotation", 0f, 360f)
        val translationX = PropertyValuesHolder.ofFloat("translationX", 0f, width.toFloat()-mFloatingButton.width)
        val buttonColor = PropertyValuesHolder.ofObject("fColor", colorEvaluator, mColors[0], selectedActonColor)
        val backgroundColor = PropertyValuesHolder.ofObject("bColor", colorEvaluator, mColors[1], lighter(selectedActonColor, 200))
        val animator = ValueAnimator()
        animator.setValues(rotation, translationX, buttonColor, backgroundColor)
        animator.addUpdateListener {
//            Log.d("vinhtv", "${it.currentPlayTime}")
            if(it.currentPlayTime in 150..250) mFloatingButton.switchIcon(v.id)
            val r = it.getAnimatedValue("rotation") as Float
            val x = it.getAnimatedValue("translationX") as Float
            val fColor = it.getAnimatedValue("fColor") as Int
            val bColor =  it.getAnimatedValue("bColor") as Int
            mFloatingButton.effect(r, x, fColor)
            mPaint.color = bColor
            invalidate()
        }
        animator.duration = 500
        animator.start()
    }

    private fun lighter(color: Int, factor: Int): Int {
        //val a = Color.alpha(color)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)

        return Color.argb(
            factor,
            r,
            g,
            b
        )
    }

    fun reset() {
        mFloatingButton.reset(mColors[0])
        mPaint.color = mColors[1]
        invalidate()
        shared = false
    }
}