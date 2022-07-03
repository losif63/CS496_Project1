package com.cs496.project1.player.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cs496.project1.R
import com.cs496.project1.player.ui.player.MusicPlayerFragment

class MusicPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MusicPlayerFragment.newInstance())
                .commitNow()
        }
    }
}