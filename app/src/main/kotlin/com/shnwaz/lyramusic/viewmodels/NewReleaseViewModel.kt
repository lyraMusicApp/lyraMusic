/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.shnwaz.lyramusic.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import com.shnwaz.lyramusic.innertube.YouTube
import com.shnwaz.lyramusic.innertube.models.AlbumItem
import com.shnwaz.lyramusic.innertube.models.filterExplicit
import com.shnwaz.lyramusic.innertube.models.filterVideo
import com.shnwaz.lyramusic.constants.HideExplicitKey
import com.shnwaz.lyramusic.constants.HideVideoKey
import com.shnwaz.lyramusic.db.MusicDatabase
import com.shnwaz.lyramusic.utils.dataStore
import com.shnwaz.lyramusic.utils.get
import com.shnwaz.lyramusic.utils.reportException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NewReleaseUiState {
    data object Loading : NewReleaseUiState
    data class Success(val albums: List<AlbumItem>) : NewReleaseUiState
    data object Empty : NewReleaseUiState
    data class Error(val throwable: Throwable?) : NewReleaseUiState
}

@HiltViewModel
class NewReleaseViewModel
@Inject
constructor(
    @ApplicationContext val context: Context,
    private val database: MusicDatabase,
) : ViewModel() {
    private val _newReleaseAlbums = MutableStateFlow<List<AlbumItem>>(emptyList())
    val newReleaseAlbums = _newReleaseAlbums.asStateFlow()
    private val _uiState = MutableStateFlow<NewReleaseUiState>(NewReleaseUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun retry() {
        load()
    }

    private fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = NewReleaseUiState.Loading
            try {
                val albums = YouTube.newReleaseAlbums().getOrThrow()
                val artists: MutableMap<Int, String> = mutableMapOf()
                val favouriteArtists: MutableMap<Int, String> = mutableMapOf()
                database.allArtistsByPlayTime().first().let { list ->
                    var favIndex = 0
                    for ((artistsIndex, artist) in list.withIndex()) {
                        artists[artistsIndex] = artist.id
                        if (artist.artist.bookmarkedAt != null) {
                            favouriteArtists[favIndex] = artist.id
                            favIndex++
                        }
                    }
                }
                val filtered =
                    albums
                        .sortedBy { album ->
                            val artistIds = album.artists.orEmpty().mapNotNull { it.id }
                            val firstArtistKey =
                                artistIds.firstNotNullOfOrNull { artistId ->
                                    if (artistId in favouriteArtists.values) {
                                        favouriteArtists.entries.firstOrNull { it.value == artistId }?.key
                                    } else {
                                        artists.entries.firstOrNull { it.value == artistId }?.key
                                    }
                                } ?: Int.MAX_VALUE
                            firstArtistKey
                        }
                        .filterExplicit(context.dataStore.get(HideExplicitKey, false))
                        .filterVideo(context.dataStore.get(HideVideoKey, false))
                _newReleaseAlbums.value = filtered
                _uiState.value =
                    if (filtered.isEmpty()) NewReleaseUiState.Empty
                    else NewReleaseUiState.Success(filtered)
            } catch (t: Throwable) {
                reportException(t)
                _uiState.value = NewReleaseUiState.Error(t)
            }
        }
    }
}
