/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.shnwaz.lyramusic.constants

enum class LibraryFilter {
    SONGS,
    ARTISTS,
    ALBUMS,
    PLAYLISTS,
    SPOTIFY,
    LIBRARY,
}

val DefaultLibraryFilterOrder =
    listOf(
        LibraryFilter.PLAYLISTS,
        LibraryFilter.SPOTIFY,
        LibraryFilter.SONGS,
        LibraryFilter.ARTISTS,
        LibraryFilter.ALBUMS,
    )

val DefaultLibraryFilterOrderPreference =
    DefaultLibraryFilterOrder.joinToString(",") { it.name }

fun String.toLibraryFilterOrder(): List<LibraryFilter> {
    val savedOrder =
        split(",")
            .mapNotNull { savedFilter ->
                DefaultLibraryFilterOrder.firstOrNull { it.name == savedFilter.trim() }
            }.distinct()
            .filter { it != LibraryFilter.LIBRARY }

    return savedOrder + DefaultLibraryFilterOrder.filterNot(savedOrder::contains)
}

fun List<LibraryFilter>.toLibraryFilterPreference(): String {
    val orderedFilters = distinct()
    return (orderedFilters + DefaultLibraryFilterOrder.filterNot(orderedFilters::contains))
        .joinToString(",") { it.name }
}
