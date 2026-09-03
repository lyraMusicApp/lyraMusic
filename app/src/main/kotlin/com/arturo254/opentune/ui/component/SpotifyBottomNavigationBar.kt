package com.arturo254.opentune.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SpotifyBottomNavigationBar(
    items: List<CurvedBottomNavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.5f),
                        Color.Black.copy(alpha = 0.8f),
                        Color.Black,
                    )
                )
            )
    ) {
        NavigationBar(
            windowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
        ) {
            items.forEachIndexed { index, item ->
                val selected = selectedIndex == index
                NavigationBarItem(
                    selected = selected,
                    onClick = { onItemSelected(index) },
                    label = {
                        Text(
                            stringResource(item.titleId),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (selected) Color.White else Color.Gray
                            )
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(if (selected) item.iconActive else item.iconInactive),
                            contentDescription = stringResource(item.titleId),
                            tint = if (selected) Color.White else Color.Gray
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.windowInsetsPadding(
                        NavigationBarDefaults.windowInsets
                    )
                )
            }
        }
    }
}

