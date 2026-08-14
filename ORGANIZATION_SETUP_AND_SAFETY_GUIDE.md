# 🛡️ Lyra Music GitHub Organization Setup & Safety Guide

This administrative guide details the exact steps required to configure and protect the **[lyraMusicApp](https://github.com/lyraMusicApp)** GitHub Organization.

---

## 1. Organization Metadata & Profile Setup

1. Log into GitHub as an Organization Owner for `lyraMusicApp`.
2. Go to **Organization Settings** -> **Profile**:
   - **Display Name**: `Lyra Music`
   - **Description**: `Official home of Lyra Music - Privacy-first, open-source Android music experience with Material You & synchronized lyrics.`
   - **URL**: `https://github.com/lyraMusicApp`
   - **Location**: `Global / Open Source`
   - **Avatar**: Upload [`assets/branding/lyra_logo.svg`](assets/branding/lyra_logo.svg) or generated high-res PNG.

---

## 2. Enabling Security & Safety Protections

To protect the organization codebase, contributors, and releases:

### A. Require Two-Factor Authentication (2FA)
1. Navigate to **Organization Settings** -> **Security** -> **Authentication security**.
2. Check **"Require two-factor authentication for everyone in your organization"**.
3. Save changes.

### B. Configure Branch Protection Rules (`main` / `master` / `lyra`)
1. Go to Repository **Settings** -> **Branches** -> **Add branch protection rule**.
2. Branch pattern name: `main` (and `lyra`).
3. Enable the following settings:
   - [x] **Require a pull request before merging** (Require 1 approving review).
   - [x] **Require status checks to pass before merging** (Require `Security & Code Safety Audit`).
   - [x] **Require signed commits** (Prevents impersonation attacks).
   - [x] **Include administrators** (Enforces rules across all team members).

### C. Enable Secret Scanning & Push Protection
1. Navigate to **Settings** -> **Code security & analysis**.
2. Enable:
   - [x] **Dependency graph**
   - [x] **Dependabot alerts** & **Dependabot security updates**
   - [x] **Secret scanning**
   - [x] **Secret scanning Push Protection** (Blocks accidental commits containing credentials/API keys).

### D. Private Vulnerability Reporting
1. Navigate to **Settings** -> **Code security & analysis** -> **Private vulnerability reporting**.
2. Click **Enable** to allow security researchers to privately disclose vulnerabilities.

---

## 3. Recommended Repository Topics / Tags

Add these tags on [github.com/lyraMusicApp/lyra-music](https://github.com/lyraMusicApp/lyra-music) under the repository settings cog:

```text
lyramusic, music-player, android, kotlin, jetpack-compose, youtube-music, lyrics, audio-player, material-you, privacy-focused, security-hardened, open-source
```
