/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.shnwaz.lyramusic.extensions

import androidx.media3.exoplayer.ExoPlayer
import timber.log.Timber

fun ExoPlayer.setOffloadEnabled(enabled: Boolean) {
    val candidates =
        listOf(
            "experimentalSetOffloadSchedulingEnabled",
            "setOffloadSchedulingEnabled",
            "setOffloadEnabled",
        )

    for (name in candidates) {
        try {
            val method = this::class.java.getMethod(name, Boolean::class.javaPrimitiveType)
            method.invoke(this, enabled)
            return
        } catch (_: NoSuchMethodException) {
        } catch (t: Throwable) {
            Timber.tag("ExoPlayerExtensions").v(t, "$name reflection failed")
            return
        }
    }

    Timber.tag("ExoPlayerExtensions").v("No offload toggle method found")
}
