/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */

@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.arturo254.opentune.ui.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.arturo254.opentune.R
import com.arturo254.opentune.constants.MiniPlayerHeight
import com.arturo254.opentune.extensions.togglePlayPause
import com.arturo254.opentune.models.MediaMetadata
import com.arturo254.opentune.playback.PlayerConnection
import com.arturo254.opentune.together.TogetherSessionState
import com.arturo254.opentune.ui.utils.highQualityThumbnailUrlOrNull
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import com.arturo254.opentune.LocalDatabase
import com.arturo254.opentune.db.entities.ArtistEntity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage


@Composable
fun SwipeableMiniPlayerBox(
    modifier: Modifier = Modifier,
    swipeSensitivity: Float,
    swipeThumbnail: Boolean,
    playerConnection: PlayerConnection,
    layoutDirection: LayoutDirection,
    coroutineScope: CoroutineScope,
    pureBlack: Boolean = false,
    useLegacyBackground: Boolean = false,
    content: @Composable (Float) -> Unit
) {
    val offsetXAnimatable = remember { Animatable(0f) }
    var dragStartTime by remember { mutableStateOf(0L) }
    var totalDragDistance by remember { mutableFloatStateOf(0f) }

    val animationSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )

    fun calculateAutoSwipeThreshold(swipeSensitivity: Float): Int {
        return (600 / (1f + kotlin.math.exp(-(-11.44748 * swipeSensitivity + 9.04945)))).roundToInt()
    }
    val autoSwipeThreshold = calculateAutoSwipeThreshold(swipeSensitivity)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(MiniPlayerHeight)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
            .let { baseModifier ->
                if (useLegacyBackground) {
                    baseModifier.background(
                        if (pureBlack) Color.Black
                        else MaterialTheme.colorScheme.surfaceContainer
                    )
                } else {
                    baseModifier.padding(horizontal = 12.dp)
                }
            }
            .let { baseModifier ->
                if (swipeThumbnail) {
                    baseModifier.pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = {
                                dragStartTime = System.currentTimeMillis()
                                totalDragDistance = 0f
                            },
                            onDragCancel = {
                                coroutineScope.launch {
                                    offsetXAnimatable.animateTo(
                                        targetValue = 0f,
                                        animationSpec = animationSpec
                                    )
                                }
                            },
                            onHorizontalDrag = { _, dragAmount ->
                                val adjustedDragAmount =
                                    if (layoutDirection == LayoutDirection.Rtl) -dragAmount else dragAmount
                                val canSkipPrevious = playerConnection.player.previousMediaItemIndex != -1
                                val canSkipNext = playerConnection.player.nextMediaItemIndex != -1
                                val allowLeft = adjustedDragAmount < 0 && canSkipNext
                                val allowRight = adjustedDragAmount > 0 && canSkipPrevious
                                if (allowLeft || allowRight) {
                                    totalDragDistance += kotlin.math.abs(adjustedDragAmount)
                                    coroutineScope.launch {
                                        offsetXAnimatable.snapTo(offsetXAnimatable.value + adjustedDragAmount)
                                    }
                                }
                            },
                            onDragEnd = {
                                val dragDuration = System.currentTimeMillis() - dragStartTime
                                val velocity = if (dragDuration > 0) totalDragDistance / dragDuration else 0f
                                val currentOffset = offsetXAnimatable.value

                                val minDistanceThreshold = 50f
                                val velocityThreshold = (swipeSensitivity * -8.25f) + 8.5f

                                val shouldChangeSong = (
                                        kotlin.math.abs(currentOffset) > minDistanceThreshold &&
                                                velocity > velocityThreshold
                                        ) || (kotlin.math.abs(currentOffset) > autoSwipeThreshold)

                                if (shouldChangeSong) {
                                    val isRightSwipe = currentOffset > 0
                                    val canSkipPrevious = playerConnection.player.previousMediaItemIndex != -1
                                    val canSkipNext = playerConnection.player.nextMediaItemIndex != -1

                                    if (isRightSwipe && canSkipPrevious) {
                                        playerConnection.player.seekToPreviousMediaItem()
                                        if (com.arturo254.opentune.ui.screens.settings.DiscordPresenceManager.isRunning()) {
                                            try { com.arturo254.opentune.ui.screens.settings.DiscordPresenceManager.restart() } catch (_: Exception) {}
                                        }
                                    } else if (!isRightSwipe && canSkipNext) {
                                        playerConnection.player.seekToNext()
                                        if (com.arturo254.opentune.ui.screens.settings.DiscordPresenceManager.isRunning()) {
                                            try { com.arturo254.opentune.ui.screens.settings.DiscordPresenceManager.restart() } catch (_: Exception) {}
                                        }
                                    }
                                }

                                coroutineScope.launch {
                                    offsetXAnimatable.animateTo(
                                        targetValue = 0f,
                                        animationSpec = animationSpec
                                    )
                                }
                            }
                        )
                    }
                } else {
                    baseModifier
                }
            }
    ) {
        content(offsetXAnimatable.value)

        // Visual indicator
        if (offsetXAnimatable.value.absoluteValue > 50f) {
            Box(
                modifier = Modifier
                    .align(if (offsetXAnimatable.value > 0) Alignment.CenterStart else Alignment.CenterEnd)
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (offsetXAnimatable.value > 0) R.drawable.skip_previous else R.drawable.skip_next
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(
                        alpha = (offsetXAnimatable.value.absoluteValue / autoSwipeThreshold).coerceIn(0f, 1f)
                    ),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Immutable
data class MiniPlayerContentColors(
    val title: Color,
    val secondary: Color,
    val progress: Color,
    val progressTrack: Color,
    val artworkContainer: Color,
    val artworkBorder: Color,
    val primaryButtonContainer: Color,
    val primaryButtonIcon: Color,
    val secondaryButtonContainer: Color,
    val buttonIcon: Color,
    val disabledButtonIcon: Color,
    val togetherContainer: Color,
    val togetherContent: Color,
)

@Composable
fun rememberMiniPlayerContentColors(useArtworkBackground: Boolean): MiniPlayerContentColors {
    val colorScheme = MaterialTheme.colorScheme
    return remember(
        useArtworkBackground,
        colorScheme.primary,
        colorScheme.onPrimary,
        colorScheme.outline,
        colorScheme.onSurface,
        colorScheme.onSurfaceVariant,
        colorScheme.surface,
        colorScheme.surfaceContainerHighest,
        colorScheme.surfaceVariant,
        colorScheme.primaryContainer,
        colorScheme.onPrimaryContainer,
    ) {
        if (useArtworkBackground) {
            MiniPlayerContentColors(
                title = Color.White,
                secondary = Color.White.copy(alpha = 0.72f),
                progress = Color.White,
                progressTrack = Color.White.copy(alpha = 0.24f),
                artworkContainer = Color.White.copy(alpha = 0.14f),
                artworkBorder = Color.White.copy(alpha = 0.22f),
                primaryButtonContainer = Color.White.copy(alpha = 0.92f),
                primaryButtonIcon = Color.Black,
                secondaryButtonContainer = Color.Black.copy(alpha = 0.22f),
                buttonIcon = Color.White,
                disabledButtonIcon = Color.White.copy(alpha = 0.38f),
                togetherContainer = Color.White.copy(alpha = 0.16f),
                togetherContent = Color.White,
            )
        } else {
            MiniPlayerContentColors(
                title = colorScheme.onSurface,
                secondary = colorScheme.onSurfaceVariant,
                progress = colorScheme.primary,
                progressTrack = colorScheme.outline.copy(alpha = 0.18f),
                artworkContainer = colorScheme.surfaceVariant,
                artworkBorder = colorScheme.outline.copy(alpha = 0.2f),
                primaryButtonContainer = colorScheme.primary,
                primaryButtonIcon = colorScheme.onPrimary,
                secondaryButtonContainer = colorScheme.surfaceContainerHighest,
                buttonIcon = colorScheme.onSurface,
                disabledButtonIcon = colorScheme.onSurface.copy(alpha = 0.38f),
                togetherContainer = colorScheme.primaryContainer,
                togetherContent = colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
fun RowScope.MiniPlayerInfo(
    mediaMetadata: MediaMetadata,
    colors: MiniPlayerContentColors? = null,
) {
    val titleColor = colors?.title ?: MaterialTheme.colorScheme.onSurface
    val artistColor = colors?.secondary ?: MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = mediaMetadata.title,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "title"
        ) { title ->
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee()
            )
        }

        AnimatedContent(
            targetState = mediaMetadata.artists,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "artist"
        ) { artists ->
            Text(
                text = artists.joinToString { it.name },
                style = MaterialTheme.typography.bodySmall,
                color = artistColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee()
            )
        }
    }
}

@Composable
private fun MiniPlayerArtwork(
    mediaMetadata: MediaMetadata?,
    position: Long,
    duration: Long,
    isLoading: Boolean,
    colors: MiniPlayerContentColors,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(44.dp)
    ) {
        if (isLoading) {
            CircularWavyProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = colors.progress,
                trackColor = colors.progressTrack
            )
        } else {
            CircularWavyProgressIndicator(
                progress = { if (duration > 0) (position.toFloat() / duration).coerceIn(0f, 1f) else 0f },
                modifier = Modifier.fillMaxSize(),
                color = colors.progress,
                trackColor = colors.progressTrack
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(colors.artworkContainer)
                .border(
                    width = 1.dp,
                    color = colors.artworkBorder,
                    shape = CircleShape
                )
        ) {
            val thumbnailUrl = mediaMetadata?.thumbnailUrl
            if (thumbnailUrl != null) {
                AsyncImage(
                    model = thumbnailUrl.highQualityThumbnailUrlOrNull(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.opentune),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun MiniPlayerTransportButton(
    iconResId: Int,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isPrimary: Boolean = false,
    colors: MiniPlayerContentColors? = null,
) {
    val containerColor = when {
        colors != null -> if (isPrimary) colors.primaryButtonContainer else colors.secondaryButtonContainer
        isPrimary -> MaterialTheme.colorScheme.surface
        else -> Color.Transparent
    }
    val borderColor = when {
        colors != null -> Color.Transparent
        enabled -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
    }
    val iconTint = when {
        colors != null -> if (isPrimary) colors.primaryButtonIcon else if (enabled) colors.buttonIcon else colors.disabledButtonIcon
        enabled -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .size(if (isPrimary) 40.dp else 36.dp)
            .clip(CircleShape)
            .background(containerColor)
            .border(width = 1.dp, color = borderColor, shape = CircleShape)
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(if (isPrimary) 22.dp else 18.dp)
        )
    }
}

@Composable
private fun MiniPlayerTransportControls(
    isPlaying: Boolean,
    playbackState: Int,
    isLoading: Boolean,
    canSkipPrevious: Boolean,
    canSkipNext: Boolean,
    playerConnection: PlayerConnection,
    colors: MiniPlayerContentColors? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MiniPlayerTransportButton(
            iconResId = R.drawable.skip_previous,
            contentDescription = null,
            onClick = playerConnection::seekToPrevious,
            enabled = canSkipPrevious,
            colors = colors,
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(40.dp)
        ) {
            MiniPlayerTransportButton(
                iconResId = when {
                    playbackState == Player.STATE_ENDED -> R.drawable.replay
                    isPlaying -> R.drawable.pause
                    else -> R.drawable.play
                },
                contentDescription = stringResource(
                    if (playbackState == Player.STATE_ENDED || !isPlaying) R.string.play else R.string.play
                ).let {
                    if (isPlaying && playbackState != Player.STATE_ENDED) "Pause" else it
                },
                onClick = {
                    if (playbackState == Player.STATE_ENDED) {
                        playerConnection.player.seekTo(0, 0)
                        playerConnection.player.playWhenReady = true
                    } else {
                        playerConnection.player.togglePlayPause()
                    }
                },
                isPrimary = true,
                colors = colors,
            )
        }

        MiniPlayerTransportButton(
            iconResId = R.drawable.skip_next,
            contentDescription = null,
            onClick = playerConnection::seekToNext,
            enabled = canSkipNext,
            colors = colors,
        )
    }
}

@Composable
fun NewMiniPlayerContent(
    pureBlack: Boolean = false,
    position: Long,
    duration: Long,
    playerConnection: PlayerConnection,
    colors: MiniPlayerContentColors = rememberMiniPlayerContentColors(useArtworkBackground = false),
) {
    val isPlaying by playerConnection.isPlaying.collectAsState()
    val playbackState by playerConnection.playbackState.collectAsState()
    val mediaMetadata by playerConnection.mediaMetadata.collectAsState()
    val togetherSessionState by playerConnection.service.togetherSessionState.collectAsState()
    val canSkipPrevious by playerConnection.canSkipPrevious.collectAsState()
    val canSkipNext by playerConnection.canSkipNext.collectAsState()

    val isLoading = playbackState == Player.STATE_BUFFERING

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp),
    ) {
        MiniPlayerArtwork(
            mediaMetadata = mediaMetadata,
            position = position,
            duration = duration,
            isLoading = isLoading,
            colors = colors,
        )

        Spacer(modifier = Modifier.width(12.dp))

        mediaMetadata?.let {
            MiniPlayerInfo(mediaMetadata = it, colors = colors)
        } ?: Spacer(Modifier.weight(1f))

        if (togetherSessionState !is TogetherSessionState.Idle) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = colors.togetherContainer,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.all_inclusive),
                        contentDescription = stringResource(R.string.music_together),
                        tint = colors.togetherContent,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        MiniPlayerTransportControls(
            isPlaying = isPlaying,
            playbackState = playbackState,
            isLoading = isLoading,
            canSkipPrevious = canSkipPrevious,
            canSkipNext = canSkipNext,
            playerConnection = playerConnection,
            colors = colors,
        )
    }
}
