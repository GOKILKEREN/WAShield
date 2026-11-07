# WaShield 🛡️

**Defensive WhatsApp payload inspection module for Android**

WaShield is an experimental Android/Xposed module concept that aims to detect suspicious message payload patterns in a WhatsApp process launched through **ShizukuPosed**. Its companion app is separate from WhatsApp and is intended to provide local status, configuration, and threat-log views.

> **Project status: early prototype (0.1.0). Not production-ready.**
> Detection rules and the companion UI are a starting point only. Hook compatibility, reliable message interception, configuration synchronization, and safe blocking have not been validated against a real WhatsApp build.

## Goals

- Inspect suspicious text and payload patterns using local heuristic rules.
- Keep the companion interface separate from the official WhatsApp installation.
- Use the existing WhatsApp installation and session; do not repackage or patch the WhatsApp APK.
- Keep analysis local. The prototype has no telemetry or network reporting feature.
- Prefer conservative, observable behavior over risky runtime modification.

## Intended environment

| Component | Target |
| --- | --- |
| Android | Android 13 for initial device testing |
| Runtime | ShizukuPosed with compatible classic Xposed API support |
| Build JDK | 17 |
| Gradle | 8.7 |
| Android Gradle Plugin | 8.5.2 |
| Kotlin | 1.9.24 |
| Compile / target SDK | 34 / 34 |
| Minimum SDK | 26 |

Compatibility depends on the exact ShizukuPosed release, Xposed API exposed by it, Android version, and WhatsApp version. These combinations must be tested rather than assumed.

## Current prototype components

- Heuristic detector rules for text length, zero-width characters, bidirectional controls, null bytes, control characters, emoji floods, Base64-like blobs, suspicious vCards, Unicode anomalies, repeated characters, suspicious links, and payload size.
- Xposed entry point and experimental hook/bootstrap classes.
- Room-backed local threat-log storage.
- A separate Jetpack Compose companion UI.
- A basic ContentProvider-based bridge between the module process and companion app.
- GitHub Actions workflow for a cloud debug build.

Some UI controls and data paths are placeholders and are **not fully connected** to the hook process. A detected pattern is not proof of malicious intent, and a log entry is not proof that a message was blocked.

## Build

The repository is configured to build on GitHub Actions using JDK 17 and Gradle 8.7. Push changes to `main` or run **Actions → WaShield Android APK → Run workflow**.

For a local build, install Android SDK components and Gradle 8.7, then run:

```bash
gradle --no-daemon assembleDebug
```

The debug APK is produced at:

```
app/build/outputs/apk/debug/app-debug.apk
```

The Xposed API is a compile-only dependency and is not packaged into the APK. The project currently does not include a Gradle wrapper JAR; CI uses its installed Gradle distribution.

## Initial validation plan

1. Confirm the project compiles and the APK installs.
2. Confirm the companion app opens without requesting WhatsApp credentials.
3. Activate WaShield in the compatible ShizukuPosed module manager.
4. Launch the official WhatsApp app through ShizukuPosed.
5. Confirm module initialization in logs before testing any message-handling behavior.
6. Validate detector rules with harmless synthetic text in an isolated test setup.
7. Test compatibility and false positives before considering any blocking behavior.

**Do not** test untrusted payloads on a primary WhatsApp account or valuable data. Back up important chats first. Never clear WhatsApp storage, uninstall WhatsApp, or disable account protections as a troubleshooting shortcut. Keep the normal WhatsApp installation and session intact.

## Known limitations and security notes

- Hook targets are heuristic and may change between WhatsApp releases.
- Generic method interception can cause instability; safe blocking semantics are not established.
- UI rule toggles are not yet reliably synchronized with the hook process.
- The IPC/provider authorization model needs a security review before use beyond local experiments.
- Whitelisting, filtering, notifications, export, and detailed log workflows are incomplete.
- Detection coverage is limited; false positives and false negatives are expected.
- A successful APK build does not establish that hooks load, that all message paths are inspected, or that payloads are safely blocked.

## Privacy

The intended design is local-first. The prototype does not intentionally transmit message contents to a server. Treat logs as sensitive because they may contain message previews or package metadata. Do not share logs publicly without reviewing and redacting them.

## Contributing

Issues and pull requests are welcome. Include Android version, WhatsApp version, ShizukuPosed version, device model, and sanitized logs when reporting compatibility problems. Never include phone numbers, session tokens, private messages, or account backups.

## License

No license has been declared yet. All rights are reserved by the repository owner unless a license is added. Do not assume this project is open for redistribution or reuse.
