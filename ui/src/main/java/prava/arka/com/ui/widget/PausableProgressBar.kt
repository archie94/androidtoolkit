package prava.arka.com.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.Transformation
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import prava.arka.com.ui.R

internal class PausableProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    @AttrRes defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {
    private val frontProgressView: View
    private val maxProgressView: View
    private var animation: PausableScaleAnimation? = null
    private var duration = DEFAULT_PROGRESS_DURATION
    private var callback: Callback? = null

    internal interface Callback {
        fun onStartProgress()
        fun onFinishProgress()
    }

    constructor(context: Context) : this(context, null) {}

    init {
        LayoutInflater.from(context).inflate(R.layout.pausable_progress, this)
        frontProgressView = findViewById(R.id.front_progress)
        maxProgressView = findViewById(R.id.max_progress) // work around
    }

    fun setDuration(duration: Long) {
        this.duration = duration
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setMax() {
        finishProgress(true)
    }

    fun setMin() {
        finishProgress(false)
    }

    fun setMinWithoutCallback() {
        maxProgressView.setBackgroundResource(R.color.progress_secondary)
        maxProgressView.visibility = View.GONE
        frontProgressView.visibility = View.GONE
        animation?.setAnimationListener(null)
        animation?.cancel()
    }

    fun setMaxWithoutCallback() {
        maxProgressView.setBackgroundResource(R.color.progress_max_active)
        maxProgressView.visibility = View.VISIBLE
        frontProgressView.visibility = View.GONE
        animation?.setAnimationListener(null)
        animation?.cancel()
    }

    private fun finishProgress(isMax: Boolean) {
        frontProgressView.visibility = View.GONE
        if (isMax) maxProgressView.setBackgroundResource(R.color.progress_max_active)
        maxProgressView.visibility = if (isMax) View.VISIBLE else View.GONE
        animation?.setAnimationListener(null)
        animation?.cancel()
        if (callback != null) {
            callback!!.onFinishProgress()
        }
    }

    fun startProgress() {
        maxProgressView.visibility = View.GONE
        frontProgressView.visibility = View.VISIBLE
        animation = PausableScaleAnimation(
            0f,
            1f,
            1f,
            1f,
            Animation.ABSOLUTE,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f
        )
        animation?.duration = duration
        animation?.interpolator = LinearInterpolator()
        animation?.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    frontProgressView.visibility = View.VISIBLE
                    callback?.onStartProgress()
                }

                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    frontProgressView.visibility = View.GONE
                    callback?.onFinishProgress()
                }
            }
        )
        animation?.fillAfter = true
        frontProgressView.startAnimation(animation)
    }

    fun pauseProgress() {
        animation?.pause()
    }

    fun resumeProgress() {
        animation?.resume()
    }

    fun clear() {
        animation?.setAnimationListener(null)
        animation?.cancel()
        animation = null
    }

    private inner class PausableScaleAnimation internal constructor(
        fromX: Float,
        toX: Float,
        fromY: Float,
        toY: Float,
        pivotXType: Int,
        pivotXValue: Float,
        pivotYType: Int,
        pivotYValue: Float
    ) :
        ScaleAnimation(
            fromX,
            toX,
            fromY,
            toY,
            pivotXType,
            pivotXValue,
            pivotYType,
            pivotYValue
        ) {
        private var mElapsedAtPause: Long = 0
        private var mPaused = false
        override fun getTransformation(
            currentTime: Long,
            outTransformation: Transformation,
            scale: Float
        ): Boolean {
            if (mPaused && mElapsedAtPause == 0L) {
                mElapsedAtPause = currentTime - startTime
            }
            if (mPaused) {
                startTime = currentTime - mElapsedAtPause
            }
            return super.getTransformation(currentTime, outTransformation, scale)
        }

        /***
         * pause animation
         */
        fun pause() {
            if (mPaused) return
            mElapsedAtPause = 0
            mPaused = true
        }

        /***
         * resume animation
         */
        fun resume() {
            mPaused = false
        }
    }

    companion object {
        private const val DEFAULT_PROGRESS_DURATION = 2000L
    }
}
