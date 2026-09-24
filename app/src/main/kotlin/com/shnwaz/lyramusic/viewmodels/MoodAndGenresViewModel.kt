/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.shnwaz.lyramusic.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shnwaz.lyramusic.innertube.YouTube
import com.shnwaz.lyramusic.innertube.pages.MoodAndGenres
import com.shnwaz.lyramusic.utils.reportException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodAndGenresViewModel
@Inject
constructor() : ViewModel() {
    val moodAndGenres = MutableStateFlow<List<MoodAndGenres.Item>?>(null)

    init {
        viewModelScope.launch {
            val result = YouTube.explore()
            result
                .map { it.moodAndGenres }
                .getOrNull()
                ?.takeIf { it.isNotEmpty() }
                ?.let { moodAndGenres.value = it }

            if (moodAndGenres.value == null) {
                YouTube.moodAndGenres()
                    .onSuccess { sections ->
                        moodAndGenres.value = sections.flatMap { it.items }
                    }
                    .onFailure { reportException(it) }
            }

            if (moodAndGenres.value == null) {
                result.exceptionOrNull()?.let(::reportException)
            }
        }
    }
}
