package my.vinhtv.androidcanvas

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import my.vinhtv.androidcanvas.samples.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.button -> open(AvatarShowCaseActivity::class.java)
            R.id.button2 -> open(CounterFabActivity::class.java)
            R.id.button3 -> open(MessageShowCaseActivity::class.java)
            R.id.button4 -> open(SocialShareActivity::class.java)
            R.id.button5 -> open(FabOptionsActivity::class.java)
            R.id.button6 -> open(ThreeDotsActivity::class.java)
            R.id.button7 -> open(AnimateCircleActivity::class.java)
            R.id.button8 -> open(BackgroundActivity::class.java)
        }
    }

    private fun <T> open(cls: Class<T>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }
}
