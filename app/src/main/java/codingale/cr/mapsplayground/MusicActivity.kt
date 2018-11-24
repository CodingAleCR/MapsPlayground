package codingale.cr.mapsplayground

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_music.*
import org.jetbrains.anko.startService
import org.jetbrains.anko.stopService

class MusicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        music_title.startAnimation(animation)

        btn_play.setOnClickListener {
            startService<MusicService>()
        }

        btn_stop.setOnClickListener {
            stopService<MusicService>()
        }
    }
}
