# Sometimes settings are not saved (#569)

[Sometimes settings are not saved](https://github.com/subhra74/xdm/issues/569#top)
[bugSomething isn't working](https://github.com/subhra74/xdm/issues?q=state%3Aopen%20label%3A%22bug%22)

[corobin](https://github.com/corobin)
[on May 25, 2021](https://github.com/subhra74/xdm/issues/569#issue-900128626)
PLEASE DO NOT JUST SAY "It does not work, or something not working etc." Provide enough relevent details so that the issue can be analyzed and reproduced easily
Describe the bug Settings are intermittently not saved across program restarts.
To Reproduce I have not found a consistent way to reproduce this.
Steps to reproduce the behavior:
1. Open XDM
2. Change some setting
3. Exit program
4. re-launch program and check if setting is as set before
Examples:
1. Toggle for browser monitor at the bottom right of main window
2. In settings pane, the checkbox for "disable floating DOWNLOAD VIDEO popup"
there are nothing special about these two settings as far as i can tell, just the ones i noticed. you may not see the problem with these but may see others. also, the lack of memory is not consistent. sometimes it persists the setting.
Expected behavior saving settings should be consistent
Screenshots n/a
please complete the following information:
- OS: Win10x6420H2
- Browser n/a
- XDM addon Version n/a
- XDM Version 7.2.11
Generated log using below method `Microsoft Windows [Version 10.0.19042.985] (c) Microsoft Corporation. All rights reserved.
C:\Program Files (x86)\XDM>"java-runtime\bin\java" -jar xdman.jar [ main ] loading... [ main ] 11.0.6 10.0 [ main ] Loading config... [ main ] Creating folders [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts en [ main ] Loading language en [ main ] Context initialized [ Thread-1 ] instance starting... [ Thread-1 ] instance started. [ Thread-1 ] Lock acquired... [ AWT-EventQueue-0 ] showing main window. Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png Error setting Dock icon Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/bg_nav.png [ AWT-EventQueue-0 ] List changed Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png checking for app update [ Thread-0 ] manifest download response: 200 7.2.11 7.2.11 [ AWT-EventQueue-0 ] Max download: 100 [ AWT-EventQueue-0 ] Category changed [ AWT-EventQueue-0 ] Checked [ AWT-EventQueue-0 ] Category changed [ AWT-EventQueue-0 ] Launch CMD: "C:\Program Files (x86)\XDM\java-runtime\bin\javaw.exe" -Xmx1024m -jar "C:\Program Files (x86)\XDM\xdman.jar" -m "C:\Program Files (x86)\XDM\java-runtime\bin\javaw.exe" -jar "C:\Program Files (x86)\XDM\xdman.jar" -m [ AWT-EventQueue-0 ] Max download: 100 [ AWT-EventQueue-0 ] Category changed [ AWT-EventQueue-0 ] Max download: 100 [ AWT-EventQueue-0 ] Category changed
C:\Program Files (x86)\XDM>"java-runtime\bin\java" -jar xdman.jar [ main ] loading... [ main ] 11.0.6 10.0 [ main ] Loading config... [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts en [ main ] Loading language en [ main ] Context initialized [ Thread-1 ] instance starting... [ Thread-1 ] instance started. [ AWT-EventQueue-0 ] showing main window. [ Thread-1 ] Lock acquired... Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png Error setting Dock icon Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/bg_nav.png [ AWT-EventQueue-0 ] List changed Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png checking for app update [ Thread-0 ] manifest download response: 200 7.2.11 7.2.11 [ AWT-EventQueue-0 ] Max download: 100 [ AWT-EventQueue-0 ] Category changed
C:\Program Files (x86)\XDM>`
Additional context Add any other context about the problem here.

[bugSomething isn't working](https://github.com/subhra74/xdm/issues?q=state%3Aopen%20label%3A%22bug%22)

- 
            