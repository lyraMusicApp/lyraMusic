<div align="center">

<img src="assets/banner.jpg" width="100%" alt="Lyra Music - Classical Oil Painting Banner" style="border-radius: 12px; box-shadow: 0 12px 32px rgba(0,0,0,0.5);">

<br><br>

# Lyra Music

*Harmonizing modern technology with the timeless art of sound.*

[Releases](https://github.com/lyraMusicApp/lyraMusic/releases) &bull; [Crowdin](https://crowdin.com/project/lyramusic) &bull; [Discussions](https://github.com/lyraMusicApp/lyraMusic/discussions) &bull; [License](LICENSE)

</div>

---

### The Art of Playback

Lyra Music is an open-source Android music player crafted with Jetpack Compose and Material You. Named after the celestial constellation of the ancient lyre, it is designed to bring an aesthetic, private, and uncompromised listening experience to Android.

---

### Features

* **Seamless Streaming:** High-fidelity YouTube Music streaming and instant offline caching for your personal library.
* **Synchronized Poetry:** Real-time synchronized lyrics integration powered by LRCLIB and Kugou.
* **Living Canvas:** Dynamic Material You theming that shifts with your device palette and artwork colors.
* **Listening Chronicles:** In-depth listening statistics, artist breakdowns, and visual analytics.
* **Harmonic Bridges:** Last.fm scrobbling, Discord Rich Presence via Kizzy, and ShazamKit song recognition.
* **Pure & Unbound:** Free, open source, ad-free, and built with zero analytics or trackers.

---

### Download & Installation

Obtain the latest signed release APK from the [Releases](https://github.com/lyraMusicApp/lyraMusic/releases) archive.

* **Compatibility:** Android 8.0 (API Level 26) or higher.

---

### Craftsmanship & Architecture

* **Foundation:** Kotlin 2.0
* **Visuals:** Jetpack Compose with Material 3
* **Audio Engine:** AndroidX Media3 (ExoPlayer)
* **Local Archive:** Room Database
* **Network & Data:** Ktor & Retrofit

---

### Build from Source

```bash
# Clone the repository
git clone -b lyra https://github.com/lyraMusicApp/lyraMusic.git
cd lyraMusic

# Compile release artifact
./gradlew :app:assembleRelease
```

The generated APK will be available under `app/build/outputs/apk/release/app-release.apk`.

---

### Global Localization

Lyra Music is translated into languages worldwide through community collaboration on [Crowdin](https://crowdin.com/project/lyramusic). If you would like to contribute translations or request proofreader access, visit our Crowdin project or participate in [Discussions](https://github.com/lyraMusicApp/lyraMusic/discussions).

---

### Lineage & License

* Developed from the foundational work of [OpenTune](https://github.com/Arturo254/OpenTune) by Arturo254.
* Licensed under the [GNU General Public License v3.0](LICENSE).