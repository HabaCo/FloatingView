package github.habaco.android.view.floating

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import github.habaco.android.view.R

class FloatingView private constructor(private val context: Context) {

    private var title: CharSequence? = null
    private var message: CharSequence? = null

    var onShowAction = {}
    private var onShow = {
        contentView.visibility = View.VISIBLE
        onShowAction()
    }

    var onHideAction = {}
    private var onHide = {
        contentView.visibility = View.INVISIBLE
        if (contentView.parent != null)
            windowManager?.removeView(contentView)
        onHideAction()
    }

    var onClickAction = {}

    @NonNull
    private lateinit var contentView: View

    private var useDefaultAnimator = true

    private val windowManager: WindowManager? = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?

    private val defaultOnShowAnimator: Animator by lazy {
        val slideOffset: Int =
            if (contentView.height == 0) {
                contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                contentView.measuredHeight
            } else {
                contentView.height
            }
        Log.i(">>>>", "onShow offset:${slideOffset}")

        val animatorSlide =
            AnimatorUtil.buildSimpleSlideAnimator(
                AnimatorUtil.SlideDirection.TopToBottom,
                slideOffset.toFloat(),
                contentView
            )
        val animatorFade = AnimatorUtil.buildSimpleFadeAnimator(
            AnimatorUtil.FadeDirection.FadeIn,
            contentView
        ).apply {
            startDelay = 50
        }

        AnimatorSet().apply {
            interpolator = AccelerateDecelerateInterpolator()

            playTogether(
                animatorSlide,
                animatorFade
            )

            doOnStart {
                onShow()
            }
        }
    }

    private val defaultOnHideAnimator: Animator by lazy {
        val slideOffset: Int =
            if (contentView.height == 0) {
                contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                contentView.measuredHeight
            } else {
                contentView.height
            }

        val animatorSlide =
            AnimatorUtil.buildSimpleSlideAnimator(
                AnimatorUtil.SlideDirection.BottomToTop,
                slideOffset.toFloat(),
                contentView
            )
        val animatorFade = AnimatorUtil.buildSimpleFadeAnimator(
            AnimatorUtil.FadeDirection.FadeOut,
            contentView
        ).apply {
            startDelay = 50
        }

        AnimatorSet().apply {
            interpolator = AccelerateDecelerateInterpolator()

            playTogether(
                animatorSlide,
                animatorFade
            )

            doOnEnd {
                onHide()
            }
        }
    }

    /**
     * show with default params
     */
   fun show() {
        val params = WindowManager.LayoutParams()
        params.gravity = Gravity.TOP or Gravity.START
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        params.flags =
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

        params.format = PixelFormat.RGBA_8888
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.y =
            FloatingUtil.getStatusBarHeight(context)

        show(params)
    }

    /**
     * add view to [WindowManager] with [WindowManager.LayoutParams]
     */
    fun show(params: WindowManager.LayoutParams) {
        contentView.setOnClickListener {
            onClickAction()
            hide(it)
        }

        if (contentView.parent == null) {
            windowManager?.addView(contentView, params)

            if (useDefaultAnimator) {
                defaultOnShowAnimator.start()
            } else {
                onShow()
            }
        }
    }

    /**
     * try to hide view if holding view instance
     */
    fun hide(contentView: View = this.contentView) {
        if (contentView.parent != null && !defaultOnShowAnimator.isRunning) {

            if (useDefaultAnimator) {
                defaultOnHideAnimator.start()
            } else {
                onHide()
            }
        }
    }

    @Suppress("unused")
    class Builder (private val context: Context) {

        private val mParams = Params()

        fun title(title: CharSequence?) = this.also { mParams.title = title }
        fun title(@StringRes resId: Int) = this.also { mParams.title = context.resources.getString(resId) }

        fun message(message: CharSequence?) = this.also { mParams.message = message }
        fun message(@StringRes resId: Int) = this.also { mParams.message = context.resources.getString(resId) }

        fun icon(drawable: Drawable?) = this.also { mParams.icon = drawable }
        fun icon(@DrawableRes resId: Int) = this.also { mParams.icon = ContextCompat.getDrawable(context, resId) }

        fun contentView(view: View?) = this.also { mParams.contentView = view }
        fun contentView(@LayoutRes resId: Int) = this.also { mParams.contentView = LayoutInflater.from(context).inflate(resId, null) }

        fun useDefaultView(useDefaultView: Boolean = true) = this.also { mParams.useDefaultView = useDefaultView }

        fun useDefaultAnimator(useDefaultAnimator: Boolean = true) = this.also { mParams.useDefaultAnimator = useDefaultAnimator }

        fun onClick(onClickAction: () -> Unit) = this.also { mParams.onClickAction = onClickAction }

        fun onShow(onShowAction: () -> Unit) = this.also { mParams.onShowAction = onShowAction }

        fun onHide(onHideAction: () -> Unit) = this.also { mParams.onHideAction = onHideAction }

        @Throws(IllegalArgumentException::class)
        fun build(): FloatingView {
            return FloatingView(context).apply {
                title = mParams.title
                message = mParams.message
                onShowAction = mParams.onShowAction
                onHideAction = mParams.onHideAction
                onClickAction = mParams.onClickAction

                useDefaultAnimator = mParams.useDefaultAnimator

                if (mParams.contentView == null && mParams.useDefaultView) {
                    contentView = LayoutInflater.from(context).inflate(R.layout.view_simple_floating, FrameLayout(context), false)
                    contentView.findViewById<TextView>(R.id.textTitle)?.text = mParams.title
                    contentView.findViewById<TextView>(R.id.textMessage)?.text = mParams.message
                    contentView.findViewById<ImageView>(R.id.imageIcon)?.setImageDrawable(mParams.icon)
                } else if (mParams.contentView != null) {
                    contentView = mParams.contentView!!
                } else {
                    throw IllegalArgumentException(
                        "contentView must not be null, or you can use default floating view"
                    )
                }
            }
        }

        inner class Params {
            var title: CharSequence? = null
            var message: CharSequence? = null
            var icon: Drawable? = null
            var contentView: View? = null

            var onShowAction = { }

            var onHideAction = { }

            var onClickAction = { }

            var useDefaultView = true

            var useDefaultAnimator = true
        }
    }
}