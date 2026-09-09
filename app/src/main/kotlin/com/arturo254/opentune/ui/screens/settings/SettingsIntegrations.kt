/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */

package com.arturo254.opentune.ui.screens.settings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsIntegrationsSection(
    integrations: List<SettingsIntegrationAction>,
    modifier: Modifier = Modifier,
) {
    if (integrations.isEmpty()) return

    if (integrations.size == 1) {
        val action = integrations.first()
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val scale by animateFloatAsState(
            targetValue = if (isPressed) SettingsAnimations.PressScale else 1f,
            animationSpec = SettingsAnimations.pressSpring(),
            label = "integrationScale",
        )

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .scale(scale),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            onClick = action.onClick,
            interactionSource = interactionSource,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(action.accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = action.icon,
                        contentDescription = null,
                        tint = action.accentColor,
                        modifier = Modifier.size(26.dp),
                    )
                }

                Text(
                    text = action.label,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    } else {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                count = integrations.size,
                key = { integrations[it].label },
            ) { index ->
                IntegrationPill(action = integrations[index])
            }
        }
    }
}

@Composable
fun IntegrationPill(
    action: SettingsIntegrationAction,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) SettingsAnimations.PillPressScale else 1f,
        animationSpec = SettingsAnimations.pressSpring(),
        label = "pillScale",
    )
    val lift by animateFloatAsState(
        targetValue = if (isPressed) SettingsAnimations.PillPressLift.value else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "pillLift",
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .graphicsLayer { translationY = lift },
        shape = RoundedCornerShape(SettingsDimensions.IntegrationPillCornerRadius),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        onClick = action.onClick,
        interactionSource = interactionSource,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(SettingsDimensions.IntegrationIconSize)
                    .clip(RoundedCornerShape(8.dp))
                    .background(action.accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = action.icon,
                    contentDescription = null,
                    tint = action.accentColor,
                    modifier = Modifier.size(SettingsDimensions.IntegrationIconInnerSize),
                )
            }

            Text(
                text = action.label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
