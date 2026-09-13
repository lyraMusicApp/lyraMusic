# Lyra Music v3.0.10

## Highlights & What's New

### Security & Compliance
- **Official Security Advisory**: Published [GHSA-f779-qgcj-q93q](https://github.com/lyraMusicApp/lyraMusic/security/advisories/GHSA-f779-qgcj-q93q).
- **Hardened Architecture**: Isolated BotGuard / PoToken token generation in WebView sandbox, enforced strict TLS 1.3 encryption on all external network requests, validated lyrics parsers against malformed payloads, and added comprehensive DMCA safe-harbor compliance.

### 100% Transparent Navigation Bar
- Complete edge-to-edge transparent navigation bar across all screens (Home, Library, Explore, Settings).
- Disabled Android OS contrast scrim enforcement, eliminating dark/black overlays behind the floating toolbar.
- Floating toolbar renders cleanly with zero elevation container backgrounds or shadows.

### Classic Material 3 About Screen
- Restored classic Material 3 card layout for the About screen with app icon shimmer, version badges, developer profile card, and quick social links (GitHub, Telegram, Instagram).

### Performance & Security
- Safe signed production release APK signed with official release keystore (APK Signature Scheme v1, v2, v3, and v4).
- Production release configuration with `isDebuggable = false`.
- Migrated official application package name to `com.shnwaz.lyramusic`.
- Cleaned up background services and removed unused lock screen activity.

## App Details

- Package: `com.shnwaz.lyramusic`
- Version: `3.0.10`
- Version Code: `144`
- APK: `LyraMusic.apk` / `LyraMusic-v3.0.10.apk`

## Verification

- Built with `:app:assembleRelease`.
- Signed with release keystore using v1, v2, v3, and v4 signature schemes.
- Production release build (`isDebuggable = false`).
