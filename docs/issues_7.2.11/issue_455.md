# A video URL produces no download options. (#455)

[A video URL produces no download options.](https://github.com/subhra74/xdm/issues/455#top)

[AaronMishaling](https://github.com/AaronMishaling)
[on Jan 12, 2021](https://github.com/subhra74/xdm/issues/455#issue-784599748)
Using a video URL from YouTube and inputting it into XDM shows no download options (seen below). However, it used to work a little while back, and downloading the same video using the browser extension (when it wasn't removed on Firefox) would download fine.
Steps to reproduce the behavior:
1. Copy URL
2. Input into XDM under the 'download video' option in the 'file' dropdown
3. Click 'find'
- OS: Linux (Ubuntu 20.04, w/ Plasma DE)
- Browser: Firefox (v 84.0.2)
- XDM addon Version: has been removed from Firefox extensions store
- XDM Version: 7.2.11
[ main ] loading... [ main ] 11.0.6 5.8.0-36-generic [ main ] Loading config... [ main ] Creating folders [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts en [ main ] Loading language en [ main ] Context initialized [ Thread-4 ] instance starting... [ Thread-4 ] instance started. [ AWT-EventQueue-0 ] showing main window. [ Thread-4 ] Lock acquired... Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/icon.png Error setting Dock icon Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/bg_nav.png [ AWT-EventQueue-0 ] List changed checking for app update [ Thread-3 ] manifest download response: 200 7.2.11 7.2.11 Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/icon.png [ Thread-5 ] /opt/xdman/youtube-dl [ Thread-5 ] --no-warnings [ Thread-5 ] -q [ Thread-5 ] -i [ Thread-5 ] -J [ Thread-5 ] [https://www.youtube.com/watch?v=tJaqpr4unzM&list](https://www.youtube.com/watch?v=tJaqpr4unzM&list) [ Thread-5 ] Writing JSON to: /home/aaron/.xdman/temp/b3b9d4a5-a1a0-42ad-a672-043ee1acbeaf [ Thread-5 ] java.lang.NullPointerException at xdman.videoparser.YdlResponse.parse(YdlResponse.java:28) at xdman.videoparser.YoutubeDLHandler.start(YoutubeDLHandler.java:123) at xdman.ui.components.MediaDownloaderWnd$2.run(MediaDownloaderWnd.java:480) [ AWT-EventQueue-0 ] Total video found: 0

- 
            