# XDM freezes the system like a fork bomb when certain websites are opened with xdm add-on enabled. (#1242)

[XDM freezes the system like a fork bomb when certain websites are opened with xdm add-on enabled.](https://github.com/subhra74/xdm/issues/1242#top)

[footedroom575](https://github.com/footedroom575)
[on Jul 3, 2024](https://github.com/subhra74/xdm/issues/1242#issue-2388819057)

[#768](https://github.com/subhra74/xdm/discussions/768)
PLEASE DO NOT JUST SAY "It does not work, or something not working etc." Provide enough relevant details so that the issue can be analyzed and reproduced easily
Describe the bug XDM seems to hang the system and freeze everything and acts like a fork bomb when certain websites are opened while the XDM monitoring add-on in browser is enabled.
To Reproduce Steps to reproduce the behavior:
1. Have XDM Browser Monitor installed on Firefox
2. Go to [https://www.alfredforum.com/topic/18052-external-script-using-google-zx/](https://www.alfredforum.com/topic/18052-external-script-using-google-zx/)
3. System is frozen to death
[https://www.alfredforum.com/topic/18052-external-script-using-google-zx/](https://www.alfredforum.com/topic/18052-external-script-using-google-zx/)
Expected behavior XDM should monitor links but do not create download pop ups that freeze everything
please complete the following information:
- OS: Linux (6.5.13-7-MANJARO (64-bit))
- Browser: Firefox 124.0.1
- XDM addon Version: 2.2
- XDM Version: Version 7.2.11 with Java Eclipse OpenJ9 11.0.6
Generated log using below method
- [https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
[https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
` Jul 03 15:18:02 fiction systemd[730]: Started Xtreme Download Manager

Jul 03 15:18:02 fiction plasmashell[8158]: [ main ] loading... Jul 03 15:18:02 fiction plasmashell[8158]: [ main ] 11.0.6 6.5.13-7-MANJARO Jul 03 15:18:02 fiction plasmashell[8158]: [ main ] Loading config... Jul 03 15:18:02 fiction plasmashell[8158]: [ main ] Creating folders Jul 03 15:18:02 fiction plasmashell[8158]: [ main ] starting monitoring... Jul 03 15:18:02 fiction plasmashell[8158]: [ main ] Init app Jul 03 15:18:02 fiction plasmashell[8158]: [ main ] Loading fonts Jul 03 15:18:02 fiction plasmashell[8158]: en Jul 03 15:18:02 fiction plasmashell[8158]: [ main ] Loading language en Jul 03 15:18:02 fiction plasmashell[8158]: [ main ] Context initialized Jul 03 15:18:03 fiction plasmashell[8158]: [ Thread-4 ] java.net.BindException: Address already in use (Bind failed) Jul 03 15:18:03 fiction plasmashell[8158]: at java.base/java.net.PlainSocketImpl.socketBind(Native Method) Jul 03 15:18:03 fiction plasmashell[8158]: at java.base/java.net.AbstractPlainSocketImpl.bind(Unknown Source) Jul 03 15:18:03 fiction plasmashell[8158]: at java.base/java.net.ServerSocket.bind(Unknown Source) Jul 03 15:18:03 fiction plasmashell[8158]: at java.base/java.net.ServerSocket.bind(Unknown Source) Jul 03 15:18:03 fiction plasmashell[8158]: at xdman.monitoring.BrowserMonitor.run(BrowserMonitor.java:186) Jul 03 15:18:03 fiction plasmashell[8158]: at java.base/java.lang.Thread.run(Unknown Source) Jul 03 15:18:03 fiction plasmashell[8158]: [ Thread-4 ] instance already runninng Jul 03 15:18:03 fiction java[1099]: New session Jul 03 15:18:03 fiction java[1099]: [ Thread-7 ] Response set Jul 03 15:18:03 fiction java[1099]: [ Thread-7 ] java.net.SocketException: Connection reset Jul 03 15:18:03 fiction java[1099]: at java.base/java.net.SocketInputStream.read(Unknown Source) Jul 03 15:18:03 fiction java[1099]: at java.base/java.net.SocketInputStream.read(Unknown Source) Jul 03 15:18:03 fiction java[1099]: at java.base/java.net.SocketInputStream.read(Unknown Source) Jul 03 15:18:03 fiction java[1099]: at xdman.util.NetUtils.readLine(NetUtils.java:16) Jul 03 15:18:03 fiction java[1099]: at xdman.monitoring.Request.read(Request.java:16) Jul 03 15:18:03 fiction java[1099]: at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485) Jul 03 15:18:03 fiction java[1099]: at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516) Jul 03 15:18:03 fiction java[1099]: at java.base/java.lang.Thread.run(Unknown Source) Jul 03 15:18:06 fiction systemd[730]: [app-xdman@autostart.service](mailto:app-xdman@autostart.service): Consumed 5.434s CPU time. Jul 03 15:18:09 fiction plasmashell[903]: qml: temp unit: 0 Jul 03 15:18:12 fiction plasmashell[903]: qml: temp unit: 0 Jul 03 15:18:15 fiction plasmashell[903]: qml: temp unit: 0 Jul 03 15:18:21 fiction plasmashell[903]: qml: temp unit: 0 Jul 03 15:18:22 fiction systemd[1]: systemd-hostnamed.service: Deactivated successfully. Jul 03 15:18:27 fiction plasmashell[903]: qml: temp unit: 0 Jul 03 15:18:32 fiction rtkit-daemon[818]: Supervising 7 threads of 4 processes of 1 users. Jul 03 15:18:32 fiction rtkit-daemon[818]: Supervising 7 threads of 4 processes of 1 users. Jul 03 15:18:52 fiction systemd-logind[487]: Power key pressed short. Jul 03 15:18:52 fiction systemd[730]: Created slice Slice /app/dbus-:1.2-org.kde.Shutdown. Jul 03 15:18:52 fiction systemd[730]: Started dbus-:[1.2-org.kde.Shutdown@0.service](mailto:1.2-org.kde.Shutdown@0.service) `
System froze so had to shutdown by holding power key for 5 sec. The above log is copied from journalctl

```
journalctl
```

I believe problem is with the add-on that incorrectly identifies link and not xdm application itself. Please lmk if required more info from my side.

- 
            