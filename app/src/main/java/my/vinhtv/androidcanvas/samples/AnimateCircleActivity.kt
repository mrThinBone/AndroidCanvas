package my.vinhtv.androidcanvas.samples

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_showcase_animatecircle.*
import my.vinhtv.androidcanvas.R

class AnimateCircleActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showcase_animatecircle)
        Handler().postDelayed({
            animate_circle.animateCrazyProgress()
        }, 1000)
    }

}