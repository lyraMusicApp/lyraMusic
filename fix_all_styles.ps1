$playerDir = "C:\Users\SHNWAZ\.gemini\antigravity\scratch\lyraApp\app\src\main\kotlin\com\arturo254\opentune\ui\player"

# Remove non-standalone player files that use backdrop/custom hooks
$remove = @("CloudGlowPlayer.kt", "ColourfullPlayer.kt", "NeonMiniPlayer.kt", "NeonPlaybackCore.kt")
foreach ($r in $remove) {
    $p = Join-Path $playerDir $r
    if (Test-Path $p) { Remove-Item $p -Force }
}

# Fix ApplePlayer.kt
$appleFile = Join-Path $playerDir "ApplePlayer.kt"
if (Test-Path $appleFile) {
    $c = Get-Content $appleFile -Raw
    $c = $c -replace "SliderStyle\.DEFAULT", "SliderStyle.Standard"
    $c = $c -replace "SliderStyle\.SQUIGGLY", "SliderStyle.Wavy"
    $c = $c -replace "SliderStyle\.SLIM", "SliderStyle.Simple"
    $c = $c -replace "import coil\.compose\.\*", "import coil3.compose.AsyncImage"
    [System.IO.File]::WriteAllText($appleFile, $c, [System.Text.Encoding]::UTF8)
}

# Fix FoldPlayer.kt
$foldFile = Join-Path $playerDir "FoldPlayer.kt"
if (Test-Path $foldFile) {
    $c = Get-Content $foldFile -Raw
    $c = $c -replace "import androidx\.compose\.material\.icons\.rounded\.\*", "import androidx.compose.material.icons.Icons`nimport androidx.compose.material.icons.rounded.*"
    [System.IO.File]::WriteAllText($foldFile, $c, [System.Text.Encoding]::UTF8)
}

Write-Host "Player files cleaned and updated."
