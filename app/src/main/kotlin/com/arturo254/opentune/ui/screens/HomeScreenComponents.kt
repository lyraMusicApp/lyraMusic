/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.arturo254.opentune.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.CoroutineScope
import com.arturo254.opentune.R
import com.arturo254.opentune.constants.GridThumbnailHeight
import com.arturo254.opentune.constants.ListItemHeight
import com.arturo254.opentune.constants.ListThumbnailSize
import com.arturo254.opentune.constants.ThumbnailCornerRadius
import com.arturo254.opentune.db.entities.Album
import com.arturo254.opentune.db.entities.Artist
import com.arturo254.opentune.db.entities.LocalItem
import com.arturo254.opentune.db.entities.Playlist
import com.arturo254.opentune.db.entities.Song
import com.arturo254.opentune.extensions.toMediaItem
import com.arturo254.opentune.extensions.togglePlayPause
import com.arturo254.opentune.innertube.models.AlbumItem
import com.arturo254.opentune.innertube.models.ArtistItem
import com.arturo254.opentune.innertube.models.PlaylistItem
import com.arturo254.opentune.innertube.models.SongItem
import com.arturo254.opentune.innertube.models.WatchEndpoint
import com.arturo254.opentune.innertube.models.YTItem
import com.arturo254.opentune.innertube.pages.HomePage
import com.arturo254.opentune.models.MediaMetadata
import com.arturo254.opentune.models.toMediaMetadata
import com.arturo254.opentune.playback.PlayerConnection
import com.arturo254.opentune.playback.queues.ListQueue
import com.arturo254.opentune.playback.queues.YouTubeQueue
import com.arturo254.opentune.ui.component.AlbumGridItem
import com.arturo254.opentune.ui.component.ArtistGridItem
import com.arturo254.opentune.ui.component.LocalMenuState
import com.arturo254.opentune.ui.component.MenuState
import com.arturo254.opentune.ui.component.NavigationTitle
import com.arturo254.opentune.ui.component.SongGridItem
import com.arturo254.opentune.ui.component.SongListItem
import com.arturo254.opentune.ui.component.YouTubeGridItem
import com.arturo254.opentune.ui.component.shimmer.GridItemPlaceHolder
import com.arturo254.opentune.ui.component.shimmer.ShimmerHost
import com.arturo254.opentune.ui.component.shimmer.TextPlaceholder
import com.arturo254.opentune.ui.menu.AlbumMenu
import com.arturo254.opentune.ui.menu.ArtistMenu
import com.arturo254.opentune.ui.menu.SongMenu
import com.arturo254.opentune.ui.menu.YouTubeAlbumMenu
import com.arturo254.opentune.ui.menu.YouTubeArtistMenu
import com.arturo254.opentune.ui.menu.YouTubePlaylistMenu
import com.arturo254.opentune.ui.menu.YouTubeSongMenu
import com.arturo254.opentune.ui.utils.highQualityThumbnailUrlOrNull
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import com.arturo254.opentune.models.SimilarRecommendation
import kotlin.math.ceil
import kotlin.random.Random
import kotlin.math.min

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.arturo254.opentune.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuickPicksSection(
    quickPicks: List<Song>,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    modifier: Modifier = Modifier
) {
    val distinctQuickPicks = remember(quickPicks) { quickPicks.distinctBy { it.id } }

    HorizontalCenteredHeroCarousel(
        state = rememberCarouselState { distinctQuickPicks.size },
        maxItemWidth = 250.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp)
    ) { index ->
        val song = distinctQuickPicks[index]
        val isActive = song.id == mediaMetadata?.id

        Box(
            modifier = Modifier
                .fillMaxSize()
                .maskClip(MaterialTheme.shapes.extraLarge)
                .maskBorder(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    MaterialTheme.shapes.extraLarge
                )
                .combinedClickable(
                    onClick = {
                        if (isActive) {
                            playerConnection.player.togglePlayPause()
                        } else {
                            playerConnection.playQueue(YouTubeQueue.radio(song.toMediaMetadata()))
                        }
                    },
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        menuState.show {
                            SongMenu(
                                originalSong = song,
                                navController = navController,
                                onDismiss = menuState::dismiss
                            )
                        }
                    }
                )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.song.thumbnailUrl.highQualityThumbnailUrlOrNull())
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            if (isActive && isPlaying) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(32.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.volume_up),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = song.song.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.artists.joinToString { it.name },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpeedDialSection(
    speedDialSongs: List<Song>,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val distinctSpeedDial = remember(speedDialSongs) { speedDialSongs.distinctBy { it.id }.take(24) }
    val speedDialIndexById = remember(distinctSpeedDial) { distinctSpeedDial.mapIndexed { index, song -> song.id to index }.toMap() }
    val tileSize = 130.dp
    val spacing = 10.dp
    val state = rememberLazyGridState()
    val rowCount = min(3, distinctSpeedDial.size + 1)
    val gridHeight = (tileSize * rowCount) + (spacing * (rowCount - 1))

    fun playSpeedDialQueue(startIndex: Int) {
        if (distinctSpeedDial.isEmpty()) return
        playerConnection.playQueue(
            ListQueue(
                title = context.getString(R.string.speed_dial),
                items = distinctSpeedDial.map { it.toMediaItem() },
                startIndex = startIndex,
            )
        )
    }

    val dotState by
        remember(state, distinctSpeedDial.size) {
            derivedStateOf {
                val songsPerDot = 8
                val totalSongs = distinctSpeedDial.size
                if (totalSongs <= 0) {
                    Triple(0, 0, 0)
                } else {
                    val pages = ceil(totalSongs / songsPerDot.toFloat()).toInt().coerceAtLeast(1)
                    val visibleSongIndex =
                        state.firstVisibleItemIndex.coerceIn(0, (totalSongs - 1).coerceAtLeast(0))
                    val currentPage = (visibleSongIndex / songsPerDot).coerceIn(0, pages - 1)
                    val dots = min(3, pages)
                    val selectedDot =
                        if (pages <= 3) currentPage
                        else ((currentPage.toFloat() / (pages - 1).coerceAtLeast(1)) * (dots - 1))
                            .toInt()
                            .coerceIn(0, dots - 1)
                    Triple(dots, selectedDot, pages)
                }
            }
        }

    val (dotsCount, selectedDotIndex) = dotState.let { (dots, selected, _) -> dots to selected }

    Column(modifier = modifier.fillMaxWidth()) {
        LazyHorizontalGrid(
            state = state,
            rows = GridCells.Fixed(rowCount),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(spacing),
            contentPadding = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal).asPaddingValues(),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(gridHeight),
        ) {
            items(
                items = distinctSpeedDial,
                key = { it.id },
                contentType = { "speed_dial_song" }
            ) { song ->
                val songIndex = speedDialIndexById[song.id] ?: 0
                val isActive = song.id == mediaMetadata?.id

                Box(
                    modifier = Modifier
                        .width(tileSize)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .combinedClickable(
                            onClick = {
                                if (isActive) {
                                    playerConnection.player.togglePlayPause()
                                } else {
                                    playSpeedDialQueue(songIndex)
                                }
                            },
                            onLongClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                menuState.show {
                                    SongMenu(
                                        originalSong = song,
                                        navController = navController,
                                        onDismiss = menuState::dismiss
                                    )
                                }
                            }
                        )
                ) {
                    AsyncImage(
                        model = song.song.thumbnailUrl.highQualityThumbnailUrlOrNull(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )

                    Text(
                        text = song.song.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    )

                    if (isActive && isPlaying) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.volume_up),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(6.dp).size(16.dp)
                            )
                        }
                    }
                }
            }

            item(key = "speed_dial_random", contentType = "speed_dial_random") {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .width(tileSize)
                        .aspectRatio(1f)
                        .combinedClickable(
                            onClick = {
                                if (distinctSpeedDial.isNotEmpty()) {
                                    playSpeedDialQueue(Random.nextInt(distinctSpeedDial.size))
                                }
                            },
                            onLongClick = {}
                        )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.casino),
                            contentDescription = stringResource(R.string.speed_dial_random),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }

        if (dotsCount > 1) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                repeat(dotsCount) { index ->
                    val isSelected = index == selectedDotIndex
                    Surface(
                        color =
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
                        shape = CircleShape,
                        modifier =
                            Modifier.size(
                                if (isSelected) 8.dp else 6.dp
                            ),
                    ) {}
                }
            }
        }
    }
}

/**
 * Keep Listening section - horizontal grid of local items
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeepListeningSection(
    keepListening: List<LocalItem>,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val rows = if (keepListening.size > 6) 2 else 1
    val gridHeight = (GridThumbnailHeight + with(LocalDensity.current) {
        MaterialTheme.typography.bodyLarge.lineHeight.toDp() * 2 +
                MaterialTheme.typography.bodyMedium.lineHeight.toDp() * 2
    }) * rows

    LazyHorizontalGrid(
        state = rememberLazyGridState(),
        rows = GridCells.Fixed(rows),
        modifier = modifier
            .fillMaxWidth()
            .height(gridHeight)
    ) {
        items(
            items = keepListening,
            key = { item -> 
                when (item) {
                    is Song -> "song_${item.id}"
                    is Album -> "album_${item.id}"
                    is Artist -> "artist_${item.id}"
                    is Playlist -> "playlist_${item.id}"
                }
            }
        ) { item ->
            LocalGridItem(
                item = item,
                mediaMetadata = mediaMetadata,
                isPlaying = isPlaying,
                navController = navController,
                playerConnection = playerConnection,
                menuState = menuState,
                haptic = haptic,
                scope = scope
            )
        }
    }
}

/**
 * Forgotten Favorites section - horizontal grid of songs
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForgottenFavoritesSection(
    forgottenFavorites: List<Song>,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    horizontalLazyGridItemWidth: Dp,
    lazyGridState: LazyGridState,
    snapLayoutInfoProvider: SnapLayoutInfoProvider,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    modifier: Modifier = Modifier
) {
    val rows = min(4, forgottenFavorites.size)
    val distinctForgottenFavorites = remember(forgottenFavorites) { forgottenFavorites.distinctBy { it.id } }
    
    LazyHorizontalGrid(
        state = lazyGridState,
        rows = GridCells.Fixed(rows),
        flingBehavior = rememberSnapFlingBehavior(snapLayoutInfoProvider),
        contentPadding = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal)
            .asPaddingValues(),
        modifier = modifier
            .fillMaxWidth()
            .height(ListItemHeight * rows)
    ) {
        items(
            items = distinctForgottenFavorites,
            key = { it.id }
        ) { song ->
            SongListItem(
                song = song,
                showInLibraryIcon = true,
                isActive = song.id == mediaMetadata?.id,
                isPlaying = isPlaying,
                isSwipeable = false,
                trailingContent = {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            menuState.show {
                                SongMenu(
                                    originalSong = song,
                                    navController = navController,
                                    onDismiss = menuState::dismiss
                                )
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.more_vert),
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .width(horizontalLazyGridItemWidth)
                    .combinedClickable(
                        onClick = {
                            if (song.id == mediaMetadata?.id) {
                                playerConnection.player.togglePlayPause()
                            } else {
                                playerConnection.playQueue(YouTubeQueue.radio(song.toMediaMetadata()))
                            }
                        },
                        onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            menuState.show {
                                SongMenu(
                                    originalSong = song,
                                    navController = navController,
                                    onDismiss = menuState::dismiss
                                )
                            }
                        }
                    )
            )
        }
    }
}

/**
 * Account Playlists section - horizontal row of YouTube playlists
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountPlaylistsSection(
    accountPlaylists: List<PlaylistItem>,
    accountName: String,
    accountImageUrl: String?,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val distinctPlaylists = remember(accountPlaylists) { accountPlaylists.distinctBy { it.id } }
    
    LazyRow(
        contentPadding = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal)
            .asPaddingValues(),
        modifier = modifier
    ) {
        items(
            items = distinctPlaylists,
            key = { it.id }
        ) { item ->
            YouTubeGridItemWrapper(
                item = item,
                mediaMetadata = mediaMetadata,
                isPlaying = isPlaying,
                navController = navController,
                playerConnection = playerConnection,
                menuState = menuState,
                haptic = haptic,
                scope = scope
            )
        }
    }
}

/**
 * Similar Recommendations section
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimilarRecommendationsSection(
    recommendation: SimilarRecommendation,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    LazyRow(
        contentPadding = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal)
            .asPaddingValues(),
        modifier = modifier
    ) {
        items(
            items = recommendation.items,
            key = { it.id }
        ) { item ->
            YouTubeGridItemWrapper(
                item = item,
                mediaMetadata = mediaMetadata,
                isPlaying = isPlaying,
                navController = navController,
                playerConnection = playerConnection,
                menuState = menuState,
                haptic = haptic,
                scope = scope
            )
        }
    }
}

/**
 * HomePage Section - a single section from YouTube home page
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePageSectionContent(
    section: HomePage.Section,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    LazyRow(
        contentPadding = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal)
            .asPaddingValues(),
        modifier = modifier
    ) {
        items(
            items = section.items,
            key = { it.id }
        ) { item ->
            YouTubeGridItemWrapper(
                item = item,
                mediaMetadata = mediaMetadata,
                isPlaying = isPlaying,
                navController = navController,
                playerConnection = playerConnection,
                menuState = menuState,
                haptic = haptic,
                scope = scope
            )
        }
    }
}

/**
 * Loading shimmer for home page sections
 */
@Composable
fun HomeLoadingShimmer(modifier: Modifier = Modifier) {
    ShimmerHost(modifier = modifier) {
        TextPlaceholder(
            height = 36.dp,
            modifier = Modifier
                .padding(12.dp)
                .width(250.dp),
        )
        LazyRow {
            items(4) {
                GridItemPlaceHolder()
            }
        }
    }
}

// ============== Helper Composables ==============

/**
 * Wrapper for YouTube grid items with click handling
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun YouTubeGridItemWrapper(
    item: YTItem,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    YouTubeGridItem(
        item = item,
        isActive = item.id in listOf(mediaMetadata?.album?.id, mediaMetadata?.id),
        isPlaying = isPlaying,
        coroutineScope = scope,
        thumbnailRatio = 1f,
        modifier = modifier.combinedClickable(
            onClick = {
                when (item) {
                    is SongItem -> playerConnection.playQueue(
                        YouTubeQueue(
                            item.endpoint ?: WatchEndpoint(videoId = item.id),
                            item.toMediaMetadata()
                        )
                    )
                    is AlbumItem -> navController.navigate("album/${item.id}")
                    is ArtistItem -> navController.navigate("artist/${item.id}")
                    is PlaylistItem -> navController.navigate("online_playlist/${item.id}")
                }
            },
            onLongClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                menuState.show {
                    when (item) {
                        is SongItem -> YouTubeSongMenu(
                            song = item,
                            navController = navController,
                            onDismiss = menuState::dismiss
                        )
                        is AlbumItem -> YouTubeAlbumMenu(
                            albumItem = item,
                            navController = navController,
                            onDismiss = menuState::dismiss
                        )
                        is ArtistItem -> YouTubeArtistMenu(
                            artist = item,
                            onDismiss = menuState::dismiss
                        )
                        is PlaylistItem -> YouTubePlaylistMenu(
                            playlist = item,
                            coroutineScope = scope,
                            onDismiss = menuState::dismiss
                        )
                    }
                }
            }
        )
    )
}

/**
 * Local item grid item for songs, albums, artists
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LocalGridItem(
    item: LocalItem,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    when (item) {
        is Song -> SongGridItem(
            song = item,
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        if (item.id == mediaMetadata?.id) {
                            playerConnection.player.togglePlayPause()
                        } else {
                            playerConnection.playQueue(YouTubeQueue.radio(item.toMediaMetadata()))
                        }
                    },
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        menuState.show {
                            SongMenu(
                                originalSong = item,
                                navController = navController,
                                onDismiss = menuState::dismiss
                            )
                        }
                    }
                ),
            isActive = item.id == mediaMetadata?.id,
            isPlaying = isPlaying
        )

        is Album -> AlbumGridItem(
            album = item,
            isActive = item.id == mediaMetadata?.album?.id,
            isPlaying = isPlaying,
            coroutineScope = scope,
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { navController.navigate("album/${item.id}") },
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        menuState.show {
                            AlbumMenu(
                                originalAlbum = item,
                                navController = navController,
                                onDismiss = menuState::dismiss
                            )
                        }
                    }
                )
        )

        is Artist -> ArtistGridItem(
            artist = item,
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { navController.navigate("artist/${item.id}") },
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        menuState.show {
                            ArtistMenu(
                                originalArtist = item,
                                coroutineScope = scope,
                                onDismiss = menuState::dismiss
                            )
                        }
                    }
                )
        )

        is Playlist -> { /* Not displayed */ }
    }
}

/**
 * Account playlist navigation title with image
 */
@Composable
fun AccountPlaylistsTitle(
    accountName: String,
    accountImageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationTitle(
        label = stringResource(R.string.your_youtube_playlists),
        title = accountName,
        thumbnail = {
            if (accountImageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(accountImageUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .diskCacheKey(accountImageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.person),
                    error = painterResource(id = R.drawable.person),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(ListThumbnailSize)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = null,
                    modifier = Modifier.size(ListThumbnailSize)
                )
            }
        },
        onClick = onClick,
        modifier = modifier
    )
}

/**
 * Similar recommendations navigation title
 */
@Composable
fun SimilarRecommendationsTitle(
    recommendation: SimilarRecommendation,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationTitle(
        label = stringResource(R.string.similar_to),
        title = recommendation.title.title,
        thumbnail = recommendation.title.thumbnailUrl?.let { thumbnailUrl ->
            {
                val shape = if (recommendation.title is Artist) CircleShape 
                    else RoundedCornerShape(ThumbnailCornerRadius)
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(ListThumbnailSize)
                        .clip(shape)
                )
            }
        },
        onClick = {
            when (recommendation.title) {
                is Song -> navController.navigate("album/${recommendation.title.album!!.id}")
                is Album -> navController.navigate("album/${recommendation.title.id}")
                is Artist -> navController.navigate("artist/${recommendation.title.id}")
                is Playlist -> {}
            }
        },
        modifier = modifier
    )
}

/**
 * HomePage section navigation title
 */
@Composable
fun HomePageSectionTitle(
    section: HomePage.Section,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationTitle(
        title = section.title,
        label = section.label,
        thumbnail = section.thumbnail?.let { thumbnailUrl ->
            {
                val shape = if (section.endpoint?.isArtistEndpoint == true) CircleShape 
                    else RoundedCornerShape(ThumbnailCornerRadius)
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(ListThumbnailSize)
                        .clip(shape)
                )
            }
        },
        onClick = section.endpoint?.browseId?.let { browseId ->
            {
                if (browseId == "FEmusic_moods_and_genres")
                    navController.navigate(Screens.MoodAndGenres.route)
                else
                    navController.navigate("browse/$browseId")
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.AccountPlaylistsContainer(
    viewModel: HomeViewModel,
    accountName: String?,
    accountImageUrl: String?,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    scope: CoroutineScope
) {
    item {
        val accountPlaylists by viewModel.accountPlaylists.collectAsState()
        
        // Check if list is not null and not empty
        val currentPlaylists = accountPlaylists
        if (!currentPlaylists.isNullOrEmpty()) {
            Column {
                 AccountPlaylistsTitle(
                    accountName = accountName ?: "",
                    accountImageUrl = accountImageUrl,
                    onClick = { navController.navigate("account") },
                    modifier = Modifier
                )
                AccountPlaylistsSection(
                    accountPlaylists = currentPlaylists,
                    accountName = accountName ?: "",
                    accountImageUrl = accountImageUrl,
                    mediaMetadata = mediaMetadata,
                    isPlaying = isPlaying,
                    navController = navController,
                    playerConnection = playerConnection,
                    menuState = menuState,
                    haptic = haptic,
                    scope = scope
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.SimilarRecommendationsContainer(
    viewModel: HomeViewModel,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    navController: NavController,
    playerConnection: PlayerConnection,
    menuState: MenuState,
    haptic: HapticFeedback,
    scope: CoroutineScope
) {
     item {
        val similarRecommendations by viewModel.similarRecommendations.collectAsState()
        
        Column {
            similarRecommendations?.forEach { recommendation ->
                SimilarRecommendationsTitle(
                    recommendation = recommendation,
                    navController = navController,
                    modifier = Modifier
                )
                SimilarRecommendationsSection(
                    recommendation = recommendation,
                    mediaMetadata = mediaMetadata,
                    isPlaying = isPlaying,
                    navController = navController,
                    playerConnection = playerConnection,
                    menuState = menuState,
                    haptic = haptic,
                    scope = scope
                )
            }
        }
    }
}

// ── Modern Homepage Components ───────────────────────────────────────────────

@Composable
fun HomeModernHeader(
    accountName: String,
    accountImageUrl: String?,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .padding(top = 14.dp, bottom = 8.dp)
    ) {
        // Top row: Profile avatar on left, Search and Favorites buttons on right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile avatar (clickable -> account/login)
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2A2D37))
                    .border(1.5.dp, Color(0xFFD4E84B), CircleShape)
                    .clickable { navController.navigate("account") },
                contentAlignment = Alignment.Center
            ) {
                if (!accountImageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = accountImageUrl,
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.account),
                        contentDescription = "Profile",
                        tint = Color(0xFFD4E84B),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Top right action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Search Button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E222B).copy(alpha = 0.85f))
                        .clickable { navController.navigate("search") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Favorites / Liked Button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E222B).copy(alpha = 0.85f))
                        .clickable { navController.navigate("library") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.favorite_border),
                        contentDescription = "Favorites",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Dynamic greeting: Real user name when logged in, or clean welcome when not
        val isLoggedIn = accountName.isNotBlank() && accountName != "Guest"
        Text(
            text = if (isLoggedIn) "Hi, $accountName" else "Welcome to Lyra Music",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

@Composable
fun HomePillChipsRow(
    selectedChipTitle: String,
    onChipSelected: (String) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val chips = listOf("All", "New Release", "Trending", "Top daily", "Relax", "Workout")
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chips) { chip ->
            val isSelected = chip == selectedChipTitle
            val containerColor = if (isSelected) Color(0xFFD4E84B) else Color(0xFF1E222A).copy(alpha = 0.85f)
            val textColor = if (isSelected) Color(0xFF111827) else Color(0xFFE5E7EB)

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(containerColor)
                    .clickable {
                        when (chip) {
                            "New Release" -> navController.navigate("new_release")
                            "Trending" -> navController.navigate("charts_screen")
                            else -> onChipSelected(chip)
                        }
                    }
                    .padding(horizontal = 18.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chip,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColor
                    )
                )
            }
        }
    }
}

data class HeroCardItem(
    val id: String,
    val title: String,
    val description: String,
    val playlistId: String,
    val gradientColors: List<Color>,
    val textColor: Color,
    val subTextColor: Color,
    val buttonBgColor: Color,
    val imageUrl: String
)

@Composable
fun CuratedTrendingHeroCard(
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cards = remember {
        listOf(
            HeroCardItem(
                id = "1",
                title = "Bollywood Hits",
                description = "Biggest Hindi chartbusters, viral party songs & romantic hits.",
                playlistId = "RDCLAK5uy_n9Fbdw7e6ap-98_A-8JYBmPv64v-ErMrE",
                gradientColors = listOf(Color(0xFFD7B6F8), Color(0xFFC7A5F4), Color(0xFFB58EF0)),
                textColor = Color(0xFF1E1038),
                subTextColor = Color(0xFF423063),
                buttonBgColor = Color(0xFF381566),
                imageUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=400&q=80"
            ),
            HeroCardItem(
                id = "2",
                title = "Punjabi Pop 100",
                description = "Top trending Punjabi party anthems, hip-hop & high beats.",
                playlistId = "RDCLAK5uy_l43N7i0KzY85B6c2N8_3053v-wB5h-8jA",
                gradientColors = listOf(Color(0xFFFDA4AF), Color(0xFFFB7185), Color(0xFFF43F5E)),
                textColor = Color(0xFF4C0519),
                subTextColor = Color(0xFF881337),
                buttonBgColor = Color(0xFF4C0519),
                imageUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=400&q=80"
            ),
            HeroCardItem(
                id = "3",
                title = "Desi Lo-Fi & Chill",
                description = "Acoustic Hindi covers & late night chill melodies.",
                playlistId = "RDCLAK5uy_m-0_Zz_QnZ18gPq2iU5q9e9W4G_7q88cA",
                gradientColors = listOf(Color(0xFF93C5FD), Color(0xFF60A5FA), Color(0xFF3B82F6)),
                textColor = Color(0xFF1E3A8A),
                subTextColor = Color(0xFF172554),
                buttonBgColor = Color(0xFF1E3A8A),
                imageUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&w=400&q=80"
            ),
            HeroCardItem(
                id = "4",
                title = "Arijit Singh Romantics",
                description = "Soulful Bollywood melodies & all-time romantic favorites.",
                playlistId = "RDCLAK5uy_kh9o-lW2N81G6xX8-FqgQfGzG24B9Yc0I",
                gradientColors = listOf(Color(0xFFFDE047), Color(0xFFA3E635), Color(0xFF84CC16)),
                textColor = Color(0xFF1A2E05),
                subTextColor = Color(0xFF365314),
                buttonBgColor = Color(0xFF1A2E05),
                imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=400&q=80"
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "Curated & trending",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
        )

        // Smoothly Slidable / Swipable Hero Carousel
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(cards, key = { it.id }) { item ->
                Box(
                    modifier = Modifier
                        .width(320.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(Brush.horizontalGradient(colors = item.gradientColors))
                        .clickable { onCardClick(item.playlistId) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Text and Controls Column
                        Column(
                            modifier = Modifier
                                .weight(1.15f)
                                .padding(start = 18.dp, top = 14.dp, bottom = 14.dp, end = 6.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = item.textColor
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = item.subTextColor,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Bottom Sleek Control Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Small Circular Play Button
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(item.buttonBgColor)
                                        .clickable { onCardClick(item.playlistId) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.play),
                                        contentDescription = "Play",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                // Favorite Icon
                                Icon(
                                    painter = painterResource(R.drawable.favorite_border),
                                    contentDescription = "Favorite",
                                    tint = item.buttonBgColor,
                                    modifier = Modifier
                                        .size(17.dp)
                                        .clickable { onCardClick(item.playlistId) }
                                )

                                // Download Icon
                                Icon(
                                    painter = painterResource(R.drawable.download),
                                    contentDescription = "Download",
                                    tint = item.buttonBgColor,
                                    modifier = Modifier
                                        .size(17.dp)
                                        .clickable { onCardClick(item.playlistId) }
                                )

                                // More Icon
                                Icon(
                                    painter = painterResource(R.drawable.more_horiz),
                                    contentDescription = "More",
                                    tint = item.buttonBgColor,
                                    modifier = Modifier
                                        .size(17.dp)
                                        .clickable { onCardClick(item.playlistId) }
                                )
                            }
                        }

                        // Right Illustration Artwork
                        Box(
                            modifier = Modifier
                                .weight(0.85f)
                                .fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(130.dp)
                                    .clip(RoundedCornerShape(topEnd = 26.dp, bottomEnd = 26.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}

data class DailyPlaylistItem(
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val playlistId: String
)

@Composable
fun TopDailyPlaylistsSection(
    navController: NavController,
    onPlaylistClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val samplePlaylists = listOf(
        DailyPlaylistItem("Bollywood Hot 50", "By T-Series • Hindi Trending Hits", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=300&q=80", "RDCLAK5uy_n9Fbdw7e6ap-98_A-8JYBmPv64v-ErMrE"),
        DailyPlaylistItem("Punjabi Top 50", "By Speed Records • Top Trending Punjabi", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=300&q=80", "RDCLAK5uy_l43N7i0KzY85B6c2N8_3053v-wB5h-8jA"),
        DailyPlaylistItem("Indie India Viral", "By Desi Music Factory • Viral Desi Tracks", "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&w=300&q=80", "RDCLAK5uy_m-0_Zz_QnZ18gPq2iU5q9e9W4G_7q88cA"),
        DailyPlaylistItem("South Indian Hits", "By Lyra Music • Tamil, Telugu, Malayalam", "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=300&q=80", "RDCLAK5uy_kh9o-lW2N81G6xX8-FqgQfGzG24B9Yc0I")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Top daily playlists",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Text(
                text = "See all",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF9CA3AF),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.clickable { navController.navigate("charts_screen") }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        samplePlaylists.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlaylistClick(item.playlistId) }
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Column {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF9CA3AF)
                            )
                        )
                    }
                }

                // Play circle button on right
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF262A34))
                        .clickable { onPlaylistClick(item.playlistId) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.play),
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
