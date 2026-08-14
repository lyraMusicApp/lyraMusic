# Contributing to Lyra Music (`lyraMusicApp`)

Thank you for your interest in contributing to **Lyra Music**! We welcome contributions from developers, designers, and community members of all skill levels under the [lyraMusicApp](https://github.com/lyraMusicApp) organization.

---

## 📋 Code of Conduct & Safety Guidelines

Before contributing, please read our [Code of Conduct](CODE_OF_CONDUCT.md) and [Security Policy](SECURITY.md). All community members are expected to maintain a safe, respectful, and productive environment.

> [!IMPORTANT]
> **Do not report security vulnerabilities in public issues**. Use [GitHub Private Vulnerability Reporting](https://github.com/lyraMusicApp/lyra-music/security/advisories/new) or contact maintainers privately.

---

## 🛠️ How to Contribute

### 1. Reporting Bugs

Search existing issues at [lyraMusicApp Issues](https://github.com/lyraMusicApp/lyra-music/issues) before opening a new report. When submitting a bug report:
- Use our structured Bug Report Template.
- Include app version, Android OS version, device architecture, steps to reproduce, expected vs actual behavior, and logcat outputs if available.

### 2. Requesting Features

Have an idea to enhance Lyra Music?
- Open a feature request issue using our Feature Request Template.
- Describe the problem it solves, proposed UI workflow, and consistency with Material You guidelines.

### 3. Pull Requests Workflow

Follow this step-by-step process for code contributions:

1. **Fork the Repository**: Create a personal fork on GitHub.
2. **Create a Feature Branch**:
   ```bash
   git checkout -b feature/awesome-new-player-ui
   ```
3. **Write Clean, Safe Code**:
   - Adhere to Kotlin standard conventions & Jetpack Compose best practices.
   - Ensure privacy-first design: no tracking, no invasive permissions.
   - Test UI responsiveness across screen sizes (Phone, Tablet, Android Auto).
4. **Build & Test Locally**:
   Run assemble commands before opening your PR:
   ```powershell
   # Windows PowerShell local build
   .\gradlew.bat :app:assembleUniversalDebug
   ```
5. **Submit Your PR**:
   - Complete the [Pull Request Template](.github/PULL_REQUEST_TEMPLATE.md).
   - Ensure all automated checks (GitHub Actions CI/Security) pass.

---

## 🎨 UI & Code Formatting Standards

- **Compose Code**: Prefer small, single-responsibility composables in modular packages.
- **Material You Design**: Use system dynamic colors (`DynamicMaterialTheme`) where applicable.
- **SVG / Vector Assets**: Store raw vector assets under `assets/branding/` or `app/src/main/res/drawable/` with clean vector paths.

---

## 📜 Licensing

By contributing to Lyra Music, you agree that your contributions will be licensed under the **GNU General Public License v3.0 (GPL-3.0)**.
