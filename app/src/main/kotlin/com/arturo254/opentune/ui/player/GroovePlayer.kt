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
                    val repeatIcon = when (repeatMode) {
                        Player.REPEAT_MODE_ONE -> R.drawable.repeat_one_on
                        Player.REPEAT_MODE_ALL -> R.drawable.repeat_on
                        else -> R.drawable.repeat
                    }
                    val repeatTint = if (repeatMode != Player.REPEAT_MODE_OFF) accentColor else textSecondary
                    Icon(
                        painter = painterResource(repeatIcon),
                        contentDescription = "Repeat",
                        tint = repeatTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}








