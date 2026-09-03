package com.arturo254.opentune.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Enum representing user rank based on total listening hours.
 * The enum name matches the display name used in storage (e.g., "Echo").
 * Each rank has an associated threshold in hours; the user attains the rank
 * when their total listening time meets or exceeds that threshold.
 */
enum class AirBeatsRank(val thresholdHours: Int) {
    Echo(1),
    Pulse(5),
    Bronze(10),
    Silver(20),
    Gold(35),
    Platinum(50),
    Diamond(75),
    Elite(100),
    Master(150),
    Legend(250),
    Mythic(400),
    Immortal(600),
    Cosmic(1000),
    Nova(1500),
    Celestial(2500),
    Godlike(4000),
    Universal(6000),
    Eternal(10000);

    companion object {
        /**
         * Returns the highest rank for which the given total listening hours meet the threshold.
         */
        fun fromHours(hours: Int): AirBeatsRank {
            return entries.lastOrNull { it.thresholdHours <= hours } ?: Echo
        }
    }
}

@Composable
fun RankBadge(
    rank: AirBeatsRank,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.size(size)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = rank.name.take(1),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
