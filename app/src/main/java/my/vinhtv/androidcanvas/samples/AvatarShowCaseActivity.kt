package my.vinhtv.androidcanvas.samples

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import my.vinhtv.androidcanvas.AvatarView
import my.vinhtv.androidcanvas.R

class AvatarShowCaseActivity: AppCompatActivity() {

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showcase_avatar)
        val myView = findViewById<AvatarView>(R.id.my_view)
//        myView.setOnClickListener({})
        handler.postDelayed({
            Glide.with(this)
                    .load("https://yt3.ggpht.com/a-/AJLlDp2RCJBaec1oEPJE5GFL3JbqObwkVGw96tmS7g=s900-mo-c-c0xffffffff-rj-k-no")
                    .into(myView)
        }, 2000)
    }
}