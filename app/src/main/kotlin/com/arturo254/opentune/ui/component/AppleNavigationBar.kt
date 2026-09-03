package com.arturo254.opentune.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arturo254.opentune.ui.screens.Screens

import com.arturo254.opentune.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppleNavigationBar(
    modifier: Modifier = Modifier,
    items: List<CurvedBottomNavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    backdrop: PlatformBackdrop
) {
    val layer = rememberGraphicsLayer()
    val luminanceAnimation = remember { Animatable(0.3f) }

    val themeContrastColor by animateColorAsState(
        targetValue = Color.White,
        animationSpec = tween(500),
        label = "ContrastColor"
    )

    val itemBgColor by animateColorAsState(
        targetValue = Color.White.copy(alpha = 0.2f),
        animationSpec = tween(500),
        label = "ItemBgColor"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalFloatingToolbar(
            modifier = Modifier
                .drawBackdropCustomShape(
                    backdrop = backdrop,
                    layer = layer,
                    luminanceAnimation = luminanceAnimation.value,
                    shape = CircleShape
                )
                .wrapContentSize(),
            colors = androidx.compose.material3.FloatingToolbarDefaults.standardFloatingToolbarColors()
                .copy(toolbarContainerColor = Color.Transparent),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp),
            expanded = true
        ) {
            val searchItemIndex = items.indexOfFirst { it.titleId == R.string.search }
            val hasSearch = searchItemIndex != -1
            
            val toolbarItems = if (hasSearch) {
                items.filterIndexed { index, _ -> index != searchItemIndex }
            } else {
                items.dropLast(1)
            }

            toolbarItems.forEach { item ->
                val actualIndex = items.indexOf(item)
                val isSelected = selectedIndex == actualIndex
                Button(
                    onClick = { onItemSelected(actualIndex) },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = if (isSelected) itemBgColor else Color.Transparent,
                        contentColor = if (isSelected) Color(0xFFFA233B) else themeContrastColor
                    ),
                    modifier = Modifier.padding(horizontal = 0.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 2.dp, vertical = 4.dp)
                ) {
                    androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = if (isSelected) item.iconActive else item.iconInactive),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(2.dp))
                        androidx.compose.material3.Text(
                            text = androidx.compose.ui.res.stringResource(id = item.titleId),
                            style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        val searchItemIndex = items.indexOfFirst { it.titleId == R.string.search }
        val hasSearch = searchItemIndex != -1
        val fabItemIndex = if (hasSearch) searchItemIndex else items.size - 1
        val fabItem = items[fabItemIndex]
        val isFabSelected = selectedIndex == fabItemIndex

        FloatingActionButton(
            modifier = Modifier
                .drawBackdropCustomShape(
                    backdrop = backdrop,
                    layer = layer,
                    luminanceAnimation = luminanceAnimation.value,
                    shape = CircleShape
                ),
            onClick = { onItemSelected(fabItemIndex) },
            shape = CircleShape,
            containerColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = if (isFabSelected) fabItem.iconActive else fabItem.iconInactive),
                    contentDescription = null,
                    tint = if (isFabSelected) Color(0xFFFA233B) else themeContrastColor,
                    modifier = Modifier.size(24.dp)
                )
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(2.dp))
                androidx.compose.material3.Text(
                    text = androidx.compose.ui.res.stringResource(id = fabItem.titleId),
                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = if (isFabSelected) Color(0xFFFA233B) else themeContrastColor,
                    maxLines = 1
                )
            }
        }
    }
}

