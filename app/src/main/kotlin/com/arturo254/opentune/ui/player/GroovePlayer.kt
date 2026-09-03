package com.arturo254.opentune.ui.player

import com.arturo254.opentune.ui.component.BottomSheetState
import com.arturo254.opentune.ui.component.bottomSheetDraggable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.media3.common.Player
import coil3.compose.AsyncImage
import com.arturo254.opentune.R
import com.arturo254.opentune.playback.PlayerConnection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroovePlayer(
    state: BottomSheetState,
    playerConnection: PlayerConnection,
    mediaMetadata: com.arturo254.opentune.models.MediaMetadata?,
    playbackState: Int,
    duration: Long,
    position: Long,
    sliderPosition: Long?,
    onSliderPositionChange: (Long) -> Unit,
    onSliderPositionChangeFinished: () -> Unit,
    isPlaying: Boolean,
    isLoading: Boolean,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onCollapse: () -> Unit,
    shuffleModeEnabled: Boolean,
    onShuffleClick: () -> Unit,
    repeatMode: Int,
    onRepeatClick: () -> Unit,
    onLyricsClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    val density = androidx.compose.ui.platform.LocalDensity.current.density
    val accentColor = Color(0xFF00C2FF)
    val backgroundColor = Color.White
    val textPrimary = Color(0xFF2E3345)
    val textSecondary = Color(0xFF8A8F9E)

    val queueWindows by playerConnection.queueWindows.collectAsState()
    val currentWindowIndex by playerConnection.currentWindowIndex.collectAsState()

    val canSkipPrevious = playerConnection.player.hasPreviousMediaItem()
    val canSkipNext = playerConnection.player.hasNextMediaItem()
    
    var flyingCardIndex by remember { mutableStateOf<Int?>(null) }

    Box(modifier = Modifier.fillMaxSize().bottomSheetDraggable(state)
            .background(backgroundColor)
            .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
    ) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCollapse) {
                    Icon(
                        painter = painterResource(R.drawable.expand_more),
                        contentDescription = "Collapse",
                        tint = textPrimary
                    )
                }
                Text(
                    text = "Music World",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = textPrimary
                )
                IconButton(onClick = onMenuClick) {
                    Icon(
                        painter = painterResource(R.drawable.more_vert),
                        contentDescription = "Menu",
                        tint = textPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Timers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = com.arturo254.opentune.utils.makeTimeString(position),
                    color = textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = com.arturo254.opentune.utils.makeTimeString(duration),
                    color = textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Disc / Center Animation Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (mediaMetadata != null) {
                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF0F0F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = mediaMetadata.thumbnailUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Song Info
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = mediaMetadata?.title.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = mediaMetadata?.artists?.joinToString { it.name }.orEmpty(),
                    fontSize = 14.sp,
                    color = textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Controls Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onShuffleClick) {
                    Icon(
                        painter = painterResource(if (shuffleModeEnabled) R.drawable.shuffle_on else R.drawable.shuffle),
                        contentDescription = "Shuffle",
                        tint = textPrimary
                    )
                }

                IconButton(onClick = onPrevious, enabled = canSkipPrevious) {
                    Icon(
                        painter = painterResource(R.drawable.skip_previous),
                        contentDescription = "Previous",
                        tint = if (canSkipPrevious) textPrimary else textSecondary.copy(alpha = 0.4f)
                    )
                }

                FloatingActionButton(
                    onClick = onPlayPause,
                    shape = CircleShape,
                    containerColor = accentColor,
                    contentColor = Color.White,
                    modifier = Modifier.size(64.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            painter = painterResource(if (isPlaying) R.drawable.pause else R.drawable.play),
                            contentDescription = "Play/Pause",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                IconButton(onClick = onNext, enabled = canSkipNext) {
                    Icon(
                        painter = painterResource(R.drawable.skip_next),
                        contentDescription = "Next",
                        tint = if (canSkipNext) textPrimary else textSecondary.copy(alpha = 0.4f)
                    )
                }

                IconButton(onClick = onRepeatClick) {
                        Player.REPEAT_MODE_ALL -> R.drawable.repeat_on
                        else -> R.drawable.repeat
                    }
                    val repeatTint = if (repeatMode != Player.REPEAT_MODE_OFF) accentColor else Color(0xFF8A8F9E)
                    Icon(
                        painter = painterResource(repeatIcon),
                        contentDescription = "Repeat",
                        tint = repeatTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1.25f)) // More weight below to push controls up
            Spacer(modifier = Modifier.height(80.dp)) // Padding to account for the queue cards at the bottom
        }

        // Floating Vertical Stack (Custom Animated Deck)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .offset(y = 75.dp) // Adjusted for taller cards
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f))
                    )
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            // Render up to 5 cards to allow smooth transition of leaving and entering cards
            val visibleIndices = (currentWindowIndex - 1 .. currentWindowIndex + 4).filter { it in queueWindows.indices }
            
            // Render from back to front (highest index first)
            val sortedIndices = visibleIndices.sortedByDescending { it }
            
            sortedIndices.forEach { index ->
                key(index) {
                    val window = queueWindows[index]
                    
                    val isFlying = flyingCardIndex == index
                    val flyProgress by animateFloatAsState(
                        targetValue = if (isFlying) 1f else 0f,
                        animationSpec = tween(durationMillis = 800, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                        finishedListener = {
                            if (it == 1f && isFlying) {
                                flyingCardIndex = null
                                playerConnection.player.seekToDefaultPosition(index)
                                playerConnection.player.play()
                            }
                        }
                    )
                    
                    // Smoothly animate the card's offset relative to the current playing index
                    // By subtracting 1, the currently playing song is pushed to offset -1 (hidden),
                    // and the NEXT song sits at offset 0 (front of stack).
                    val targetOffset = (index - currentWindowIndex - 1).toFloat()
                    val offsetInfo by animateFloatAsState(
                        targetValue = targetOffset,
                        animationSpec = tween(durationMillis = 500)
                    )
                    
                    val absoluteOffset = kotlin.math.abs(offsetInfo)
                    
                    // Math for the 3-card stack
                    val stackScale = if (offsetInfo > 0) {
                        1f - (offsetInfo * 0.12f).coerceIn(0f, 1f) // Increased shrink rate so back cards are smaller
                    } else {
                        1f + (absoluteOffset * 0.1f)
                    }

                    // Translation Y: future items move UP (stacking vertically). Past items move DOWN and fall away.
                    val stackTransY = if (offsetInfo > 0) {
                        -offsetInfo * 42f * density // Decreased push up so back cards stick out less
                    } else {
                        absoluteOffset * 80f * density // drop down fast
                    }

                    // Alpha: Front card fully lit, behind cards lower lit ("low lighten")
                    val stackAlpha = if (offsetInfo > 2.5f) {
                        0f // Hide cards beyond the 3rd
                    } else if (offsetInfo > 0) {
                        1f - (offsetInfo * 0.35f).coerceIn(0f, 1f)
                    } else {
                        1f - (absoluteOffset * 1.5f).coerceIn(0f, 1f)
                    }

                    // Interpolate towards flying state!
                    val finalScale = stackScale + (1f - stackScale) * flyProgress
                    val finalTransY = stackTransY + (-480f * density - stackTransY) * flyProgress
                    val finalRotationX = 360f * flyProgress
                    val finalAlpha = stackAlpha + (1f - stackAlpha) * flyProgress
                    val finalZIndex = -offsetInfo + (100f - (-offsetInfo)) * flyProgress

                    // Morph dimensions
                    val cardWidth = 300.dp + (220.dp - 300.dp) * flyProgress
                    val cardHeight = 140.dp + (220.dp - 140.dp) * flyProgress
                    val cornerRadius = 22.dp + (110.dp - 22.dp) * flyProgress

                    Box(
                        modifier = Modifier
                            .width(cardWidth)
                            .height(cardHeight)
                            .zIndex(finalZIndex)
                            .graphicsLayer {
                                scaleX = finalScale
                                scaleY = finalScale
                                rotationX = finalRotationX
                                translationY = finalTransY
                                alpha = finalAlpha
                                cameraDistance = 12f * density
                                shape = RoundedCornerShape(cornerRadius)
                                clip = true
                            }
                            .clickable {
                                if (flyingCardIndex == null) {
                                    flyingCardIndex = index
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Soft blur behind carousel element
                        AsyncImage(
                            model = window.mediaItem.mediaMetadata.artworkUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(16.dp)
                        )
                        
                        // Actual thumbnail
                        AsyncImage(
                            model = window.mediaItem.mediaMetadata.artworkUri,
                            contentDescription = "Queue Thumbnail",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(22.dp))
                        )
                        
                        // Dimming overlay for cards behind to enhance the "low lighten" effect
                        val overlayAlpha = if (offsetInfo > 0) (offsetInfo * 0.25f).coerceIn(0f, 0.6f) else 0f
                        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = overlayAlpha)))
                    }
                }
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    if (ms < 0) return "0:00"
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}








