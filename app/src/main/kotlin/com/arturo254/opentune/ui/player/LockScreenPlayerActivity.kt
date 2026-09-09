package com.arturo254.opentune.ui.player

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.Player
import coil3.compose.AsyncImage
import com.arturo254.opentune.MainActivity
import com.arturo254.opentune.R
import com.arturo254.opentune.db.MusicDatabase
import com.arturo254.opentune.db.entities.Song
import com.arturo254.opentune.extensions.togglePlayPause
import com.arturo254.opentune.models.MediaMetadata
import com.arturo254.opentune.playback.MusicService
import com.arturo254.opentune.playback.MusicService.MusicBinder
import com.arturo254.opentune.playback.PlayerConnection
import com.arturo254.opentune.ui.theme.OpenTuneTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LockScreenPlayerActivity : ComponentActivity() {
    @Inject
    lateinit var database: MusicDatabase

    private var playerConnection by mutableStateOf<PlayerConnection?>(null)
    private var isMusicServiceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isMusicServiceBound = true
            if (service is MusicBinder) {
                playerConnection = PlayerConnection(this@LockScreenPlayerActivity, service, database, lifecycleScope)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isMusicServiceBound = false
            playerConnection?.dispose()
            playerConnection = null
            finish()
        }
    }

    private val screenOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT

        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(screenOffReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(screenOffReceiver, filter)
        }

        setContent {
            OpenTuneTheme(darkTheme = true, pureBlack = true) {
                val connection = playerConnection
                if (connection != null) {
                    LockScreenPlayerContent(
                        playerConnection = connection,
                        onDismiss = { finish() },
                        onOpenApp = { openMainActivity() }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.90f))
                            .clickable { finish() }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        isMusicServiceBound = bindService(
            Intent(this, MusicService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing && isMusicServiceBound) {
            try {
                unbindService(serviceConnection)
            } catch (_: Exception) {}
            isMusicServiceBound = false
            playerConnection?.dispose()
            playerConnection = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(screenOffReceiver)
        } catch (_: Exception) {}

        if (isMusicServiceBound) {
            try {
                unbindService(serviceConnection)
            } catch (_: Exception) {}
            isMusicServiceBound = false
        }
        playerConnection?.dispose()
        playerConnection = null
    }

    private fun openMainActivity() {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && keyguardManager != null) {
            keyguardManager.requestDismissKeyguard(this, object : KeyguardManager.KeyguardDismissCallback() {
                override fun onDismissSucceeded() {
                    super.onDismissSucceeded()
                    launchMainAndFinish()
                }
                override fun onDismissError() {
                    super.onDismissError()
                    launchMainAndFinish()
                }
            })
        } else {
            launchMainAndFinish()
        }
    }

    private fun launchMainAndFinish() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
        finish()
    }
}

@Composable
fun LockScreenPlayerContent(
    playerConnection: PlayerConnection,
    onDismiss: () -> Unit,
    onOpenApp: () -> Unit,
) {
    BackHandler { onDismiss() }

    val mediaMetadata by playerConnection.mediaMetadata.collectAsState()
    val isPlaying by playerConnection.isPlaying.collectAsState()
    val repeatMode by playerConnection.repeatMode.collectAsState()
    val currentSong by playerConnection.currentSong.collectAsState(initial = null)

    val isLiked = currentSong?.song?.liked == true
    val songTitle = mediaMetadata?.title?.takeIf { it.isNotBlank() } ?: "Music"
    val songArtist = mediaMetadata?.artists?.joinToString { it.name }?.takeIf { it.isNotBlank() } ?: "Turn on the music"
    val thumbnailUrl = currentSong?.song?.thumbnailUrl ?: mediaMetadata?.thumbnailUrl

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.90f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        // Dismiss button at top right
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 24.dp)
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.12f), CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.close),
                contentDescription = "Close",
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(20.dp)
            )
        }

        // Center card container
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .widthIn(max = 380.dp)
                .shadow(elevation = 24.dp, shape = RoundedCornerShape(32.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* Consume clicks so card doesn't dismiss */ },
            shape = RoundedCornerShape(32.dp),
            color = Color(0xF21C1A22),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.14f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Album artwork container with music note badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(22.dp))
                        .clickable { onOpenApp() }
                ) {
                    if (thumbnailUrl != null) {
                        AsyncImage(
                            model = thumbnailUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF2A2A38), Color(0xFF1B1A24))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.music_note),
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }

                    // Music Note Badge at Top Right
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(30.dp)
                            .background(Color.Black.copy(alpha = 0.45f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.music_note),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Title
                Text(
                    text = songTitle,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenApp() }
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Artist / Subtitle
                Text(
                    text = songArtist,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenApp() }
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Controls Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Repeat Button
                    val repeatIcon = if (repeatMode == Player.REPEAT_MODE_ONE) {
                        R.drawable.repeat_one
                    } else {
                        R.drawable.repeat
                    }
                    val repeatTint by animateColorAsState(
                        if (repeatMode != Player.REPEAT_MODE_OFF) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.White.copy(alpha = 0.45f)
                        },
                        label = "repeatTint"
                    )
                    IconButton(
                        onClick = {
                            val player = playerConnection.player
                            player.repeatMode = when (player.repeatMode) {
                                Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
                                Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
                                else -> Player.REPEAT_MODE_OFF
                            }
                        },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            painter = painterResource(repeatIcon),
                            contentDescription = "Repeat",
                            tint = repeatTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Previous Track Button
                    IconButton(
                        onClick = {
                            playerConnection.player.seekToPreviousMediaItem()
                        },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.skip_previous),
                            contentDescription = "Previous",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Play / Pause Button (Large center action)
                    IconButton(
                        onClick = {
                            playerConnection.player.togglePlayPause()
                        },
                        modifier = Modifier.size(54.dp)
                    ) {
                        Crossfade(
                            targetState = isPlaying,
                            animationSpec = tween(150),
                            label = "playPause"
                        ) { playing ->
                            Icon(
                                painter = painterResource(
                                    if (playing) R.drawable.pause else R.drawable.play
                                ),
                                contentDescription = if (playing) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }

                    // Next Track Button
                    IconButton(
                        onClick = {
                            playerConnection.player.seekToNextMediaItem()
                        },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.skip_next),
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Star / Favorite Button
                    val starTint by animateColorAsState(
                        if (isLiked) Color(0xFFFFD54F) else Color.White.copy(alpha = 0.50f),
                        label = "starTint"
                    )
                    IconButton(
                        onClick = {
                            playerConnection.toggleLike()
                        },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isLiked) R.drawable.star_filled else R.drawable.star
                            ),
                            contentDescription = "Favorite",
                            tint = starTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
