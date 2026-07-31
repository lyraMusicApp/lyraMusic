$srcDir = "C:\Users\SHNWAZ\.gemini\antigravity\scratch\AirBeats\app\src\main\java\com\darkxvenom\airbeats\ui\player"
$destDir = "C:\Users\SHNWAZ\.gemini\antigravity\scratch\lyraApp\app\src\main\kotlin\com\arturo254\opentune\ui\player"

$filesToCopy = @(
    "AppleMiniPlayer.kt", "ApplePlayer.kt", "ArcProgressBar.kt", "AtmosphericBackground.kt",
    "CloudGlowPlayer.kt", "ColourfullPlayer.kt", "FluidBackground.kt", "FoldPlayer.kt",
    "FrostPlayer.kt", "FuturisticPlayer.kt", "GalaxyPlayer.kt", "GroovePlayer.kt",
    "IosStyledPlayer.kt", "MinimalPlayer.kt", "NeonMiniPlayer.kt", "NeonPlaybackCore.kt",
    "NewClassicMiniPlayer.kt", "PaperPlayer.kt", "PopsyPlayer.kt", "RadialSongCarousel.kt"
)

foreach ($f in $filesToCopy) {
    $srcPath = Join-Path $srcDir $f
    $destPath = Join-Path $destDir $f
    if (Test-Path $srcPath) {
        $content = Get-Content $srcPath -Raw
        $content = $content -replace "com\.darkxvenom\.airbeats", "com.arturo254.opentune"
        [System.IO.File]::WriteAllText($destPath, $content, [System.Text.Encoding]::UTF8)
        Write-Host "Copied: $f"
    }
}
