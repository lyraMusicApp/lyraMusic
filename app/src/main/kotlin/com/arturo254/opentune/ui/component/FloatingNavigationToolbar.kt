@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.arturo254.opentune.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arturo254.opentune.R
import com.arturo254.opentune.ui.screens.Screens

@Composable
fun FloatingNavigationToolbar(
    items: List<Screens>,
    pureBlack: Boolean,
    liquidGlass: Boolean = false,
    modifier: Modifier = Modifier,
    onFabClick: (() -> Unit)? = null,
    fabIconRes: Int? = null,
    fabContentDescription: String = "",
    onShuffleClick: (() -> Unit)? = null,
    shuffleIconRes: Int? = null,
    shuffleContentDescription: String = "",
    onMusicRecognitionClick: (() -> Unit)? = null,
    musicRecognitionContentDescription: String = "",
    isSelected: (Screens) -> Boolean,
    onItemClick: (Screens, Boolean) -> Unit,
) {
    val hasOverflowAction = onShuffleClick != null && shuffleIconRes != null
    val hasFabAction = onFabClick != null && fabIconRes != null

    // Liquid Glass border & light refraction
    val glassModifier = if (liquidGlass) {
        Modifier.liquidGlassStyle(pureBlack = pureBlack)
    } else {
        Modifier
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        val showSelectedLabels = maxWidth >= 360.dp

        Surface(
            shape = RoundedCornerShape(32.dp),
            color = if (pureBlack) Color.Black else Color(0xEE141722),
            tonalElevation = 6.dp,
            modifier = Modifier
                .widthIn(max = 440.dp)
                .height(54.dp)
                .then(glassModifier)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEach { screen ->
                    val selected = isSelected(screen)
                    FloatingNavigationToolbarItem(
                        screen = screen,
                        selected = selected,
                        showSelectedLabel = showSelectedLabels,
                        pureBlack = pureBlack,
                        liquidGlass = liquidGlass,
                        onClick = { onItemClick(screen, selected) },
                    )
                }

                if (hasOverflowAction) {
                    FloatingToolbarOverflowAction(
                        pureBlack = pureBlack,
                        liquidGlass = liquidGlass,
                        onShuffleClick = onShuffleClick,
                        shuffleIconRes = shuffleIconRes,
                        shuffleContentDescription = shuffleContentDescription,
                        onMusicRecognitionClick = onMusicRecognitionClick,
                        musicRecognitionContentDescription = musicRecognitionContentDescription,
                    )
                } else if (hasFabAction) {
                    FloatingToolbarFabAction(
                        pureBlack = pureBlack,
                        liquidGlass = liquidGlass,
                        onClick = onFabClick,
                        iconRes = fabIconRes,
                        contentDescription = fabContentDescription,
                    )
                }
            }
        }
    }
}

// ── Efecto visual Liquid Glass ───────────────────────────────────────────────

private fun Modifier.liquidGlassStyle(
    pureBlack: Boolean,
    shape: Shape = RoundedCornerShape(32.dp),
): Modifier =
    this
        .clip(shape)
        .border(
            width = 0.8.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = if (pureBlack) 0.22f else 0.38f),
                    Color.White.copy(alpha = if (pureBlack) 0.04f else 0.08f),
                ),
            ),
            shape = shape,
        )
        .drawWithContent {
            drawContent()

            // highlight horizontal (vidrio)
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = if (pureBlack) 0.10f else 0.18f),
                        Color.Transparent,
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f)
                ),
                size = size,
                cornerRadius = CornerRadius(
                    x = size.height / 4f,
                    y = size.height / 4f
                )
            )

            // brillo superior
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = if (pureBlack) 0.06f else 0.12f),
                        Color.Transparent,
                    ),
                    startY = 0f,
                    endY = size.height * 0.6f
                ),
                size = size,
                cornerRadius = CornerRadius(
                    x = size.height / 4f,
                    y = size.height / 4f
                )
            )
        }

// ── Subcomponentes ───────────────────────────────────────────────────────────

@Composable
private fun FloatingToolbarOverflowAction(
    pureBlack: Boolean,
    liquidGlass: Boolean,
    onShuffleClick: (() -> Unit)?,
    shuffleIconRes: Int?,
    shuffleContentDescription: String,
    onMusicRecognitionClick: (() -> Unit)?,
    musicRecognitionContentDescription: String,
) {
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "overflowScale",
    )

    Box {
        Box(
            modifier = Modifier
                .scale(scale)
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFD4E84B))
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = { fabMenuExpanded = !fabMenuExpanded }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.more_horiz),
                contentDescription = shuffleContentDescription.ifEmpty {
                    stringResource(R.string.more)
                },
                tint = Color(0xFF111827),
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = fabMenuExpanded,
            onDismissRequest = { fabMenuExpanded = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = if (pureBlack) Color.Black else MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 6.dp,
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.music_recognition)) },
                onClick = {
                    fabMenuExpanded = false
                    onMusicRecognitionClick?.invoke()
                },
                leadingIcon = {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = floatingToolbarMenuIconContainerColor(pureBlack = pureBlack),
                        contentColor = floatingToolbarMenuIconContentColor(pureBlack = pureBlack),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(R.drawable.mic),
                                contentDescription =
                                    musicRecognitionContentDescription.ifEmpty {
                                        stringResource(R.string.music_recognition)
                                    },
                            )
                        }
                    }
                },
                enabled = onMusicRecognitionClick != null,
                colors =
                    MenuDefaults.itemColors(
                        textColor = if (pureBlack) Color.White else MaterialTheme.colorScheme.onSurface,
                        leadingIconColor = if (pureBlack) Color.White.copy(alpha = 0.82f) else MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = if (pureBlack) Color.White.copy(alpha = 0.38f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        disabledLeadingIconColor = if (pureBlack) Color.White.copy(alpha = 0.38f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                    ),
            )

            if (onShuffleClick != null && shuffleIconRes != null) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.shuffle)) },
                    onClick = {
                        fabMenuExpanded = false
                        onShuffleClick()
                    },
                    leadingIcon = {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = floatingToolbarMenuIconContainerColor(pureBlack = pureBlack),
                            contentColor = floatingToolbarMenuIconContentColor(pureBlack = pureBlack),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(shuffleIconRes),
                                    contentDescription =
                                        shuffleContentDescription.ifEmpty {
                                            stringResource(R.string.shuffle)
                                        },
                                )
                            }
                        }
                    },
                    colors =
                        MenuDefaults.itemColors(
                            textColor = if (pureBlack) Color.White else MaterialTheme.colorScheme.onSurface,
                            leadingIconColor = if (pureBlack) Color.White.copy(alpha = 0.82f) else MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                )
            }
        }
    }
}

@Composable
private fun FloatingToolbarFabAction(
    pureBlack: Boolean,
    liquidGlass: Boolean,
    onClick: (() -> Unit)?,
    iconRes: Int?,
    contentDescription: String,
) {
    if (onClick == null || iconRes == null) return
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "fabScale",
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .size(38.dp)
            .clip(CircleShape)
            .background(Color(0xFFD4E84B))
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription.ifEmpty {
                stringResource(R.string.create_playlist)
            },
            tint = Color(0xFF111827),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun FloatingNavigationToolbarItem(
    screen: Screens,
    selected: Boolean,
    showSelectedLabel: Boolean,
    pureBlack: Boolean,
    liquidGlass: Boolean,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) Color(0xFFD4E84B) else Color.Transparent,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "tabContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) Color(0xFF111827) else Color.White.copy(alpha = 0.85f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "tabContent",
    )
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "tabScale",
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .size(38.dp)
            .clip(CircleShape)
            .background(color = containerColor, shape = CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                role = Role.Tab,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(if (selected) screen.iconIdActive else screen.iconIdInactive),
            contentDescription = stringResource(screen.titleId),
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ── Color helpers ────────────────────────────────────────────────────────────

@Composable
private fun floatingToolbarContainerColor(pureBlack: Boolean, liquidGlass: Boolean): Color {
    return Color(0xEE181A22)
}

@Composable
private fun floatingToolbarFabContainerColor(pureBlack: Boolean, liquidGlass: Boolean): Color {
    return Color(0xFFD4E84B)
}

@Composable
private fun floatingToolbarFabContentColor(pureBlack: Boolean, liquidGlass: Boolean): Color {
    return Color(0xFF111827)
}

@Composable
private fun floatingToolbarSelectedItemContainerColor(pureBlack: Boolean, liquidGlass: Boolean): Color {
    return Color(0xFFD4E84B)
}

@Composable
private fun floatingToolbarSelectedItemContentColor(pureBlack: Boolean, liquidGlass: Boolean): Color {
    return Color(0xFF111827)
}

@Composable
private fun floatingToolbarItemContentColor(pureBlack: Boolean, liquidGlass: Boolean): Color {
    return Color.White.copy(alpha = 0.82f)
}


// Estas dos no cambian con liquidGlass (solo se usan en el DropdownMenu)
@Composable
private fun floatingToolbarMenuIconContainerColor(pureBlack: Boolean): Color {
    return if (pureBlack) Color.White.copy(alpha = 0.12f) else MaterialTheme.colorScheme.secondaryContainer
}

@Composable
private fun floatingToolbarMenuIconContentColor(pureBlack: Boolean): Color {
    return if (pureBlack) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
}