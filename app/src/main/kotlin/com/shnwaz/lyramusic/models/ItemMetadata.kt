/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */

package com.shnwaz.lyramusic.models

import androidx.compose.runtime.Immutable

@Immutable
data class ItemMetadata(
    val isLiked: Boolean = false,
    val isInLibrary: Boolean = false,
    val downloadState: Int? = null,
)
