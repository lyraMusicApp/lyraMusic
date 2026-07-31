$playerDir = "C:\Users\SHNWAZ\.gemini\antigravity\scratch\lyraApp\app\src\main\kotlin\com\arturo254\opentune\ui\player"
$srcDir = "C:\Users\SHNWAZ\.gemini\antigravity\scratch\AirBeats\app\src\main\java\com\darkxvenom\airbeats\ui\player"

# Create AirBeatsTheme.kt first
$themeFile = Join-Path $playerDir "AirBeatsTheme.kt"
$themeContent = "package com.arturo254.opentune.ui.player`n`nimport androidx.compose.ui.graphics.Color`n`nval NeonPurple = Color(0xFF9D00FF)`n"
[System.IO.File]::WriteAllText($themeFile, $themeContent, [System.Text.Encoding]::UTF8)

$filesToCopy = @(
    "AppleMiniPlayer.kt", "ApplePlayer.kt", "ArcProgressBar.kt", "AtmosphericBackground.kt",
    "CloudGlowPlayer.kt", "ColourfullPlayer.kt", "FluidBackground.kt", "FoldPlayer.kt",
    "FrostPlayer.kt", "FuturisticPlayer.kt", "GalaxyPlayer.kt", "GroovePlayer.kt",
    "MinimalPlayer.kt", "NeonMiniPlayer.kt", "NeonPlaybackCore.kt",
    "NewClassicMiniPlayer.kt", "PaperPlayer.kt", "PopsyPlayer.kt", "RadialSongCarousel.kt"
)

foreach ($f in $filesToCopy) {
    $srcPath = Join-Path $srcDir $f
    $destPath = Join-Path $playerDir $f
    if (Test-Path $srcPath) {
        $content = Get-Content $srcPath -Raw
        
        # Package and general imports
        $content = $content -replace "package com\.darkxvenom\.airbeats\.ui\.player", "package com.arturo254.opentune.ui.player"
        $content = $content -replace "com\.darkxvenom\.airbeats", "com.arturo254.opentune"
        $content = $content -replace "import coil\.compose\.AsyncImage", "import coil3.compose.AsyncImage"
        $content = $content -replace "import coil\.compose\.\*", "import coil3.compose.AsyncImage"
        
        # Helper method names
        $content = $content -replace "\.highQualityThumbnail\b", ".highQualityThumbnailUrlOrNull"
        
        # Missing drawables in lyraApp
        $content = $content -replace "R\.drawable\.airplay\b", "R.drawable.volume_up"
        $content = $content -replace "R\.drawable\.ic_bluetooth\b", "R.drawable.volume_up"
        $content = $content -replace "R\.drawable\.menu\b", "R.drawable.more_vert"
        $content = $content -replace "R\.drawable\.lyrics_apple\b", "R.drawable.lyrics"
        
        # Fonts
        $content = $content -replace "fontFamily\s*=\s*SpotifyFontFamily", "fontFamily = androidx.compose.ui.text.font.FontFamily.Default"
        
        # Add Material Icons import if Icons used
        if ($content -match "\bIcons\." -and $content -notmatch "import androidx\.compose\.material\.icons\.Icons") {
            $content = $content -replace "package com\.arturo254\.opentune\.ui\.player", "package com.arturo254.opentune.ui.player`nimport androidx.compose.material.icons.Icons`nimport androidx.compose.material.icons.filled.*"
        }

        [System.IO.File]::WriteAllText($destPath, $content, [System.Text.Encoding]::UTF8)
        Write-Host "Re-processed cleanly: $f"
    }
}
