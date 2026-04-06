# XDM 8.0.29 Fails to Use Manual Proxy Configuration on Windows 10 (#1253)

[XDM 8.0.29 Fails to Use Manual Proxy Configuration on Windows 10](https://github.com/subhra74/xdm/issues/1253#top)

[JuanSystems](https://github.com/JuanSystems)
[on Oct 4, 2024](https://github.com/subhra74/xdm/issues/1253#issue-2567177250)
Title: XDM 8.0.29 Fails to Use Manual Proxy Configuration on Windows 10
Description: I've encountered an issue with XDM version 8.0.29 where the program fails to connect to the internet when using a proxy on Windows 10. Despite manually configuring the proxy settings in XDM, it does not recognize the proxy and cannot establish a connection, even when using tools like Proxifier to force the connection.
Steps to Reproduce:
Install XDM version 8.0.29 on Windows 10. Configure the proxy settings manually within XDM. Attempt to download any file or connect to a server through XDM. Expected Behavior: XDM should be able to connect to the internet using the configured proxy, similar to other applications on the system.
Actual Behavior: XDM fails to connect to the internet, showing no activity. All other applications can connect through the proxy without issues, but XDM does not.
Additional Information:
The same version (8.0.29) works fine on Debian 12 and Linux Mint Faye with the manual proxy configuration. XDM version 7.2.11 (stable) works correctly with the proxy on Windows 10, so this issue seems specific to version 8.0.29. I prefer using version 8.0.29 due to its improved features, but the proxy issue is preventing its usage in my environment. Environment:
Operating System: Windows 10 XDM Version: 8.0.29 Proxy Configuration: Manual (not working) Other Versions Tested: 7.2.11 (working) Please let me know if any additional information is needed or if there are any workarounds to resolve this issue. Thank you!

- 
            