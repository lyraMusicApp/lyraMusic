/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */

package com.arturo254.opentune.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.AsyncImage
import com.arturo254.opentune.LocalPlayerAwareWindowInsets
import com.arturo254.opentune.R
import com.arturo254.opentune.ui.component.NavigationTitle
import com.arturo254.opentune.ui.component.shimmer.ShimmerHost
import com.arturo254.opentune.ui.component.shimmer.TextPlaceholder
import com.arturo254.opentune.viewmodels.MoodAndGenresViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoodAndGenresScreen(
    navController: NavController,
    viewModel: MoodAndGenresViewModel = hiltViewModel(),
) {
    val moodAndGenres by viewModel.moodAndGenres.collectAsState()
    val gridState = rememberLazyGridState()
    val density = LocalDensity.current
    val windowInsets = LocalPlayerAwareWindowInsets.current
    val topPadding = with(density) { windowInsets.getTop(this).toDp() }
    val bottomPadding = with(density) { windowInsets.getBottom(this).toDp() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val scrollToTop =
        backStackEntry?.savedStateHandle?.getStateFlow("scrollToTop", false)?.collectAsState()

    LaunchedEffect(scrollToTop?.value) {
        if (scrollToTop?.value == true) {
            gridState.animateScrollToItem(0)
            backStackEntry?.savedStateHandle?.set("scrollToTop", false)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 170.dp),
        state = gridState,
        contentPadding = PaddingValues(
            start = 10.dp,
            top = topPadding,
            end = 10.dp,
            bottom = bottomPadding,
        ),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            NavigationTitle(
                title = stringResource(R.string.mood_and_genres),
                modifier = Modifier.animateItem(),
            )
        }

        if (moodAndGenres == null) {
            items(12) {
                ShimmerHost {
                    TextPlaceholder(
                        height = MoodAndGenresButtonHeight,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.padding(6.dp),
                    )
                }
            }
        } else {
            items(
                items = moodAndGenres.orEmpty(),
                key = { item -> "${item.title}:${item.endpoint.browseId}:${item.endpoint.params}" },
            ) { item ->
                MoodAndGenresButton(
                    title = item.title,
                    stripeColor = item.stripeColor,
                    onClick = {
                        navController.navigate("youtube_browse/${item.endpoint.browseId}?params=${item.endpoint.params}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .animateItem(),
                )
            }
        }
    }
}

fun getGenreArtworkUrl(title: String): String {
    val lower = title.lowercase()
    return when {
        lower.contains("chill") -> "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?auto=format&fit=crop&w=300&q=80"
        lower.contains("commute") || lower.contains("drive") || lower.contains("car") -> "https://images.unsplash.com/photo-1449965408869-eaa3f722e40d?auto=format&fit=crop&w=300&q=80"
        lower.contains("energ") -> "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?auto=format&fit=crop&w=300&q=80"
        lower.contains("feel good") || lower.contains("happy") -> "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=300&q=80"
        lower.contains("focus") || lower.contains("study") -> "https://images.unsplash.com/photo-1434030216411-0b793f4b4173?auto=format&fit=crop&w=300&q=80"
        lower.contains("gaming") || lower.contains("game") -> "https://images.unsplash.com/photo-1538481199705-c710c4e965fc?auto=format&fit=crop&w=300&q=80"
        lower.contains("party") || lower.contains("dance") || lower.contains("club") -> "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=300&q=80"
        lower.contains("romance") || lower.contains("love") -> "https://images.unsplash.com/photo-1518199266791-5375a83190b7?auto=format&fit=crop&w=300&q=80"
        lower.contains("sad") -> "https://images.unsplash.com/photo-1516589178581-6cd7833ae3b2?auto=format&fit=crop&w=300&q=80"
        lower.contains("sleep") || lower.contains("relax") || lower.contains("bed") -> "https://images.unsplash.com/photo-1511295742362-92c96b124e52?auto=format&fit=crop&w=300&q=80"
        lower.contains("workout") || lower.contains("gym") || lower.contains("fitness") -> "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?auto=format&fit=crop&w=300&q=80"
        lower.contains("african") -> "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=300&q=80"
        lower.contains("arabic") -> "https://images.unsplash.com/photo-1548013146-72479768bada?auto=format&fit=crop&w=300&q=80"
        lower.contains("bengali") -> "https://images.unsplash.com/photo-1534447677768-be436bb09401?auto=format&fit=crop&w=300&q=80"
        lower.contains("rock") || lower.contains("metal") -> "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?auto=format&fit=crop&w=300&q=80"
        lower.contains("pop") -> "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=300&q=80"
        lower.contains("hip-hop") || lower.contains("rap") -> "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&w=300&q=80"
        lower.contains("jazz") || lower.contains("blues") -> "https://images.unsplash.com/photo-1511192336575-5a79af67a629?auto=format&fit=crop&w=300&q=80"
        lower.contains("classical") -> "https://images.unsplash.com/photo-1507838153414-b4b713384a76?auto=format&fit=crop&w=300&q=80"
        else -> "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=300&q=80"
    }
}

@Composable
fun MoodAndGenresButton(
    title: String,
    stripeColor: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val base = Color(stripeColor)
    val darkVariant = Color(
        red = (base.red * 0.45f).coerceIn(0f, 1f),
        green = (base.green * 0.45f).coerceIn(0f, 1f),
        blue = (base.blue * 0.45f).coerceIn(0f, 1f),
    )
    val gradient = Brush.linearGradient(
        colors = listOf(base, darkVariant),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )
    val artworkUrl = remember(title) { getGenreArtworkUrl(title) }

    Box(
        modifier = modifier
            .height(MoodAndGenresButtonHeight)
            .clip(RoundedCornerShape(18.dp))
            .background(gradient)
            .clickable(onClick = onClick)
    ) {
        // Rotated artwork thumbnail at bottom right corner matching Spotify/YT Music
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 4.dp, bottom = 2.dp)
        ) {
            AsyncImage(
                model = artworkUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .rotate(16f)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        // Left gradient overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.45f),
                            Color.Transparent
                        ),
                        startX = 0f,
                        endX = 180f
                    )
                )
        )

        // Title text top-start
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    offset = Offset(0f, 1.5f),
                    blurRadius = 4f,
                ),
            ),
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        )
    }
}

val MoodAndGenresButtonHeight = 92.dp