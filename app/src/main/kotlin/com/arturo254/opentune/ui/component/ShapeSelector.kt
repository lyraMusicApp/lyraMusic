package com.arturo254.opentune.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import com.arturo254.opentune.R
import com.arturo254.opentune.utils.getPlayerControlShape

data class PlayerShapeOption(
    val name: String,
    val shape: RoundedPolygon,
    val displayName: String,
)

enum class PlayerShapeTarget {
    SMALL_BUTTONS,
    PLAY_PAUSE,
    MINIPLAYER_THUMBNAIL,
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UnifiedShapeBottomSheet(
    selectedSmallButtonsShape: String,
    selectedPlayPauseShape: String,
    selectedMiniPlayerShape: String,
    onSmallButtonsShapeSelected: (String) -> Unit,
    onPlayPauseShapeSelected: (String) -> Unit,
    onMiniPlayerShapeSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    initialTab: PlayerShapeTarget = PlayerShapeTarget.SMALL_BUTTONS,
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(
            when (initialTab) {
                PlayerShapeTarget.SMALL_BUTTONS -> 0
                PlayerShapeTarget.PLAY_PAUSE -> 1
                PlayerShapeTarget.MINIPLAYER_THUMBNAIL -> 2
            },
        )
    }

    val availableShapes = remember {
        listOf(
            "Pill" to "Pill",
            "Circle" to "Circle",
            "Square" to "Square",
            "Diamond" to "Diamond",
            "Pentagon" to "Pentagon",
            "Heart" to "Heart",
            "Oval" to "Oval",
            "Arch" to "Arch",
            "SemiCircle" to "Semicircle",
            "Triangle" to "Triangle",
            "Arrow" to "Arrow",
            "Fan" to "Fan",
            "Gem" to "Gem",
            "Bun" to "Bun",
            "Ghostish" to "Ghost-ish",
            "Cookie4Sided" to "Cookie 4",
            "Cookie6Sided" to "Cookie 6",
            "Cookie7Sided" to "Cookie 7",
            "Cookie9Sided" to "Cookie 9",
            "Cookie12Sided" to "Cookie 12",
            "Clover4Leaf" to "Clover 4",
            "Clover8Leaf" to "Clover 8",
            "Sunny" to "Sunny",
            "VerySunny" to "Very Sunny",
            "Burst" to "Burst",
            "SoftBurst" to "Soft Burst",
            "Boom" to "Boom",
            "SoftBoom" to "Soft Boom",
            "Flower" to "Flower",
            "PixelCircle" to "Pixel Circle",
            "PixelTriangle" to "Pixel Triangle",
            "Puffy" to "Puffy",
            "PuffyDiamond" to "Puffy Diamond",
            "Slanted" to "Slanted",
            "ClamShell" to "Clam Shell",
        ).map { (name, displayName) ->
            PlayerShapeOption(name, getPlayerControlShape(name), displayName)
        }
    }

    val tabTitles = listOf("Small", "Play", "Mini")
    val currentSelectedShape =
        when (selectedTabIndex) {
            0 -> selectedSmallButtonsShape
            1 -> selectedPlayPauseShape
            2 -> selectedMiniPlayerShape
            else -> selectedSmallButtonsShape
        }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
        ) {
            Text(
                text = "Shape selector",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                text = "Customize player button shapes",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 20.dp),
            )
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier =
                    Modifier
                        .padding(bottom = 20.dp)
                        .clip(RoundedCornerShape(12.dp)),
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        },
                    )
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.heightIn(max = 400.dp),
            ) {
                items(availableShapes) { shapeOption ->
                    PlayerShapeItem(
                        shapeOption = shapeOption,
                        isSelected = shapeOption.name == currentSelectedShape,
                        onClick = {
                            when (selectedTabIndex) {
                                0 -> onSmallButtonsShapeSelected(shapeOption.name)
                                1 -> onPlayPauseShapeSelected(shapeOption.name)
                                2 -> onMiniPlayerShapeSelected(shapeOption.name)
                            }
                            onDismiss()
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlayerShapeItem(
    shapeOption: PlayerShapeOption,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "shapeScale",
    )
    val backgroundColor by animateColorAsState(
        targetValue =
            if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainerHighest
            },
        animationSpec = tween(250),
        label = "shapeBackground",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = tween(250),
        label = "shapeBorder",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier =
            Modifier
                .scale(scale)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp),
                )
                .clickable(onClick = onClick)
                .padding(8.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(40.dp)
                    .clip(shapeOption.shape.toShape())
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    ),
        )
        Text(
            text = shapeOption.displayName,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            minLines = 2,
            color =
                if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnifiedShapeSelectorButton(
    smallButtonsShape: String,
    playPauseShape: String,
    miniPlayerShape: String,
    onSmallButtonsShapeSelected: (String) -> Unit,
    onPlayPauseShapeSelected: (String) -> Unit,
    onMiniPlayerShapeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    PreferenceEntry(
        title = { Text("Shape selector") },
        description = "Customize player button shapes",
        icon = {
            Icon(
                painter = painterResource(R.drawable.palette),
                contentDescription = null,
            )
        },
        onClick = { showBottomSheet = true },
        modifier = modifier,
    )

    if (showBottomSheet) {
        UnifiedShapeBottomSheet(
            selectedSmallButtonsShape = smallButtonsShape,
            selectedPlayPauseShape = playPauseShape,
            selectedMiniPlayerShape = miniPlayerShape,
            onSmallButtonsShapeSelected = onSmallButtonsShapeSelected,
            onPlayPauseShapeSelected = onPlayPauseShapeSelected,
            onMiniPlayerShapeSelected = onMiniPlayerShapeSelected,
            onDismiss = { showBottomSheet = false },
            sheetState = sheetState,
        )
    }
}
