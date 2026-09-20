/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.shnwaz.lyramusic.kugou.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchSongResponse(
    val status: Int,
    val errcode: Int,
    val error: String,
    val data: Data,
) {
    @Serializable
    data class Data(
        val info: List<Info>,
    ) {
        @Serializable
        data class Info(
            val duration: Int,
            val hash: String,
        )
    }
}
