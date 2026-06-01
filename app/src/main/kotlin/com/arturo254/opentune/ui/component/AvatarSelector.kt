package com.arturo254.opentune.ui.component

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import coil3.compose.AsyncImage
import com.arturo254.opentune.R
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

val Context.avatarDataStore: DataStore<Preferences> by preferencesDataStore(name = "avatar_preferences")

sealed class AvatarSelection {
    data object Default : AvatarSelection()
    data class DiceBear(val url: String) : AvatarSelection()
    data class Custom(val uri: String) : AvatarSelection()
}

class AvatarPreferenceManager(private val context: Context) {
    private val selectedAvatarTypeKey = stringPreferencesKey("selected_avatar_type")
    private val customAvatarUriKey = stringPreferencesKey("custom_avatar_uri")
    private val diceBearAvatarUrlKey = stringPreferencesKey("dicebear_avatar_url")

    val getAvatarSelection: Flow<AvatarSelection> =
        context.avatarDataStore.data.map { preferences ->
            when (preferences[selectedAvatarTypeKey] ?: "default") {
                "dicebear" -> preferences[diceBearAvatarUrlKey]?.let(AvatarSelection::DiceBear) ?: AvatarSelection.Default
                "custom" -> preferences[customAvatarUriKey]?.let(AvatarSelection::Custom) ?: AvatarSelection.Default
                else -> AvatarSelection.Default
            }
        }

    suspend fun saveAvatarSelection(selection: AvatarSelection) {
        context.avatarDataStore.edit { preferences ->
            when (selection) {
                AvatarSelection.Default -> {
                    preferences[selectedAvatarTypeKey] = "default"
                    preferences.remove(customAvatarUriKey)
                    preferences.remove(diceBearAvatarUrlKey)
                }
                is AvatarSelection.Custom -> {
                    preferences[selectedAvatarTypeKey] = "custom"
                    preferences[customAvatarUriKey] = selection.uri
                    preferences.remove(diceBearAvatarUrlKey)
                }
                is AvatarSelection.DiceBear -> {
                    preferences[selectedAvatarTypeKey] = "dicebear"
                    preferences[diceBearAvatarUrlKey] = selection.url
                    preferences.remove(customAvatarUriKey)
                }
            }
        }
    }
}

private enum class DiceBearStyle(val value: String, val displayName: String) {
    SHAPES("shapes", "Shapes"),
    INITIALS("initials", "Initials"),
    IDENTICON("identicon", "Identicon"),
    PIXEL_ART("pixel-art", "Pixel Art"),
    LORELEI("lorelei", "Lorelei"),
    GLASS("glass", "Glass"),
    THUMBS("thumbs", "Thumbs"),
    FUN_EMOJI("fun-emoji", "Fun Emoji"),
}

private object DiceBearGenerator {
    fun presetAvatars(style: DiceBearStyle): List<String> {
        val seeds = listOf(
            "Lyra", "Shnwaz", "Music", "Nova", "Echo", "Wave", "Orbit", "Aster",
            "Vibe", "Pulse", "Amaya", "Riley", "Avery", "Leo", "Sara", "Aiden",
            "Mira", "Aria", "Zayn", "Noor",
        ).shuffled(Random(System.currentTimeMillis()))
        return seeds.map { seed ->
            "https://api.dicebear.com/7.x/${style.value}/png?seed=$seed&size=256"
        }
    }
}

@Composable
fun AvatarSelector(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val avatarManager = remember { AvatarPreferenceManager(context) }
    val currentSelection by avatarManager.getAvatarSelection.collectAsState(initial = AvatarSelection.Default)
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var showAvatarSheet by remember { mutableStateOf(false) }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@rememberLauncherForActivityResult
            coroutineScope.launch {
                isLoading = true
                val savedUri = saveAvatarToInternalStorage(context, uri)
                if (savedUri != null) {
                    avatarManager.saveAvatarSelection(AvatarSelection.Custom(savedUri))
                } else {
                    error = context.getString(R.string.error_saving_image)
                }
                isLoading = false
            }
        }

    error?.let {
        LaunchedEffect(it) {
            delay(4000)
            error = null
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.avatar_selection),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                }
            }

            CurrentAvatarDisplay(
                selection = currentSelection,
                isLoading = isLoading,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(R.string.custom_avatar))
                }
                Button(
                    onClick = { showAvatarSheet = true },
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Avatars")
                }
            }

            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        cleanupSavedAvatars(context)
                        avatarManager.saveAvatarSelection(AvatarSelection.Default)
                    }
                },
                enabled = currentSelection !is AvatarSelection.Default && !isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.restore_default_avatar))
            }

            AnimatedVisibility(error != null) {
                Text(
                    text = error.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }

    if (showAvatarSheet) {
        DiceBearAvatarSheet(
            currentSelection = currentSelection,
            onDismiss = { showAvatarSheet = false },
            onAvatarSelected = { url ->
                coroutineScope.launch {
                    avatarManager.saveAvatarSelection(AvatarSelection.DiceBear(url))
                }
                showAvatarSheet = false
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiceBearAvatarSheet(
    currentSelection: AvatarSelection,
    onDismiss: () -> Unit,
    onAvatarSelected: (String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedStyle by remember { mutableStateOf(DiceBearStyle.SHAPES) }
    var avatarUrls by remember(selectedStyle) { mutableStateOf(DiceBearGenerator.presetAvatars(selectedStyle)) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.choose_predefined_avatar),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                FilledTonalButton(
                    onClick = {
                        val nextIndex = (DiceBearStyle.entries.indexOf(selectedStyle) + 1) % DiceBearStyle.entries.size
                        selectedStyle = DiceBearStyle.entries[nextIndex]
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(selectedStyle.displayName)
                }
                IconButton(
                    onClick = { avatarUrls = DiceBearGenerator.presetAvatars(selectedStyle) },
                ) {
                    Icon(painter = painterResource(R.drawable.shuffle), contentDescription = null)
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight(0.72f),
            ) {
                items(avatarUrls) { url ->
                    val isSelected = currentSelection is AvatarSelection.DiceBear && currentSelection.url == url
                    Box(
                        modifier =
                            Modifier
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color =
                                        if (isSelected) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                                        },
                                    shape = CircleShape,
                                )
                                .clickable { onAvatarSelected(url) },
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentAvatarDisplay(
    selection: AvatarSelection,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(3.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.55f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.size(40.dp), strokeWidth = 3.dp)
            selection is AvatarSelection.Custom -> {
                AsyncImage(
                    model = selection.uri,
                    contentDescription = stringResource(R.string.custom_avatar),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            selection is AvatarSelection.DiceBear -> {
                AsyncImage(
                    model = selection.url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            else -> {
                Icon(
                    painter = painterResource(R.drawable.person),
                    contentDescription = stringResource(R.string.default_avatar),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(40.dp),
                )
            }
        }
    }
}

private suspend fun saveAvatarToInternalStorage(context: Context, uri: Uri): String? =
    withContext(Dispatchers.IO) {
        runCatching {
            cleanupSavedAvatars(context)
            val outputFile = File(context.filesDir, "custom_avatar_${System.currentTimeMillis()}")
            context.contentResolver.openInputStream(uri)?.use { input ->
                outputFile.outputStream().use { output -> input.copyTo(output) }
            } ?: return@runCatching null
            Uri.fromFile(outputFile).toString()
        }.getOrNull()
    }

private fun cleanupSavedAvatars(context: Context) {
    context.filesDir
        .listFiles { file -> file.name.startsWith("custom_avatar_") }
        ?.forEach { runCatching { it.delete() } }
}
