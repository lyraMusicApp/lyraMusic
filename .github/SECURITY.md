# Security Policy

The Lyra Music team takes the security and privacy of our users very seriously. We appreciate responsible disclosure of vulnerabilities.

## Supported Versions

Only the latest active release branch and versions are supported with security updates.

| Version | Supported          |
| ------- | ------------------ |
| 3.0.x   | :white_check_mark: |
| 2.x     | :x:                |
| < 2.0   | :x:                |

## Reporting a Vulnerability

If you discover a security vulnerability in Lyra Music, please **do not** report it in a public GitHub issue, comment, or discussion.

Instead, please report vulnerabilities via one of the following methods:

1. **GitHub Private Vulnerability Reporting (Recommended):**
   Navigate to the [Security Advisories](https://github.com/lyraMusicApp/lyraMusic/security/advisories) tab of this repository and click **"Report a vulnerability"**.

2. **Maintainer Contact:**
   Contact the core maintainers directly via email: `shnwazdeveloper@gmail.com` with the subject `[SECURITY VULNERABILITY] Lyra Music`.

### What to Include in Your Report

To help us triage and resolve the issue quickly, please provide:
- A clear description of the vulnerability and its potential impact.
- Detailed step-by-step reproduction instructions or a minimal Proof of Concept (PoC).
- The affected component, file, or endpoint.
- Your suggested remediation or fix, if any.
- The version of Lyra Music, Android OS version, and device tested.

### Response Timeline

- **Initial Acknowledgment:** Within **48 hours** of submission.
- **Triage & Assessment:** Within **5 business days** confirming vulnerability validity and severity.
- **Fix & Disclosure:** We will coordinate with the reporter on a timeline before releasing a security patch. We follow responsible coordinated vulnerability disclosure.

## Security & Privacy Architecture

Lyra Music is built with strict privacy and security standards:

- **No Telemetry / No Tracking:** Lyra Music does not collect, log, or sell user personal data, browsing habits, or playback history to third parties.
- **Local-Only Storage:** User preferences, playlists, and session tokens are stored strictly locally on the user's device using Android's private app data sandbox.
- **Network Security:** All network requests to supported backend services (YouTube InnerTube, Paxsenix, LRCLIB, Last.fm) are transmitted exclusively over encrypted HTTPS/TLS connections.
- **Dependency Auditing:** Dependencies are routinely audited for known CVEs and updated regularly.

## DMCA & Legal Policy

For copyright notices, takedown requests, and fair use disclaimers, please consult our [DMCA & Legal Notice](DMCA.md).
