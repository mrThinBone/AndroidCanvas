package my.vinhtv.androidcanvas.socialshare

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.socialshare_context_actions.view.*
import my.vinhtv.androidcanvas.R

@SuppressLint("ViewConstructor")
class SocialContextActions
(context: Context, clickListener: OnClickListener) : LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.socialshare_context_actions, this, true)
        social_share_fb.setOnClickListener(clickListener)
        social_share_google_plus.setOnClickListener(clickListener)
        social_share_twitter.setOnClickListener(clickListener)
    }

    fun dismiss() {
        (parent as ViewGroup).removeView(this)
    }

}