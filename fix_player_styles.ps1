$playerDir = "C:\Users\SHNWAZ\.gemini\antigravity\scratch\lyraApp\app\src\main\kotlin\com\arturo254\opentune\ui\player"

$files = Get-ChildItem $playerDir -Filter *.kt

foreach ($f in $files) {
    $path = $f.FullName
    $content = Get-Content $path -Raw

    # 1. Coil imports
    $content = $content -replace "import coil\.compose\.AsyncImage", "import coil3.compose.AsyncImage"
    $content = $content -replace "import coil\.compose\.\*", "import coil3.compose.AsyncImage"

    # 2. Thumbnail helper replacement
    $content = $content -replace "\.highQualityThumbnail\b", ".highQualityThumbnailUrlOrNull"

    # 3. Missing Drawables fallback
    $content = $content -replace "R\.drawable\.airplay\b", "R.drawable.volume_up"
    $content = $content -replace "R\.drawable\.ic_bluetooth\b", "R.drawable.volume_up"

    # 4. SpotifyFontFamily fallback
    $content = $content -replace "fontFamily\s*=\s*SpotifyFontFamily", "fontFamily = androidx.compose.ui.text.font.FontFamily.Default"

    # 5. NeonPurple color definition if missing
    if ($content -match "NeonPurple" -and $content -notmatch "val NeonPurple") {
        $content = "import androidx.compose.ui.graphics.Color`nval NeonPurple = Color(0xFF9D00FF)`n" + $content
    }

    [System.IO.File]::WriteAllText($path, $content, [System.Text.Encoding]::UTF8)
}
Write-Host "Updated imports and references in player style files."
