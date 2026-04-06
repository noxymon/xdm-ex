# XDM not able to draw interface after reopening. (#484)

[XDM not able to draw interface after reopening.](https://github.com/subhra74/xdm/issues/484#top)

[HritwikSinghal](https://github.com/HritwikSinghal)
[on Feb 7, 2021](https://github.com/subhra74/xdm/issues/484#issue-803062631)
Describe the bug
** On Manjaro Wayland 20.2, also happened on ubuntu 20.04 wayland. DID NOT TEST ON XORG**
I install XDM, i open XDM, i close it by clicking the 'X' on top menu bar. I again open XDM and it does not open. It shows that one window is open in dash and overview but that window has no interface. see the screenshots
To Reproduce Steps to reproduce the behavior:
1. Install XDM by following the instructions.
2. Generally works fine for few days, but after random X days, this occurs.
3. I start my pc, open xdm (from all apps). It opens fine. I close it by clicking on "X" on top bar.
4. I again open it from 'all apps' or 'dash' but this time this bug happens.
Expected behavior XDM should open normally.
Screenshots
please complete the following information:
- OS: Manjaro Nibia 20.2
- XDM Version: 7.2.11
Generated log using below method
- [https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
[https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)

```
$ /opt/xdman  jre/bin/java -jar xdman.jar [ main ] loading... [ main ] 11.0.6 5.10.7-3-MANJARO [ main ] Loading config... [ main ] Creating folders [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts en [ main ] Loading language en [ main ] Context initialized [ Thread-4 ] instance starting... [ Thread-4 ] instance started. [ AWT-EventQueue-0 ] showing main window. [ Thread-4 ] Lock acquired... Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/icon.png Error setting Dock icon Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/bg_nav.png [ AWT-EventQueue-0 ] List changed [ AWT-EventQueue-0 ] SystemTray is not supported checking for app update [ Thread-3 ] manifest download response: 200 7.2.11 7.2.11 New session New session New session # I closed XDM here and reopened it. [ Thread-7 ] Response set [ Thread-7 ] java.net.SocketException: Connection reset at java.base/java.net.SocketInputStream.read(Unknown Source) at java.base/java.net.SocketInputStream.read(Unknown Source) at java.base/java.net.SocketInputStream.read(Unknown Source) at xdman.util.NetUtils.readLine(NetUtils.java:16) at xdman.monitoring.Request.read(Request.java:16) at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485) at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516) at java.base/java.lang.Thread.run(Unknown Source) New session # closed and reopened again [ Thread-8 ] Response set [ Thread-8 ] java.net.SocketException: Connection reset at java.base/java.net.SocketInputStream.read(Unknown Source) at java.base/java.net.SocketInputStream.read(Unknown Source) at java.base/java.net.SocketInputStream.read(Unknown Source) at xdman.util.NetUtils.readLine(NetUtils.java:16) at xdman.monitoring.Request.read(Request.java:16) at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485) at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516) at java.base/java.lang.Thread.run(Unknown Source) New session
```

Additional Info
- 
This does not happen when i disable "Animation Tweaks" Gnome Shell Extension.

- 
[Here](https://github.com/Selenium-H/Animation-Tweaks/issues/39) is issue on Animation Tweaks GH page

This does not happen when i disable "Animation Tweaks" Gnome Shell Extension.
[Here](https://github.com/Selenium-H/Animation-Tweaks/issues/39) is issue on Animation Tweaks GH page

- 
            