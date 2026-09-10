/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.arturo254.opentune.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.arturo254.opentune.LocalPlayerConnection
import com.arturo254.opentune.constants.LiquidGlassNavBarKey
import com.arturo254.opentune.constants.MiniPlayerBackgroundStyle
import com.arturo254.opentune.constants.MiniPlayerBackgroundStyleKey
import com.arturo254.opentune.constants.MiniPlayerHeight
import com.arturo254.opentune.constants.SwipeSensitivityKey
import com.arturo254.opentune.constants.SwipeThumbnailKey
import com.arturo254.opentune.ui.theme.PlayerColorExtractor
import com.arturo254.opentune.utils.rememberEnumPreference
import com.arturo254.opentune.utils.rememberPreference
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
    NewMiniPlayer(
        position = position,
        duration = duration,
        modifier = modifier,
        pureBlack = pureBlack,
        isPairedWithNavigation = isPairedWithNavigation,
    )
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
        if (shouldUseArtworkBackground && backgroundPalette != null) {
            miniPlayerBackgroundStyle
        } else {
            MiniPlayerBackgroundStyle.THEME
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
    containerColor: Color,
    modifier: Modifier = Modifier,
) {
    when (style) {
        MiniPlayerBackgroundStyle.THEME -> {
            Box(
                modifier = modifier.background(containerColor),
            )
        }

        MiniPlayerBackgroundStyle.GRADIENT -> {
            val colors = requireNotNull(palette)
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
            val colors = requireNotNull(palette)
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
