package my.vinhtv.androidcanvas.samples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_showcase_socialshare.*
import my.vinhtv.androidcanvas.R

class SocialShareActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showcase_socialshare)
        reset_button.setOnClickListener {
            m_social_share.reset()
        }
    }
}