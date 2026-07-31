/*
 * OpenTune / LyraApp Project
 * Licensed Under GPL-3.0
 */

package com.arturo254.opentune.utils

import com.arturo254.opentune.db.entities.AlbumEntity
import com.arturo254.opentune.db.entities.ArtistEntity
import com.arturo254.opentune.db.entities.Song
import com.arturo254.opentune.db.entities.SongEntity
import com.arturo254.opentune.innertube.YouTube
import com.arturo254.opentune.innertube.models.SongItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLDecoder
import java.util.regex.Pattern

data class SpotifyTrackInfo(
    val name: String,
    val artist: String,
    val album: String = "",
    val durationSeconds: Int = 0,
)

data class SpotifyPlaylistResult(
    val title: String,
    val tracks: List<SpotifyTrackInfo>,
)

object SpotifyImporter {

    private val client = OkHttpClient.Builder().build()

    fun extractSpotifyIdAndType(url: String): Pair<String, String>? {
        val trimmed = url.trim()
        val pattern = Pattern.compile("(playlist|album|track)[/:]([a-zA-Z0-9]{22})")
        val matcher = pattern.matcher(trimmed)
        if (matcher.find()) {
            val type = matcher.group(1) ?: return null
            val id = matcher.group(2) ?: return null
            return Pair(type, id)
        }
        return null
    }

    suspend fun fetchSpotifyPlaylist(url: String): SpotifyPlaylistResult = withContext(Dispatchers.IO) {
        val idAndType = extractSpotifyIdAndType(url)
            ?: throw IllegalArgumentException("Invalid Spotify URL. Example: https://open.spotify.com/playlist/...")

        val type = idAndType.first
        val id = idAndType.second

        val embedUrl = "https://open.spotify.com/embed/$type/$id"
        val request = Request.Builder()
            .url(embedUrl)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .build()

        val response = client.newCall(request).execute()
        val html = response.body?.string() ?: ""

        val tracks = mutableListOf<SpotifyTrackInfo>()
        var title = "Spotify Import"

        // Try extracting __NEXT_DATA__ JSON script tag
        val scriptPattern = Pattern.compile("<script id=\"__NEXT_DATA__\" type=\"application/json\">(.*?)</script>", Pattern.DOTALL)
        val scriptMatcher = scriptPattern.matcher(html)

        if (scriptMatcher.find()) {
            val jsonStr = scriptMatcher.group(1)
            try {
                val json = JSONObject(jsonStr)
                val entity = json.optJSONObject("props")
                    ?.optJSONObject("pageProps")
                    ?.optJSONObject("state")
                    ?.optJSONObject("data")
                    ?.optJSONObject("entity")

                if (entity != null) {
                    title = entity.optString("name", entity.optString("title", "Spotify Playlist"))

                    val trackList = entity.optJSONArray("trackList")
                    if (trackList != null) {
                        for (i in 0 until trackList.length()) {
                            val item = trackList.optJSONObject(i) ?: continue
                            val name = item.optString("title", item.optString("name", ""))
                            val artist = item.optString("subtitle", item.optString("artists", ""))
                            val album = item.optString("album", "")
                            val duration = item.optInt("duration", 0) / 1000
                            if (name.isNotBlank()) {
                                tracks.add(SpotifyTrackInfo(name = name, artist = artist, album = album, durationSeconds = duration))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Fallback: If no tracks from __NEXT_DATA__, try oEmbed endpoint
        if (tracks.isEmpty()) {
            try {
                val oembedUrl = "https://open.spotify.com/oembed?url=" + URLDecoder.decode(url, "UTF-8")
                val req = Request.Builder().url(oembedUrl).build()
                val resp = client.newCall(req).execute()
                val oembedJson = JSONObject(resp.body?.string() ?: "{}")
                val oembedTitle = oembedJson.optString("title", "")
                if (oembedTitle.isNotBlank()) {
                    title = oembedTitle
                }
            } catch (_: Exception) {}
        }

        // Secondary fallback: Extract title & tracks using Regex from embed HTML
        if (tracks.isEmpty()) {
            val titleMatch = Pattern.compile("<title>(.*?)</title>").matcher(html)
            if (titleMatch.find()) {
                title = titleMatch.group(1)?.replace(" | Spotify", "")?.trim() ?: "Spotify Import"
            }

            // Regex match for track listings embedded in HTML
            val trackPattern = Pattern.compile("\"name\":\"([^\"]+)\".*?\"artists\":\\[\\{\"name\":\"([^\"]+)\"", Pattern.DOTALL)
            val matcher = trackPattern.matcher(html)
            val seen = mutableSetOf<String>()
            while (matcher.find()) {
                val name = matcher.group(1) ?: continue
                val artist = matcher.group(2) ?: ""
                val key = "$name-$artist".lowercase()
                if (seen.add(key)) {
                    tracks.add(SpotifyTrackInfo(name = name, artist = artist))
                }
            }
        }

        if (tracks.isEmpty()) {
            throw IllegalStateException("No tracks could be found in Spotify link. Please check the playlist visibility.")
        }

        SpotifyPlaylistResult(title = title, tracks = tracks)
    }

    suspend fun resolveSpotifyTracksToSongs(
        tracks: List<SpotifyTrackInfo>,
        onProgress: (current: Int, total: Int) -> Unit = { _, _ -> },
    ): ArrayList<Song> = withContext(Dispatchers.IO) {
        val resultSongs = ArrayList<Song>()
        val total = tracks.size

        tracks.forEachIndexed { index, track ->
            onProgress(index + 1, total)
            val query = "${track.artist} ${track.name}".trim()
            if (query.isBlank()) return@forEachIndexed

            try {
                val searchResult = YouTube.search(query, YouTube.SearchFilter.FILTER_SONG).getOrNull()
                val songItem = searchResult?.items?.filterIsInstance<SongItem>()?.firstOrNull()

                if (songItem != null) {
                    val songEntity = SongEntity(
                        id = songItem.id,
                        title = songItem.title,
                        duration = songItem.duration ?: track.durationSeconds,
                        thumbnailUrl = songItem.thumbnail,
                        albumId = songItem.album?.id,
                        albumName = songItem.album?.name,
                        explicit = songItem.explicit,
                    )
                    val artists = songItem.artists.map { ArtistEntity(id = it.id ?: "", name = it.name) }
                    val album = songItem.album?.let { AlbumEntity(id = it.id, title = it.name, songCount = 0, duration = 0) }

                    resultSongs.add(Song(song = songEntity, artists = artists, album = album))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        resultSongs
    }
}
