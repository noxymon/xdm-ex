# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Xtreme Download Manager (XDM) — a Java Swing desktop download manager that accelerates downloads, captures streaming video (YouTube, DASH, HLS, HDS), integrates with browsers via native messaging, and includes a built-in video converter (FFmpeg-based). Current version: 7.2.11.

## Build Commands

The project uses Maven with a wrapper. All commands run from the root directory:

```bash
./mvnw clean install          # Build fat JAR (target/xdman.jar)
./mvnw clean package           # Package without running tests
./mvnw test                    # Tests are skipped by default (surefire skipTests=true)
java -jar target/xdman.jar     # Run the application
```

- Requires **Java 25** (compiler source/target/release = 25).
- The `maven-assembly-plugin` produces a single fat JAR with all dependencies. Entry point: `xdman.Main`.
- On Windows, use `mvnw.cmd` instead of `./mvnw`.

## Architecture

### Core Application Flow

`Main` → `XDMApp.start(args)` — `XDMApp` is the central singleton that implements `DownloadListener`, `DownloadWindowListener`, and manages the full download lifecycle. It holds the download registry (`Map<String, DownloadEntry>`), coordinates the UI, browser monitor, and queue scheduler.

### Key Packages (all under `xdman`)

- **`downloaders/`** — Protocol-specific download engines. Abstract base: `Downloader` → segment-based downloading with `Segment`, `SegmentDownloader`, `AbstractChannel`. Subpackages: `http/`, `ftp/`, `dash/`, `hls/`, `hds/`, `metadata/`.
- **`monitoring/`** — `BrowserMonitor` listens for browser requests via native messaging. Protocol-specific handlers: `M3U8Handler` (HLS), `F4mHandler` (HDS), `YtUtil`, `FBHandler`, `VimeoHandler`, `InstagramHandler`.
- **`network/`** — Low-level networking: `SocketFactory`, `ProxyResolver`, `KeepAliveConnectionCache`, `WebRequest`. Subpackages: `http/`, `ftp/`.
- **`ui/components/`** — Swing UI windows and panels. `MainWindow` is the primary application window. `XDMFrame` is the base frame class. Other notable: `DownloadWindow`, `NewDownloadWindow`, `VideoDownloadWindow`, `SettingsPage`, `ClipboardPasteWnd`.
- **`ui/laf/`** — Custom FlatLaf-based look-and-feel UI delegates (`XDMButtonUI`, `XDMScrollBarUI`, etc.).
- **`ui/res/`** — Resource loaders: `StringResource` (i18n), `ImageResource`, `ColorResource`, `FontResource`.
- **`mediaconversion/`** — FFmpeg integration for converting downloaded videos. `FFmpeg` executes conversions, `FormatLoader` loads supported formats.
- **`videoparser/`** — `YoutubeDLHandler` integration for video metadata extraction.
- **`util/`** — Platform utilities (`WinUtils`, `LinuxUtils`, `MacUtils`), `NativeMessagingHostInstaller`, `FFmpegDownloader`, `UpdateChecker`.
- **`win32/`** — Windows-specific JNA native methods.

### Browser Extensions

Located in `extensions/`:
- `xdm-browser-monitor/` — Chrome/Firefox extension that intercepts downloads
- `native-messaging/` — Native messaging host for browser-to-app communication

### Translations

Translation files in `src/main/resources/lang/` (e.g., `en.txt`, `de.txt`). Loaded by `StringResource`.

### Configuration

`Config` manages application settings. `QueueManager` and `QueueScheduler` handle download queue scheduling.

## Key Dependencies

- **FlatLaf 3.7.1** — Modern look-and-feel for Swing
- **JNA 5.16.0** — Native platform access (Windows registry, system tray, etc.)
- **commons-net 3.6** — FTP protocol support
- **json-simple 1.1.1** — JSON parsing
- **xz 1.8** — XZ compression support

## Docs

`docs/issues_7.2.11/` contains issue analyses with an `INDEX.md`. `scripts/` has Python utilities for issue management (`generate_index.py`, `clean_issues.py`, `verify_missing.py`).
