package me.bogerchan.niervisualizer.renderer.circle

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.animation.LinearInterpolator
import me.bogerchan.niervisualizer.renderer.IRenderer
import me.bogerchan.niervisualizer.util.NierAnimator


/**
 * Thanks to the project of android-visualizer by Felix Palmer
 * I added more cool visual effects on this basis.
 *
 * Created by BogerChan on 2017/12/9.
 */
class CircleWaveRenderer(
        private val paint: Paint = getDefaultPaint(),
        private val divisions: Int = 1,
        private val type: Type = Type.TYPE_A,
        private val modulationStrength: Float = 0.2f,
        private val amplification: Float = 3f,
        private val animator: NierAnimator = getDefaultAnimator()) : IRenderer {

    enum class Type {
        TYPE_A, TYPE_B
    }

    private var mAggresive = 0.4f
    private var mModulation = 0.0
    private var mAngleModulation = 0f
    private lateinit var mFFTPoints: FloatArray
    private val mLastDrawArea = Rect()

    companion object {
        private fun getDefaultPaint() = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 4f
            color = Color.parseColor("#e6ebfe")
        }

        private fun getDefaultAnimator() = NierAnimator(interpolator = LinearInterpolator(),
                duration = 20000,
                values = floatArrayOf(0f, 360f))
    }

    override fun onStart(captureSize: Int) {
        mFFTPoints = FloatArray(captureSize * 4)
        animator.start()
    }

    override fun onStop() {
        animator.stop()
    }

    override fun calculate(drawArea: Rect, data: ByteArray) {
        if (mLastDrawArea != drawArea) {
            mLastDrawArea.set(drawArea)
        }
        val drawHeight = drawArea.height()
        for (j in 0 until data.size / divisions) {
            // Calculate dbValue
            val count = data.size / divisions

//            val start_idx = count * (50f/22000f)
//            val end_idx = count * (4000f/22000f)
//            val i = ((end_idx - start_idx) * (j / count.toDouble()) + start_idx).toInt()

            val start_freq = 750.0
            val end_freq = 16000.0
            val i_freq = Math.pow(10.0, (Math.log10(end_freq) - Math.log10(start_freq))*(j/count.toDouble()) + Math.log10(start_freq))
            val i = Math.min((i_freq / 22000.0 * count), (count - 1.0))

            val rfk = data[(divisions * i).toInt()] * (1.0-(i % 1.0)) + data[(divisions * (i+1)).toInt()] * (i % 1.0)
            val ifk = data[(divisions * i).toInt() + 1] * (1.0-(i % 1.0)) + data[(divisions * (i+1)).toInt() + 1] * (i % 1.0)
            val magnitude = (rfk * rfk + ifk * ifk).toFloat() / 2
            val dbValue = (75 * Math.log10(magnitude.toDouble()).toFloat() * amplification)
                    .let { Math.max(0f, it+60f)  }
            val cartPoint = when (type) {
                Type.TYPE_A -> floatArrayOf((j * divisions).toFloat() / (data.size - 1), drawHeight / 2f + dbValue)
                Type.TYPE_B -> floatArrayOf((j * divisions).toFloat() / (data.size - 1), drawHeight / 2f - dbValue)
            }

            val polarPoint = toPolar(cartPoint, drawArea)
            mFFTPoints[j * 4] = polarPoint[0]
            mFFTPoints[j * 4 + 1] = polarPoint[1]

            val i_freq2 = Math.pow(10.0, (Math.log10(end_freq) - Math.log10(start_freq))*((j+1)/count.toDouble()) + Math.log10(start_freq))
            val i2 = Math.min((i_freq2 / 22000.0 * count), count - 1.0)

//            val rfk2 = data[divisions * (i2 + 0) % data.size]
//            val ifk2 = data[(divisions * (i2 + 0) + 1) % data.size]
            val rfk2 = data[(divisions * i2).toInt()] * (1.0-(i2 % 1.0)) + data[(divisions * (i2+1)).toInt()] * (i2 % 1.0)
            val ifk2 = data[(divisions * i2).toInt() + 1] * (1.0-(i2 % 1.0)) + data[(divisions * (i2+1)).toInt() + 1] * (i2 % 1.0)
            val magnitude2 = (rfk2 * rfk2 + ifk2 * ifk2).toFloat() / 2
            val dbValue2 = (75 * Math.log10(magnitude2.toDouble()).toFloat() * amplification)
                .let { Math.max(0f, it+60f)  }

            val cartPoint2 = when (type) {
                Type.TYPE_A -> floatArrayOf(((j + 1) * divisions).toFloat() / (data.size - 1) % 1f, drawHeight / 2f + dbValue2)
                Type.TYPE_B -> floatArrayOf(((j + 1) * divisions).toFloat() / (data.size - 1) % 1f, drawHeight / 2f - dbValue2)
            }

            val polarPoint2 = toPolar(cartPoint2, drawArea)
            mFFTPoints[j * 4 + 2] = polarPoint2[0]
            mFFTPoints[j * 4 + 3] = polarPoint2[1]
        }
    }

    override fun render(canvas: Canvas) {
        canvas.save()
        canvas.rotate(animator.computeCurrentValue(),
                (mLastDrawArea.left + mLastDrawArea.right) / 2F,
                (mLastDrawArea.top + mLastDrawArea.bottom) / 2F)
        canvas.drawLines(mFFTPoints, paint)
        canvas.restore()
    }

    override fun getInputDataType() = IRenderer.DataType.FFT

    private fun toPolar(cartesian: FloatArray, rect: Rect): FloatArray {
        val cX = (rect.width() / 2).toDouble()
        val cY = (rect.height() / 2).toDouble()
        val angle = cartesian[0].toDouble() * 2.0 * Math.PI
        val radius = (rect.width() / 2 * (1 - mAggresive) + mAggresive * cartesian[1] / 2) * (1 - modulationStrength + modulationStrength * (1 + Math.sin(mModulation)) / 2)
        return floatArrayOf((cX + radius * Math.sin(angle + mAngleModulation)).toFloat(), (cY + radius * Math.cos(angle + mAngleModulation)).toFloat())
    }
}