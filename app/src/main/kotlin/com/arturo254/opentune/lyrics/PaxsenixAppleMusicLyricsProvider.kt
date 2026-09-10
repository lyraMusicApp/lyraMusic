/*
 * ArchiveTune (2026)
 * © Rukamori — github.com/rukamori
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.arturo254.opentune.lyrics

import android.content.Context
import com.arturo254.opentune.constants.EnablePaxsenixAppleMusicLyricsKey
import com.arturo254.opentune.lyrics.paxsenix.PaxsenixLyrics
import com.arturo254.opentune.utils.dataStore
import com.arturo254.opentune.utils.get

object PaxsenixAppleMusicLyricsProvider : LyricsProvider {
    override val name = "Paxsenix: Apple Music"

    override fun isEnabled(context: Context): Boolean = context.dataStore[EnablePaxsenixAppleMusicLyricsKey] ?: true

    override suspend fun getLyrics(
        id: String,
        title: String,
        artist: String,
        album: String?,
        duration: Int,
    ): Result<String> = PaxsenixLyrics.getAppleMusicLyrics(title, artist, duration)

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
