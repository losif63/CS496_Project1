package com.cs496.project1

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.CodeBoy.MediaFacer.mediaHolders.audioContent
import com.bumptech.glide.Glide
import com.cs496.project1.player.MusicItem
import org.w3c.dom.Text

class RecycleAdapter3 (private val dataSet: Array<MusicItem>) :
    RecyclerView.Adapter<RecycleAdapter3.ViewHolder>()
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

        internal val albumArt : ImageView
        internal val trackNumber : TextView
        internal val trackTitle : TextView
        internal val trackDuration : TextView
        internal val itemContainer : ConstraintLayout

        init {
            albumArt = view.findViewById(R.id.album_art)
            trackNumber = view.findViewById(R.id.track_number)
            trackTitle = view.findViewById(R.id.track_title)
            trackDuration = view.findViewById(R.id.track_duration)
            itemContainer = view.findViewById(R.id.music_item_container)
        }
    }

    //METHOD

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleAdapter3.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_music, parent, false)
        return RecycleAdapter3.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecycleAdapter3.ViewHolder, position: Int) {

        Glide.with(holder.albumArt.context).load(dataSet[position].metadata.art_uri).centerCrop().into(holder.albumArt)
        holder.trackNumber.text = (position+1).toString()
        holder.trackTitle.text = dataSet[position].metadata.title
        val duration = dataSet[position].metadata.duration / 1000
        val durationMin = duration / 60
        val durationSec = (duration % 60).toString().padStart(2, '0')
        holder.trackDuration.text = "$durationMin:$durationSec"
        holder.itemContainer.setOnClickListener {
            mListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}