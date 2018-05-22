package my.vinhtv.androidcanvas

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

class AvatarView: AppCompatImageView {

    private val character = "V"
    private val halfBorderWidth = 1f

    private val bounds = RectF()
    private val borderBounds = RectF()
    private val textBounds = Rect()
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context): this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs)  {
        with(backgroundPaint) {
            style = Paint.Style.FILL
            color = resources.getColor(R.color.horizon_ground_from)
        }
        with(borderPaint) {
            color = resources.getColor(R.color.inner_border)
            style = Paint.Style.STROKE
            strokeWidth = halfBorderWidth*2
        }
        with(pressPaint) {
            color = resources.getColor(R.color.horizon_sky_to)
            style = Paint.Style.FILL
        }
        with(textPaint) {
            color = resources.getColor(R.color.inner_border)
            textAlign = Paint.Align.CENTER
        }
        setOnClickListener { }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        bounds.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        borderBounds.set(bounds)
        borderBounds.inset(halfBorderWidth, halfBorderWidth)
        textPaint.textSize = measuredWidth/2f
        textPaint.getTextBounds(character, 0, character.length, textBounds)
    }

    override fun onDraw(canvas: Canvas?) {
        if(drawable == null) {
            val textBottom = Math.round(bounds.height() / 2 + textBounds.height() / 2).toFloat()

            canvas?.let {
                if (isPressed) it.drawOval(bounds, pressPaint)
                else it.drawOval(bounds, backgroundPaint)
                it.drawText(character, bounds.width() / 2, textBottom, textPaint)
            }
        } else {
            super.onDraw(canvas)
        }
        canvas?.drawOval(borderBounds, borderPaint)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        invalidate()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        if(drawable != null) {
            setOnClickListener(null)
            val bitmap = (drawable as BitmapDrawable).bitmap
            val circleDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
            circleDrawable.isCircular = true
            super.setImageDrawable(circleDrawable)
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        bm?.let {
            setOnClickListener(null)
            val circleDrawable = RoundedBitmapDrawableFactory.create(resources, bm)
            circleDrawable.isCircular = true
            super.setImageDrawable(circleDrawable)
        }
    }

    override fun setImageURI(uri: Uri?) {
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            setImageBitmap(bitmap)
        }
    }

    override fun setImageResource(resId: Int) {
        val bm = BitmapFactory.decodeResource(resources, resId)
        setImageBitmap(bm)
    }
}