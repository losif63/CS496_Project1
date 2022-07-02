package com.example.contentprovidertest

import android.content.res.Resources
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.CodeBoy.MediaFacer.mediaHolders.pictureContent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cs496.project1.R

class RecycleAdapter2 (private val dataSet: Array<pictureContent>) :
    RecyclerView.Adapter<RecycleAdapter2.ViewHolder>()
{
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    private lateinit var mListener: OnItemClickListener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    /** Provides a reference to the views for each data item.  */
    public class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val imageView : ImageView

        init {
            imageView = view.findViewById(R.id.imageView)
        }
    }

    //METHOD

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var imageSize = Resources.getSystem().displayMetrics.widthPixels / 3
        Glide.with(holder.imageView.context).load(dataSet[position].assertFileStringUri).centerCrop().override(imageSize).into(holder.imageView)
        holder.imageView.setOnClickListener {
            mListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


}