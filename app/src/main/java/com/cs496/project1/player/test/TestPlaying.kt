package com.cs496.project1.player.test

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.CodeBoy.MediaFacer.AudioGet
import com.CodeBoy.MediaFacer.MediaFacer
import com.cs496.project1.player.ui.MusicPlayerActivity

private const val TAG = "MUSICTEST"

fun test(context: Context) {
    com.cs496.project1.player.initializePlayerService(context)
    val instance = com.cs496.project1.player.getInstance()!!
    val audioContents = MediaFacer
        .withAudioContex(context)
        .getAllAudioContent(AudioGet.externalContentUri);
    if(audioContents.count() > 0) {
        Log.d(TAG, "Music found (${audioContents.count()})")
        val content = audioContents.get(audioContents.count() - 1)
        val filePath = content.filePath
        Log.d(TAG, "Music uri ${filePath}")

        instance.add(filePath)
        instance.play(0)

        context.startActivity(Intent(context, MusicPlayerActivity::class.java))
    } else {
        Log.d(TAG, "None music found")
    }
}