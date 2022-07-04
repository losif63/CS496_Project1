package me.bogerchan.niervisualizer.renderer.circle

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import me.bogerchan.niervisualizer.renderer.IRenderer


/**
 * Created by BogerChan on 2017/12/9.
 */
class CircleSolidRenderer(
        private val paint: Paint = getDefaultPaint(),
        private val amplification: Float = 1f) : IRenderer {

    private val mLastDrawArea = Rect()
    private var mComputedRadius = 0F

    companion object {
        private fun getDefaultPaint() = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.CYAN
        }
    }

    override fun onStart(captureSize: Int) {

    }

    override fun onStop() {
    }

    override fun calculate(drawArea: Rect, data: ByteArray) {
        if (mLastDrawArea != drawArea) {
            mLastDrawArea.set(drawArea)
        }
        var magnitude = 0f
        for(i in 7..37) {
            val m = (data[i] * data[i] + data[i+1] * data[i+1]).toFloat()
                .let { Math.max(0f, it+40f)+0f }
            magnitude += m
        }
        magnitude = magnitude / 30
        val dbValue = 75 * Math.log10(magnitude.toDouble()).toFloat()
        mComputedRadius = Math.min(mLastDrawArea.width(), mLastDrawArea.height()) *
                (0.1f + dbValue / 383 * 2.5f) * amplification * 0.375F
    }

    override fun render(canvas: Canvas) {
        canvas.save()
        canvas.drawCircle((mLastDrawArea.left + mLastDrawArea.right) / 2F,
                (mLastDrawArea.top + mLastDrawArea.bottom) / 2F,
                mComputedRadius,
                paint)
        canvas.restore()
    }

    override fun getInputDataType() = IRenderer.DataType.FFT
}