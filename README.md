<div align="center">
  <img src="assets/icon.png" width="120" height="120" alt="Lyra Music Logo">

  # Lyra Music

  <p align="center">
    <strong>A modern, high-performance, and privacy-focused Android music player built with Jetpack Compose and Material You.</strong>
  </p>

  <p align="center">
    <a href="https://github.com/lyraMusicApp/lyraMusic/releases/latest">
      <img src="https://img.shields.io/github/v/release/lyraMusicApp/lyraMusic?style=flat-square&color=8B5CF6&label=Release" alt="Latest Release">
    </a>
    <a href="https://github.com/lyraMusicApp/lyraMusic/releases">
      <img src="https://img.shields.io/github/downloads/lyraMusicApp/lyraMusic/total?style=flat-square&color=10B981&label=Downloads" alt="Downloads">
    </a>
    <a href="https://crowdin.com/project/lyramusic">
      <img src="https://img.shields.io/badge/Crowdin-Translate-2E8555?style=flat-square" alt="Crowdin Translations">
    </a>
    <a href="LICENSE">
      <img src="https://img.shields.io/badge/License-GPL--3.0-0EA5E9?style=flat-square" alt="License: GPL-3.0">
    </a>
  </p>

  <p align="center">
    <img src="https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026%2B)-3DDC84?style=flat-square" alt="Platform: Android 8.0+">
    <img src="https://img.shields.io/badge/Language-Kotlin%202.0-7F52FF?style=flat-square" alt="Language: Kotlin 2.0">
    <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square" alt="UI: Jetpack Compose">
    <img src="https://img.shields.io/badge/Design-Material%203%20%2F%20Material%20You-FF7043?style=flat-square" alt="Design: Material You">
    <img src="https://img.shields.io/badge/Media-AndroidX%20Media3%20%2F%20ExoPlayer-E11D48?style=flat-square" alt="Media: ExoPlayer">
  </p>

  <p align="center">
    <a href="#overview">Overview</a> •
    <a href="#key-features">Key Features</a> •
    <a href="#download-and-installation">Download</a> •
    <a href="#user-guide">User Guide</a> •
    <a href="#crowdin-and-localization">Localization</a> •
    <a href="#architecture-and-tech-stack">Architecture</a> •
    <a href="#build-from-source">Build Guide</a> •
    <a href="#troubleshooting-and-faq">FAQ</a> •
    <a href="#contributing">Contributing</a> •
    <a href="#license">License</a>
  </p>
</div>

---

## Overview

Lyra Music is an open-source Android music streaming client and local player designed for simplicity, customization, and audio fidelity. Developed entirely in modern Kotlin with Jetpack Compose, Lyra Music delivers fluid animations, dynamic Material You theming, real-time analytics, and a native playback engine powered by AndroidX Media3.

---

## Key Features

### Playback and Audio Engine
* **High-Quality Streaming:** Fast streaming backend powered by YouTube Music integration.
* **Offline Caching and Downloads:** Download songs, albums, and playlists for offline listening.
* **Media3 Playback Service:** Full background playback support, seamless lock screen controls, and Android Auto/headset hardware key handling.
* **Synchronized and Plain Lyrics:** Real-time synchronized lyrics integration via LRCLIB and Kugou providers.
* **Equalizer and Audio Effects:** Built-in audio equalization and customizable output controls.

### Personalization and Statistics
* **Listening Analytics:** Real-time hours tracked, artist rankings, and most-played track breakdowns.
* **Artwork Visualizer:** Dynamic visualizer integrating album art color palettes.
* **Last.fm Scrobbling:** Real-time scrobble synchronization for Last.fm accounts.
* **Discord Presence (RPC):** Rich presence status integration using Kizzy RPC.
* **Music Recognition:** Instant audio identification powered by ShazamKit.

### Modern Interface and Usability
* **Material You Dynamic Theming:** System wallpaper color extraction (Android 12+) and customizable color accents.
* **Floating Navigation Interface:** Clean, gesture-driven bottom bar navigation with smooth transitions.
* **Playlist Management:** Import external playlists from Spotify and manage custom local playlists.
* **Privacy Focused:** Zero third-party trackers, telemetry, or invasive advertisements.

---

## Download and Installation

### System Requirements
* **Operating System:** Android 8.0 (API Level 26) or higher.
* **Storage Space:** Minimum 50 MB free internal storage.
* **Network:** Stable internet connection required for streaming and lyrics fetching.

### Installation Steps
1. Navigate to the [Releases](https://github.com/lyraMusicApp/lyraMusic/releases) page.
2. Select the latest version and download `app-release.apk` (or the architecture-specific APK for your device).
3. Open the downloaded APK on your Android device.
4. If prompted, grant permission to install applications from your browser or file manager.
5. Launch Lyra Music from your app drawer.

---

## User Guide

### 1. Discover and Search
* Use the Search tab to query tracks, artists, albums, or public playlists.
* Tap any track to begin immediate playback or add it to the active queue.

### 2. Offline Mode
* Tap the three dots menu next to any song, album, or playlist and select **Download**.
* Downloaded tracks can be accessed under **Library > Downloads** without an active internet connection.

### 3. Importing Spotify Playlists
* Navigate to **Library > Import Playlist**.
* Paste your public Spotify playlist link and confirm the import. Lyra Music will match and add the tracks to your local library.

### 4. Enabling Synchronized Lyrics
* Open the Player view and swipe up or tap the lyrics icon.
* If a song does not automatically display lyrics, tap the lyrics provider toggle to switch between LRCLIB and Kugou.

### 5. Enabling Discord Rich Presence
* Open **Settings > Social Integrations > Discord RPC**.
* Enable the toggle to broadcast current track, artist, and playback progress to your Discord profile.

---

## Crowdin and Localization

Lyra Music is localized by the global open-source community using Crowdin.

* **Crowdin Project:** [https://crowdin.com/project/lyramusic](https://crowdin.com/project/lyramusic)
* **Configuration:** Source strings are stored in `/app/src/main/res/values/strings.xml` and mapped via `crowdin.yml`.

### How to Contribute Translations:
1. Create a free account on [Crowdin](https://crowdin.com).
2. Visit the [Lyra Music Project](https://crowdin.com/project/lyramusic) on Crowdin.
3. Select your language and begin translating unlocalized strings or suggesting improvements.
4. If you wish to become a language **Proofreader** to approve and validate translations, open a thread on [GitHub Discussions](https://github.com/lyraMusicApp/lyraMusic/discussions).

---

## Architecture and Tech Stack

Lyra Music is built using clean Android development patterns and modern libraries:

* **UI Layer:** Jetpack Compose, Material 3, Accompanist, Coil (Image Loading).
* **Architecture:** MVVM (Model-View-ViewModel) with Kotlin Coroutines and StateFlow.
* **Audio Layer:** AndroidX Media3 ExoPlayer, MediaSessionService.
* **Local Persistence:** Room Database for caching metadata, playlists, and history.
* **Network and Serialization:** Ktor Client, Retrofit, Kotlinx Serialization.
* **Integrations:**
  * `innertube`: Internal YouTube Music client.
  * `lrclib`: Synchronized lyrics provider.
  * `kugou`: Alternative lyric database.
  * `kizzy`: Discord RPC client.
  * `lastfm`: Last.fm scrobble API.
  * `shazamkit`: Music recognition bridge.

---

## Build from Source

### Prerequisites
Before building the project, ensure you have the following software installed:
* **Android Studio:** Ladybug (2024.2.1) or newer.
* **Java Development Kit (JDK):** Version 21 (OpenJDK / Eclipse Temurin).
* **Android SDK:** Version 36 with Build Tools 36.0.0.
* **Git:** Version 2.30+ installed and added to PATH.

### Build Steps

1. **Clone the Repository:**
   ```bash
   git clone -b lyra https://github.com/lyraMusicApp/lyraMusic.git
   cd lyraMusic
   ```

2. **Open in Android Studio:**
   * Launch Android Studio.
   * Select **Open** and choose the `lyraMusic` directory.
   * Allow Gradle to sync dependencies and project configuration.

3. **Compile via Command Line:**
   * **Debug Build:**
     * Windows (PowerShell / Command Prompt):
       ```powershell
       .\gradlew.bat :app:assembleDebug
       ```
     * Linux / macOS:
       ```bash
       ./gradlew :app:assembleDebug
       ```
   * **Release Build:**
     * Windows (PowerShell / Command Prompt):
       ```powershell
       .\gradlew.bat :app:assembleRelease
       ```
     * Linux / macOS:
       ```bash
       ./gradlew :app:assembleRelease
       ```

4. **Locate Generated APK Artifacts:**
   * Debug: `app/build/outputs/apk/debug/app-debug.apk`
   * Release: `app/build/outputs/apk/release/app-release.apk`

---

## Troubleshooting and FAQ

#### 1. Why are some songs not playing?
Verify that you have an active network connection. If using private DNS or VPN services, ensure YouTube domains are not blocked.

#### 2. The app stops playing in the background.
Android battery optimization may kill background services. To resolve:
* Go to Android Settings > Apps > Lyra Music.
* Select Battery > Set to **Unrestricted**.

#### 3. How do I report a bug or request a feature?
Open a detailed report under the [Issues](https://github.com/lyraMusicApp/lyraMusic/issues) tab. Please include your Android version, device model, and reproduction steps.

---

## Contributing

We welcome contributions from developers, designers, and translators.

### Development Workflow
1. Fork the repository on GitHub.
2. Create a descriptive topic branch (`git checkout -b feature/new-audio-effect`).
3. Implement your changes following Kotlin coding conventions and project formatting standards.
4. Test your changes thoroughly on a physical device or emulator.
5. Push your branch to your fork (`git push origin feature/new-audio-effect`).
6. Submit a Pull Request targeting the `lyra` branch.

---

## Credits and Acknowledgments

Lyra Music incorporates and builds upon work from the open-source community:

* **[OpenTune](https://github.com/Arturo254/OpenTune)** by Arturo254 (Foundational open-source music player).
* **[SimpMusic](https://github.com/maxrave-dev/SimpMusic)** by maxrave-dev (Streaming architecture and player enhancements).
* **[LRCLIB](https://lrclib.net/)** (Synchronized lyrics provider).
* All contributors, translators, and testers supporting the project.

---

## License

This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)**.  
See the [LICENSE](LICENSE) file for the complete terms and conditions.