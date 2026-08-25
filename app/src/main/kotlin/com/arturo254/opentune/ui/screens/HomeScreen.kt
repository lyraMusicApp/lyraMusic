/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.arturo254.opentune.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.material3.MaterialTheme
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arturo254.opentune.innertube.utils.parseCookieString
import com.arturo254.opentune.LocalPlayerAwareWindowInsets
import com.arturo254.opentune.LocalPlayerConnection
import com.arturo254.opentune.R
import com.arturo254.opentune.constants.InnerTubeCookieKey
import com.arturo254.opentune.constants.DisableBlurKey
import com.arturo254.opentune.constants.ShowHomeCategoryChipsKey
import com.arturo254.opentune.ui.component.ChipsRow
import com.arturo254.opentune.ui.component.LocalBottomSheetPageState
import com.arturo254.opentune.ui.component.LocalMenuState
import com.arturo254.opentune.ui.component.NavigationTitle
import com.arturo254.opentune.ui.utils.SnapLayoutInfoProvider
import com.arturo254.opentune.utils.rememberPreference
import com.arturo254.opentune.models.toMediaMetadata
import com.arturo254.opentune.viewmodels.HomeViewModel


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val menuState = LocalMenuState.current
    val bottomSheetPageState = LocalBottomSheetPageState.current
    val playerConnection = LocalPlayerConnection.current ?: return
    val haptic = LocalHapticFeedback.current

    val isPlaying by playerConnection.isPlaying.collectAsState()
    val mediaMetadata by playerConnection.mediaMetadata.collectAsState()

    val quickPicks by viewModel.quickPicks.collectAsState()
    val speedDialSongs by viewModel.speedDialSongs.collectAsState()
    val forgottenFavorites by viewModel.forgottenFavorites.collectAsState()
    val keepListening by viewModel.keepListening.collectAsState()
    val homePage by viewModel.homePage.collectAsState()

    val selectedChip by viewModel.selectedChip.collectAsState()

    val isLoading: Boolean by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()

    val forgottenFavoritesLazyGridState = rememberLazyGridState()

    val accountName by viewModel.accountName.collectAsState()
    val accountImageUrl by viewModel.accountImageUrl.collectAsState()
    val innerTubeCookie by rememberPreference(InnerTubeCookieKey, "")
    val (disableBlur) = rememberPreference(DisableBlurKey, true)
    val (showHomeCategoryChips) = rememberPreference(ShowHomeCategoryChipsKey, true)
    val isLoggedIn = remember(innerTubeCookie) {
        "SAPISID" in parseCookieString(innerTubeCookie)
    }
    val url = if (isLoggedIn) accountImageUrl else null

    val scope = rememberCoroutineScope()
    val lazylistState = rememberLazyListState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val scrollToTop =
        backStackEntry?.savedStateHandle?.getStateFlow("scrollToTop", false)?.collectAsState()

    LaunchedEffect(scrollToTop?.value) {
        if (scrollToTop?.value == true) {
            lazylistState.animateScrollToItem(0)
            backStackEntry?.savedStateHandle?.set("scrollToTop", false)
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { lazylistState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                val len = lazylistState.layoutInfo.totalItemsCount
                if (lastVisibleIndex != null && lastVisibleIndex >= len - 3) {
                    viewModel.loadMoreYouTubeItems(homePage?.continuation)
                }
            }
    }

    if (selectedChip != null) {
        BackHandler {
            // if a chip is selected, go back to the normal homepage first
            viewModel.toggleChip(selectedChip)
        }
    }

    LaunchedEffect(showHomeCategoryChips, selectedChip) {
        if (!showHomeCategoryChips && selectedChip != null) {
            viewModel.toggleChip(selectedChip)
        }
    }

    LaunchedEffect(forgottenFavorites) {
        forgottenFavoritesLazyGridState.scrollToItem(0)
    }

    val color1 = MaterialTheme.colorScheme.primary
    val color2 = MaterialTheme.colorScheme.secondary
    val color3 = MaterialTheme.colorScheme.tertiary
    val color4 = MaterialTheme.colorScheme.primaryContainer
    val color5 = MaterialTheme.colorScheme.secondaryContainer
    val surfaceColor = MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Glowing Ambient Mesh gradient background layer at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.65f) // Cover top 65% of screen
                .align(Alignment.TopCenter)
                .zIndex(-1f) // Place behind all content
                .drawWithCache {
                    val width = this.size.width
                    val height = this.size.height

                    val brush1 = Brush.radialGradient(
                        colors = listOf(
                            color1.copy(alpha = 0.42f),
                            color1.copy(alpha = 0.22f),
                            color1.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = Offset(width * 0.15f, height * 0.12f),
                        radius = width * 0.65f
                    )

                    val brush2 = Brush.radialGradient(
                        colors = listOf(
                            color2.copy(alpha = 0.35f),
                            color3.copy(alpha = 0.18f),
                            color3.copy(alpha = 0.06f),
                            Color.Transparent
                        ),
                        center = Offset(width * 0.85f, height * 0.18f),
                        radius = width * 0.70f
                    )

                    val brush3 = Brush.radialGradient(
                        colors = listOf(
                            color4.copy(alpha = 0.25f),
                            color5.copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        center = Offset(width * 0.5f, height * 0.45f),
                        radius = width * 0.6f
                    )

                    val overlayBrush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            surfaceColor.copy(alpha = 0.4f),
                            surfaceColor.copy(alpha = 0.85f),
                            surfaceColor
                        ),
                        startY = height * 0.35f,
                        endY = height
                    )

                    onDrawBehind {
                        drawRect(brush = brush1)
                        drawRect(brush = brush2)
                        drawRect(brush = brush3)
                        drawRect(brush = overlayBrush)
                    }
                }
        ) {}
        
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .pullToRefresh(
                    state = pullRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = viewModel::refresh
                )
        ) {
            val horizontalLazyGridItemWidthFactor = if (maxWidth * 0.475f >= 320.dp) 0.475f else 0.9f
            val horizontalLazyGridItemWidth = maxWidth * horizontalLazyGridItemWidthFactor
            val forgottenFavoritesSnapLayoutInfoProvider = remember(forgottenFavoritesLazyGridState) {
                SnapLayoutInfoProvider(
                    lazyGridState = forgottenFavoritesLazyGridState,
                    positionInLayout = { layoutSize, itemSize ->
                        (layoutSize * horizontalLazyGridItemWidthFactor / 2f - itemSize / 2f)
                    }
                )
            }

            LazyColumn(
                state = lazylistState,
                contentPadding = LocalPlayerAwareWindowInsets.current.asPaddingValues()
            ) {
                // 1. Top Header with User Profile, "Hi, {name}", Search and Heart buttons
                item {
                    HomeModernHeader(
                        accountName = accountName,
                        accountImageUrl = url,
                        navController = navController
                    )
                }

                // 2. Pill Filter Chips (Active chip in bright lime-green)
                item {
                    HomePillChipsRow(
                        selectedChipTitle = selectedChip?.title ?: "All",
                        onChipSelected = { chipTitle ->
                            if (chipTitle == "All") {
                                if (selectedChip != null) {
                                    viewModel.toggleChip(selectedChip)
                                }
                            } else {
                                val match = homePage?.chips.orEmpty().find { it.title.equals(chipTitle, ignoreCase = true) }
                                if (match != null) {
                                    viewModel.toggleChip(match)
                                }
                            }
                        },
                        navController = navController
                    )
                }

                // 3. Curated & Trending Hero Banner Card
                item {
                    CuratedTrendingHeroCard(
                        onCardClick = { playlistId ->
                            navController.navigate("online_playlist/$playlistId")
                        }
                    )
                }

                // 4. Top Daily Playlists Section
                item {
                    TopDailyPlaylistsSection(
                        navController = navController,
                        onPlaylistClick = { playlistId ->
                            navController.navigate("online_playlist/$playlistId")
                        }
                    )
                }


                quickPicks?.takeIf { it.isNotEmpty() }?.let { picks ->
            /*
                item {
                    NavigationTitle(
                        title = stringResource(R.string.quick_picks),
                        modifier = Modifier.animateItem()
                    )
                }
            */

                item {
                    QuickPicksSection(
                        quickPicks = picks,
                        mediaMetadata = mediaMetadata,
                        isPlaying = isPlaying,
                        navController = navController,
                        playerConnection = playerConnection,
                        menuState = menuState,
                        haptic = haptic
                    )
                }
            }

            speedDialSongs.takeIf { it.isNotEmpty() }?.let { songs ->
                item {
                    NavigationTitle(
                        title = stringResource(R.string.speed_dial),
                        modifier = Modifier.animateItem()
                    )
                }

                item {
                    SpeedDialSection(
                        speedDialSongs = songs,
                        mediaMetadata = mediaMetadata,
                        isPlaying = isPlaying,
                        navController = navController,
                        playerConnection = playerConnection,
                        menuState = menuState,
                        haptic = haptic
                    )
                }
            }

            keepListening?.takeIf { it.isNotEmpty() }?.let { items ->
                item {
                    NavigationTitle(
                        title = stringResource(R.string.keep_listening),
                        modifier = Modifier.animateItem()
                    )
                }

                item {
                    KeepListeningSection(
                        keepListening = items,
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

            AccountPlaylistsContainer(
                viewModel = viewModel,
                accountName = accountName,
                accountImageUrl = url,
                mediaMetadata = mediaMetadata,
                isPlaying = isPlaying,
                navController = navController,
                playerConnection = playerConnection,
                menuState = menuState,
                haptic = haptic,
                scope = scope
            )

            forgottenFavorites?.takeIf { it.isNotEmpty() }?.let { favorites ->
                item {
                    NavigationTitle(
                        title = stringResource(R.string.forgotten_favorites),
                        modifier = Modifier.animateItem()
                    )
                }

                item {
                    ForgottenFavoritesSection(
                        forgottenFavorites = favorites,
                        mediaMetadata = mediaMetadata,
                        isPlaying = isPlaying,
                        horizontalLazyGridItemWidth = horizontalLazyGridItemWidth,
                        lazyGridState = forgottenFavoritesLazyGridState,
                        snapLayoutInfoProvider = forgottenFavoritesSnapLayoutInfoProvider,
                        navController = navController,
                        playerConnection = playerConnection,
                        menuState = menuState,
                        haptic = haptic
                    )
                }
            }

            SimilarRecommendationsContainer(
                viewModel = viewModel,
                mediaMetadata = mediaMetadata,
                isPlaying = isPlaying,
                navController = navController,
                playerConnection = playerConnection,
                menuState = menuState,
                haptic = haptic,
                scope = scope
            )

            homePage?.sections?.forEach { section ->
                item {
                    HomePageSectionTitle(
                        section = section,
                        navController = navController,
                        modifier = Modifier.animateItem()
                    )
                }

                item {
                    HomePageSectionContent(
                        section = section,
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

            if (isLoading || homePage?.continuation != null && homePage?.sections?.isNotEmpty() == true) {
                item {
                    HomeLoadingShimmer(modifier = Modifier.animateItem())
                }
            }
            }

            Indicator(
                isRefreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(LocalPlayerAwareWindowInsets.current.asPaddingValues()),
            )
        }
    }
}
