/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.arturo254.opentune.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arturo254.opentune.LocalPlayerAwareWindowInsets
import com.arturo254.opentune.R
import com.arturo254.opentune.constants.AccountChannelHandleKey
import com.arturo254.opentune.constants.AccountEmailKey
import com.arturo254.opentune.constants.AccountNameKey
import com.arturo254.opentune.constants.DiscordUsernameKey
import com.arturo254.opentune.constants.GridThumbnailHeight
import com.arturo254.opentune.constants.InnerTubeCookieKey
import com.arturo254.opentune.constants.LastFMUsernameKey
import com.arturo254.opentune.innertube.utils.parseCookieString
import com.arturo254.opentune.ui.component.ChipsRow
import com.arturo254.opentune.ui.component.IconButton
import com.arturo254.opentune.ui.component.LocalMenuState
import com.arturo254.opentune.ui.component.YouTubeGridItem
import com.arturo254.opentune.ui.component.shimmer.GridItemPlaceHolder
import com.arturo254.opentune.ui.component.shimmer.ShimmerHost
import com.arturo254.opentune.ui.menu.YouTubeAlbumMenu
import com.arturo254.opentune.ui.menu.YouTubeArtistMenu
import com.arturo254.opentune.ui.menu.YouTubePlaylistMenu
import com.arturo254.opentune.ui.utils.backToMain
import com.arturo254.opentune.utils.rememberPreference
import com.arturo254.opentune.viewmodels.AccountContentType
import com.arturo254.opentune.viewmodels.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AccountScreen(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val menuState = LocalMenuState.current
    val haptic = LocalHapticFeedback.current

    val coroutineScope = rememberCoroutineScope()

    val innerTubeCookie by rememberPreference(InnerTubeCookieKey, "")
    val accountName by rememberPreference(AccountNameKey, "Guest")
    val accountEmail by rememberPreference(AccountEmailKey, "")
    val accountChannelHandle by rememberPreference(AccountChannelHandleKey, "")
    val discordUsername by rememberPreference(DiscordUsernameKey, "")
    val lastfmUsername by rememberPreference(LastFMUsernameKey, "")

    val isLoggedIn = remember(innerTubeCookie) {
        "SAPISID" in parseCookieString(innerTubeCookie)
    }

    val playlists by viewModel.playlists.collectAsState()
    val albums by viewModel.albums.collectAsState()
    val artists by viewModel.artists.collectAsState()
    val selectedContentType by viewModel.selectedContentType.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isLoggedIn) {
            // Modern Glassmorphic Login & Integrations Hub
            LazyColumn(
                contentPadding = LocalPlayerAwareWindowInsets.current.asPaddingValues(),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    // Hero Branding Header Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(26.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF1F2432),
                                        Color(0xFF141722)
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.18f),
                                        Color.White.copy(alpha = 0.04f)
                                    )
                                ),
                                shape = RoundedCornerShape(26.dp)
                            )
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                Color(0xFFD4E84B).copy(alpha = 0.25f),
                                                Color(0xFF1E222B)
                                            )
                                        )
                                    )
                                    .border(1.5.dp, Color(0xFFD4E84B), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.account),
                                    contentDescription = null,
                                    tint = Color(0xFFD4E84B),
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Text(
                                text = stringResource(R.string.account),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )

                            Text(
                                text = "Sign in to access your library, cloud playlists, Discord Presence, and Last.fm scrobbling.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF9CA3AF)
                                ),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.integrations),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                // 1. Google / YouTube Music Sign-In
                item {
                    IntegrationLoginCard(
                        title = "Google / YouTube Music",
                        description = "Sync liked songs, cloud playlists, library & recommendations",
                        icon = R.drawable.google,
                        iconBgColor = Color(0xFFEA4335),
                        actionText = stringResource(R.string.login),
                        isConnected = false,
                        onClick = { navController.navigate("login") }
                    )
                }

                // 2. Discord Rich Presence
                item {
                    val isDiscordConnected = discordUsername.isNotBlank()
                    IntegrationLoginCard(
                        title = "Discord Rich Presence",
                        description = if (isDiscordConnected) "Connected as @$discordUsername" else "Broadcast playback activity and real-time status to Discord",
                        icon = R.drawable.discord,
                        iconBgColor = Color(0xFF5865F2),
                        actionText = if (isDiscordConnected) "Settings" else "Connect",
                        isConnected = isDiscordConnected,
                        onClick = { navController.navigate("settings/discord") }
                    )
                }

                // 3. Last.fm Scrobbler
                item {
                    val isLastFMConnected = lastfmUsername.isNotBlank()
                    IntegrationLoginCard(
                        title = "Last.fm Scrobbler",
                        description = if (isLastFMConnected) "Connected as $lastfmUsername" else "Track and scrobble music listening statistics to Last.fm",
                        icon = R.drawable.sync,
                        iconBgColor = Color(0xFFD51007),
                        actionText = if (isLastFMConnected) "Settings" else "Connect",
                        isConnected = isLastFMConnected,
                        onClick = { navController.navigate("settings/lastfm") }
                    )
                }

                // 4. Advanced PoToken / Web Client Token
                item {
                    IntegrationLoginCard(
                        title = "PO Token Generator",
                        description = "Generate visitor data and tokens for YouTube playback",
                        icon = R.drawable.account,
                        iconBgColor = Color(0xFF00D2C4),
                        actionText = "Configure",
                        isConnected = false,
                        onClick = { navController.navigate("settings/potoken") }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        } else {
            // Logged-in state with Profile Header + Library Grids
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = GridThumbnailHeight + 24.dp),
                contentPadding = LocalPlayerAwareWindowInsets.current.asPaddingValues(),
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    // Profile Banner
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.account),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = accountName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (accountEmail.isNotBlank() || accountChannelHandle.isNotBlank()) {
                                        Text(
                                            text = accountEmail.takeIf { it.isNotBlank() } ?: accountChannelHandle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                            OutlinedButton(
                                onClick = { navController.navigate("settings/account") },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(stringResource(R.string.account))
                            }
                        }
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    ChipsRow(
                        chips = listOf(
                            AccountContentType.PLAYLISTS to stringResource(R.string.filter_playlists),
                            AccountContentType.ALBUMS to stringResource(R.string.filter_albums),
                            AccountContentType.ARTISTS to stringResource(R.string.filter_artists),
                        ),
                        currentValue = selectedContentType,
                        onValueUpdate = { viewModel.setSelectedContentType(it) },
                    )
                }

                when (selectedContentType) {
                    AccountContentType.PLAYLISTS -> {
                        items(
                            items = playlists.orEmpty().distinctBy { it.id },
                            key = { it.id },
                        ) { item ->
                            YouTubeGridItem(
                                item = item,
                                fillMaxWidth = true,
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {
                                            navController.navigate("online_playlist/${item.id}")
                                        },
                                        onLongClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            menuState.show {
                                                YouTubePlaylistMenu(
                                                    playlist = item,
                                                    coroutineScope = coroutineScope,
                                                    onDismiss = menuState::dismiss,
                                                )
                                            }
                                        },
                                    ),
                            )
                        }

                        if (playlists == null) {
                            items(8) {
                                ShimmerHost {
                                    GridItemPlaceHolder(fillMaxWidth = true)
                                }
                            }
                        }
                    }

                    AccountContentType.ALBUMS -> {
                        items(
                            items = albums.orEmpty().distinctBy { it.id },
                            key = { it.id }
                        ) { item ->
                            YouTubeGridItem(
                                item = item,
                                fillMaxWidth = true,
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {
                                            navController.navigate("album/${item.id}")
                                        },
                                        onLongClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            menuState.show {
                                                YouTubeAlbumMenu(
                                                    albumItem = item,
                                                    navController = navController,
                                                    onDismiss = menuState::dismiss
                                                )
                                            }
                                        }
                                    )
                            )
                        }

                        if (albums == null) {
                            items(8) {
                                ShimmerHost {
                                    GridItemPlaceHolder(fillMaxWidth = true)
                                }
                            }
                        }
                    }

                    AccountContentType.ARTISTS -> {
                        items(
                            items = artists.orEmpty().distinctBy { it.id },
                            key = { it.id }
                        ) { item ->
                            YouTubeGridItem(
                                item = item,
                                fillMaxWidth = true,
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {
                                            navController.navigate("artist/${item.id}")
                                        },
                                        onLongClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            menuState.show {
                                                YouTubeArtistMenu(
                                                    artist = item,
                                                    onDismiss = menuState::dismiss
                                                )
                                            }
                                        }
                                    )
                            )
                        }

                        if (artists == null) {
                            items(8) {
                                ShimmerHost {
                                    GridItemPlaceHolder(fillMaxWidth = true)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    TopAppBar(
        title = { Text(stringResource(R.string.account)) },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain,
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    contentDescription = null,
                )
            }
        },
    )
}

@Composable
private fun IntegrationLoginCard(
    title: String,
    description: String,
    icon: Int,
    actionText: String,
    isConnected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconBgColor: Color = Color(0xFF262A36),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF161922))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color.White.copy(alpha = 0.03f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(iconBgColor.copy(alpha = if (isConnected) 0.9f else 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = if (isConnected) Color.White else iconBgColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF9CA3AF)
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (isConnected) Color(0xFF2A3142)
                        else Color(0xFFD4E84B)
                    )
                    .clickable(onClick = onClick)
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isConnected) Color.White else Color(0xFF111827)
                    )
                )
            }
        }
    }
}
