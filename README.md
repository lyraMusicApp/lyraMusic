<div align="center">

<img src="assets/lyra_logo.png" width="108" height="108" alt="Lyra Music Logo">

# Lyra Music

A clean, modern, and privacy-focused Android music player built with Jetpack Compose.

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android">
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="Compose">
  <img src="https://img.shields.io/badge/Audio-ExoPlayer-E11D48?style=flat-square&logo=googleplay&logoColor=white" alt="ExoPlayer">
  <img src="https://img.shields.io/badge/License-GPL--3.0-0EA5E9?style=flat-square&logo=gnu" alt="GPL-3.0">
  <a href="SECURITY.md"><img src="https://img.shields.io/badge/Security-Policy-brightgreen?style=flat-square&logo=github" alt="Security Policy"></a>
  <a href="DMCA.md"><img src="https://img.shields.io/badge/DMCA-Protected-007ACC?style=flat-square&logo=shield" alt="DMCA Protected"></a>
</p>

[Releases](https://github.com/lyraMusicApp/lyraMusic/releases) &bull; [Discussions](https://github.com/lyraMusicApp/lyraMusic/discussions) &bull; [Security](SECURITY.md) &bull; [DMCA Policy](DMCA.md) &bull; [License](LICENSE)

</div>

---

### Features

* **Streaming & Offline:** YouTube Music streaming, high-quality audio playback, and local offline caching.
* **Lyrics Support:** Synchronized and plain lyrics powered by Paxsenix, BetterLyrics, LRCLIB, KuGou, and Megalobiz.
* **Modern UI:** Material You dynamic theming, multiple player designs (V1–V10), dynamic mini player backgrounds, and fluid gesture controls.
* **Listening Stats:** Real-time playback analytics, listening visualizer, and community leaderboard.
* **Integrations:** Last.fm scrobbling, Discord Rich Presence (via Kizzy RPC), and ShazamKit song recognition.
* **Privacy-First:** Free, open source, ad-free, with zero telemetry or user tracking.

---

### Download

Download the latest APK release directly from the [Releases](https://github.com/lyraMusicApp/lyraMusic/releases) page.

* **Requirement:** Android 8.0 (API 26) or higher.

---

### Tech Stack

<p align="left">
  <img src="https://skillicons.dev/icons?i=kotlin,androidstudio,gradle,github,git&theme=dark" alt="Tech Stack Icons" />
</p>

* **Language:** Kotlin
* **UI:** Jetpack Compose & Material 3
* **Audio Engine:** AndroidX Media3 (ExoPlayer)
* **Database:** Room
* **Networking:** Ktor & Retrofit

---

### Build from Source

```bash
# Clone the repository
git clone -b lyra https://github.com/lyraMusicApp/lyraMusic.git
cd lyraMusic

# Build debug APK
./gradlew :app:assembleDebug

# Build release APK
./gradlew :app:assembleRelease
```

---

### DMCA & Legal Notice

Lyra Music is an open-source client application that connects to publicly available web endpoints. Lyra Music does not host, store, or transmit copyrighted media files, nor does it circumvent DRM technologies. For full terms, takedown procedures, and designated agent information, please see [DMCA.md](DMCA.md) and [SECURITY.md](SECURITY.md).

---

### Credits & License

* Based on [OpenTune](https://github.com/Arturo254/OpenTune) by Arturo254.
* Enhancements inspired by [ArchiveTune](https://github.com/rukamori/ArchiveTune).
* Licensed under the [GNU General Public License v3.0](LICENSE).
