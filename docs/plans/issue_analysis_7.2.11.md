# XDM 7.2.11 — Issue Analysis & Fix Plan

**Date:** 2026-04-07
**Source:** 150 archived open issues from `docs/issues_7.2.11/INDEX.md`
**Scope:** Categorized analysis with root causes, affected components, and prioritized recommendations.

---

## Issue Categories Overview

| # | Category                          | Issues | Priority   |
|---|-----------------------------------|--------|------------|
| 1 | File Assembly / FFmpeg Failures   | 11     | Critical   |
| 2 | Browser Extension Breakage        | 21     | Critical   |
| 3 | UI / Display Rendering            | 15     | High       |
| 4 | Download Protocol / Streaming     | 23     | High       |
| 5 | Settings / Config Persistence     | 9      | High       |
| 6 | Filename / Encoding               | 5      | Medium     |
| 7 | Data Loss / Crash Recovery        | 4      | Critical   |
| 8 | Performance / Stability           | 5      | High       |
| 9 | Installation / Startup            | 5      | Medium     |
| 10| Feature Requests                  | 6      | Low        |

---

## 1. File Assembly / FFmpeg Failures (11 issues)

**Issues:** #184, #192, #198, #212, #272, #314, #409, #476, #802, #839, #1319

**Symptoms:** Download reaches 100% then fails with _"Failed to append/convert file parts, please check if the drive is full or write protected"_. Temp chunks exist but final file is never produced. Regression from 7.1.10.

**Root Causes:**
1. **FFmpeg exit code 30** — `HlsDownloader.assemble()` and `DashDownloader.assemble()` invoke FFmpeg but do not capture stderr, making errors opaque.
2. **Non-ASCII file paths** — Chinese/special characters in output filenames break FFmpeg on Windows via `ProcessBuilder`.
3. **7.2.11 assembly regression** — confirmed working in 7.1.10/2018, broken in 7.2.11.
4. **Missing output file extension** — logs show paths like `_06` with no `.mp4`, causing FFmpeg format detection failure.

**Affected Components:**
- `xdman.downloaders.hls.HlsDownloader` — `assemble()` (line 647)
- `xdman.downloaders.dash.DashDownloader` — `assemble()` (line 259)
- `xdman.downloaders.SegmentImpl` — `transferComplete()`
- FFmpeg `ProcessBuilder` wrapper

**Recommended Fixes:**
1. Capture and log FFmpeg stderr/stdout for diagnostics
2. Sanitize/quote file paths for non-ASCII on Windows
3. Add output file extension to FFmpeg invocation
4. Add fallback binary-append for simple TS concatenation (restoring 7.1.10 behavior)
5. Interpret FFmpeg exit codes properly

---

## 2. Browser Extension Breakage (21 issues)

**Issues:** #235, #291, #317, #330, #372, #404, #535, #578, #737, #913, #918, #949, #1014, #1087, #1114, #1162, #1166, #1168, #1208, #1301, #1303

### 2a. Chrome Extension Unavailable (6 issues)
Extension removed from Chrome Web Store; download links return 404; Linux packages don't bundle it.

### 2b. Manifest V2 Deprecation (3 issues)
Chrome deprecating MV2; community MV3 port exists but is unofficial. Requires CORS changes in `xdman.jar`.

### 2c. Extension-to-App Communication Failures (5 issues)
Bundled AdoptOpenJDK 11.0.6 has connectivity issues with native messaging. Regression from 7.2.10.

### 2d. Firefox Privacy Setting Incompatibility (3 issues)
`privacy.firstparty.isolate` blocks extension→localhost communication.

### 2e. Extension Persistence (2 issues)
Side-loaded extensions don't survive Chrome reboots.

**Recommended Fixes:**
1. **Migrate to Manifest V3** — official port with CORS support in HTTP server
2. **Re-publish to Chrome Web Store** and update all links
3. **Update bundled JDK** to current LTS
4. **Use native messaging instead of localhost HTTP** for Firefox (bypasses FPI)
5. **Distribute via official stores** instead of side-loading

---

## 3. UI / Display Rendering (15 issues)

**Issues:** #207, #234, #256, #366, #371, #384, #417, #479, #484, #492, #514, #520, #586, #912, #1248

### 3a. HiDPI / Scaling Breakage (4 issues)
Custom rendering ignores OS DPI; scale config persists with no reset mechanism.

### 3b. Window Management (2 issues)
Dialogs split across monitors; window size not persisted.

### 3c. GUI Fails to Render (3 issues)
Blank windows on Wayland; stale instance lock blocks relaunch.

### 3d. Transparency Artifacts (2 issues)
Black areas when transparency disabled; missing icons.

### 3e. System Tray Issues (3 issues)
"JavaEmbeddedFrame" tooltip; misplaced tray icon; overlay on wrong tabs.

**Recommended Fixes:**
1. Read OS DPI and set sensible defaults; add `--reset-ui` CLI flag
2. Center dialogs on active monitor via `GraphicsEnvironment`
3. Detect Wayland → set `GDK_BACKEND=x11`; use PID-based lock files
4. Fix opaque repaint when transparency is off
5. Use AppIndicator for Linux tray; set tooltip explicitly

---

## 4. Download Protocol / Streaming (23 issues)

**Issues:** #79, #159, #169, #173, #292, #307, #328, #348, #362, #573, #576, #669, #735, #790, #828, #847, #990, #1032, #1076, #1134, #1151, #1261, #1283

### 4a. Session Expiry / Google Drive (3 issues)
Short-lived authenticated URLs not re-fetched on resume.

### 4b. "Server Sent Invalid Response" (4 issues)
Chunked transfer-encoding misparsed; premature stream closure.

### 4c. Video/Audio Muxing — DASH (5 issues)
Bilibili/Skillshare serve separate audio+video; XDM captures only video.

### 4d. HLS/TS Issues (3 issues)
Duration stretching; A/V desync; encrypted segments unsupported.

### 4e. Download Stalls / Non-Starts (5 issues)
Double-redirect treated as failure; signed URLs expire on resume; no magnet support.

### 4f. Stream Detection Regression (3 issues)
Extension fails to re-register listeners; YouTube detection broken.

**Recommended Fixes:**
1. Token-aware URL refresh via browser extension callback
2. Support chunked transfer-encoding properly; validate Content-Length
3. Auto-detect and pair DASH audio+video streams
4. Run `ffmpeg -bsf:a aac_adtstoasc` after TS assembly; handle discontinuities
5. Allow multi-hop redirects; regenerate signed URLs on resume
6. Persist `webRequest` listeners across navigations

---

## 5. Settings / Config Persistence (9 issues)

**Issues:** #243, #470, #474, #485, #569, #570, #672, #727, #755

**Root Causes:** Non-atomic config writes lost on crash; language always reloads as "en"; proxy fields never persisted; download folder ignored in favor of hardcoded default; `JFileChooser` blocks EDT.

**Affected Components:** `Config`, `SettingsPage`, `MainWindow`

**Recommended Fixes:**
1. Atomic write-then-rename for config files
2. Store and reload locale from config correctly
3. Persist proxy fields
4. Derive category paths from user-configured base folder
5. Replace `JFileChooser` with native dialog or move off EDT

---

## 6. Filename / Encoding (5 issues)

**Issues:** #411, #648, #688, #894, #993

**Root Cause:** `Content-Disposition` decoded as ISO-8859-1 instead of UTF-8 (RFC 6266/5987). Bundled JRE fonts lack CJK glyphs.

**Recommended Fixes:**
1. Parse `filename*=UTF-8''...` per RFC 5987
2. Decode URL-encoded filenames as UTF-8
3. Bundle or reference system fonts with CJK coverage

---

## 7. Data Loss / Crash Recovery (4 issues)

**Issues:** #252, #642, #750, #884

**Root Cause:** Download list file written non-atomically; truncated on crash. No recovery from orphaned temp chunks. Temp folders not cleaned after link-refresh.

**Recommended Fixes:**
1. Implement write-ahead / atomic-rename for download list
2. Add recovery scan for orphaned temp directories
3. Clean temp folders after link-refresh completion

---

## 8. Performance / Stability (5 issues)

**Issues:** #426, #714, #927, #995, #1242

**Root Causes:**
- `DownloadTableModel.sort()` comparator violates transitivity → `TimSort` crash on large lists
- Speed-limiter dialog not modal on Linux → main window freeze
- Browser monitor lacks rate-limiting → popup storm on resource-heavy sites
- Downloads auto-pause without retry on transient failures

**Recommended Fixes:**
1. Fix comparator to satisfy `Comparable` contract
2. Set tool dialogs as application-modal
3. Add rate-limiting and MIME-type filtering to browser monitor
4. Implement exponential backoff retry

---

## 9. Installation / Startup (5 issues)

**Issues:** #127, #240, #456, #567, #589

**Root Causes:** Socket-based instance lock can't detect stale processes; bundled 64-bit-only JRE; no auto-launch on Linux for extension.

**Recommended Fixes:**
1. PID-based lock file with stale detection
2. Architecture detection in installer
3. XDG autostart / systemd user service for Linux
4. Kill old processes during upgrade

---

## 10. Feature Requests (6 issues)

**Issues:** #175, #179, #322, #977, #1009, #1084

**Requests:**
- Speed limiter enforcement (token-bucket rate limiting)
- Inline download progress in main window (not separate popups)
- Shutdown-cancel button in progress panel
- Catch subtitle/audio formats (VTT, SRT)
- User-configurable browser monitor file-type filter
- Restore video conversion and download toolbar from pre-8.x

---

## Implementation Priority

### Phase 1 — Critical (blocks core functionality)
1. Fix FFmpeg assembly pipeline (Category 1) — high issue count, complete download failure
2. Migrate browser extension to MV3 + re-publish (Category 2) — extension non-functional for all Chrome users
3. Crash-safe download list persistence (Category 7) — data loss on power failure

### Phase 2 — High (significant UX degradation)
4. Fix config persistence (Category 5) — settings lost repeatedly
5. Fix DASH audio+video pairing (Category 4c) — no audio on major sites
6. Fix HiDPI / Wayland rendering (Category 3a, 3c) — app unusable on modern Linux
7. Fix performance crashes (Category 8) — TimSort crash, popup storms

### Phase 3 — Medium (improvement)
8. Fix filename encoding (Category 6)
9. Fix HTTP protocol handling (Category 4b, 4e)
10. Fix installation/startup (Category 9)

### Phase 4 — Low (enhancements)
11. Feature requests (Category 10)
