package com.wynneplaga.beyondfilm

import android.animation.Animator
import androidx.core.animation.doOnEnd

class AnimationSet(private vararg val animations: Animator, private val defaultDuration: Long = -1) {

    init {
        if (defaultDuration > 0) {
            animations.forEach { animator  ->
                if (animator.duration == 300L) {
                    animator.duration = defaultDuration
                }
            }
        }
    }

    fun start(sequentially: Boolean = true) {
        if (sequentially) {
            animations.dropLast(1).forEachIndexed { index, animation ->
                animation.doOnEnd { animations[index + 1].start() }
            }
            animations.first().start()
        } else {
            animations.forEach(Animator::start)
        }
    }

}