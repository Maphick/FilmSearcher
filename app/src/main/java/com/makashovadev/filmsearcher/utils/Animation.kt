package com.makashovadev.filmsearcher.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView


// анимация с использованием ObjectAnimator
fun objectAnimatorTranslationAnim(view: ImageView) {
    val animatorSet = AnimatorSet()
    val animDown = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0F, 100F)
    animDown.duration = 1000
    animDown.interpolator = BounceInterpolator()
    val animUp = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 100F, 0F)
    animUp.duration = 1000
    animUp.interpolator = BounceInterpolator()
    animatorSet.play(animUp).after(animDown).after(500)
    animatorSet.start()
}

// анимация с использованием ObjectAnimator
fun objectAnimatorScaleAnim(view: ImageView) {
    val animatorSet = AnimatorSet()
    val animScaleXStart = ObjectAnimator.ofFloat(view, View.SCALE_X, 1F, 2F)
    animScaleXStart.duration = 1000
    animScaleXStart.interpolator = BounceInterpolator()
    val animScaleYStart = ObjectAnimator.ofFloat(view, View.ROTATION, 0F, 90F)
    animScaleYStart.duration = 1000
    animScaleYStart.interpolator = BounceInterpolator()
    val animScaleXEnd = ObjectAnimator.ofFloat(view, View.SCALE_X, 2F, 1F)
    animScaleXEnd.duration = 1000
    animScaleXEnd.interpolator = BounceInterpolator()
    val animScaleYEnd = ObjectAnimator.ofFloat(view, View.ROTATION, 90F, 0F)
    animScaleYEnd.duration = 1000
    animScaleYEnd.interpolator = BounceInterpolator()
    animatorSet.playTogether(animScaleXStart, animScaleYStart)
    animatorSet.play(animScaleXEnd).after(animScaleXStart).after(500)
    animatorSet.play(animScaleYEnd).after(animScaleXStart).after(500)
    animatorSet.start()
}


//анимация с использованием  ViewPropertyAnimation
// обработка события нажатия на постер: появляется/исчезает
fun ViewPropertyAnimation(view: ImageView) {
    val view_alpha = if (view.alpha == 0f) 1f else 0f
    view.animate()
        .setDuration(300)
        .setInterpolator(DecelerateInterpolator())
        .alpha(view_alpha)
        .start()
}
