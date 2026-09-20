/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.shnwaz.lyramusic.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shnwaz.lyramusic.innertube.YouTube
import com.shnwaz.lyramusic.innertube.YouTube.SearchFilter.Companion.FILTER_SONG
import com.shnwaz.lyramusic.innertube.models.filterExplicit
import com.shnwaz.lyramusic.innertube.models.filterVideo
import com.shnwaz.lyramusic.innertube.pages.SearchSummaryPage
import com.shnwaz.lyramusic.constants.HideExplicitKey
import com.shnwaz.lyramusic.constants.HideVideoKey
import com.shnwaz.lyramusic.models.ItemsPage
import com.shnwaz.lyramusic.utils.dataStore
import com.shnwaz.lyramusic.utils.get
import com.shnwaz.lyramusic.utils.reportException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnlineSearchViewModel
@Inject
constructor(
    @ApplicationContext val context: Context,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val query = savedStateHandle.get<String>("query") ?: ""
    val filter = MutableStateFlow<YouTube.SearchFilter?>(null)
    var summaryPage by mutableStateOf<SearchSummaryPage?>(null)
    val viewStateMap = mutableStateMapOf<String, ItemsPage?>()

    init {
        viewModelScope.launch {
            filter.collect { filter ->
                if (query.isNotBlank()) {
                    if (filter == null) {
                        if (summaryPage == null) {
                            YouTube
                                .searchSummary(query)
                                .onSuccess {
                                    summaryPage = it.filterExplicit(context.dataStore.get(HideExplicitKey, false)).filterVideo(context.dataStore.get(HideVideoKey, false))
                                }.onFailure {
                                    reportException(it)
                                }
                        }
                        if (viewStateMap[FILTER_SONG.value] == null) {
                            loadSearchPage(FILTER_SONG)
                        }
                    } else {
                        if (viewStateMap[filter.value] == null) {
                            loadSearchPage(filter)
                        }
                    }
                }
            }
        }
    }

    fun loadMore() {
        val filter = filter.value ?: FILTER_SONG
        viewModelScope.launch {
            val viewState = viewStateMap[filter.value] ?: return@launch
            val continuation = viewState.continuation
            if (continuation != null) {
                val searchResult =
                    YouTube.searchContinuation(continuation).getOrNull() ?: return@launch
                viewStateMap[filter.value] = ItemsPage(
                    (viewState.items + searchResult.items)
                        .distinctBy { it.id }
                        .filterExplicit(context.dataStore.get(HideExplicitKey, false))
                        .filterVideo(context.dataStore.get(HideVideoKey, false)),
                    searchResult.continuation
                )
            }
        }
    }

    private suspend fun loadSearchPage(filter: YouTube.SearchFilter) {
        YouTube
            .search(query, filter)
            .onSuccess { result ->
                viewStateMap[filter.value] =
                    ItemsPage(
                        result.items
                            .distinctBy { it.id }
                            .filterExplicit(context.dataStore.get(HideExplicitKey, false))
                            .filterVideo(context.dataStore.get(HideVideoKey, false)),
                        result.continuation,
                    )
            }.onFailure {
                reportException(it)
            }
    }
}
