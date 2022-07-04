package com.cs496.project1.player.ui.player

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
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
import jp.wasabeef.glide.transformations.BlurTransformation
import me.bogerchan.niervisualizer.NierVisualizerManager
import me.bogerchan.niervisualizer.renderer.circle.CircleBarRenderer
import me.bogerchan.niervisualizer.renderer.circle.CircleRenderer
import me.bogerchan.niervisualizer.renderer.circle.CircleSolidRenderer
import me.bogerchan.niervisualizer.renderer.circle.CircleWaveRenderer
import me.bogerchan.niervisualizer.renderer.columnar.ColumnarType1Renderer
import me.bogerchan.niervisualizer.renderer.columnar.ColumnarType2Renderer
import me.bogerchan.niervisualizer.renderer.columnar.ColumnarType3Renderer
import me.bogerchan.niervisualizer.renderer.columnar.ColumnarType4Renderer
import me.bogerchan.niervisualizer.renderer.line.LineRenderer
import me.bogerchan.niervisualizer.renderer.other.ArcStaticRenderer
import me.bogerchan.niervisualizer.util.NierAnimator

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
    private lateinit var visualizerManager : NierVisualizerManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentMusicPlayerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_music_player, container, false)
        viewModel = ViewModelProvider(this).get(MusicPlayerViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val surfaceView = binding.surfaceView
        surfaceView.apply{
            setBackgroundColor(0)
            setZOrderOnTop(true)
            holder.setFormat(PixelFormat.TRANSPARENT)
        }
        visualizerManager = NierVisualizerManager()
        val audioSessionId = getInstance().getPlayer().audioSessionId
        visualizerManager.init(audioSessionId)
        visualizerManager.start(surfaceView, arrayOf(
            CircleSolidRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#40FFFFFF")
                },
                amplification = .45f
            ),
            CircleBarRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    strokeWidth = 4f
                    color = Color.parseColor("#efe3f2ff")
                },
                modulationStrength = 1f,
                type = CircleBarRenderer.Type.TYPE_A_AND_TYPE_B,
                amplification = 1f, divisions = 8
            ),
            CircleBarRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    strokeWidth = 5f
                    color = Color.parseColor("#e3f2ff")
                },
                modulationStrength = 0.1f,
                amplification = 1.2f,
                divisions = 8
            ),
            CircleWaveRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    strokeWidth = 6f
                    color = Color.WHITE
                },
                modulationStrength = 0.2f,
                type = CircleWaveRenderer.Type.TYPE_B,
                amplification = 1f,
                animator = NierAnimator(
                    interpolator = LinearInterpolator(),
                    duration = 20000,
                    values = floatArrayOf(0f, -360f)
                )
            ),
            CircleWaveRenderer(
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    strokeWidth = 6f
                    color = Color.parseColor("#7fcee7fe")
                },
                modulationStrength = 0.2f,
                type = CircleWaveRenderer.Type.TYPE_B,
                amplification = 1f,
                divisions = 8,
                animator = NierAnimator(
                    interpolator = LinearInterpolator(),
                    duration = 20000,
                    values = floatArrayOf(0f, -360f)
                )
            )
        ))

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
//        if(mVisualizer != null) mVisualizer.release()
        visualizerManager.release()
    }

}