# Phase 3 Implementation Plan: Medium-Priority Fixes

**Date:** 2026-04-07
**Priority:** Medium (improvement)
**Issues covered:** 19 of 150 total
**Estimated effort:** ~26-44 hours (4-6 developer-days)

---

## Executive Summary

Phase 3 addresses three categories: filename/encoding corruption affecting non-Latin users (5 issues), HTTP protocol handling failures causing downloads to fail or stall (9 issues), and installation/startup problems preventing XDM from launching or running as a background service (5 issues).

The root causes are precisely locatable: a single-byte character cast in `NetUtils.readLine()` that destroys multi-byte UTF-8 header values, a hard-coded single-redirect limit in `HttpChannel.connectImpl()` that rejects CDN double-redirects, a socket-based instance lock in `BrowserMonitor.run()` that cannot detect stale processes, and the absence of architecture detection in Linux packaging. All three categories can proceed in parallel with no inter-category dependencies.

---

## Category 6: Filename / Encoding Fix

**Issues:** #411 (Polish), #648 (Vietnamese), #688 (Chinese), #894 (Japanese), #993 (Chinese) (5 issues)

### Problem Statement

**Root Cause A: Byte-to-char truncation in HTTP header reading**

`NetUtils.readLine()` at `NetUtils.java:14-25` reads raw bytes and casts each to `(char)` at line 22:
```java
buf.append((char) x);
```
For multi-byte UTF-8 sequences (CJK, Vietnamese, Polish diacritics), each byte is independently promoted to a character, producing Mojibake.

Same pattern in `ChunkedInputStream.readLine()` at `ChunkedInputStream.java:306-326`, line 324:
```java
buf.append((char) x);
```

**Root Cause B: Percent-encoding decoded per-byte, not as UTF-8**

`XDMUtils.decodeFileName()` at `XDMUtils.java:56-75` decodes percent-encoded sequences byte-by-byte and casts each to `(char)` at line 67:
```java
int c = Integer.parseInt(ch[i + 1] + "" + ch[i + 2], 16);
buf.append((char) c);
```
This converts each individual byte of a multi-byte UTF-8 sequence into a separate character.

**Root Cause C: RFC 5987 charset not used**

`NetUtils.getExtendedContentDisposition()` (lines 102-118) parses `filename*=UTF-8''value` but does not use the charset. Splits on `'` and takes everything after the last apostrophe, then calls `decodeFileName()` which has the per-byte bug above.

**Root Cause D: Font missing CJK glyphs**

`FontResource.java:13-14` uses `"Inter"` with `"Segoe UI Variable Text"` fallback. Neither has CJK coverage.

### Current vs Expected Behavior

| Scenario | Current | Expected |
|----------|---------|----------|
| Chinese `Content-Disposition` | `è¤\u0090å\u0090\u0088` Mojibake | `複合語起源` correct |
| URL-encoded Chinese filename | Decoded per-byte, garbled | Decoded as UTF-8, correct |
| Polish diacritics `ą`,`ę`,`ś` | Appear as `Ä…`, `Ä™`, `Å›` | Correct Polish characters |
| CJK filename in download list | Square boxes or garbled | Correct glyphs rendered |

### Implementation Steps

#### Task 6.1: Fix `XDMUtils.decodeFileName()` UTF-8 decoding (Complexity: S)

**File:** `app/src/main/java/xdman/util/XDMUtils.java` -- `decodeFileName()` (lines 56-75)

Replace per-byte decode loop with:
```java
return java.net.URLDecoder.decode(str, StandardCharsets.UTF_8);
```
Wrapped in try-catch, falling back to current behavior if not valid percent-encoded UTF-8. Retain existing invalid-character stripping (lines 60-63) as post-processing.

#### Task 6.2: Fix RFC 5987 `filename*` parsing (Complexity: M)

**File:** `app/src/main/java/xdman/util/NetUtils.java` -- `getExtendedContentDisposition()` (lines 102-118)

1. Parse charset from `filename*` value (format: `charset'language'value`)
2. Decode using `URLDecoder.decode(value, charset)` where charset is extracted
3. Default to UTF-8 if charset absent/unrecognized (per RFC 6266)

#### Task 6.3: Fix raw `filename=` non-ASCII heuristic (Complexity: S)

**File:** `app/src/main/java/xdman/util/NetUtils.java` -- `getNameFromContentDisposition()` (lines 120-149)

After extracting `filename=` value (line 135): apply UTF-8 re-decode heuristic:
```java
new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
```
Only apply if result is valid UTF-8 (use `CharsetDecoder` with `CodingErrorAction.REPORT`).

#### Task 6.4: Add CJK/extended-Latin font fallback (Complexity: S)

**File:** `app/src/main/java/xdman/ui/res/FontResource.java`

In `createFont()` (line 16): check `font.canDisplayUpTo("\u4e2d")`. If fails, use `new Font(Font.SANS_SERIF, style, size)` which maps to system composite fonts including CJK. Platform-specific fallbacks: Windows `"Microsoft YaHei"`, Linux `"Noto Sans CJK"`.

### Files to Modify

| File | Changes |
|------|---------|
| `app/src/main/java/xdman/util/XDMUtils.java` | `decodeFileName()` UTF-8 |
| `app/src/main/java/xdman/util/NetUtils.java` | `getExtendedContentDisposition()`, `getNameFromContentDisposition()` |
| `app/src/main/java/xdman/ui/res/FontResource.java` | CJK font fallback |

### Testing Strategy

1. Local HTTP server with `Content-Disposition: attachment; filename*=UTF-8''%E8%A4%87%E5%90%88.zip` (Japanese), `filename*=UTF-8''%C4%85%C4%99%C5%9B.txt` (Polish), raw `filename="中文.zip"`
2. Verify `XDMUtils.decodeFileName()` handles Chinese, Japanese, Korean, Vietnamese, Polish
3. Verify `getNameFromContentDisposition()` correct for both `filename*=` and raw `filename=`
4. Visual: launch XDM, add CJK-named download, verify rendering

### Risk Assessment

**Low risk.** Changes isolated to string decoding utilities. Fallback preserves existing behavior. Font changes only add fallback entries.

**Regression concern:** Some servers send non-UTF-8 encodings (e.g., GB2312). The UTF-8-first heuristic may misinterpret these. Mitigated: only re-decode if result is valid UTF-8.

---

## Category 4b/4e: HTTP Protocol Handling

**Issues:** #169, #307, #348, #362, #576, #828, #847, #1261, #1283 (9 issues)

### Problem Statement

**Root Cause A: Double-redirect treated as failure**

`HttpChannel.connectImpl()` at `HttpChannel.java:143-162`. When a 3xx redirect is received and `totalLength > 0` (resumed download), line 144-147:
```java
if (totalLength > 0) {
    errorCode = XDMConstants.ERR_INVALID_RESP;
    Logger.log(chunk + " Redirecting twice");
    return false;
}
```

CDNs like GitHub Releases, AWS S3, Google Drive routinely use 2-3 redirects. On initial download (`totalLength == 0`), the first redirect is followed (line 148-161). On segment connections (`totalLength > 0`), any redirect is rejected. Issue #576 log explicitly shows `"Redirecting twice"`.

**Root Cause B: Signed URL expiry on resume**

`HttpDownloader.createChannel()` at `HttpDownloader.java:29` always uses `metadata.getUrl()` from initial capture time. CDNs with time-limited signed URLs (AWS `X-Amz-Expires=300`, Google `&expire=`) go stale during pause. No mechanism to refresh.

**Root Cause C: Content-length/chunked-encoding conflict**

`XDMHttpClient.connect()` at `XDMHttpClient.java:103` reads `content-length` even when `transfer-encoding: chunked` is present. Per RFC 7230, content-length must be ignored with chunked encoding. `FixedRangeInputStream` then enforces the wrong length.

**Root Cause D: FixedRangeInputStream.read() bug**

`FixedRangeInputStream.read()` at `FixedRangeInputStream.java:49`:
```java
rem -= x;  // x is the byte VALUE (0-255), not 1
```
Single-byte `read()` returns byte value, not count. Should be `rem -= 1`. High byte values (0xFF = 255) cause `rem` to go negative prematurely.

### Current vs Expected Behavior

| Scenario | Current | Expected |
|----------|---------|----------|
| GitHub release (302 -> 302 -> 200) | "Server sent invalid response" | Follows redirect chain |
| Resume after 10min (signed URL) | "Download Session Expired" | Re-fetches fresh URL |
| Chunked response with content-length | Potential stream corruption | Ignores content-length |
| Multi-segment resume through CDN | "Redirecting twice" error | Each segment follows redirects |

### Implementation Steps

#### Task 4.1: Allow multi-hop redirects (Complexity: M)

**File:** `app/src/main/java/xdman/downloaders/http/HttpChannel.java` -- `connectImpl()` (lines 49-283)

1. Replace `if (totalLength > 0)` block (lines 144-147) with redirect counter (max 5)
2. For 3xx responses regardless of `totalLength`: update `url` from `location` header, set `isRedirect = true`, `continue`
3. Move redirect-handling code (lines 148-161) out of `else` branch
4. After limit exceeded: return `false` with `ERR_INVALID_RESP`
5. After success: update `redirected = true` and `redirectUrl = url`

#### Task 4.2: Fix signed URL expiry on resume (Complexity: L)

**Files:** `HttpDownloader.java`, `HttpChannel.java`, `HttpMetadata.java`

1. In `HttpDownloader.createChannel()` (line 26-32): detect signed URL patterns (`X-Amz-Expires`, `Signature`, `token=`). Mark metadata as requiring redirect-refresh.
2. In `HttpChannel.connectImpl()`: store resolved URL + timestamp after redirect. Before using cached redirect, check expiry.
3. In `HttpMetadata`: add `resolvedUrl` and `resolvedTimestamp` fields. Persist in `save()`/`load()`.
4. In `Downloader.chunkFailed()` (line 213-248): before reporting `ERR_SESSION_FAILED`, attempt one refresh cycle -- reset segment URLs to original metadata URL and retry.

#### Task 4.3: Fix content-length / chunked conflict (Complexity: S)

**File:** `app/src/main/java/xdman/network/http/XDMHttpClient.java` -- `connect()` (lines 63-128)

After reading headers (line 103): if `transfer-encoding: chunked` present, set `length = -1` regardless of `getContentLength()` result.

#### Task 4.4: Fix `FixedRangeInputStream.read()` decrement (Complexity: S)

**File:** `app/src/main/java/xdman/network/FixedRangeInputStream.java` -- `read()` (lines 37-52)

Change line 49 from `rem -= x` to `rem -= 1`.

### Files to Modify

| File | Changes |
|------|---------|
| `app/src/main/java/xdman/downloaders/http/HttpChannel.java` | Multi-hop redirects |
| `app/src/main/java/xdman/downloaders/http/HttpDownloader.java` | Signed URL detection |
| `app/src/main/java/xdman/downloaders/metadata/HttpMetadata.java` | Resolved URL fields |
| `app/src/main/java/xdman/network/http/XDMHttpClient.java` | Chunked/content-length fix |
| `app/src/main/java/xdman/network/FixedRangeInputStream.java` | `read()` decrement fix |

### Testing Strategy

1. Test server with 302 -> 302 -> 200; verify HttpChannel follows both
2. Test server returning 302 to URL with 5-second expiry; pause 10s, resume; verify fresh URL obtained
3. Server responding with `Transfer-Encoding: chunked` AND `Content-Length`; verify download completes
4. Unit test: `FixedRangeInputStream(stream, 100)` read 100 bytes one-at-a-time; verify exactly 100 succeed
5. Regression: GitHub releases download (known double-redirect)

### Risk Assessment

- **Medium risk** for Tasks 4.1/4.2: redirect logic is on critical path for all HTTP downloads. Infinite loop mitigated by counter. Regression mitigated by only changing `totalLength > 0` branch.
- **Low risk** for Tasks 4.3/4.4: targeted one-line fixes with clear correctness.

### Intra-category dependencies

Task 4.1 (multi-hop redirects) should complete before Task 4.2 (signed URL refresh), since the refresh mechanism relies on redirect-following being functional.

---

## Category 9: Installation / Startup

**Issues:** #127, #240, #456, #567, #589 (5 issues)

### Problem Statement

**Root Cause A: Socket-based instance lock cannot detect stale processes**

`BrowserMonitor.run()` at `BrowserMonitor.java:182-202` binds `ServerSocket` to port 9614 on loopback. If binding fails, calls `instanceAlreadyRunning()` (line 196) and exits. Problems:

1. If port 9614 is occupied by a non-XDM application, XDM exits with "An older version already running" (issue #127)
2. `acquireGlobalLock()` (lines 204-226) uses `FileLock` on `~/.xdm-global-lock`; can persist after crash on NFS/certain Linux configs
3. No PID validation that the port holder is actually XDM

**Root Cause B: 64-bit-only bundled JRE**

Issue #567: bundled JRE at `/opt/xdman/jre/` is 64-bit only. On 32-bit Linux: "cannot execute binary file: Exec format error." On ARM (Raspberry Pi): JRE directory absent entirely. Launcher script hardcodes JRE path without architecture detection. `XDMUtils.getOsArch()` (line 268-274) can detect arch, but runs inside JVM -- can't help if JVM itself fails.

**Root Cause C: No auto-launch on Linux**

Issue #589: after reboot, browser extension can't communicate with XDM. `LinuxUtils.addToStartup()` (line 53-71) creates `~/.config/autostart/xdman.desktop` but only on first run (`Config.isFirstRun()`). Desktop file uses `\r\n` line endings (line 105) instead of `\n`, which some DEs reject.

### Current vs Expected Behavior

| Scenario | Current | Expected |
|----------|---------|----------|
| XDM crashed, restart | "Older version already running" if port held by other app | Detects stale lock, starts normally |
| 32-bit Linux install | "Exec format error" | Uses system Java or reports clear error |
| Raspberry Pi (ARM) | "No such file or directory" | Detects missing JRE, tries system `java` |
| Reboot on Linux | Extension non-functional | XDM auto-starts via XDG autostart |
| Autostart desktop file | Has `\r\n` line endings | Unix `\n` line endings |

### Implementation Steps

#### Task 9.1: PID-based instance lock (Complexity: L)

**File:** `app/src/main/java/xdman/monitoring/BrowserMonitor.java` -- `run()` (lines 182-202), `acquireGlobalLock()` (lines 204-226)

1. Before binding `ServerSocket`, check `~/.xdman/xdm.pid`
2. If PID file exists, check if process is alive:
   - Linux/Mac: `new File("/proc/" + pid).exists()` or `kill -0`
   - Windows: `tasklist /FI "PID eq <pid>"` or JNA `Kernel32`
3. If dead: delete stale PID + lock files, proceed
4. If alive: attempt connect to port 9614, verify XDM protocol (`/sync` request, expect JSON). If non-XDM: use different port or report conflict
5. After binding: write current PID via `ProcessHandle.current().pid()` (Java 9+)
6. Register shutdown hook to delete PID file

#### Task 9.2: Architecture-aware launcher script (Complexity: M)

**New file:** launcher shell script for `/usr/bin/xdman`

1. Check `/opt/xdman/jre/bin/java` matches system architecture via `uname -m`
2. If incompatible/missing: fall back to `java` on `$PATH`
3. Verify Java version >= 11 via `java -version` output
4. If no compatible Java: print clear error with install instructions

#### Task 9.3: Fix Linux autostart desktop file (Complexity: S)

**File:** `app/src/main/java/xdman/util/LinuxUtils.java` -- `getDesktopFileString()` (lines 104-111), `addToStartup()` (lines 53-71)

1. Replace all `\r\n` with `\n` in format string (line 105-108)
2. Fix `Exec` line to handle `java.home` with/without trailing `/`
3. Add `StartupNotify=false` and `X-GNOME-Autostart-enabled=true`
4. Call `addToStartup()` based on user setting, not just `isFirstRun()`

#### Task 9.4: Startup validation on every launch (Complexity: S)

**File:** `app/src/main/java/xdman/XDMApp.java` -- `instanceStarted()` (lines 103-128)

1. On every launch: verify autostart desktop file exists with correct paths; regenerate if stale
2. Verify native messaging host manifests are current after upgrades

### Files to Modify

| File | Changes |
|------|---------|
| `app/src/main/java/xdman/monitoring/BrowserMonitor.java` | PID lock, stale detection |
| `app/src/main/java/xdman/util/LinuxUtils.java` | Line endings, format fix |
| `app/src/main/java/xdman/XDMApp.java` | Startup validation |
| New launcher script | Architecture-aware Java detection |

### Testing Strategy

1. Start XDM, `kill -9`, restart immediately; verify starts
2. Bind port 9614 with `nc -l 9614`; start XDM; verify detects port conflict
3. 32-bit Linux VM: install XDM; verify falls back to system Java
4. ARM (Raspberry Pi): verify detects missing JRE, uses system Java
5. Linux GNOME/KDE: verify `~/.config/autostart/xdman.desktop` created with `\n` line endings
6. Reboot; verify XDM auto-starts

### Risk Assessment

- **Medium risk** for Task 9.1: instance detection affects all platforms. `/proc` doesn't work on Mac. Mitigated: keep `ServerSocket` as primary, add PID as supplementary.
- **Low risk** for Tasks 9.2-9.4: new script doesn't modify existing code; line ending fix is trivially correct.

---

## Dependencies Between Categories

```
Category 6 (Encoding)  ────> independent
Category 4b/4e (HTTP)  ────> independent
Category 9 (Install)   ────> independent
```

All three modify non-overlapping code paths. Full parallel execution possible.

**Intra-category dependencies:**
- Task 4.1 (redirects) before Task 4.2 (signed URL refresh)
- Task 6.1 (decodeFileName) before Task 6.2 (getExtendedContentDisposition) -- latter calls former
- Task 9.1 (PID lock) before Task 9.4 (startup validation) -- changes init flow

---

## Complexity Summary

| Task | Category | Description | Size | Est. Hours |
|------|----------|-------------|------|------------|
| 6.1 | Encoding | Fix `decodeFileName()` UTF-8 | S | 1-2 |
| 6.2 | Encoding | Fix RFC 5987 `filename*` parsing | M | 2-4 |
| 6.3 | Encoding | Fix raw `filename=` non-ASCII | S | 1-2 |
| 6.4 | Encoding | CJK/extended-Latin font fallback | S | 1-2 |
| 4.1 | HTTP | Multi-hop redirect support | M | 3-5 |
| 4.2 | HTTP | Signed URL refresh on resume | L | 6-10 |
| 4.3 | HTTP | Chunked/content-length conflict | S | 1 |
| 4.4 | HTTP | `FixedRangeInputStream.read()` bug | S | 0.5 |
| 9.1 | Install | PID-based instance lock | L | 6-10 |
| 9.2 | Install | Architecture-aware launcher | M | 3-5 |
| 9.3 | Install | Fix autostart desktop file | S | 1 |
| 9.4 | Install | Startup validation on every launch | S | 1-2 |
| | | **Total** | | **26-44** |
