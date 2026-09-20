/*
 * ArchiveTune (2026)
 * © Rukamori — github.com/rukamori
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.shnwaz.lyramusic.lyrics

import android.content.Context
import com.shnwaz.lyramusic.constants.EnablePaxsenixYouTubeLyricsKey
import com.shnwaz.lyramusic.lyrics.paxsenix.PaxsenixLyrics
import com.shnwaz.lyramusic.utils.dataStore
import com.shnwaz.lyramusic.utils.get

object PaxsenixYouTubeLyricsProvider : LyricsProvider {
    override val name = "Paxsenix: YouTube"

    override fun isEnabled(context: Context): Boolean = context.dataStore[EnablePaxsenixYouTubeLyricsKey] ?: true

    override suspend fun getLyrics(
        id: String,
        title: String,
        artist: String,
        album: String?,
        duration: Int,
    ): Result<String> = PaxsenixLyrics.getYouTubeLyrics(title, artist, duration)

    override suspend fun getAllLyrics(
        id: String,
        title: String,
        artist: String,
        album: String?,
        duration: Int,
        callback: (String) -> Unit,
    ) {
        getLyrics(id, title, artist, album, duration).onSuccess(callback)
    }
}
