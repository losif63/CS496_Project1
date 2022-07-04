package com.cs496.project1.player.ui.player

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.cs496.project1.R
import com.cs496.project1.databinding.FragmentMusicPlayerBinding
import com.cs496.project1.player.getInstance
import com.gauravk.audiovisualizer.visualizer.BarVisualizer
import com.gauravk.audiovisualizer.visualizer.BlastVisualizer
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer
import jp.wasabeef.glide.transformations.BlurTransformation

object CommonBindingAdapters{
    @BindingAdapter("app:loadAlbumArtBackgroundImage","app:placeholder")
    @JvmStatic fun loadAlbumArtBackgroundImage(imageView: ImageView, url: String, placeholder: Drawable){
        Glide.with(imageView.context)
            .load(url)
            .placeholder(placeholder)
            .error(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transform(CenterCrop(), BlurTransformation(16, 3))
            .into(imageView)
    }
}

class MusicPlayerFragment : Fragment() {

    companion object {
        fun newInstance() = MusicPlayerFragment()
    }

    private lateinit var viewModel: MusicPlayerViewModel
    private lateinit var mVisualizer : CircleLineVisualizer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentMusicPlayerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_music_player, container, false)
        viewModel = ViewModelProvider(this).get(MusicPlayerViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        mVisualizer = binding.visualizer
        val audioSessionId = getInstance().getPlayer().audioSessionId
        if(audioSessionId != -1) mVisualizer.setAudioSessionId(audioSessionId)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mVisualizer != null) mVisualizer.release()
    }

}