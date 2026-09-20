/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.shnwaz.lyramusic.utils

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.shnwaz.lyramusic.constants.DiscordPresenceIntervalUnitKey
import com.shnwaz.lyramusic.constants.DiscordPresenceIntervalValueKey
import com.shnwaz.lyramusic.utils.dataStore

fun getPresenceIntervalMillis(context: Context): Long {
    val intervalPreset = context.dataStore[stringPreferencesKey("discordPresenceIntervalPreset")] ?: "20s"
    val customValue = context.dataStore[DiscordPresenceIntervalValueKey] ?: 30
    val customUnit = context.dataStore[DiscordPresenceIntervalUnitKey] ?: "S"

    return when (intervalPreset) {
        "Disabled" -> 0L // no throttling
        "20s" -> 20_000L
        "50s" -> 50_000L
        "1m" -> 60_000L
        "5m" -> 300_000L
        "Custom" -> {
            val safeValue = if (customUnit == "S" && customValue < 30) 30 else customValue
            val multiplier = when (customUnit) {
                "M" -> 60_000L
                "H" -> 3_600_000L
                else -> 1_000L
            }
            safeValue * multiplier
        }
        else -> 20_000L
    }
}

