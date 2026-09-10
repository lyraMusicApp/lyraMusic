/*
 * ArchiveTune (2026)
 * Â© Rukamori â€” github.com/rukamori
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.arturo254.opentune.constants

fun String.toPlaylistTagOrder(availableTagIds: List<String>): List<String> {
    val availableIds = availableTagIds.toSet()
    val savedOrder =
        split(",")
            .map(String::trim)
            .filter { it.isNotEmpty() && it in availableIds }
            .distinct()

    return savedOrder + availableTagIds.filterNot(savedOrder::contains)
}

fun List<String>.toPlaylistTagPreference(): String =
    distinct().joinToString(",")

