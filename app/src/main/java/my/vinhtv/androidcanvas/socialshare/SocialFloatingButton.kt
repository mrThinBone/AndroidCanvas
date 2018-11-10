package my.vinhtv.androidcanvas.socialshare

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.widget.ImageView
import my.vinhtv.androidcanvas.R

/**
 * https://stackoverflow.com/questions/43531552/how-do-i-change-the-solid-color-of-a-ripple-drawable
 * https://stackoverflow.com/questions/27787870/how-to-use-rippledrawable-programmatically-in-code-not-xml-with-android-5-0-lo
 */
@SuppressLint("ViewConstructor")
class SocialFloatingButton(context: Context, val color: Int) : ImageView(context) {

    private var iconDrawable: Int = 0
    private val bgShape: GradientDrawable

    init {
        setBackgroundResource(R.drawable.socialshare_floating_shape)
        setIcon(R.drawable.socialshare_ic_share)
        elevation = 5.0f
        setPadding(50, 50, 50, 50)
        bgShape = (background as RippleDrawable).findDrawableByLayerId(R.id.round_shape_content) as GradientDrawable
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = if(measuredWidth > measuredHeight) measuredHeight else measuredWidth
        setMeasuredDimension(w, w)
    }

    fun effect(rotation: Float, translationX: Float, color: Int) {
        this.rotation = rotation
        this.translationX = translationX
        bgShape.color = ColorStateList.valueOf(color)
    }

    fun switchIcon(id: Int) {
        if(iconDrawable != R.drawable.socialshare_ic_share) return
        //Log.d("vinhtv", "switch")
        val drawable = when(id) {
            R.id.social_share_fb -> R.drawable.socialshare_ic_facebook
            R.id.social_share_twitter -> R.drawable.socialshare_ic_twitter
            else -> R.drawable.socialshare_ic_google_plus
        }
        setIcon(drawable)
    }

    fun reset(defaultColor: Int) {
        this.rotation = 0f
        this.translationX = 0f
        bgShape.color = ColorStateList.valueOf(defaultColor)
        setIcon(R.drawable.socialshare_ic_share)
    }

    override fun setImageResource(resId: Int) {
    }

    private fun setIcon(drawable: Int) {
        iconDrawable = drawable
        super.setImageResource(drawable)
    }

    /*override fun setBackgroundColor(color: Int) {
        bgShape.color = ColorStateList.valueOf(color)
    }*/
}