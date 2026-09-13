# Security Policy

The Lyra Music team takes the security, legal compliance, and privacy of our users very seriously. We appreciate responsible disclosure of security vulnerabilities and maintain strict adherence to intellectual property laws and privacy standards.

---

## Supported Versions

Only the latest active release branch and version lines receive active security updates, bug fixes, and dependency patches.

| Version | Status | Security Updates | Minimum Android Version |
| :--- | :--- | :--- | :--- |
| **v3.0.x** (Current) | **Active / Supported** | :white_check_mark: Supported | Android 8.0 (API 26+) |
| **v2.x** | **End of Life (EOL)** | :x: Not Supported | Android 7.0 (API 24+) |
| **< v2.0** | **Deprecated** | :x: Not Supported | Deprecated |

We strongly recommend all users stay on the latest release to ensure they receive the latest security improvements, stability patches, and API updates.

---

## Published Security Advisories

| Advisory ID | Title | Severity | Affected Versions | Patched Version |
| :--- | :--- | :--- | :--- | :--- |
| [GHSA-f779-qgcj-q93q](https://github.com/lyraMusicApp/lyraMusic/security/advisories/GHSA-f779-qgcj-q93q) | Security & Privacy Hardening Update | Low | < 3.0.10 | 3.0.10 |


---

## Reporting a Vulnerability

### Private Disclosure Channels
* **GitHub Security Advisories:** Report privately via [GitHub Security Advisories](https://github.com/lyraMusicApp/lyraMusic/security/advisories/new).
* **Maintainer Direct Email:** Contact maintainers directly at [shnwazdeveloper@gmail.com](mailto:shnwazdeveloper@gmail.com) with the subject `[SECURITY VULNERABILITY] Lyra Music`.

### What to Include in Your Report
* **Reproduction Steps:** Step-by-step instructions to reproduce the vulnerability reliably.
* **PoC Payload:** Proof of Concept (PoC) scripts, sample data, or payloads demonstrating the exploit.
* **Affected Classes/Endpoints:** The specific classes, source files, methods, or network endpoints involved.
* **Environment Information:** Lyra Music version, Android OS version / API level, device model, and build variant.

### Response Timeline
* **24–48h acknowledgment:** Prompt confirmation of report receipt.
* **3–5 day validation:** Technical triage, reproduction, and CVSS severity assessment.
* **Coordinated patch release:** Fix development, regression testing, and security advisory publication.
* **Public researcher credit:** Recognition in the release notes and GitHub Security Advisory (unless anonymity is requested).

---

## Security & Privacy Architecture

Lyra Music is engineered around the principle of user sovereignty, local execution, and privacy preservation:

* **Zero Telemetry & Tracking:** Lyra Music contains **no tracking libraries**, no third-party analytics SDKs (no Firebase Analytics, no Google Analytics, no Facebook SDK), and no telemetry servers. Your playback data and browsing history never leave your device.
* **Local Sandboxed Storage:** User playlists, favorites, listening history, and preferences are stored exclusively on your device using Android's private app sandbox (`Room` SQLite database and AndroidX `DataStore`). No remote database is maintained.
* **Credential Isolation:** Optional authentication tokens (such as YouTube Music cookies or Last.fm session tokens) are stored locally in private application storage. They are never sent to any third-party intermediary or central relay server.
* **Strict Transport Security (TLS/HTTPS):** All network communication with external services (YouTube InnerTube, Paxsenix, LRCLIB, KuGou, Last.fm) is encrypted end-to-end using modern TLS 1.3 / TLS 1.2 protocols. Cleartext (HTTP) traffic is strictly prohibited.
* **Integrity & Token Generation:** BotGuard and PoToken routines execute locally via isolated WebView sandboxes without exporting sensitive device state.
* **Automated Dependency Auditing:** Repository dependencies are monitored continuously via GitHub Dependabot to detect and patch CVEs in third-party libraries promptly.

---

## DMCA & Legal Policy

### 1. General Disclaimer & Non-Affiliation
* **Independent Open-Source Project:** Lyra Music is an independent, free, and open-source Android application licensed under the [GNU General Public License v3.0 (GPL-3.0)](LICENSE).
* **Non-Affiliation Notice:** Lyra Music is **NOT** affiliated with, endorsed by, sponsored by, or officially associated with Google LLC, YouTube, Alphabet Inc., Spotify AB, or any of their subsidiaries or affiliates.
* **Trademarks:** YouTube, YouTube Music, Google Play, Android, and all related marks and logos are trademarks of Google LLC. Spotify is a registered trademark of Spotify AB. All other trademarks, trade names, and copyrights cited in this repository belong to their respective owners.

### 2. Media Hosting & Content Policy
* **No Media Hosted or Distributed:** Lyra Music does **NOT** host, store, cache on remote servers, stream from private servers, or broadcast any audio, video, or copyright-protected media files.
* **Client-Side Architecture:** The application acts exclusively as a local media player frontend and protocol client. When a user plays a track, the application initiates an outbound HTTP request directly from the user's Android device to publicly accessible content delivery endpoints operated by the respective service provider.
* **Repository Content:** This GitHub repository contains solely original source code, build scripts, vector assets, and documentation. No copyrighted audio, video recordings, or unauthorized media files are present in this repository.

### 3. Non-Circumvention & DRM Compliance (17 U.S.C. § 1201)
* **No Circumvention of DRM:** Lyra Music does **NOT** bypass, decrypt, impair, or circumvent any technological protection measures, DRM systems (such as Widevine, FairPlay, or PlayReady), or digital locks.
* **Interoperability & Fair Use:** The application requests only publicly exposed, unencrypted media streams and API endpoints provided by the service for standard web and mobile clients, in strict accordance with the interoperability exception under 17 U.S.C. § 1201(f) and fair use principles.

### 4. DMCA Notice and Takedown Procedure (17 U.S.C. § 512(c))
Lyra Music respects the intellectual property rights of creators and copyright holders. In accordance with Title II of the **Digital Millennium Copyright Act (17 U.S.C. § 512)**, we will respond expeditiously to valid notices of claimed copyright infringement.

If you are a copyright owner or an authorized representative, you may submit a formal Notice of Claimed Infringement containing the following mandatory information:
1. **Identification of the Work:** A description of the copyrighted work claimed to have been infringed.
2. **Identification of Material:** Specific URLs, commit hashes, or file paths within this repository where the alleged infringing material is located.
3. **Contact Details:** Your full legal name, title, organization, physical address, telephone number, and official email address.
4. **Good Faith Statement:** A statement affirming that: *"I have a good faith belief that use of the copyrighted materials described above on the allegedly infringing web pages is not authorized by the copyright owner, its agent, or the law."*
5. **Accuracy & Perjury Statement:** A statement affirming that: *"The information in this notification is accurate and, under penalty of perjury, I am the owner, or an agent authorized to act on behalf of the owner, of an exclusive right that is allegedly infringed."*
6. **Signature:** A physical or electronic signature of the copyright owner or authorized representative.

### 5. Designated Agent for Legal & DMCA Notices
Please send all formal DMCA and legal correspondence to our designated contact:

* **Designated Email:** 📧 **`shnwazdeveloper@gmail.com`**
* **Subject Line:** `[DMCA NOTICE] Lyra Music - Claim of Copyright Infringement`
* **Confidential Security Advisory:** You may also file a private [Security Advisory](https://github.com/lyraMusicApp/lyraMusic/security/advisories/new) tagged as `Legal / DMCA`.

**Takedown Turnaround Time:** Upon receipt of a legally compliant notice meeting all statutory requirements, we will investigate and take appropriate action (including removing or disabling access to the disputed material) within **48 to 72 hours**.

### 6. Counter-Notification Procedure (17 U.S.C. § 512(g))
If material you contributed was removed or disabled as a result of a mistake or misidentification, you may submit a written counter-notification pursuant to Section 512(g)(3) of the DMCA containing:
1. Your physical or electronic signature.
2. Identification of the material that was removed or to which access was disabled, and the location where it appeared.
3. A statement under penalty of perjury that you have a good faith belief that the material was removed or disabled as a result of mistake or misidentification.
4. Your name, address, telephone number, and a statement consenting to the jurisdiction of the federal district court.

### 7. Limitation of Liability
Pursuant to Sections 15 and 16 of the GNU General Public License v3.0, Lyra Music is provided *"as-is"*, without warranty of any kind, express or implied. The developers and contributors disclaim all liability for any direct, indirect, incidental, or consequential damages resulting from the use or inability to use this software.
