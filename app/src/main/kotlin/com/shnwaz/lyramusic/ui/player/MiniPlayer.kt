/*
 * Lyra Music Project (2026)
 * Shnwaz (github.com/shnwazdeveloper)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.shnwaz.lyramusic.ui.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.shnwaz.lyramusic.LocalPlayerConnection
import com.shnwaz.lyramusic.R
import com.shnwaz.lyramusic.constants.LiquidGlassNavBarKey
import com.shnwaz.lyramusic.constants.MiniPlayerBackgroundStyle
import com.shnwaz.lyramusic.constants.MiniPlayerBackgroundStyleKey
import com.shnwaz.lyramusic.constants.MiniPlayerHeight
import com.shnwaz.lyramusic.constants.UseAppleMiniPlayerKey
import com.shnwaz.lyramusic.constants.UseNewMiniPlayerDesignKey
import com.shnwaz.lyramusic.extensions.togglePlayPause
import com.shnwaz.lyramusic.constants.SwipeSensitivityKey
import com.shnwaz.lyramusic.constants.SwipeThumbnailKey
import com.shnwaz.lyramusic.ui.theme.PlayerColorExtractor
import com.shnwaz.lyramusic.utils.rememberEnumPreference
import com.shnwaz.lyramusic.utils.rememberPreference
import kotlin.math.roundToInt

private const val MiniPlayerPaletteCacheSize = 24

@Composable
fun MiniPlayer(
    position: Long,
    duration: Long,
    modifier: Modifier = Modifier,
    pureBlack: Boolean,
    isPairedWithNavigation: Boolean = false,
) {
    val useNewMiniPlayerDesign by rememberPreference(UseNewMiniPlayerDesignKey, defaultValue = true)
    val useAppleMiniPlayer by rememberPreference(UseAppleMiniPlayerKey, defaultValue = false)

    when {
        useAppleMiniPlayer -> AppleMiniPlayer(position, duration, modifier, pureBlack)
        useNewMiniPlayerDesign -> NewMiniPlayer(position, duration, modifier, pureBlack, isPairedWithNavigation)
        else -> ClassicMiniPlayer(position, duration, modifier, pureBlack)
    }
}

@Composable
private fun ClassicMiniPlayer(
    position: Long,
    duration: Long,
    modifier: Modifier,
    pureBlack: Boolean,
) {
    val playerConnection = LocalPlayerConnection.current ?: return
    val metadata by playerConnection.mediaMetadata.collectAsState()
    val isPlaying by playerConnection.isPlaying.collectAsState()
    val playbackState by playerConnection.playbackState.collectAsState()
    val canSkipNext by playerConnection.canSkipNext.collectAsState()
    val swipeSensitivity by rememberPreference(SwipeSensitivityKey, 0.73f)
    val swipeThumbnail by rememberPreference(SwipeThumbnailKey, true)
    val backgroundStyle by rememberEnumPreference(MiniPlayerBackgroundStyleKey, MiniPlayerBackgroundStyle.THEME)
    val scope = rememberCoroutineScope()

    SwipeableMiniPlayerBox(
        modifier = modifier,
        swipeSensitivity = swipeSensitivity,
        swipeThumbnail = swipeThumbnail,
        playerConnection = playerConnection,
        layoutDirection = LocalLayoutDirection.current,
        coroutineScope = scope,
        pureBlack = pureBlack,
        useLegacyBackground = false,
    ) { offset ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp)),
        ) {
            MiniPlayerBackground(
                style = backgroundStyle,
                palette = null,
                artworkUrl = metadata?.thumbnailUrl,
                containerColor = if (pureBlack) Color.Black else MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxSize(),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offset.roundToInt(), 0) }
                    .padding(horizontal = 12.dp),
            ) {
            metadata?.let { song ->
                AsyncImage(
                    model = song.thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                )
                Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                    Text(song.title, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.basicMarquee(), fontWeight = FontWeight.Medium)
                    Text(song.artists.joinToString { it.name }, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }
            } ?: Spacer(Modifier.weight(1f))
            IconButton(onClick = {
                if (playbackState == Player.STATE_ENDED) playerConnection.player.seekTo(0, 0)
                playerConnection.player.togglePlayPause()
            }) {
                Icon(painterResource(if (isPlaying) R.drawable.pause else R.drawable.play), null)
            }
            IconButton(onClick = playerConnection::seekToNext, enabled = canSkipNext) {
                Icon(painterResource(R.drawable.skip_next), null)
            }
            }
        }
    }
}

@Composable
private fun AppleMiniPlayer(
    position: Long,
    duration: Long,
    modifier: Modifier,
    pureBlack: Boolean,
) {
    val playerConnection = LocalPlayerConnection.current ?: return
    val metadata by playerConnection.mediaMetadata.collectAsState()
    val isPlaying by playerConnection.isPlaying.collectAsState()
    val playbackState by playerConnection.playbackState.collectAsState()
    val canSkipNext by playerConnection.canSkipNext.collectAsState()
    val swipeSensitivity by rememberPreference(SwipeSensitivityKey, 0.73f)
    val swipeThumbnail by rememberPreference(SwipeThumbnailKey, true)
    val backgroundStyle by rememberEnumPreference(MiniPlayerBackgroundStyleKey, MiniPlayerBackgroundStyle.THEME)
    val scope = rememberCoroutineScope()
    val progress = if (duration > 0) (position.toFloat() / duration).coerceIn(0f, 1f) else 0f

    SwipeableMiniPlayerBox(
        modifier = modifier,
        swipeSensitivity = swipeSensitivity,
        swipeThumbnail = swipeThumbnail,
        playerConnection = playerConnection,
        layoutDirection = LocalLayoutDirection.current,
        coroutineScope = scope,
        pureBlack = pureBlack,
    ) { offset ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp)),
        ) {
            MiniPlayerBackground(
                style = backgroundStyle,
                palette = null,
                artworkUrl = metadata?.thumbnailUrl,
                containerColor = if (pureBlack) Color.Black else MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxSize(),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offset.roundToInt(), 0) }
                    .padding(horizontal = 10.dp),
            ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(50.dp).clip(CircleShape),
            ) {
                AsyncImage(
                    model = metadata?.thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.34f)))
                Icon(
                    painterResource(if (isPlaying) R.drawable.pause else R.drawable.play),
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp).clickable {
                        if (playbackState == Player.STATE_ENDED) playerConnection.player.seekTo(0, 0)
                        playerConnection.player.togglePlayPause()
                    },
                )
            }
            Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                Text(metadata?.title.orEmpty(), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold, modifier = Modifier.basicMarquee())
                Text(metadata?.artists?.joinToString { it.name }.orEmpty(), maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                Box(Modifier.fillMaxWidth().padding(top = 4.dp).height(2.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f))) {
                    Box(Modifier.fillMaxWidth(progress).height(2.dp).background(MaterialTheme.colorScheme.primary))
                }
            }
            IconButton(onClick = playerConnection::seekToNext, enabled = canSkipNext) {
                Icon(painterResource(R.drawable.skip_next), null)
            }
            }
        }
    }
}

@Composable
private fun NewMiniPlayer(
    position: Long,
    duration: Long,
    modifier: Modifier = Modifier,
    pureBlack: Boolean,
    isPairedWithNavigation: Boolean = false,
) {
    val playerConnection = LocalPlayerConnection.current ?: return
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val coroutineScope = rememberCoroutineScope()
    val swipeSensitivity by rememberPreference(SwipeSensitivityKey, 0.73f)
    val swipeThumbnail by rememberPreference(SwipeThumbnailKey, true)
    val liquidGlass by rememberPreference(LiquidGlassNavBarKey, defaultValue = false)
    val miniPlayerBackgroundStyle by rememberEnumPreference(
        key = MiniPlayerBackgroundStyleKey,
        defaultValue = MiniPlayerBackgroundStyle.THEME,
    )
    val mediaMetadata by playerConnection.mediaMetadata.collectAsState()
    var gradientColors by remember {
        mutableStateOf<List<Color>>(emptyList())
    }
    val gradientColorsCache =
        remember {
            object : LinkedHashMap<String, List<Color>>(MiniPlayerPaletteCacheSize, 0.75f, true) {
                override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, List<Color>>?): Boolean =
                    size > MiniPlayerPaletteCacheSize
            }
        }
    val fallbackColor = MaterialTheme.colorScheme.surface.toArgb()
    val shouldUseArtworkBackground = miniPlayerBackgroundStyle != MiniPlayerBackgroundStyle.THEME

    LaunchedEffect(
        mediaMetadata?.id,
        mediaMetadata?.thumbnailUrl,
        shouldUseArtworkBackground,
        fallbackColor,
    ) {
        if (!shouldUseArtworkBackground) {
            gradientColors = emptyList()
            return@LaunchedEffect
        }

        val currentMetadata = mediaMetadata
        val thumbnailUrl = currentMetadata?.thumbnailUrl
        if (currentMetadata == null || thumbnailUrl.isNullOrBlank()) {
            gradientColors = emptyList()
            return@LaunchedEffect
        }

        val cachedColors = gradientColorsCache[currentMetadata.id]
        if (cachedColors != null) {
            gradientColors = cachedColors
            return@LaunchedEffect
        }

        val request =
            ImageRequest
                .Builder(context)
                .data(thumbnailUrl)
                .size(PlayerColorExtractor.Config.IMAGE_SIZE, PlayerColorExtractor.Config.IMAGE_SIZE)
                .allowHardware(false)
                .build()

        val extractedColors =
            runCatching {
                val result =
                    withContext(Dispatchers.IO) {
                        context.imageLoader.execute(request)
                    }
                val bitmap = result.image?.toBitmap() ?: return@runCatching emptyList()
                val palette =
                    withContext(Dispatchers.Default) {
                        Palette
                            .from(bitmap)
                            .maximumColorCount(PlayerColorExtractor.Config.MAX_COLOR_COUNT)
                            .resizeBitmapArea(PlayerColorExtractor.Config.BITMAP_AREA)
                            .generate()
                    }
                PlayerColorExtractor.extractGradientColors(
                    palette = palette,
                    fallbackColor = fallbackColor,
                )
            }.getOrDefault(emptyList())

        if (extractedColors.isNotEmpty()) {
            gradientColorsCache[currentMetadata.id] = extractedColors
        }
        gradientColors = extractedColors
    }

    val backgroundPalette =
        remember(gradientColors) {
            MiniPlayerBackgroundPalette.from(gradientColors)
        }
    val effectiveBackgroundStyle =
        when {
            miniPlayerBackgroundStyle == MiniPlayerBackgroundStyle.THEME -> MiniPlayerBackgroundStyle.THEME
            miniPlayerBackgroundStyle in setOf(
                MiniPlayerBackgroundStyle.GRADIENT,
                MiniPlayerBackgroundStyle.GLOW,
                MiniPlayerBackgroundStyle.GLOW_ANIMATED,
            ) && backgroundPalette == null -> MiniPlayerBackgroundStyle.THEME
            else -> miniPlayerBackgroundStyle
        }

    val contentColors =
        rememberMiniPlayerContentColors(
            useArtworkBackground = effectiveBackgroundStyle != MiniPlayerBackgroundStyle.THEME,
        )

    val miniPlayerShape =
        remember(isPairedWithNavigation) {
            if (isPairedWithNavigation) {
                RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp,
                )
            } else {
                RoundedCornerShape(32.dp)
            }
        }

    val miniPlayerContainerColor =
        when {
            liquidGlass && pureBlack -> Color.Black.copy(alpha = 0.70f)
            liquidGlass -> MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.78f)
            else -> MaterialTheme.colorScheme.surfaceContainer
        }

    val glassModifier =
        if (liquidGlass) {
            Modifier
                .border(
                    width = 0.8.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = if (pureBlack) 0.22f else 0.38f),
                            Color.White.copy(alpha = if (pureBlack) 0.04f else 0.08f),
                        )
                    ),
                    shape = miniPlayerShape,
                )
                .drawWithContent {
                    drawContent()
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = if (pureBlack) 0.10f else 0.18f),
                                Color.Transparent,
                            ),
                            start = Offset.Zero,
                            end = Offset(size.width, 0f),
                        ),
                        size = size.copy(height = size.height * 0.42f),
                        cornerRadius = CornerRadius(size.height / 2f, size.height / 2f),
                    )
                }
        } else {
            Modifier
        }

    SwipeableMiniPlayerBox(
        modifier = modifier,
        swipeSensitivity = swipeSensitivity,
        swipeThumbnail = swipeThumbnail,
        playerConnection = playerConnection,
        layoutDirection = layoutDirection,
        coroutineScope = coroutineScope,
        pureBlack = pureBlack,
        useLegacyBackground = false,
    ) { offsetX ->
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(MiniPlayerHeight)
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .clip(miniPlayerShape)
                    .then(glassModifier),
        ) {
            MiniPlayerBackground(
                style = effectiveBackgroundStyle,
                palette = backgroundPalette,
                artworkUrl = mediaMetadata?.thumbnailUrl,
                containerColor = miniPlayerContainerColor,
                modifier = Modifier.fillMaxSize(),
            )
            NewMiniPlayerContent(
                pureBlack = pureBlack,
                position = position,
                duration = duration,
                playerConnection = playerConnection,
                colors = contentColors,
            )
        }
    }
}

@Composable
private fun MiniPlayerBackground(
    style: MiniPlayerBackgroundStyle,
    palette: MiniPlayerBackgroundPalette?,
    artworkUrl: String?,
    containerColor: Color,
    modifier: Modifier = Modifier,
) {
    val fallbackPalette = MiniPlayerBackgroundPalette(
        first = MaterialTheme.colorScheme.primary,
        second = MaterialTheme.colorScheme.secondary,
        third = MaterialTheme.colorScheme.tertiary,
        fourth = MaterialTheme.colorScheme.primaryContainer,
    )
    val colors = palette ?: fallbackPalette

    when (style) {
        MiniPlayerBackgroundStyle.THEME -> {
            Box(
                modifier = modifier.background(containerColor),
            )
        }

        MiniPlayerBackgroundStyle.GRADIENT -> {
            Box(modifier = modifier) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colorStops =
                                        arrayOf(
                                            0f to colors.first.copy(alpha = 0.95f),
                                            0.52f to colors.second.copy(alpha = 0.82f),
                                            1f to colors.third.copy(alpha = 0.72f),
                                        ),
                                ),
                            ),
                )
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.32f)),
                )
            }
        }

        MiniPlayerBackgroundStyle.GLOW -> {
            Box(
                modifier =
                    modifier.drawWithCache {
                        val width = size.width
                        val height = size.height
                        val startGlow =
                            Brush.radialGradient(
                                colors = listOf(colors.first.copy(alpha = 0.82f), colors.first.copy(alpha = 0.38f), Color.Transparent),
                                center = Offset(width * 0.12f, height * 0.42f),
                                radius = width * 0.72f,
                            )
                        val endGlow =
                            Brush.radialGradient(
                                colors = listOf(colors.second.copy(alpha = 0.78f), colors.second.copy(alpha = 0.34f), Color.Transparent),
                                center = Offset(width * 0.88f, height * 0.58f),
                                radius = width * 0.72f,
                            )
                        val topGlow =
                            Brush.radialGradient(
                                colors = listOf(colors.third.copy(alpha = 0.58f), Color.Transparent),
                                center = Offset(width * 0.52f, height * 0.05f),
                                radius = width * 0.54f,
                            )
                        val bottomGlow =
                            Brush.radialGradient(
                                colors = listOf(colors.fourth.copy(alpha = 0.46f), Color.Transparent),
                                center = Offset(width * 0.46f, height * 1.05f),
                                radius = width * 0.54f,
                            )

                        onDrawBehind {
                            drawRect(Color.Black)
                            drawRect(startGlow)
                            drawRect(endGlow)
                            drawRect(topGlow)
                            drawRect(bottomGlow)
                            drawRect(Color.Black.copy(alpha = 0.24f))
                        }
                    },
            )
        }

        MiniPlayerBackgroundStyle.BLUR -> {
            Box(modifier = modifier) {
                if (!artworkUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = artworkUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().blur(28.dp),
                    )
                }
                Box(Modifier.fillMaxSize().background(containerColor.copy(alpha = 0.48f)))
            }
        }

        MiniPlayerBackgroundStyle.GLOW_ANIMATED -> {
            val transition = rememberInfiniteTransition(label = "miniPlayerGlow")
            val phase by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(20_000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "miniPlayerGlowPhase",
            )
            Box(
                modifier = modifier.drawWithCache {
                    val first = lerp(colors.first, colors.second, phase)
                    val second = lerp(colors.third, colors.fourth, phase)
                    val left = Brush.radialGradient(
                        colors = listOf(first.copy(alpha = 0.86f), Color.Transparent),
                        center = Offset(size.width * (0.12f + phase * 0.72f), size.height * 0.25f),
                        radius = size.width * 0.9f,
                    )
                    val right = Brush.radialGradient(
                        colors = listOf(second.copy(alpha = 0.78f), Color.Transparent),
                        center = Offset(size.width * (0.88f - phase * 0.72f), size.height * 0.75f),
                        radius = size.width * 0.82f,
                    )
                    onDrawBehind {
                        drawRect(Color.Black)
                        drawRect(left)
                        drawRect(right)
                        drawRect(Color.Black.copy(alpha = 0.28f))
                    }
                },
            )
        }

        MiniPlayerBackgroundStyle.LIVE_MESH -> {
            val transition = rememberInfiniteTransition(label = "miniPlayerMesh")
            val rotation by transition.animateFloat(
                initialValue = -8f,
                targetValue = 8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(18_000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "miniPlayerMeshRotation",
            )
            Box(modifier = modifier) {
                if (!artworkUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = artworkUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = 1.45f
                                scaleY = 1.45f
                                rotationZ = rotation
                            }
                            .blur(34.dp),
                    )
                }
                Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.36f)))
            }
        }
    }
}

@Immutable
private data class MiniPlayerBackgroundPalette(
    val first: Color,
    val second: Color,
    val third: Color,
    val fourth: Color,
) {
    companion object {
        fun from(colors: List<Color>): MiniPlayerBackgroundPalette? {
            val first = colors.firstOrNull() ?: return null
            val second = colors.getOrElse(1) { first }
            val third = colors.getOrElse(2) { second }
            val fourth = colors.getOrElse(3) { first }
            return MiniPlayerBackgroundPalette(
                first = first,
                second = second,
                third = third,
                fourth = fourth,
            )
        }
    }
}
