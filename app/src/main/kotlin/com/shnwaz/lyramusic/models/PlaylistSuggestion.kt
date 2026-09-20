/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */


package com.shnwaz.lyramusic.models

import com.shnwaz.lyramusic.innertube.models.YTItem

data class PlaylistSuggestion(
    val items: List<YTItem>,
    val continuation: String?,
    val currentQueryIndex: Int,
    val totalQueries: Int,
    val query: String,
    val hasMore: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

data class PlaylistSuggestionPage(
    val items: List<YTItem>,
    val continuation: String?
)

data class PlaylistSuggestionQuery(
    val query: String,
    val priority: Int
)