package com.cs496.project1

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
//        setTitle("Back")
//        var colorDrawable : ColorDrawable = ColorDrawable(Color.parseColor("#FFFFFF"))
//        supportActionBar?.setBackgroundDrawable(colorDrawable)

        var imageView = findViewById<ImageView>(R.id.imageView)
        val intent : Intent = intent
        val uri = Uri.parse(intent.getStringExtra("imageUri"))
        Glide.with(this).load(uri).fitCenter().into(imageView)
    }
}