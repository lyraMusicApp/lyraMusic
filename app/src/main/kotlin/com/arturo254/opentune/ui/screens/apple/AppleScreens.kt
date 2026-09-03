package com.arturo254.opentune.ui.screens.apple

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import androidx.core.net.toUri
import com.arturo254.opentune.LocalDatabase
import com.arturo254.opentune.LocalPlayerAwareWindowInsets
import com.arturo254.opentune.LocalPlayerConnection
import com.arturo254.opentune.R
import com.arturo254.opentune.constants.DarkModeKey
import com.arturo254.opentune.db.entities.Song
import com.arturo254.opentune.innertube.models.AlbumItem
import com.arturo254.opentune.innertube.models.ArtistItem
import com.arturo254.opentune.innertube.models.PlaylistItem
import com.arturo254.opentune.innertube.models.SongItem
import com.arturo254.opentune.innertube.models.YTItem
import com.arturo254.opentune.models.toMediaMetadata
import com.arturo254.opentune.playback.queues.YouTubeQueue
import com.arturo254.opentune.ui.screens.settings.DarkMode
import com.arturo254.opentune.ui.utils.highQualityThumbnailUrlOrNull
import com.arturo254.opentune.utils.rememberEnumPreference
import com.arturo254.opentune.extensions.toMediaItem
import com.arturo254.opentune.viewmodels.HomeViewModel
import com.arturo254.opentune.viewmodels.MoodAndGenresViewModel
import com.arturo254.opentune.viewmodels.OnlineSearchSuggestionViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import java.net.URLEncoder

@Composable
fun isAppInDarkTheme(): Boolean {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val darkTheme by rememberEnumPreference(DarkModeKey, defaultValue = DarkMode.AUTO)
    return remember(darkTheme, isSystemInDarkTheme) {
        if (darkTheme == DarkMode.AUTO) isSystemInDarkTheme else darkTheme == DarkMode.ON
    }
}

val AppleBg @Composable get() = if (isAppInDarkTheme()) Color(0xFF000000) else Color(0xFFF2F2F7)
val AppleText @Composable get() = if (isAppInDarkTheme()) Color.White else Color.Black
val AppleRed = Color(0xFFFA233B)

@Composable
fun AppleMeshBackground() {
    val color1 = MaterialTheme.colorScheme.primary
    val color2 = MaterialTheme.colorScheme.secondary
    val color3 = MaterialTheme.colorScheme.tertiary
    val color4 = MaterialTheme.colorScheme.primaryContainer
    val color5 = MaterialTheme.colorScheme.secondaryContainer
    val surfaceColor = AppleBg

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize(0.7f)
            .drawWithCache {
                val width = size.width
                val height = size.height

                val brush1 = Brush.radialGradient(
                    colors = listOf(color1.copy(alpha = 0.38f), color1.copy(alpha = 0.24f), color1.copy(alpha = 0.14f), color1.copy(alpha = 0.06f), Color.Transparent),
                    center = Offset(width * 0.15f, height * 0.1f),
                    radius = width * 0.55f,
                )
                val brush2 = Brush.radialGradient(
                    colors = listOf(color2.copy(alpha = 0.34f), color2.copy(alpha = 0.2f), color2.copy(alpha = 0.11f), color2.copy(alpha = 0.05f), Color.Transparent),
                    center = Offset(width * 0.85f, height * 0.2f),
                    radius = width * 0.65f,
                )
                val brush3 = Brush.radialGradient(
                    colors = listOf(color3.copy(alpha = 0.3f), color3.copy(alpha = 0.17f), color3.copy(alpha = 0.09f), color3.copy(alpha = 0.04f), Color.Transparent),
                    center = Offset(width * 0.3f, height * 0.45f),
                    radius = width * 0.6f,
                )
                val brush4 = Brush.radialGradient(
                    colors = listOf(color4.copy(alpha = 0.26f), color4.copy(alpha = 0.14f), color4.copy(alpha = 0.08f), color4.copy(alpha = 0.03f), Color.Transparent),
                    center = Offset(width * 0.7f, height * 0.5f),
                    radius = width * 0.7f,
                )
                val brush5 = Brush.radialGradient(
                    colors = listOf(color5.copy(alpha = 0.22f), color5.copy(alpha = 0.12f), color5.copy(alpha = 0.06f), color5.copy(alpha = 0.02f), Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.75f),
                    radius = width * 0.8f,
                )
                val overlayBrush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Transparent, surfaceColor.copy(alpha = 0.22f), surfaceColor.copy(alpha = 0.55f), surfaceColor),
                    startY = height * 0.4f,
                    endY = height,
                )

                onDrawBehind {
                    drawRect(brush1)
                    drawRect(brush2)
                    drawRect(brush3)
                    drawRect(brush4)
                    drawRect(brush5)
                    drawRect(overlayBrush)
                }
            },
    )
}

@Composable
fun AppleHeader(
    title: String,
    modifier: Modifier = Modifier,
    isAtTop: Boolean = true,
    hazeState: HazeState? = null,
    profileUrl: String? = null,
    onProfileClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        AnimatedContent(
            targetState = isAtTop,
            transitionSpec = {
                fadeIn(tween(300)).togetherWith(fadeOut(tween(300)))
            },
            modifier = Modifier.matchParentSize(),
            label = "AppleHeaderBackground"
        ) { isAtTopState ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (isAtTopState) {
                            Modifier.background(Color.Transparent)
                        } else if (hazeState != null) {
                            Modifier.hazeChild(state = hazeState)
                        } else {
                            Modifier.background(AppleBg.copy(alpha = 0.95f))
                        }
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = AppleText, fontSize = 28.sp, fontWeight = FontWeight.Black)

            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                if (profileUrl != null) {
                    AsyncImage(
                        model = profileUrl,
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.person),
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppleScaffold(
    title: String,
    navController: NavController,
    profileUrl: String? = null,
    isRefreshing: Boolean? = null,
    onRefresh: (() -> Unit)? = null,
    content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit
) {
    val lazyListState = rememberLazyListState()
    val hazeState = remember { HazeState() }
    val pullRefreshState = androidx.compose.material3.pulltorefresh.rememberPullToRefreshState()
    
    Box(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppleBg)
                .let { modifier ->
                    if (isRefreshing != null && onRefresh != null) {
                        modifier.pullToRefresh(
                            state = pullRefreshState,
                            isRefreshing = isRefreshing,
                            onRefresh = onRefresh
                        )
                    } else modifier
                }
                .haze(state = hazeState)
        ) {
            AppleMeshBackground()
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 90.dp,
                    bottom = LocalPlayerAwareWindowInsets.current.asPaddingValues().calculateBottomPadding() + 40.dp,
                    start = LocalPlayerAwareWindowInsets.current.asPaddingValues().calculateStartPadding(LayoutDirection.Ltr),
                    end = LocalPlayerAwareWindowInsets.current.asPaddingValues().calculateEndPadding(LayoutDirection.Ltr)
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxSize(),
                content = content,
            )
            
            if (isRefreshing != null && onRefresh != null) {
                androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 90.dp),
                    isRefreshing = isRefreshing,
                    state = pullRefreshState
                )
            }
        } 
        val isAtTop by remember {
            derivedStateOf {
                lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
            }
        }
        AppleHeader(
            title = title,
            modifier = Modifier.align(Alignment.TopCenter),
            isAtTop = isAtTop,
            hazeState = hazeState,
            profileUrl = profileUrl,
            onProfileClick = { navController.navigate("settings") }
        )
    }
}

@Composable
fun AppleTile(
    title: String,
    subtitle: String,
    thumbnailUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .width(160.dp)
        .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = title,
            color = AppleText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                color = AppleText.copy(alpha = 0.6f),
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AppleLocalRow(items: List<Song>, playerConnection: com.arturo254.opentune.playback.PlayerConnection) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { song ->
            AppleTile(
                title = song.title,
                subtitle = song.artists.joinToString { it.name },
                thumbnailUrl = song.thumbnailUrl?.highQualityThumbnailUrlOrNull(),
                onClick = {
                    playerConnection.playQueue(
                        com.arturo254.opentune.playback.queues.ListQueue(
                            title = "Local",
                            items = items.map { it.toMediaMetadata().toMediaItem() },
                            startIndex = items.indexOf(song)
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun AppleYtRow(items: List<YTItem>, navController: NavController, playerConnection: com.arturo254.opentune.playback.PlayerConnection) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            AppleTile(
                title = item.title,
                subtitle = when(item) {
                    is SongItem -> item.artists.joinToString { it.name }
                    is AlbumItem -> item.artists?.joinToString { it.name } ?: "Album"
                    is PlaylistItem -> item.author?.name ?: "Playlist"
                    is ArtistItem -> "Artist"
                    else -> ""
                },
                thumbnailUrl = item.thumbnail?.highQualityThumbnailUrlOrNull() ?: "",
                onClick = {
                    when (item) {
                        is SongItem -> {
                            playerConnection.playQueue(YouTubeQueue(item.endpoint ?: return@AppleTile, item.toMediaMetadata()))
                        }
                        is AlbumItem -> {
                            navController.navigate("album/${item.id}")
                        }
                        is PlaylistItem -> {
                            navController.navigate("online_playlist/${item.id}")
                        }
                        is ArtistItem -> {
                            navController.navigate("artist/${item.id}")
                        }
                        else -> {}
                    }
                }
            )
        }
    }
}

@Composable
fun AppleSectionTitle(title: String) {
    Text(
        text = title,
        color = AppleText,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
fun AppleHomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val playerConnection = LocalPlayerConnection.current ?: return
    val quickPicks by viewModel.quickPicks.collectAsState()
    val forgottenFavorites by viewModel.forgottenFavorites.collectAsState()
    val accountPlaylists by viewModel.accountPlaylists.collectAsState()
    val similarRecommendations by viewModel.similarRecommendations.collectAsState()
    val homePage by viewModel.homePage.collectAsState()
    
    val accountImageUrl by viewModel.accountImageUrl.collectAsState()
    val innerTubeCookie by com.arturo254.opentune.utils.rememberPreference(com.arturo254.opentune.constants.InnerTubeCookieKey, "")
    val isLoggedIn = remember(innerTubeCookie) {
        "SAPISID" in com.arturo254.opentune.innertube.utils.parseCookieString(innerTubeCookie)
    }
    val url = if (isLoggedIn) accountImageUrl else null
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    
    AppleScaffold(
        title = "Listen Now",
        navController = navController,
        profileUrl = url,
        isRefreshing = isRefreshing,
        onRefresh = viewModel::refresh
    ) {
        quickPicks?.takeIf { it.isNotEmpty() }?.let { picks ->
            item {
                AppleSectionTitle("Made for You")
                AppleLocalRow(picks.take(12), playerConnection)
            }
        }

        accountPlaylists?.takeIf { it.isNotEmpty() }?.let { playlists ->
            item {
                AppleSectionTitle("Your Playlists")
                AppleYtRow(playlists.take(12), navController, playerConnection)
            }
        }

        forgottenFavorites?.takeIf { it.isNotEmpty() }?.let { favorites ->
            item {
                AppleSectionTitle("Forgotten Favorites")
                AppleLocalRow(favorites.take(12), playerConnection)
            }
        }

        similarRecommendations?.forEach { recommendation ->
            item {
                AppleSectionTitle("Similar to ${recommendation.title.title}")
                AppleYtRow(recommendation.items.take(12), navController, playerConnection)
            }
        }

        homePage?.sections?.forEach { section ->
            item {
                AppleSectionTitle(section.title)
                AppleYtRow(section.items.take(12), navController, playerConnection)
            }
        }
    }
}

@Composable
fun AppleExploreScreen(
    navController: NavController,
    viewModel: MoodAndGenresViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val moodAndGenres by viewModel.moodAndGenres.collectAsState()
    val explorePage by homeViewModel.explorePage.collectAsState()
    val playerConnection = LocalPlayerConnection.current ?: return

    AppleScaffold(
        title = "Browse",
        navController = navController
    ) {
        item {
            AppleSectionTitle("New releases")
            AppleYtRow(explorePage?.newReleaseAlbums.orEmpty(), navController, playerConnection)
        }
        
        moodAndGenres?.let { list ->
            item {
                AppleSectionTitle("Moods & Genres")
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp).padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val itemsPerRow = 2
                    list.chunked(itemsPerRow).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            row.forEach { item ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(AppleBg.copy(alpha = 0.5f))
                                        .clickable {
                                            navController.navigate("youtube_browse/${item.endpoint.browseId}?params=${item.endpoint.params}")
                                        }
                                        .padding(16.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = item.title,
                                        color = AppleText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            repeat(itemsPerRow - row.size) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppleLibraryScreen(navController: NavController) {
    com.arturo254.opentune.ui.screens.library.LibraryScreen(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppleSearchScreen(
    navController: NavController,
    viewModel: OnlineSearchSuggestionViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val viewState by viewModel.viewState.collectAsState()
    val database = LocalDatabase.current
    val keyboardController = androidx.compose.ui.platform.LocalSoftwareKeyboardController.current

    AppleScaffold(
        title = "Search",
        navController = navController
    ) {
        item {
            androidx.compose.material3.OutlinedTextField(
                value = query,
                onValueChange = { viewModel.query.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                placeholder = { Text("Search songs, artists, albums...", color = AppleText.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(painterResource(R.drawable.search), contentDescription = null, tint = AppleText.copy(alpha=0.5f)) },
                trailingIcon = {
                    IconButton(onClick = { navController.navigate(com.arturo254.opentune.ui.screens.musicrecognition.MusicRecognitionRoute) }) {
                        Icon(
                            painter = painterResource(R.drawable.mic),
                            contentDescription = "Music Recognition",
                            tint = AppleText.copy(alpha = 0.5f)
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = AppleBg.copy(alpha = 0.3f),
                    unfocusedContainerColor = AppleBg.copy(alpha = 0.3f),
                    focusedTextColor = AppleText,
                    unfocusedTextColor = AppleText
                ),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onSearch = {
                        val encoded = URLEncoder.encode(query, "UTF-8")
                        navController.navigate("search/$encoded")
                        keyboardController?.hide()
                    }
                ),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Search
                )
            )
        }
        
        if (query.isNotBlank() && (viewState.history.isNotEmpty() || viewState.suggestions.isNotEmpty())) {
            items(viewState.history, key = { "history_${it.query}" }) { history ->
                com.arturo254.opentune.ui.screens.search.SuggestionItem(
                    query = history.query,
                    online = false,
                    onClick = {
                        val encoded = URLEncoder.encode(history.query, "UTF-8")
                        navController.navigate("search/$encoded")
                        keyboardController?.hide()
                    },
                    onDelete = {
                        database.query {
                            delete(history)
                        }
                    },
                    onFillTextField = {
                        viewModel.query.value = history.query
                    },
                    pureBlack = false
                )
            }
            items(viewState.suggestions, key = { "suggestion_$it" }) { suggestion ->
                com.arturo254.opentune.ui.screens.search.SuggestionItem(
                    query = suggestion,
                    online = true,
                    onClick = {
                        val encoded = URLEncoder.encode(suggestion, "UTF-8")
                        navController.navigate("search/$encoded")
                        keyboardController?.hide()
                    },
                    onFillTextField = {
                        viewModel.query.value = suggestion
                    },
                    pureBlack = false
                )
            }
        } else {
            item {
                AppleSectionTitle("Browse Categories")
                Spacer(modifier = Modifier.height(10.dp))
                val genres = listOf(
                    "Pop" to Color(0xFFFF4632),
                    "Hip-Hop" to Color(0xFFBC5900),
                    "Rock" to Color(0xFFE1118C),
                    "Latin" to Color(0xFFE1118C),
                    "Educational" to Color(0xFF477D95),
                    "Documentary" to Color(0xFF509BF5),
                    "Comedy" to Color(0xFFE13300),
                    "Charts" to Color(0xFF8D67AB),
                    "Dance" to Color(0xFFD84000),
                    "Mood" to Color(0xFFE1118C),
                    "Indie" to Color(0xFFE91429),
                    "Workout" to Color(0xFF777777),
                    "K-pop" to Color(0xFF148A08),
                    "Chill" to Color(0xFFD84000),
                    "Sleep" to Color(0xFF1E3264),
                    "Party" to Color(0xFF537AA1),
                    "Decades" to Color(0xFFBA5D07)
                )
                Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(horizontal = 24.dp)) {
                    genres.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            row.forEach { (chip, color) ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(color)
                                        .clickable {
                                            navController.navigate("search/${URLEncoder.encode(chip, "UTF-8")}")
                                        }
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = chip,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun AppleStatsScreen(
    navController: NavController,
    viewModel: com.arturo254.opentune.viewmodels.StatsViewModel = hiltViewModel(),
) {
    val mostPlayedSongsStats by viewModel.mostPlayedSongsStats.collectAsState()
    val mostPlayedArtists by viewModel.mostPlayedArtists.collectAsState()
    val mostPlayedAlbums by viewModel.mostPlayedAlbums.collectAsState()
    val mostPlayedSongs by viewModel.mostPlayedSongs.collectAsState()
    val playerConnection = LocalPlayerConnection.current ?: return

    AppleScaffold(
        title = "Stats",
        navController = navController
    ) {
        if (mostPlayedSongsStats.isNotEmpty()) {
            item {
                AppleSectionTitle("Your Top Songs")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(mostPlayedSongsStats.take(12).size) { index ->
                        val songStat = mostPlayedSongsStats[index]
                        val song = mostPlayedSongs.getOrNull(index)
                        AppleTile(
                            title = songStat.title,
                            subtitle = "${songStat.songCountListened} plays",
                            thumbnailUrl = songStat.thumbnailUrl?.highQualityThumbnailUrlOrNull(),
                            onClick = {
                                if (song != null) {
                                    playerConnection.playQueue(
                                        YouTubeQueue(
                                            endpoint = com.arturo254.opentune.innertube.models.WatchEndpoint(videoId = songStat.id),
                                            preloadItem = song.toMediaMetadata(),
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
        
        if (mostPlayedArtists.isNotEmpty()) {
            item {
                AppleSectionTitle("Your Top Artists")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(mostPlayedArtists.take(12)) { artist ->
                        AppleTile(
                            title = artist.artist.name,
                            subtitle = "${artist.songCount} plays",
                            thumbnailUrl = artist.artist.thumbnailUrl?.highQualityThumbnailUrlOrNull(),
                            onClick = {
                                navController.navigate("artist/${artist.id}")
                            }
                        )
                    }
                }
            }
        }
        
        if (mostPlayedAlbums.isNotEmpty()) {
            item {
                AppleSectionTitle("Your Top Albums")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(mostPlayedAlbums.take(12)) { album ->
                        AppleTile(
                            title = album.album.title,
                            subtitle = "${album.songCountListened} plays",
                            thumbnailUrl = album.album.thumbnailUrl?.highQualityThumbnailUrlOrNull(),
                            onClick = {
                                navController.navigate("album/${album.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}


