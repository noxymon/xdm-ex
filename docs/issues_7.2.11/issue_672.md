# Setting proxy in proxy settings doesn't work and disappears (#672)

[Setting proxy in proxy settings doesn't work and disappears](https://github.com/subhra74/xdm/issues/672#top)

[dshemnv](https://github.com/dshemnv)
[on Oct 12, 2021](https://github.com/subhra74/xdm/issues/672#issue-1023555873)
Describe the bug Proxy settings in GUI disappear.
To Reproduce Steps to reproduce the behavior:
1. Go to settings
2. Click on "Network Settings"
3. Scroll to "Proxy Settings"
4. Check "Use proxy server"
5. Enter proxy information
6. Go back
7. Go to "Network Settings" Again
8. Proxy information is gone
Expected behavior Proxy settings don't disappear and are used.
https://user-images.githubusercontent.com/48387052/136925424-487781dc-b956-4742-ba5e-9a22b04ea999.gifScreenshots
please complete the following information:
- OS: EndeavourOS (Arch Linux)
- XDM Version 7.2.11
Generated log using below method

```
[ main ] loading... [ main ] 11.0.6 5.14.11-arch1-1 [ main ] Loading config... [ main ] Creating folders [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts en [ main ] Loading language en [ main ] Context initialized [ Thread-4 ] java.net.BindException: Adresse déjà utilisée (Bind failed) at java.base/java.net.PlainSocketImpl.socketBind(Native Method) at java.base/java.net.AbstractPlainSocketImpl.bind(Unknown Source) at java.base/java.net.ServerSocket.bind(Unknown Source) at java.base/java.net.ServerSocket.bind(Unknown Source) at xdman.monitoring.BrowserMonitor.run(BrowserMonitor.java:186) at java.base/java.lang.Thread.run(Unknown Source) [ Thread-4 ] instance already runninng
```


```
[ main ] loading... [ main ] 11.0.6 5.14.11-arch1-1 [ main ] Loading config... [ main ] Creating folders [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts en [ main ] Loading language en [ main ] Context initialized [ Thread-4 ] java.net.BindException: Adresse déjà utilisée (Bind failed) at java.base/java.net.PlainSocketImpl.socketBind(Native Method) at java.base/java.net.AbstractPlainSocketImpl.bind(Unknown Source) at java.base/java.net.ServerSocket.bind(Unknown Source) at java.base/java.net.ServerSocket.bind(Unknown Source) at xdman.monitoring.BrowserMonitor.run(BrowserMonitor.java:186) at java.base/java.lang.Thread.run(Unknown Source) [ Thread-4 ] instance already runninng
```

- 
            