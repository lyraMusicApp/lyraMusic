# Security Policy for Lyra Music (`lyraMusicApp`)

At **Lyra Music**, security, user privacy, and data protection are core principles of our open-source application and GitHub organization ([github.com/lyraMusicApp](https://github.com/lyraMusicApp)). We are committed to maintaining a safe, transparent, and trustworthy ecosystem for all users and contributors.

---

## 🛡️ Supported Versions

We actively provide security patches and updates for the following versions of Lyra Music:

| Version | Supported | Security Maintenance |
| :--- | :---: | :--- |
| `v3.x` (Latest Release) | ✅ **Yes** | Full Security & Vulnerability Updates |
| `v2.x` | ⚠️ **Limited** | Critical Security Patches Only |
| `< v2.0` | ❌ **No** | End-of-Life (Please Upgrade) |

---

## 🔒 Reporting a Vulnerability / Safety Concern

We take security reports seriously. If you discover a security vulnerability, privacy defect, or safety issue within Lyra Music, please **do not open a public GitHub issue**.

### Preferred Reporting Method

1. **GitHub Private Vulnerability Reporting** *(Recommended)*:
   Navigate to the [Security Tab on GitHub](https://github.com/lyraMusicApp/lyra-music/security/advisories/new) and select **"Report a vulnerability"**.

2. **Direct Contact**:
   - Telegram Maintainer: [@sexyafraid](https://t.me/sexyafraid)
   - Email / Security Lead: Open a private vulnerability report or contact via GitHub organization maintainers.

### What to Include in Your Security Report

To help us evaluate and address the issue efficiently, please include:
- **Type of Issue**: (e.g., Data leakage, dependency vulnerability, insecure storage, API key exposure, malicious payload risk)
- **Affected Component/Version**: Exact release tag or commit hash.
- **Steps to Reproduce**: Proof-of-concept steps, stack traces, or sample commands.
- **Impact Assessment**: How an attacker could exploit the issue and potential risk to users.

---

## ⏱️ Response & Disclosure SLA

We adhere to standard Coordinated Vulnerability Disclosure practices:

- **Initial Acknowledgment**: Within **48 hours** of report receipt.
- **Triage & Risk Evaluation**: Within **5 business days**.
- **Fix & Patch Window**: Critical vulnerabilities patched within **14 days**.
- **Public Disclosure**: Advisories published on GitHub Security Advisories after patch release.

---

## 🔐 App Security & Privacy Architecture

Lyra Music is designed with strict privacy and security principles:

1. **Zero Tracking & Telemetry**: No user telemetry, analytics keyloggers, or third-party ad tracking scripts are present in signed releases.
2. **Local Data Security**: Playlists, offline songs, and playback caches are stored in private app sandboxes (`Context.filesDir`).
3. **Verified Signed Builds**: Official release builds (`Lyra-Music.apk`) are signed with v2/v3 APK signatures and published alongside SHA-256 checksums for binary integrity verification.
4. **Third-Party API Protection**: All external network requests (lyrics providers, metadata APIs) use TLS 1.3 encrypted HTTPS endpoints.
5. **Dependency Integrity**: Dependencies are scanned via automated Dependabot alerts and static code analysis workflows.

---

## 📜 Binary Integrity Verification

Before installing APK files from GitHub releases, verify the SHA-256 checksum:

```powershell
# PowerShell Checksum Verification
Get-FileHash -Algorithm SHA256 .\Lyra-Music.apk
```

Compare the output hash against the official release hashes published at [lyraMusicApp Releases](https://github.com/lyraMusicApp/lyra-music/releases).
