package com.cs496.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CodeBoy.MediaFacer.AudioGet
import com.CodeBoy.MediaFacer.MediaFacer
import com.CodeBoy.MediaFacer.PictureGet
import com.CodeBoy.MediaFacer.mediaHolders.audioContent
import com.CodeBoy.MediaFacer.mediaHolders.pictureContent

class MusicListActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
        recyclerView = findViewById<RecyclerView>(R.id.recycleView)


        val allMusic : ArrayList<audioContent> = MediaFacer.withAudioContex(this).getAllAudioContent(
            AudioGet.externalContentUri)
        val musicList = allMusic.toTypedArray()

        val myAdapter = MusicAdapter(musicList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myAdapter
        myAdapter.setOnItemClickListener(object: MusicAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = intent
                intent.putExtra("filePath", musicList[position].filePath)
                setResult(RESULT_OK, intent)
                finish()
            }
        })
    }
}