# XDM keeps creating "Downloads" folder (#570)

[XDM keeps creating "Downloads" folder](https://github.com/subhra74/xdm/issues/570#top)
[bugSomething isn't working](https://github.com/subhra74/xdm/issues?q=state%3Aopen%20label%3A%22bug%22)

[corobin](https://github.com/corobin)
[on May 25, 2021](https://github.com/subhra74/xdm/issues/570#issue-900135154)
PLEASE DO NOT JUST SAY "It does not work, or something not working etc." Provide enough relevent details so that the issue can be analyzed and reproduced easily
Describe the bug When the Downloads folder is located somewhere other than the windows default, even if XDM is configured to download into a different location, it always creates an empty "Downloads" folder in the default location
To Reproduce Steps to reproduce the behavior:
1. open XDM
2. change download location
3. for convenience also check to download everything into the same folder
4. exit XDM
5. change downloads folder location:
a. go to windows default download folder
b. right click 'properties'
c. go to 'location' tab
d. move it to somewhere else arbitrary
6. if the old downloads folder is not automatically removed, delete it now
7. launch XDM
8. in explorer, navigate to parent folder of default download folder location (usually the user folder)
XDM has just created an empty downloads folder there. can repeat delete/launch XDM to verify it is XDM doing it
Expected behavior there should not be a "downloads" folder in the default location
Screenshots n/a
please complete the following information:
- OS: Win10x64 20H2
- Browser n/a
- XDM addon Version n/a
- XDM Version 7.2.11
Generated log using below method `Microsoft Windows [Version 10.0.19042.985] (c) Microsoft Corporation. All rights reserved.
C:\Program Files (x86)\XDM>"java-runtime\bin\java" -jar xdman.jar [ main ] loading... [ main ] 11.0.6 10.0 [ main ] Loading config... [ main ] Creating folders [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts en [ main ] Loading language en [ main ] Context initialized [ Thread-1 ] instance starting... [ Thread-1 ] instance started. [ Thread-1 ] Lock acquired... [ AWT-EventQueue-0 ] showing main window. Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png Error setting Dock icon Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/bg_nav.png [ AWT-EventQueue-0 ] List changed Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png checking for app update [ Thread-0 ] manifest download response: 200 7.2.11 7.2.11 [ AWT-EventQueue-0 ] Max download: 100 [ AWT-EventQueue-0 ] Category changed [ AWT-EventQueue-0 ] Checked [ AWT-EventQueue-0 ] Category changed
C:\Program Files (x86)\XDM>"java-runtime\bin\java" -jar xdman.jar [ main ] loading... [ main ] 11.0.6 10.0 [ main ] Loading config... [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts en [ main ] Loading language en [ main ] Context initialized [ Thread-1 ] instance starting... [ Thread-1 ] instance started. [ Thread-1 ] Lock acquired... [ AWT-EventQueue-0 ] showing main window. Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png Error setting Dock icon Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/bg_nav.png [ AWT-EventQueue-0 ] List changed Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png checking for app update [ Thread-0 ] manifest download response: 200 7.2.11 7.2.11
C:\Program Files (x86)\XDM>`
Additional context Add any other context about the problem here.

[bugSomething isn't working](https://github.com/subhra74/xdm/issues?q=state%3Aopen%20label%3A%22bug%22)

- 
            