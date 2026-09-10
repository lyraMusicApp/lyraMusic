/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.arturo254.opentune.constants

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
        LibraryFilter.LIBRARY,
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

    return savedOrder + DefaultLibraryFilterOrder.filterNot(savedOrder::contains)
}

fun List<LibraryFilter>.toLibraryFilterPreference(): String {
    val orderedFilters = distinct()
    return (orderedFilters + DefaultLibraryFilterOrder.filterNot(orderedFilters::contains))
        .joinToString(",") { it.name }
}
