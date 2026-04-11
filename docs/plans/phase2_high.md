# Phase 2 Implementation Plan: High-Priority Fixes

**Date:** 2026-04-07
**Priority:** High (significant UX degradation)
**Issues covered:** 26 of 150 total
**Estimated effort:** ~14 developer-days

---

## Executive Summary

Phase 2 addresses four categories: settings that silently vanish on restart, videos from major sites downloading without audio, a completely unusable UI on HiDPI/Wayland Linux desktops, and crashes or system freezes triggered by large download lists or resource-heavy websites.

The root causes are well-localized: Config persistence fails because of non-atomic writes and missing `config.save()` calls in two of four settings pages. DASH audio pairing only works for YouTube because the matching logic is hardcoded to `youtube.com`/`googlevideo.com` hosts. The HiDPI code is effectively disabled (`getScaledInt()` returns its input unchanged) while Wayland receives no detection or fallback. The TimSort crash is a textbook violation of the `Comparator` contract.

---

## Category 5: Config Persistence Fix

**Issues:** #243, #470, #474, #485, #569, #570, #672, #727, #755 (9 issues)

### Problem Statement

**Root Cause A: Non-atomic config writes**

`Config.save()` at `Config.java:74-154` opens `FileWriter` directly on `~/.xdman/config.txt`. If process crashes mid-write, file is truncated. The catch block (line 147-148) is empty -- write failures produce zero diagnostics.

**Root Cause B: Missing `config.save()` in two settings pages**

`SettingsPage.saveNetworkSettings()` (line 2445-2521) updates in-memory `Config` for proxy, timeout, segments, TCP window size -- but never calls `config.save()`. Same for `saveAdvSettings()` (line 2150-2169). Only `saveOverviewSettings()` (line 2421) and `saveMonitoringSettings()` (line 2442) call `config.save()`.

Network/advanced settings are only persisted on clean shutdown via `XDMApp.exit()` (line 264). A crash after editing loses them entirely. This is the direct cause of issue #672 (proxy settings disappear).

**Root Cause C: Language always reloads as "en"**

`StringResource.loadLang()` at `StringResource.java:38` hardcodes:
```java
InputStream inStream = StringResource.class.getResourceAsStream("/lang/en.txt");
```
English loads regardless of the `code` parameter. The overlay on line 52 is applied but the initial English load means `loadLang()` returns `true` even when the overlay fails. Direct cause of issue #727.

### Current vs Expected Behavior

| Scenario | Current | Expected |
|----------|---------|----------|
| Set proxy, restart XDM | Proxy gone | Proxy persisted |
| Set language to Chinese, restart | UI shows English | UI shows Chinese |
| XDM killed during save | Config truncated/lost | Config intact (atomic) |
| Change Network/Advanced settings | Only saved on clean exit | Saved immediately |

### Implementation Steps

#### Task 5.1: Atomic config write (Complexity: S)

**File:** `app/src/main/java/xdman/Config.java` -- `save()` (lines 74-154)

Write to `config.txt.tmp`, then atomically rename to `config.txt` using `Files.move()` with `StandardCopyOption.ATOMIC_MOVE`. Add exception logging to catch blocks.

#### Task 5.2: Add missing config.save() calls (Complexity: S)

**File:** `app/src/main/java/xdman/ui/components/SettingsPage.java`

Add `config.save()` at the end of `saveNetworkSettings()` (after line 2520) and `saveAdvSettings()` (after line 2168).

#### Task 5.3: Fix language loading (Complexity: S)

**File:** `app/src/main/java/xdman/ui/res/StringResource.java` -- `loadLang()` (line 38)

Change from:
```java
InputStream inStream = StringResource.class.getResourceAsStream("/lang/en.txt");
```
to:
```java
InputStream inStream = StringResource.class.getResourceAsStream("/lang/" + code + ".txt");
```
Keep existing fallback logic for missing language files.

#### Task 5.4: Move JFileChooser off EDT (Complexity: M)

**File:** `app/src/main/java/xdman/ui/components/SettingsPage.java` -- lines 824, 830, 872

`JFileChooser` on Windows scans network drives, blocking EDT for seconds. Replace with `FileDialog` (native) or wrap in `SwingWorker`.

### Files to Modify

| File | Changes |
|------|---------|
| `app/src/main/java/xdman/Config.java` | Atomic `save()` |
| `app/src/main/java/xdman/ui/components/SettingsPage.java` | Add `config.save()` calls; JFileChooser off EDT |
| `app/src/main/java/xdman/ui/res/StringResource.java` | Fix `loadLang()` language parameter |

### Testing Strategy

- Serialize Config, kill process mid-write, verify atomic rename keeps old file
- Set proxy in Network Settings, kill XDM, restart, verify proxy present
- Set language to non-English, restart, verify correct UI language
- Regression: all settings categories round-trip correctly

### Risk Assessment

**Low risk.** Atomic write is well-understood. Missing `config.save()` is a one-line fix. `StringResource` fix is a single parameter change. None alter control flow.

---

## Category 4c: DASH Audio+Video Pairing

**Issues:** #292, #573, #790, #990, #1134 (5 issues)

### Problem Statement

Bilibili, Skillshare, and other non-YouTube sites serve DASH with separate audio and video tracks. XDM captures only video, producing silent files.

`MonitoringSession.processDashSegment()` (line 508-636) immediately returns `false` at line 514 for non-YouTube hosts:
```java
if (!(host.contains("youtube.com") || host.contains("googlevideo.com"))) {
    Logger.log("non yt host");
    return false;
}
```

Non-YouTube requests fall through to `processNormalVideo()` (line 685), which creates plain `HttpMetadata` (not `DashMetadata`) with only the single URL. Audio streams with non-video MIME types are silently ignored.

The `DashMetadata` class already supports a second URL (`url2`), headers (`headers2`), and lengths (`len1`, `len2`). `DashDownloader` handles two-track downloads using tags `"T1"` (video) and `"T2"` (audio). **The infrastructure exists -- it's just gated behind the YouTube host check.**

### Current vs Expected Behavior

| Scenario | Current | Expected |
|----------|---------|----------|
| Download from Bilibili | Video only, no audio | Video + audio muxed |
| Download from Skillshare | Video only, no audio | Video + audio muxed |
| YouTube DASH | Works (paired) | Works (unchanged) |

### Implementation Steps

#### Task 4c.1: Create generic DASH stream detector (Complexity: L)

**File:** `app/src/main/java/xdman/monitoring/MonitoringSession.java`

Add `processGenericDashSegment()` that detects DASH audio/video from any host based on MIME type and URL patterns. Pairing strategy: use `Referer` header from `ParsedHookData.getRequestHeaders()` to group streams from the same page (streams from the same DASH manifest share the same referer).

#### Task 4c.2: Create GenericDashUtil pairing logic (Complexity: M)

**New file:** `app/src/main/java/xdman/monitoring/GenericDashUtil.java`

- Maintain `videoQueue` and `audioQueue` keyed by referer (not YouTube `id`)
- `addToQueue(DASH_INFO info)` with referer-based deduplication
- `getDASHPair(DASH_INFO info)` that matches by referer + temporal proximity

**File:** `app/src/main/java/xdman/monitoring/DASH_INFO.java` -- add `referer` field

#### Task 4c.3: Modify onVideo flow (Complexity: M)

**File:** `app/src/main/java/xdman/monitoring/MonitoringSession.java` -- `onVideo()` (line 135)

Change processing order:
1. Try YouTube-specific DASH detection (existing `processDashSegment`)
2. Try generic DASH detection (new `processGenericDashSegment`) for audio MIME types
3. Fall back to `processNormalVideo`

#### Task 4c.4: Add audio MIME-type detection (Complexity: S)

**File:** `app/src/main/java/xdman/monitoring/MonitoringSession.java` -- `processNormalVideo()` (line 685)

Add detection for `audio/webm` (Bilibili). When standalone audio arrives with a recently captured video from the same referer, auto-pair as DASH.

### Files to Modify

| File | Changes |
|------|---------|
| `app/src/main/java/xdman/monitoring/MonitoringSession.java` | Generic DASH detector; flow change; audio MIME |
| `app/src/main/java/xdman/monitoring/GenericDashUtil.java` | **New**: Referer-based pairing |
| `app/src/main/java/xdman/monitoring/YtUtil.java` | Extract `DASH_INFO` to standalone class |

### Testing Strategy

- Manual: Bilibili test URLs from issue #292; verify audio+video download
- Manual: YouTube DASH regression test
- Unit test: simulate interleaved audio/video arrivals with same referer; verify pairing
- Edge case: sites with single combined stream (should bypass DASH logic)

### Risk Assessment

**Medium risk.** Referer-based pairing may produce false matches on sites with multiple players. YouTube-specific path must remain untouched for regression safety. `DASH_INFO` extraction requires care.

---

## Category 3: HiDPI / Wayland Rendering

**Issues:** #207, #366, #484, #492, #514, #520, #1248 (7 issues)

### Problem Statement

**Sub-problem A: DPI scaling is disabled**

`XDMUtils.getScaledInt()` at `XDMUtils.java:564-573` unconditionally returns its input:
```java
public static final int getScaledInt(int value) {
    return value;
}
```
All DPI-aware sizing code calls this and gets unscaled values. On 4K at 200%, all UI elements are half their intended size.

`XDMApp.start()` (lines 139-145) applies zoom from `Config.zoomLevelIndex`, but the default index 0 maps to `ZOOM_LEVEL_VALUES[0] = -1`, and scaling is only set when index `> 0`. So first run = no DPI scaling. The bundled AdoptOpenJDK 11.0.6 also has broken automatic scaling.

**Sub-problem B: Wayland blank window**

No `GDK_BACKEND=x11` fallback anywhere in codebase (confirmed by grep). Java Swing on Wayland produces blank/corrupted windows.

**Sub-problem C: Stale instance lock blocks relaunch**

`BrowserMonitor.run()` (line 182-202) binds to port 9614. If port is occupied by non-XDM application, new XDM calls `instanceAlreadyRunning()` and exits silently. If previous instance crashed, `acquireGlobalLock()` file lock may be stale.

### Current vs Expected Behavior

| Scenario | Current | Expected |
|----------|---------|----------|
| 4K display, first run | Tiny, illegible UI | Auto-detect DPI, scale appropriately |
| Wayland (Ubuntu 24.04) | Blank window | Fallback to XWayland |
| Previous instance crashed | New instance exits silently | Detect stale lock, start normally |
| User sets zoom 200% | Works if config persists | Works with proper DPI baseline |

### Implementation Steps

#### Task 3.1: Implement DPI detection in getScaledInt() (Complexity: M)

**File:** `app/src/main/java/xdman/util/XDMUtils.java` -- `getScaledInt()` (line 564)

Since FlatLaf is in use, call `UIScale.scale(value)` from FlatLaf, which respects both OS DPI and user zoom. This avoids double-scaling with FlatLaf's internal handling.

#### Task 3.2: Add --reset-ui CLI flag (Complexity: S)

**Files:** `app/src/main/java/xdman/Main.java`, `app/src/main/java/xdman/XDMApp.java` -- `start()` (line 136)

Add `--reset-ui` argument that resets `zoomLevelIndex` to 0 in config. Escape hatch for users who set extreme zoom and can't interact with UI.

#### Task 3.3: Add Wayland detection and XWayland fallback (Complexity: S)

**File:** `app/src/main/java/xdman/Main.java` -- static initializer (lines 7-17)

Detect `$XDG_SESSION_TYPE == "wayland"` or `$WAYLAND_DISPLAY` is set. Set `System.setProperty("awt.toolkit.name", "XToolkit")` or add `GDK_BACKEND=x11` to environment before AWT initialization.

#### Task 3.4: Fix stale instance detection (Complexity: M)

**File:** `app/src/main/java/xdman/monitoring/BrowserMonitor.java` -- `run()` (line 182), `acquireGlobalLock()` (line 204)

Before binding socket, attempt to connect to port 9614. If connection succeeds, an instance is truly running. If `ConnectException`, port is stale -- proceed. Alternatively, use PID-based lock with `ProcessHandle.of(pid).isPresent()` (Java 9+).

### Files to Modify

| File | Changes |
|------|---------|
| `app/src/main/java/xdman/util/XDMUtils.java` | `getScaledInt()` -- use FlatLaf `UIScale.scale()` |
| `app/src/main/java/xdman/Main.java` | `--reset-ui` arg parsing; Wayland detection |
| `app/src/main/java/xdman/XDMApp.java` | Handle `--reset-ui` in `start()` |
| `app/src/main/java/xdman/monitoring/BrowserMonitor.java` | Stale instance detection |

### Testing Strategy

- Manual: HiDPI display (4K at 200%) -- verify correctly sized UI
- Manual: Ubuntu 24.04 Wayland -- verify window renders
- Manual: kill -9 XDM, relaunch immediately, verify it starts
- Manual: `--reset-ui` flag from command line
- Regression: 96dpi display, verify no double-scaling

### Risk Assessment

**Medium-high risk.** DPI changes affect every panel. If FlatLaf's `UIScale.scale()` and manual `getScaledInt()` double-scale, UI will be oversized. Careful multi-DPI testing essential. Wayland fallback is low risk. Instance lock change is medium risk.

---

## Category 8: Performance / Stability

**Issues:** #426, #714, #927, #995, #1242 (5 issues)

### Problem Statement

**Sub-problem A: TimSort crash**

`DownloadSorter.compare()` at `DownloadSorter.java:10-38` violates transitivity. For date/size sorting:
```java
res = o1.getDate() > o2.getDate() ? 1 : -1;
```
When `o1.getDate() == o2.getDate()`, returns `-1`. So `compare(a,b) = -1` AND `compare(b,a) = -1`. Violates `sgn(compare(x,y)) == -sgn(compare(y,x))`, causing `IllegalArgumentException` in TimSort.

Also: if `XDMApp.getInstance().getEntry(id)` returns null for stale IDs, `NullPointerException` in comparator.

**Sub-problem B: Browser monitor popup storm**

Each video/media URL triggers `MonitoringSession.onVideo()` -> `processNormalVideo()` -> `XDMApp.addMedia()` -> `VideoPopup.addVideo()`. No rate limiting, no MIME-type filtering for non-downloadable resources, no cap on concurrent items.

New `MonitoringSession` threads spawned unbounded at `BrowserMonitor.run()` line 191. Resource-heavy pages send hundreds of requests/second, causing fork-bomb-like exhaustion.

### Current vs Expected Behavior

| Scenario | Current | Expected |
|----------|---------|----------|
| 661 links pasted from clipboard | TimSort crash, app unrecoverable | Sorts correctly |
| Visit resource-heavy site with monitor | System freeze, popup storm | Rate-limited, filtered |
| Speed limiter dialog on Linux | Main window freezes | Dialog is modal |

### Implementation Steps

#### Task 8.1: Fix DownloadSorter comparator (Complexity: S)

**File:** `app/src/main/java/xdman/ui/components/DownloadSorter.java` -- `compare()` (lines 10-38)

```java
@Override
public int compare(String id1, String id2) {
    DownloadEntry o1 = XDMApp.getInstance().getEntry(id1);
    DownloadEntry o2 = XDMApp.getInstance().getEntry(id2);
    if (o1 == null && o2 == null) return 0;
    if (o1 == null) return 1;
    if (o2 == null) return -1;
    int res = 0;
    switch (Config.getInstance().getSortField()) {
    case 0: res = Long.compare(o1.getDate(), o2.getDate()); break;
    case 1: res = Long.compare(o1.getSize(), o2.getSize()); break;
    case 2: res = o1.getFile().compareTo(o2.getFile()); break;
    case 3: res = Integer.compare(o1.getCategory(), o2.getCategory()); break;
    }
    return Config.getInstance().getSortAsc() ? res : -res;
}
```

#### Task 8.2: Rate-limit browser monitor popups (Complexity: M)

**Files:** `MonitoringSession.java` -- `onVideo()` (line 135); `VideoPopup.java` -- `addItem()` (line 352)

- Track last popup timestamp per origin; suppress within 500ms cooldown
- Cap max items in `VideoPopup` (e.g., 50); evict oldest when exceeded
- Ensure `Config.getMinVidSize()` threshold applied consistently (existing but defaults to 1MB)

#### Task 8.3: Cap MonitoringSession threads (Complexity: M)

**File:** `app/src/main/java/xdman/monitoring/BrowserMonitor.java` -- `run()` (line 182)

Replace unbounded `new Thread()` with bounded thread pool:
```java
ExecutorService pool = Executors.newFixedThreadPool(
    Math.min(Runtime.getRuntime().availableProcessors(), 4));
```
When pool is full, reject gracefully (close socket with error response).

### Files to Modify

| File | Changes |
|------|---------|
| `app/src/main/java/xdman/ui/components/DownloadSorter.java` | Fix `compare()` transitivity and null safety |
| `app/src/main/java/xdman/monitoring/BrowserMonitor.java` | Thread pool |
| `app/src/main/java/xdman/monitoring/MonitoringSession.java` | Rate limiting in `onVideo()` |
| `app/src/main/java/xdman/ui/components/VideoPopup.java` | Item cap with eviction |

### Testing Strategy

- Unit test: 1000 entries with duplicate dates; `Collections.sort()` with fixed comparator
- Unit test: null entries; verify graceful handling
- Manual: paste 661 links; verify no crash
- Manual: resource-heavy pages from issues #927, #1242; verify no storm
- Manual: monitor thread count during heavy activity; verify bounded

### Risk Assessment

**Low risk** for comparator fix. **Medium risk** for rate limiting (must not suppress legitimate popups). **Low risk** for thread pool.

---

## Dependencies Between Categories

```
Category 5 (Config)  ──>  Category 3 (HiDPI)
   Config must save/load zoomLevelIndex correctly
   before HiDPI zoom changes can be tested

Category 8 (Performance)  ──>  Category 4c (DASH)
   Thread pool in BrowserMonitor affects MonitoringSession
   where DASH pairing happens. Land pool first.

Category 5 ──independent── Category 8
Category 3 ──independent── Category 4c
```

**Recommended order:**
1. **Category 5** (Config) -- smallest, unblocks Category 3
2. **Category 8** (Performance) -- comparator fix is critical, unblocks Category 4c thread changes
3. **Category 3** (HiDPI/Wayland) -- depends on Config being stable
4. **Category 4c** (DASH Audio) -- largest, depends on BrowserMonitor thread pool

---

## Complexity Summary

| Task | Category | Description | Size | Est. Days |
|------|----------|-------------|------|-----------|
| 5.1 | Config | Atomic config write | S | 0.5 |
| 5.2 | Config | Missing config.save() calls | S | 0.25 |
| 5.3 | Config | Language loading fix | S | 0.25 |
| 5.4 | Config | JFileChooser off EDT | M | 1 |
| 4c.1 | DASH | Generic DASH stream detector | L | 3 |
| 4c.2 | DASH | GenericDashUtil pairing logic | M | 2 |
| 4c.3 | DASH | MonitoringSession flow change | M | 1 |
| 4c.4 | DASH | Audio MIME-type additions | S | 0.5 |
| 3.1 | HiDPI | DPI detection in getScaledInt() | M | 1.5 |
| 3.2 | HiDPI | --reset-ui CLI flag | S | 0.25 |
| 3.3 | HiDPI | Wayland detection/fallback | S | 0.5 |
| 3.4 | HiDPI | Stale instance lock fix | M | 1 |
| 8.1 | Perf | Fix DownloadSorter comparator | S | 0.25 |
| 8.2 | Perf | Rate-limit video popups | M | 1 |
| 8.3 | Perf | Thread pool for MonitoringSession | M | 1 |
| | | **Total** | | **~14** |
