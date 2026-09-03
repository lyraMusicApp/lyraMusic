/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */

package com.arturo254.opentune.ui.screens.settings

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arturo254.opentune.BuildConfig
import com.arturo254.opentune.LocalPlayerAwareWindowInsets
import com.arturo254.opentune.R
import com.arturo254.opentune.ui.component.IconButton
import com.arturo254.opentune.ui.utils.backToMain

// ── Shimmer brush (reused from original, kept as-is) ──────────────────────

@Composable
fun shimmerEffect(): Brush {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.0f),
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.0f),
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -300f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerX",
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnim, y = 0f),
        end = Offset(x = translateAnim + 300f, y = 0f),
    )
}

data class SocialLink(
    val iconRes: Int,
    val url: String,
    val label: String,
    val handle: String = "",
)

// ── Main screen ────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.about),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
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
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(innerPadding)
                .windowInsetsPadding(
                    LocalPlayerAwareWindowInsets.current.only(
                        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp,
            ),
        ) {

            // ── Hero card ─────────────────────────────────────────────────
            item {
                HeroCard(shimmerBrush = shimmerEffect())
            }

            // ── Social card ───────────────────────────────────────────────
            item {
                SocialCard(
                    links = listOf(
                        SocialLink(R.drawable.github, "https://github.com/shnwazdeveloper", "GitHub", "shnwazdeveloper"),
                        SocialLink(R.drawable.telegram, "https://t.me/SHNWAZX", "Telegram", "SHNWAZX"),
                        SocialLink(R.drawable.instagram, "https://www.instagram.com/shnwazxc/", "Instagram", "shnwazxc"),
                    ),
                    onLinkClick = { uriHandler.openUri(it) },
                )
            }

            item {
                LicenseFooter(
                    onLicenseClick = {
                        uriHandler.openUri("https://github.com/shnwazdeveloper/lyra-music/blob/master/LICENSE")
                    }
                )
            }
        }
    }
}

// ── Hero card ──────────────────────────────────────────────────────────────

@Composable
private fun HeroCard(shimmerBrush: Brush) {
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
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // App icon with glowing container
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF3B82F6),
                                Color(0xFF8B5CF6)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.opentune_monochrome),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp),
                )
            }

            // App name
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            // Version + build badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                VersionBadge(
                    text = "v${BuildConfig.VERSION_NAME}",
                    containerColor = Color(0xFF3B82F6).copy(alpha = 0.25f),
                    contentColor = Color(0xFF93C5FD),
                )
                VersionBadge(
                    text = "#${BuildConfig.VERSION_CODE}",
                    containerColor = Color(0xFF8B5CF6).copy(alpha = 0.25f),
                    contentColor = Color(0xFFC4B5FD),
                )
                if (BuildConfig.DEBUG) {
                    VersionBadge(
                        text = "DEBUG",
                        containerColor = Color(0xFFEF4444).copy(alpha = 0.25f),
                        contentColor = Color(0xFFFCA5A5),
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = Color.White.copy(alpha = 0.08f),
            )

            // Dev credit row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AsyncImage(
                    model = "https://github.com/shnwazdeveloper.png",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color(0xFFD4E84B), CircleShape),
                )
                Column {
                    Text(
                        text = "Dev by shnwaz",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "GPL-3.0 License",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF9CA3AF)
                        )
                    )
                }
            }
        }
    }
}

// ── Social card ────────────────────────────────────────────────────────────

@Composable
private fun SocialCard(
    links: List<SocialLink>,
    onLinkClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF161922))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color.White.copy(alpha = 0.03f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Section header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF8B5CF6).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.link),
                        contentDescription = null,
                        tint = Color(0xFFC4B5FD),
                        modifier = Modifier.size(20.dp),
                    )
                }
                Text(
                    text = stringResource(R.string.social_links),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            // Sleek Social Link Rows
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                links.forEach { link ->
                    SocialTile(
                        iconRes = link.iconRes,
                        label = link.label,
                        handle = link.handle,
                        onClick = { onLinkClick(link.url) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SocialTile(
    iconRes: Int,
    label: String,
    handle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1F2432).copy(alpha = 0.7f))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = label,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                }
                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                    if (handle.isNotBlank()) {
                        Text(
                            text = handle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF9CA3AF)
                            )
                        )
                    }
                }
            }
            Icon(
                painter = painterResource(R.drawable.arrow_forward),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

// ── License footer ─────────────────────────────────────────────────────────

@Composable
private fun LicenseFooter(onLicenseClick: () -> Unit) {
    Box(
        modifier = Modifier
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
            .clickable(onClick = onLicenseClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3B82F6).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.policy),
                    contentDescription = null,
                    tint = Color(0xFF93C5FD),
                    modifier = Modifier.size(20.dp),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "GNU General Public License v3.0",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
                Text(
                    text = stringResource(R.string.view_license),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF9CA3AF)
                    )
                )
            }

            Icon(
                painter = painterResource(R.drawable.arrow_forward),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

// ── Small helpers ──────────────────────────────────────────────────────────

@Composable
private fun VersionBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        )
    }
}
