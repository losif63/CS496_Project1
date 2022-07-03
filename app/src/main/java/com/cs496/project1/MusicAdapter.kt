package com.cs496.project1

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.CodeBoy.MediaFacer.mediaHolders.audioContent
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class MusicAdapter(private val dataSet: Array<audioContent>) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>()
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
        internal val container : ConstraintLayout
        internal val albumImageView : ImageView
        internal val titleTextView : TextView
        internal val artistTextView: TextView
        internal val durationTextView: TextView

        init {
            container = view.findViewById(R.id.music_item_container)
            albumImageView = view.findViewById(R.id.album_image)
            titleTextView = view.findViewById(R.id.music_title)
            artistTextView = view.findViewById(R.id.music_artist)
            durationTextView = view.findViewById(R.id.music_duration)
        }
    }

    //METHOD

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = dataSet[position].title
        holder.artistTextView.text = dataSet[position].artist
        val duration = dataSet[position].duration / 1000
        val durationMin = duration / 60
        val durationSec = (duration % 60).toString().padStart(2, '0')
        holder.durationTextView.text = "$durationMin:$durationSec"
        Glide.with(holder.albumImageView.context).load(dataSet[position].art_uri).centerCrop().into(holder.albumImageView)
        holder.container.setOnClickListener {
            mListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}