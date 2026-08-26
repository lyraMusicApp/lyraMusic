<div align="center">
  <img src="assets/icon.png" width="128" height="128" alt="Lyra Music Logo" style="border-radius: 28px; box-shadow: 0 8px 24px rgba(0,0,0,0.2);">

  # 🎵 Lyra Music

  <p align="center">
    <strong>A sleek, modern, and privacy-focused Android music player built with Jetpack Compose & Material You.</strong>
  </p>

  <p align="center">
    <a href="https://github.com/lyraMusicApp/lyraMusic/releases/latest">
      <img src="https://img.shields.io/github/v/release/lyraMusicApp/lyraMusic?style=for-the-badge&logo=github&color=8B5CF6&label=Latest%20Release" alt="Latest Release">
    </a>
    <a href="https://github.com/lyraMusicApp/lyraMusic/releases">
      <img src="https://img.shields.io/github/downloads/lyraMusicApp/lyraMusic/total?style=for-the-badge&logo=android&color=10B981&label=Downloads" alt="Downloads">
    </a>
    <a href="https://crowdin.com/project/lyramusic">
      <img src="https://img.shields.io/badge/Crowdin-Translate-2E8555?style=for-the-badge&logo=crowdin&logoColor=white" alt="Crowdin Translations">
    </a>
    <a href="LICENSE">
      <img src="https://img.shields.io/badge/License-GPL--3.0-0EA5E9?style=for-the-badge&logo=gnu" alt="License: GPL-3.0">
    </a>
  </p>

  <p align="center">
    <img src="https://img.shields.io/badge/Android-8.0%2B%20(API%2026%2B)-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android">
    <img src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin">
    <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose">
    <img src="https://img.shields.io/badge/Design-Material%20You-FF7043?style=flat-square&logo=materialdesign&logoColor=white" alt="Material You">
    <img src="https://img.shields.io/badge/Audio-Media3%20%2F%20ExoPlayer-E11D48?style=flat-square&logo=googleplay&logoColor=white" alt="ExoPlayer">
  </p>

  <p align="center">
    <a href="#-key-features">Features</a> •
    <a href="#-download--installation">Download</a> •
    <a href="#-crowdin-translations">Translations</a> •
    <a href="#-tech-stack--architecture">Tech Stack</a> •
    <a href="#-build-from-source">Build</a> •
    <a href="#-credits--acknowledgments">Credits</a> •
    <a href="#-license">License</a>
  </p>
</div>

---

## ✨ Key Features

<table>
  <tr>
    <td width="33%" valign="top">
      <h3>🎧 Music & Streaming</h3>
      <ul>
        <li><b>Seamless YouTube Music Streaming</b> with high audio quality</li>
        <li><b>Smart Offline Caching & Downloads</b> for offline playback</li>
        <li><b>Background Playback</b> with full Android Media Session & Lockscreen controls</li>
        <li><b>Curated Discover Mixes</b> and personalized suggestions</li>
        <li><b>Synchronized Lyrics</b> via LRCLIB & Kugou</li>
      </ul>
    </td>
    <td width="33%" valign="top">
      <h3>📊 Listening Stats & Social</h3>
      <ul>
        <li><b>Real-Time Listening Stats</b> & listening hours tracking</li>
        <li><b>Artwork Pie-Chart Visualizer</b> for listening breakdown</li>
        <li><b>Last.fm Scrobbling</b> integration</li>
        <li><b>Discord Rich Presence</b> support via Kizzy</li>
        <li><b>ShazamKit Song Recognition</b> support</li>
      </ul>
    </td>
    <td width="33%" valign="top">
      <h3>🎨 Modern UI & Experience</h3>
      <ul>
        <li><b>Material You Dynamic Theming</b> & ambient mesh gradient</li>
        <li><b>Floating Glass Navigation</b> with fluid animations</li>
        <li><b>Spotify Playlist Importer</b> & local library backup/restore</li>
        <li><b>Ad-Free & Privacy Focused</b> with zero analytics trackers</li>
        <li><b>Equalizer & Audio Effects</b> customization</li>
      </ul>
    </td>
  </tr>
</table>

---

## 📥 Download & Installation

Get the latest signed APK directly from GitHub Releases:

<div align="center">

[![Download APK](https://img.shields.io/badge/Download-Latest_APK_Release-8B5CF6?style=for-the-badge&logo=android&logoColor=white)](https://github.com/lyraMusicApp/lyraMusic/releases/latest)

</div>

1. Head to the **[Releases](https://github.com/lyraMusicApp/lyraMusic/releases)** section.
2. Download the latest `app-release.apk` (or architecture-specific APK).
3. Install the APK on your Android device (Android 8.0+ / Oreo or higher).

---

## 🌍 Crowdin Translations

Help bring Lyra Music to more languages worldwide! We manage all community translations on **Crowdin**:

<div align="center">

[![Crowdin](https://img.shields.io/badge/Crowdin-Translate%20Lyra%20Music-2E8555?style=for-the-badge&logo=crowdin&logoColor=white)](https://crowdin.com/project/lyramusic)

</div>

* If you would like to translate or proofread Lyra Music in your native language, join the [Lyra Music Crowdin Project](https://crowdin.com/project/lyramusic).
* To request a **Proofreader** role or ask questions about localization, open a discussion in [GitHub Discussions](https://github.com/lyraMusicApp/lyraMusic/discussions).

---

## 🛠️ Tech Stack & Architecture

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material You (Material 3)
- **Audio Engine:** [AndroidX Media3 (ExoPlayer)](https://developer.android.com/media/media3)
- **Lyrics Providers:** [LRCLIB](https://lrclib.net/) & Kugou
- **Scrobbling & Social:** Last.fm API & [Kizzy](https://github.com/deadlyjack/kizzy-rpc) Discord RPC
- **Song Recognition:** ShazamKit integration
- **Build System:** Gradle (Kotlin DSL) with Android SDK 36, JDK 21

---

## 🏗️ Build From Source

### Prerequisites
- **Android Studio** Ladybug or newer
- **Android SDK:** 36
- **JDK:** 21 (Temurin / OpenJDK)
- **Git**

### Steps

1. **Clone the repository:**
   ```bash
   git clone -b lyra https://github.com/lyraMusicApp/lyraMusic.git
   cd lyraMusic
   ```

2. **Build the release APK:**
   * **Windows (PowerShell / CMD):**
     ```powershell
     .\gradlew.bat :app:assembleRelease
     ```
   * **Linux / macOS:**
     ```bash
     ./gradlew :app:assembleRelease
     ```

3. **Locate your built APK:**
   ```text
   app/build/outputs/apk/release/app-release.apk
   ```

---

## 🤝 Contributing

Contributions, bug reports, and feature suggestions are warmly welcomed!

- 🐛 **Found a bug?** Open an [Issue](https://github.com/lyraMusicApp/lyraMusic/issues).
- 💡 **Have a feature idea or question?** Join our [Discussions](https://github.com/lyraMusicApp/lyraMusic/discussions).
- 🔀 **Want to contribute code?** Fork the repository, create a feature branch, and submit a Pull Request targeting the `lyra` branch.

---

## 💖 Credits & Acknowledgments

Lyra Music is built upon the incredible work of the open-source community:

- **[OpenTune](https://github.com/Arturo254/OpenTune)** by Arturo254 — The foundational open-source music player project.
- **[SimpMusic](https://github.com/maxrave-dev/SimpMusic)** — Inspiration for streaming & player enhancements.
- **[LRCLIB](https://lrclib.net/)** — Open-source synchronized lyrics API.
- All our community translators and contributors on **Crowdin** and **GitHub**!

---

## 📄 License

This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)**.  
See the [LICENSE](LICENSE) file for details.

<div align="center">
  <sub>Crafted with ❤️ by the Lyra Music Team</sub>
</div>