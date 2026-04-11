# Phase 1 Implementation Plan: Critical Fixes

**Date:** 2026-04-07
**Priority:** Critical (blocks core functionality)
**Issues covered:** 36 of 150 total
**Estimated effort:** ~17 developer-days, parallelizable to ~9 calendar days with 2 developers

---

## Executive Summary

Phase 1 addresses three categories that collectively block core functionality for the majority of XDM's user base. **Category 1** (FFmpeg Assembly) causes every HLS and DASH download to fail at the final step due to unquoted non-ASCII paths, missing output file extensions, and opaque FFmpeg error handling. **Category 2** (Browser Extension) renders XDM unable to intercept downloads in Chrome (MV2 deprecation + Web Store delisting) and in Firefox with privacy settings enabled. **Category 7** (Crash-Safe Persistence) causes complete loss of the download list on power failure because `XDMApp.saveDownloadList()` writes directly to `downloads.txt` without atomic semantics.

The FFmpeg and persistence fixes are independent of each other. The browser extension work is self-contained but benefits from the HTTP server CORS changes being landed first.

---

## Category 1: FFmpeg Assembly Pipeline Fix

**Issues:** #184, #192, #198, #212, #272, #314, #409, #476, #802, #839, #1319 (11 issues)

### Problem Statement

**Bug A: No stderr capture -- opaque FFmpeg failures**

In `FFmpeg.java:178-180`, the `ProcessBuilder` is configured with `redirectErrorStream(true)`, but `processOutput()` (line 229) only parses progress lines (`frame=...time=`) and `Duration:` lines. All other FFmpeg output -- including error messages -- is silently discarded. When FFmpeg fails, the only diagnostic is "FFmpeg exit code: 30" (constant `FF_CONVERSION_FAILED` at line 17).

**Bug B: Non-ASCII output paths break FFmpeg on Windows**

In `HlsDownloader.java:637`:
```java
ffOutFile = new File(getOutputFolder(), UUID.randomUUID() + "_" + getOutputFileName(true));
```
And in `DashDownloader.java:250`:
```java
outFile = new File(getOutputFolder(), UUID.randomUUID() + "_" + getOutputFileName(true));
```

`getOutputFileName(true)` returns the user-visible filename which may contain Chinese characters (e.g., issue #409: `铠甲勇士捕王 - 看TV.MP4`). On Windows, `ProcessBuilder` passes arguments through Win32 `CreateProcess` using the system's active code page. Non-ASCII characters not representable in the current code page are corrupted.

**Bug C: Missing output file extension**

In issue #476, FFmpeg args log shows output path ending in `_06` with no file extension. Without `.mp4` or `.mkv`, FFmpeg cannot auto-detect the output container format.

### Current vs Expected Behavior

| Scenario | Current | Expected |
|----------|---------|----------|
| HLS download with Chinese filename | FFmpeg exit code 30, "Failed to append/convert" | File assembled successfully |
| DASH download of YouTube video | FFmpeg exit code 30, no diagnostic info | File assembled, or clear error logged |
| Any FFmpeg failure | Log shows only "FFmpeg exit code: 30" | Full FFmpeg stderr captured and logged |
| Output file has no extension | FFmpeg cannot detect format, fails | Extension inferred from MIME type or codec |

### Implementation Steps

#### Task 1.1: Capture and log FFmpeg stderr (Complexity: S)

**File:** `app/src/main/java/xdman/mediaconversion/FFmpeg.java`

- In `convert()` method (line 178-196): add a `StringBuilder` to accumulate full FFmpeg output (capped at 64KB).
- In `processOutput()` (line 229): add `Logger.log("ffmpeg> " + text)` at the start, before conditional parsing.
- After process exits (line 196): if `ffExitCode != 0`, log accumulated output.
- Add `getLastOutput()` method for callers.

#### Task 1.2: Fix non-ASCII paths on Windows (Complexity: M)

**Files:** `HlsDownloader.java`, `DashDownloader.java`

Use ASCII-only temp filenames during FFmpeg invocation, rename afterward:

Change `HlsDownloader.java:637` from:
```java
ffOutFile = new File(getOutputFolder(), UUID.randomUUID() + "_" + getOutputFileName(true));
```
to:
```java
ffOutFile = new File(getOutputFolder(), UUID.randomUUID().toString() + ".tmp");
```
Similarly at `DashDownloader.java:250`.

The existing rename logic at `HlsDownloader.java:655-659` and `DashDownloader.java:267-272` already handles the final rename to the user-visible name.

#### Task 1.3: Ensure output file extension is present (Complexity: S)

**Files:** `DashDownloader.java`, `HlsDownloader.java`

- In `DashDownloader.assemble()` (line 250): if output filename has no extension, infer from MIME types in DASH metadata or default to `.mkv`.
- In `HlsDownloader.assemble()` (line 637): if no extension, append `.ts` for original format or the selected output format extension.

#### Task 1.4: Add fallback binary concatenation for HLS (Complexity: M)

**File:** `app/src/main/java/xdman/downloaders/hls/HlsDownloader.java`

When `outputFormat == 0` (Original) and segments are `.ts` files, use binary concatenation (like `DashDownloader.assemblePart()` at line 350) before falling back to FFmpeg. This restores 7.1.10 behavior.

#### Task 1.5: Improve FFmpeg exit code handling (Complexity: S)

**File:** `app/src/main/java/xdman/mediaconversion/FFmpeg.java`

Map FFmpeg exit codes to descriptive error constants. In `HlsDownloader.assemble()` (line 645) and `DashDownloader.assemble()` (line 257), use captured stderr for user-facing error messages.

### Files to Modify

| File | Changes |
|------|---------|
| `app/src/main/java/xdman/mediaconversion/FFmpeg.java` | Capture all output; expose via getter; log on failure |
| `app/src/main/java/xdman/downloaders/hls/HlsDownloader.java` | ASCII temp path; extension fallback; binary concat |
| `app/src/main/java/xdman/downloaders/dash/DashDownloader.java` | ASCII temp path; extension inference |
| `app/src/main/java/xdman/util/XDMUtils.java` | Utility: `hasFileExtension()`, `inferExtensionFromMime()` |

### Testing Strategy

- Unit test: `FFmpeg.convert()` with mock process producing known stderr; verify capture
- Integration test: download with Chinese-character filename on Windows; verify FFmpeg succeeds
- Integration test: DASH download where filename is a bare number; verify `.mkv` appended
- Regression test: HLS `.ts` stream with `outputFormat == 0`; verify binary concat produces valid file
- Negative test: corrupt a segment file; verify FFmpeg stderr is logged with informative message

### Risk Assessment

- **Low risk**: Stderr capture is additive
- **Medium risk**: Changing temp file naming could break resume mid-assembly. Mitigated: temp file is created fresh each `assemble()` call and deleted on failure
- **Medium risk**: Binary concat fallback could produce corrupt files with mixed codecs. Mitigated: validate first segment's magic bytes; fall back to FFmpeg on mismatch

---

## Category 2: Browser Extension MV3 Migration

**Issues:** #235, #291, #317, #330, #372, #404, #535, #578, #737, #913, #918, #949, #1014, #1087, #1114, #1162, #1166, #1168, #1208, #1301, #1303 (21 issues)

### Problem Statement

**2a. Chrome MV2 deprecation**

`manifest.json:3` declares `"manifest_version": 2`. Chrome blocks MV2 extensions. MV2-only APIs in use:
- `chrome.webRequest.onHeadersReceived` with `"blocking"` option (`bg.js:488`) -- unavailable in MV3
- `chrome.browserAction` (lines 406-426) -- replaced by `chrome.action`
- `chrome.tabs.executeScript` (line 163) -- replaced by `chrome.scripting.executeScript`
- Background page (`"page": "background.html"`) -- replaced by service worker

**2b. CORS missing on HTTP server**

`MonitoringSession.java` HTTP server does not send CORS headers. In MV3, service workers require CORS for `fetch()` to localhost. `Response.java:14-38` emits headers from `HeaderCollection` but never includes `Access-Control-Allow-Origin`.

**2c. Firefox FPI incompatibility**

`privacy.firstparty.isolate` blocks `browser.cookies.getAll()` in `firefox/bg.js:57`. With FPI, cookies are isolated by first-party domain; `firstPartyDomain` must be specified in the query.

**2d. Extension persistence**

Side-loaded (developer mode) extensions don't survive Chrome restarts.

**2e. Native messaging host hardcoded IDs**

`NativeMessagingHostInstaller.java:17-18` hardcodes two extension IDs. A new MV3 extension gets a different ID.

### Current vs Expected Behavior

| Scenario | Current | Expected |
|----------|---------|----------|
| Load extension in Chrome 2026 | "Manifest version 2 is deprecated" | Extension loads and functions |
| Extension after Chrome restart | Side-loaded disabled | Persists via store install |
| Firefox with `privacy.firstparty.isolate` | Stops seeing downloads | Downloads intercepted correctly |
| MV3 service worker fetch to localhost | CORS error | Response includes CORS headers |

### Implementation Steps

#### Task 2.1: Create MV3 Chrome extension (Complexity: XL)

**New directory:** `app/extensions/xdm-browser-monitor/chrome-mv3/`

New `manifest.json`:
- `"manifest_version": 3`
- `"background": {"service_worker": "bg.js"}`
- `"action"` instead of `"browser_action"`
- `"permissions": ["activeTab", "cookies", "contextMenus", "webRequest", "nativeMessaging", "scripting"]`
- `"host_permissions": ["*://*/*"]`

Rewrite `bg.js`:
- **Use `webRequest` in non-blocking observation mode** (Recommended): observe responses, send to XDM via native messaging, let XDM initiate its own download
- Replace `chrome.browserAction.*` with `chrome.action.*` (lines 388-426)
- Replace `chrome.tabs.executeScript` with `chrome.scripting.executeScript`
- Replace `chrome.contextMenus.create({onclick: ...})` with `chrome.contextMenus.onClicked.addListener()`
- Use `chrome.storage.session` for transient state (service worker can be terminated at any time)
- Implement reconnection logic for `chrome.runtime.connectNative()` -- detect `port.onDisconnect`, reconnect on demand

#### Task 2.2: Add CORS headers to HTTP server (Complexity: S)

**File:** `app/src/main/java/xdman/monitoring/MonitoringSession.java`

In `setResponseOk()` (line 59) and `onSync()` (line 211):
```java
headers.setValue("Access-Control-Allow-Origin", "*");
headers.setValue("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
headers.setValue("Access-Control-Allow-Headers", "Content-Type");
```

Handle `OPTIONS` preflight in `processRequest()` (line 257): return 204 with CORS headers.

#### Task 2.3: Fix Firefox FPI cookie access (Complexity: S)

**File:** `app/extensions/xdm-browser-monitor/firefox/bg.js`

In `sendToXDM()` (line 57) and `sendUrlToXDM()` (line 103): always include `firstPartyDomain: null` in `cookies.getAll()` query. Firefox interprets this as "match all first-party domains" when FPI is not enabled, and it's required when FPI is enabled.

#### Task 2.4: Update native messaging host installer (Complexity: S)

**File:** `app/src/main/java/xdman/util/NativeMessagingHostInstaller.java`

At line 17-18, add the new MV3 extension's ID to `CHROME_EXTENSION_IDS`. Keep old IDs for backward compatibility.

#### Task 2.5: Update Firefox manifest (Complexity: S)

**File:** `app/extensions/xdm-browser-monitor/firefox/manifest.json`

Replace deprecated `"applications"` key with `"browser_specific_settings"` (line 27-31).

### Files to Modify

| File | Changes |
|------|---------|
| `app/extensions/xdm-browser-monitor/chrome-mv3/manifest.json` | **New**: MV3 manifest |
| `app/extensions/xdm-browser-monitor/chrome-mv3/bg.js` | **New**: Service worker rewrite |
| `app/extensions/xdm-browser-monitor/chrome-mv3/popup.js` | **New**: Updated for `chrome.action` |
| `app/extensions/xdm-browser-monitor/firefox/bg.js` | FPI fix in `cookies.getAll()` |
| `app/extensions/xdm-browser-monitor/firefox/manifest.json` | Replace `applications` key |
| `app/src/main/java/xdman/monitoring/MonitoringSession.java` | CORS headers, OPTIONS handler |
| `app/src/main/java/xdman/util/NativeMessagingHostInstaller.java` | Add new extension ID |

### Testing Strategy

- Manual: load MV3 extension in Chrome; verify native host connection and download interception
- Manual: download YouTube video via MV3; verify DASH handoff to XDM
- Manual: Firefox with `privacy.firstparty.isolate = true`; verify cookie-authenticated downloads
- Automated: mock native messaging port; verify service worker reconnects after disconnect
- CORS: `fetch()` from MV3 service worker to `http://127.0.0.1:9614/sync`; verify response received
- Regression: existing Firefox MV2 extension still works

### Risk Assessment

- **High risk**: MV3 loses `webRequestBlocking` -- browser download dialog may briefly appear before XDM intercepts. UX regression to communicate.
- **Medium risk**: Service worker suspension can lose `requests` array state. Mitigated by `chrome.storage.session`.
- **Low risk**: CORS changes are additive; Firefox FPI fix is a one-parameter addition.

---

## Category 7: Crash-Safe Download List Persistence

**Issues:** #252, #642, #750, #884 (4 issues)

### Problem Statement

**Non-atomic download list writes**

`XDMApp.saveDownloadList()` at line 888-951 writes directly to `downloads.txt` using `FileOutputStream` (line 899). This truncates the file immediately upon opening. If killed between truncation and write completion, the file is empty or partially written. `loadDownloadList()` (line 812) finds an empty file or throws on line 828, resulting in an empty download list.

**Same pattern in Config**

`Config.save()` at line 74-153 uses `FileWriter` directly on `config.txt`.

**Existing partial fix in downloaders**

`HlsDownloader.saveState()` (line 477-487) and `DashDownloader.saveState()` (line 562-572) use write-to-`.tmp`-then-rename, but with non-atomic `out.delete()` + `tmp.renameTo(out)` sequence. Race window between delete and rename.

**No orphan recovery**

Lost entries in `downloads.txt` leave temp chunks in `~/.xdman/temp/<uuid>/` unreferenced and unrecoverable.

### Current vs Expected Behavior

| Scenario | Current | Expected |
|----------|---------|----------|
| Power loss during download | Download list empty on restart | List intact; downloads resumable |
| Power loss during `saveDownloadList()` | `downloads.txt` truncated/corrupt | Previous valid file preserved |
| Orphaned temp directories | Remain forever, wasting disk | Detected; offered for recovery/cleanup |

### Implementation Steps

#### Task 7.1: Atomic write for download list (Complexity: M)

**File:** `app/src/main/java/xdman/XDMApp.java`

Replace `saveDownloadList()` (line 893):
1. Write to `downloads.txt.tmp` in the same directory
2. Flush and close writer
3. Use `Files.move(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)` -- atomic on NTFS and POSIX
4. Maintain backup: copy current `downloads.txt` to `downloads.txt.bak` before overwriting
5. In `loadDownloadList()`: if `downloads.txt` fails to parse, fall back to `downloads.txt.bak`

#### Task 7.2: Atomic write for Config (Complexity: S)

**File:** `app/src/main/java/xdman/Config.java`

Apply same pattern to `save()` (line 74): write to `config.txt.tmp`, atomically move to `config.txt`.

#### Task 7.3: Fix individual downloader state file atomicity (Complexity: S)

**Files:** `HlsDownloader.java` (line 477-487), `DashDownloader.java` (line 562-572)

Replace `delete()` + `renameTo()` with:
```java
Files.move(tmp.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
```
Catch `AtomicMoveNotSupportedException` and fall back to current pattern with `.bak` safety net.

#### Task 7.4: Orphaned temp directory recovery (Complexity: M)

**File:** `app/src/main/java/xdman/XDMApp.java`

After `loadDownloadList()` (line 249), add `recoverOrphanedDownloads()`:
1. List subdirectories in `Config.getInstance().getTemporaryFolder()`
2. For UUID-named directories not in `downloads` map, check for `state.txt`
3. If parseable, create `DownloadEntry` with state `PAUSED` and add to map
4. If corrupt, flag as orphaned for user cleanup

#### Task 7.5: Periodic auto-save with fsync (Complexity: S)

**File:** `app/src/main/java/xdman/XDMApp.java`

Add `ScheduledExecutorService` that calls `saveDownloadList()` every 30 seconds while any download is active. Include `FileChannel.force(true)` before rename for durability guarantee.

### Files to Modify

| File | Changes |
|------|---------|
| `app/src/main/java/xdman/XDMApp.java` | Atomic save; orphan recovery; periodic save |
| `app/src/main/java/xdman/Config.java` | Atomic save |
| `app/src/main/java/xdman/downloaders/hls/HlsDownloader.java` | `Files.move()` in `saveState()` |
| `app/src/main/java/xdman/downloaders/dash/DashDownloader.java` | `Files.move()` in `saveState()` |

### Testing Strategy

- Simulate crash: start download, `taskkill /F`, restart, verify list intact
- Corrupt file: truncate `downloads.txt` to 10 bytes; verify fallback to `.bak`
- Orphan recovery: delete entries from `downloads.txt` but leave temp dirs; restart; verify recovery
- Stress test: 20 concurrent downloads, repeated kills; verify no data loss
- Filesystem: verify `ATOMIC_MOVE` on NTFS and exFAT (latter may not support it; verify fallback)

### Risk Assessment

- **Low risk**: Atomic write is well-understood; fallback handles unsupported filesystems
- **Medium risk**: Orphan recovery could claim dirs from other apps. Mitigated: validate UUID patterns and expected file structure
- **Low risk**: Periodic auto-save adds minor I/O (one write/30s)

---

## Dependencies Between Categories

```
Category 1 (FFmpeg)  -----> independent, start immediately
Category 7 (Persistence) -> independent, start immediately
Category 2 (Extension) ---> internal dep: CORS (Task 2.2) before MV3 HTTP fallback
                             benefits from Cat 1 being complete for e2e testing
```

**Recommended execution order:**
1. Start Cat 1 and Cat 7 in parallel (no shared code)
2. Cat 2 Task 2.2 (CORS) and Task 2.3 (Firefox FPI) start immediately
3. Cat 2 Task 2.1 (MV3 rewrite) starts after Cat 1 is substantially complete for e2e testing

**Shared code concern:** `XDMApp.java` modified by both Cat 7 and potentially Cat 2. `MonitoringSession` changes for CORS are in a separate file.

---

## Complexity Summary

| Task | Category | Description | Size | Est. Days |
|------|----------|-------------|------|-----------|
| 1.1 | FFmpeg | Capture FFmpeg stderr | S | 0.5 |
| 1.2 | FFmpeg | Fix non-ASCII paths | M | 1.5 |
| 1.3 | FFmpeg | Ensure output extension | S | 0.5 |
| 1.4 | FFmpeg | Binary concat fallback for HLS | M | 2 |
| 1.5 | FFmpeg | Improve exit code handling | S | 0.5 |
| 2.1 | Extension | MV3 Chrome extension rewrite | XL | 5 |
| 2.2 | Extension | CORS headers on HTTP server | S | 0.5 |
| 2.3 | Extension | Firefox FPI cookie fix | S | 0.5 |
| 2.4 | Extension | Native messaging host ID update | S | 0.5 |
| 2.5 | Extension | Firefox manifest update | S | 0.25 |
| 7.1 | Persistence | Atomic write for download list | M | 1.5 |
| 7.2 | Persistence | Atomic write for Config | S | 0.5 |
| 7.3 | Persistence | Fix downloader state atomicity | S | 0.5 |
| 7.4 | Persistence | Orphaned temp recovery | M | 2 |
| 7.5 | Persistence | Periodic auto-save | S | 0.5 |
| | | **Total** | | **~17** |
