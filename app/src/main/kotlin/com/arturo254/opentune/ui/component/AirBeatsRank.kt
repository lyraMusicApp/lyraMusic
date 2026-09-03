package com.arturo254.opentune.ui.component

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
            return values().lastOrNull { it.thresholdHours <= hours } ?: Echo
        }
    }
}
