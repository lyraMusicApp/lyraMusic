<div align="center">

<img src="assets/icon.png" width="96" height="96" alt="Lyra Music Logo">

# Lyra Music

A clean, modern, and privacy-focused Android music player built with Jetpack Compose.

[Releases](https://github.com/lyraMusicApp/lyraMusic/releases) &bull; [Crowdin](https://crowdin.com/project/lyramusic) &bull; [Discussions](https://github.com/lyraMusicApp/lyraMusic/discussions) &bull; [License](LICENSE)

</div>

---

### Features

* **Streaming & Offline:** YouTube Music streaming, high-quality audio playback, and local offline caching.
* **Lyrics Support:** Synchronized and plain lyrics powered by LRCLIB and Kugou.
* **Modern UI:** Material You dynamic theming with fluid animations and gesture controls.
* **Listening Stats:** Real-time playback analytics and listening visualizer.
* **Integrations:** Last.fm scrobbling, Discord Rich Presence (via Kizzy RPC), and ShazamKit song recognition.
* **Privacy-First:** Free, open source, ad-free, and no tracking.

---

### Download

Download the latest APK release directly from the [Releases](https://github.com/lyraMusicApp/lyraMusic/releases) page.

* **Requirement:** Android 8.0 (API 26) or higher.

---

### Tech Stack

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

# Build release APK
./gradlew :app:assembleRelease
```

The APK will be located at `app/build/outputs/apk/release/app-release.apk`.

---

### Translations

Community translations are hosted on [Crowdin](https://crowdin.com/project/lyramusic). If you would like to contribute or request proofreader access, visit our Crowdin project or open a thread in [Discussions](https://github.com/lyraMusicApp/lyraMusic/discussions).

---

### Credits & License

* Based on [OpenTune](https://github.com/Arturo254/OpenTune) by Arturo254.
* Licensed under the [GNU General Public License v3.0](LICENSE).