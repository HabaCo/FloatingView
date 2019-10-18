package github.habaco.android.view.floating

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

class AnimatorUtil {
    enum class SlideDirection {
        TopToBottom,
        BottomToTop
    }

    enum class FadeDirection {
        FadeIn,
        FadeOut
    }

    companion object {

        fun buildSimpleSlideAnimator(direction: SlideDirection, slideOffset: Float, target: View, propertyName: String = "translationY", duration: Long = 500, interpolator: Interpolator = AccelerateDecelerateInterpolator()): Animator {
            return when (direction) {
                SlideDirection.TopToBottom -> ObjectAnimator.ofFloat(target, propertyName, -slideOffset, 0f)
                SlideDirection.BottomToTop -> ObjectAnimator.ofFloat(target, propertyName, 0f, -slideOffset)
            }.apply {
                this.duration = duration
                this.interpolator = interpolator
            }
        }

        fun buildSimpleFadeAnimator(fadeDirection: FadeDirection, target: View, propertyName: String = "alpha", duration: Long = 500, interpolator: Interpolator = AccelerateDecelerateInterpolator()): Animator {
            return when (fadeDirection) {
                FadeDirection.FadeIn -> ObjectAnimator.ofFloat(target, propertyName, 0f, 1f)
                FadeDirection.FadeOut -> ObjectAnimator.ofFloat(target, propertyName, 1f, 0f)
            }.apply {
                this.duration = duration
                this.interpolator = interpolator
            }
        }
    }
}