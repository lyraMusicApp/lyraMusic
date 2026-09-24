<div align="center">

  <img src="assets/lyra_logo.png" width="130" height="130" alt="Lyra Music Logo" style="border-radius: 28px;" />

  # Lyra Music

  **A modern, elegant, and privacy-focused Android music player built with Jetpack Compose.**

  <p align="center">
    <a href="https://github.com/lyraMusicApp/lyraMusic/releases"><img src="https://img.shields.io/github/v/release/lyraMusicApp/lyraMusic?style=for-the-badge&color=8A2BE2&logo=github&logoColor=white" alt="Latest Release" /></a>
    <a href="https://github.com/lyraMusicApp/lyraMusic/actions"><img src="https://img.shields.io/github/actions/workflow/status/lyraMusicApp/lyraMusic/releaseBuild.yml?branch=lyra&style=for-the-badge&logo=githubactions&logoColor=white" alt="Build Status" /></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-GPLv3-00B4D8?style=for-the-badge&logo=gnu" alt="License: GPLv3" /></a>
    <a href="https://developer.android.com"><img src="https://img.shields.io/badge/Platform-Android%208.0%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform: Android" /></a>
    <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Language: Kotlin" /></a>
  </p>

  <p align="center">
    <a href="#-overview">Overview</a> &bull;
    <a href="#-features">Features</a> &bull;
    <a href="#-download">Download</a> &bull;
    <a href="#-tech-stack">Tech Stack</a> &bull;
    <a href="#-building-from-source">Build from Source</a> &bull;
    <a href="#-dmca--legal-notice">Legal & Security</a> &bull;
    <a href="#-credits--license">Credits</a>
  </p>

</div>

---

## 📖 Overview

**Lyra Music** is a high-performance, ad-free Android music streaming and local playback client powered by **Jetpack Compose** and **Material You (Material 3)**. Built from the ground up with aesthetics, speed, and privacy in mind, Lyra provides a seamless listening experience with rich audio playback, multi-provider synchronized lyrics, customizable player designs, dynamic ambient glow effects, and complete user freedom.

> [!NOTE]
> Lyra Music is 100% free and open source. No ads, no tracking, and no proprietary account lock-ins.

---

## ✨ Features

### 🎧 Seamless Audio & Streaming
* **YouTube Music Engine**: High-fidelity audio streaming, instant playback, search, and queueing without ads.
* **Offline Caching & Downloads**: Cache songs automatically for offline listening or download tracks directly to local storage.
* **Smart Audio Focus & Normalization**: Seamless handling of audio interruptions, volume normalization, and gapless playback via AndroidX Media3 (ExoPlayer).

### 🎤 Synchronized Multi-Source Lyrics
* **Line-by-Line & Word-by-Word Sync**: Ultra-responsive synchronized lyric display with smooth auto-scroll.
* **Multi-Provider Engine**: Aggregates lyrics from **Paxsenix** (Apple Music, Spotify, Musixmatch, NetEase, YouTube), **BetterLyrics**, **LRCLIB**, **KuGou**, and **Megalobiz**.
* **Plain Lyrics Fallback**: Never miss lyrics, even for rare indie and underground songs.

### 🎨 Material You & Next-Gen Interface
* **Material 3 Dynamic Theming**: Fluid Monet color engine adapting dynamically to song album art and your wallpaper.
* **10 Interchangeable Player Styles (V1–V10)**: Customize your full-screen player layout to fit your taste.
* **Vivi Mini-Player & Ambient Glow**: Sleek floating mini-player with customizable background effects (`Theme`, `Gradient`, `Glow`).
* **Floating Liquid-Glass Navigation**: Intuitive tab navigation with active accent indicators.

### 📊 Real-Time Analytics & Leaderboard
* **Live Listening Tracker**: Real-time count of total listening hours and songs played as music streams.
* **Visualizer Pie Chart**: Interactive sliced-artwork visualizer categorizing your most played artists and genres.
* **Community Leaderboard**: Compare listening milestones and discover trending tracks with other Lyra users.

### 🔌 Third-Party Integrations
* **Discord Rich Presence**: Live playback status on your Discord profile via Kizzy RPC integration.
* **Last.fm & Libre.fm Scrobbling**: Real-time scrobble sync to your music profiles.
* **Song Recognition**: Identify currently playing songs in the background.

### 🔒 Privacy-First Architecture
* Zero telemetry, zero behavioral tracking, and no background analytics collection.
* Direct device-to-endpoint network communication without intermediate tracking proxies.

---

## 📥 Download

Download the latest signed release APK directly from GitHub Releases:

<div align="center">

[![Download Lyra Music](https://img.shields.io/badge/Download-Lyra%20Music%20v3.0.11%20APK-10B981?style=for-the-badge&logo=android&logoColor=white)](https://github.com/lyraMusicApp/lyraMusic/releases/download/v3.0.11/LyraMusic-foss-universal-release.apk)

</div>

| Specification | Requirement |
| :--- | :--- |
| **Latest Version** | [v3.0.11](https://github.com/lyraMusicApp/lyraMusic/releases/tag/v3.0.11) |
| **Minimum OS** | Android 8.0 (Oreo / API 26) |
| **Recommended OS** | Android 12+ (API 31+) for Dynamic Monet Theming |
| **Architecture** | `Universal` (FOSS release) |
| **Package Asset** | [`LyraMusic-foss-universal-release.apk`](https://github.com/lyraMusicApp/lyraMusic/releases/download/v3.0.11/LyraMusic-foss-universal-release.apk) |

Verify the release provenance with GitHub CLI:

```bash
gh attestation verify LyraMusic-foss-universal-release.apk -R lyraMusicApp/lyraMusic
```

---

## 🛠️ Tech Stack

<p align="center">
  <img src="https://skillicons.dev/icons?i=kotlin,androidstudio,gradle,git,github&theme=dark" alt="Core Technologies" />
</p>

* **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) & [Material Design 3](https://m3.material.io/)
* **Media & Audio Engine**: [AndroidX Media3 (ExoPlayer)](https://developer.android.com/guide/topics/media/media3)
* **Local Storage & Database**: [Room Database](https://developer.android.com/training/data-storage/room) & DataStore Preferences
* **Networking**: [Ktor Client](https://ktor.io/) & [OkHttp](https://square.github.io/okhttp/)
* **Image Loading**: [Coil 3](https://coil-kt.github.io/coil/)
* **Concurrency**: Kotlin Coroutines & Asynchronous Flow

---

## 🚀 Building from Source

### Prerequisites
* **JDK 17** or **JDK 21**
* **Android SDK** (API 34+ / Build Tools 34.0.0+)
* **Git** with submodule support

### Step-by-Step Build

1. **Clone the repository with submodules:**
   ```bash
   git clone --recursive -b lyra https://github.com/lyraMusicApp/lyraMusic.git
   cd lyraMusic
   ```

2. **Initialize submodules (if cloned without `--recursive`):**
   ```bash
   git submodule update --init --recursive
   ```

3. **Build Debug APK:**
   ```bash
   ./gradlew :app:assembleDebug
   ```

4. **Build Release APK:**
   ```bash
   ./gradlew :app:assembleRelease
   ```

---

## 🛡️ DMCA & Legal Notice

Lyra Music is an open-source client application that connects to publicly available third-party endpoints.
* Lyra Music does not host, upload, or transmit copyrighted media files.
* Lyra Music does not circumvent digital rights management (DRM) mechanisms.
* All audio metadata, streams, and lyrics belong to their respective copyright holders.

For takedown requests, questions, or designated agent details, please refer to our [DMCA Policy](DMCA.md) and [Security Policy](SECURITY.md).

---

## 🤝 Contributing

Contributions, bug reports, and feature suggestions are always welcome!
1. Fork the project.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📄 Credits & License

* **Author & Maintainer**: [Shnwaz](https://github.com/shnwazdeveloper)
* **License**: Released under the [GNU General Public License v3.0](LICENSE).
* **Socials**: Connect with the developer on [GitHub](https://github.com/shnwazdeveloper).

<div align="center">

Made with ❤️ by [Shnwaz](https://github.com/shnwazdeveloper)

</div>
